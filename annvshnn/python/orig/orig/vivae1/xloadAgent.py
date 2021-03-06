"""
Loads agent from the file

by Jaroslav Vitku
"""

import nef
import time
import random
from ca.nengo.math.impl import FourierFunction
from ca.nengo.model.impl import FunctionInput
from ca.nengo.model import Units
from nengoros.modules.impl.vivae import VivaeNeuralModule as NeuralModule
from nengoros.comm.nodeFactory import NodeGroup as NodeGroup
from nengoros.comm.rosutils import RosUtils as RosUtils
from nengoros.modules.impl.vivae.impl import SimulationControls as Controls
import simplemodule
import util
from vivaeLauncher import oneAgent

from design.ea.matrix.hnn.simple import HNNWMatrixEA as EA
from design.ea.matrix.hnn.simple import HNNInd as Ind
import os
from tools.io import AgentSaver as Saver
from tools.io import AgentConfig as Config

RosUtils.setAutorun(False)    

# this generates signal which turns the agent left
class EventGenerator(simplemodule.SimpleModule):
    #  .......... 9         2          1    [all sensors, two speeds, turning duration]
    def init(self,inputdims,outputdims,numpars):
        self.inputdims = inputdims;
        self.outputdims = outputdims;
        self.numpars = numpars;
        self.lo = 0.1;       # original one
        self.ro = 0.38;      # original one
        self.duration = 0;
        self.maxdur = 30;    # original one
        self.started = False;
        self.threshold = 0; # if input value is under this threshold, launch the event!
        #self.straight = [0.47, 0.47];
        self.time = 0;
        self.il=0
        self.ir=0
            
    def termination_inputs(self,values):
        if(not self.started):
            self.il=values[1];
            self.ir=values[2];
            #inputval = wa[0]*self.il + wa[4]*self.ir + wa[8]*1 // simplification
            inputval = wa[0]*self.il;
            #print 'val: '+repr(inputval)+ ' threee '+repr(self.threshold)
            if(inputval <= self.threshold and self.time>1):
                #print 'ffffffffff'
                self.duration = 0;
                self.started = True;
        
    def origin_outputs(self):
        if(self.started):
            # stopping criteria fulfilled?
            if(self.duration > self.maxdur):
                self.started = False;
                return [0, 0];
            else:
                self.duration=self.duration+1;
                return [self.lo, self.ro];
        return [0, 0];
        
    # parames are: threshold <-1,1>, leftWheel <-1,1> & rightWheel <-1,1> during an event
    def termination_params(self,values):
        if(not self.started):
            self.threshold = self.il*wa[1]+self.ir*wa[5]+wa[9]*values[0]; 
        self.lo = self.il*wa[2] + self.ir*wa[6] + wa[10]*values[0];
        self.ro = self.il*wa[3] + self.ir*wa[7] + wa[11]*values[0];
        return;
        
    def tick(self):
        self.time = self.time+1;


class SpeedSaver(nef.SimpleNode):
    def init(self):
        self.t = 0;
        self.speeds = 0;
    def termination_data(self,values,dimensions=9):
        self.speeds = self.speeds+values[8];
        self.t = self.t+1;
    def getAvgSpeed(self):
        return self.speeds/self.t;


class Summer(nef.SimpleNode):
    def init(self):
        self.output = [0,0]
        self.il = 0;
        self.ir = 0;
        self.ib = 0;
        self.bias = 0; # this is added to both ouput signals
    def termination_inputData(self,values,dimensions=2):
        self.il = values[0];
        self.ir = values[1];
    def termination_inputBias(self,values,dimensions=1):
        self.ib = values[0];
    def origin_outputs(self):
        self.bias =      wb[2]*self.il + wb[5]*self.ir+wb[8]*self.ib;   # bias added to signals
        output=[0,0]    # tmp
        output[0] = wb[0]*self.il + wb[3]*self.ir+wb[6]*self.ib +self.bias; # outputs
        output[1] = wb[1]*self.il + wb[4]*self.ir+wb[7]*self.ib +self.bias;
        
        # here is also the representation of output weights..
        self.output[0] = wc[0]*output[0]+wc[1]*output[1];
        self.output[1] = wc[2]*output[0]+wc[3]*output[1];
        return self.output;


################################################################################# higher level..


def buildExperiment(net,wa,wb,wc,visibility=False):

    numsensors=4                                # number of agents sensors (if changed, need to read correct values in the Controller)

    simulator = oneAgent(net,agentName='a',numSensors=numsensors,maxDistance=120, frictionDistance=0,nameSpace=simName,visible=visibility,mapName='data/scenarios/test/wallsIII.svg');
    vivae = simulator.getControls();

    # Input->LI
    controller = net.add(EventGenerator('Agent controller',numsensors*2+1,2,1));            # event generator
    input = net.make_input('bias1',[1])                                                     # should result in 0, 0, .33
    net.connect(input, controller.getTermination('params'))
    net.connect(simulator.getAgent('a').getOrigin(),controller.getTermination('inputs'))    # connect agent with controller

    # LII->LIII->Output
    input2 = net.make_input('bias2',[1])
    summer = net.add(Summer('Summator'));
    net.connect(input2,summer.getTermination('inputBias'));
    net.connect(controller.getOrigin('outputs'),summer.getTermination('inputData'))
    net.connect(summer.getOrigin('outputs'),simulator.getAgent('a').getTermination())

    saver = net.add(SpeedSaver('avg'))
    net.connect(simulator.getAgent('a').getOrigin(),saver.getTermination('data'));
    #saver = net.add(DataSaver('saver')) # save data ??
    return simulator


def runExperiment(net):
    print "Simulating network for "+repr(t)+" seconds"
    net.reset()
    net.run(t,dt)
    avg = net.get('avg').getAvgSpeed()*dt;
    print 'AVG speed was: '+repr(avg);
    return avg;
    
wa = [1,0,0,0, 0,0,0,0, 0,0,-0.44,-0.11, 0,0,0,0]
wb = [1,0,0, 0,1,0, 0,0,0.44]
wc = [1,0, 0,1]

############################################################ load agent

exp = 'ea_734833_agents.txt'
gen = 53
sx = Saver(exp);		

agent = sx.load(gen)
fitness = agent.fitness;
genome = agent.genome

print 'fitness of agent '+repr(gen)+' is '+repr(fitness)
print 'genome '+repr(genome)

from design.ea.matrix.hnn.simple import HNNWMatrix as decoder
simName = 'test'
wa = agent.getRange( 0,16);
wb = agent.getRange( 16,25);
wc = agent.getRange(25,29);

############################################################ build experiment

genomeLen = 29;
net=nef.Network('best agent')
net.add_to_nengo()  


simulator = buildExperiment(net,wa,wb,wc,visibility=True);
vivae = simulator.getControls();


vivae.setVisible(True)
net.reset()
net.view()

