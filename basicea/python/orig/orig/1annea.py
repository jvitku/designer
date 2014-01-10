# First EA whic approximates XOR
import nef
import random
from ca.nengo.math.impl import FourierFunction
from ca.nengo.model.impl import FunctionInput
from ca.nengo.model import Units
from nengoros.modules.impl import DefaultNeuralModule as SmartNeuron
from nengoros.comm.nodeFactory import NodeGroup as NodeGroup
from nengoros.comm.rosutils import RosUtils as RosUtils

from design.ea.matrix.ann import WMatrixEA as EA
from design.ea.matrix.ann.impl import Ind as Ind

from hanns import inFactory
import modules
from modules import mathNodes
from modules import routing
from hanns import spiketofloat
from hanns import floattospike
import inspect
import os
from ca.nengo.model.impl import NetworkImpl, NoiseFactory, FunctionInput, NetworkArrayImpl

RosUtils.prefferJroscore(False) 

actualIn=0;
actualOut=0;
# how to read input and output weights from individual in java
def setInW(w):
    useDesigned = True
    if (useDesigned):
        w = ind.getMatrixEncoder().get2DInMatrixNo(actualIn);
    else:
        for i in range(len(w)):
            for j in range(len(w[i])):
                w[i][j]=0.2
    return w;
def setOutW(w): # TODO: support for multidimensional outputs
    useDesigned=True
    if (useDesigned):
        w = ind.getMatrixEncoder().get2DOutMatrixNo(actualOut);
    else:
        for i in range(len(w)):
            for j in range(len(w[i])):
                w[i][j]=0.2
    return w;
    
def setReccurent(w):
    #useDesigned=True

    if (useRecurrent):
        w = ind.getMatrixEncoder().getWeights();
    else:
        for i in range(len(w)):
            for j in range(len(w[i])):
                w[i][j]=0
    return w;


# this method builds ANN from individual's "genome"
def buildModel(ind):
    # model
    model = nef.Network('model')

    # input
    inputs = model.add(routing.twoDimsRouter('inputs')); # TODO: general IO dimensions 
    
    # output
    output = inFactory.makeLifNeuron(mr, 'spike', model, name='output',intercept=0);
    #output = inFactory.makeLinearNeuron(mr, 'spike', model, name='output',intercept=0);
    #output = inFactory.makeSigmoidNeuron(mr, 'spike', model, name='output', inflection=0.4);
    CI = spiketofloat.controlledIntegrator(model,0);
    outputt = model.add(routing.getFirstDim('outputt')); # TODO: general IO dimensions

    model.connect(output,CI,transform=[1,0],pstc=0.1)
    model.connect(CI,outputt.getTermination('in'))
    ##model.connect(inputs.getOrigin('out'), output, index_pre=1)

    # spiking ANN
    model.make('ANN', neurons=N, dimensions=INdim, mode='spike',
        encoders = ([1,0], [0,1]), intercept= (ii, ii), max_rate = (mr,mr))

    # wire it:
    model.connect('ANN','ANN',weight_func=setReccurent);    # recurrent conencitons
    model.connect('ANN','output',weight_func=setOutW);      # ANN to output
    
    # for each input, make one neuron and connect it
    for d in range(INdim):
        # choose factory for input neurons:
        #inFactory.makeLinearNeuron(mr,d,model,'spike');
        #inFactory.makeBioLinearNeuron(mr,d,model,'spike');
        #inFactory.makeSigmoidNeuron(mr, 'spike', model, num=d);
        inFactory.makeLifNeuron(mr, 'spike', model, num=d,intercept=0);
        #inFactory.makeBioSigmoidNeuron(mr,d,model,'spike');

        # connect shared input to input neuron and neuron to ANN with weights
        actualIn=d;
        model.connect(inputs.getOrigin('out'), 'in_neuron_%d'%d, index_pre=d)
        model.connect('in_neuron_%d'%d, 'ANN',weight_func=setInW)

    return model;

# builds enire experiment
def buildExperiment(ind):
    net=nef.Network('EA designed recurrent SNN')  
    net.add_to_nengo();
    # generator
    # function .1 base freq, max freq 10 rad/s, and RMS of .5; 12 is a seed
    generator=FunctionInput('generator',[FourierFunction(.1, 5,.3, 12),
        FourierFunction(.5, 7, .7, 112)],Units.UNK)
    net.add(generator);

    # model
    model= buildModel(ind);
    net.add(model.network); 

    # plant
    plant = net.add(modules.OR('Fuzzy OR'));
    #plant = net.add(mathNodes.SUMABS('SUM'));
    # error estimation
    err = net.make('error',1,1,mode='direct');
    mse = net.add(modules.mse('MSE'));

    # wiring
    modelIn = model.get('inputs');  # get the input model
    model.network.exposeTermination(modelIn.getTermination('in'),'subInput'); # expose its termination
    net.connect(generator,model.network.getTermination('subInput'),weight=1)  # generator to termination
    net.connect(generator,plant.getTermination('inputs'))                     # generator to plant

    modelOut = model.get('outputt');
    model.network.exposeOrigin(modelOut.getOrigin('out'),'subOutput');        # expose its termination
    net.connect(model.network.getOrigin('subOutput'), err, weight=-1)

    net.connect(plant.getOrigin('output'),err)  
    net.connect(err,mse.getTermination('error'))
    return net;

# runs the experiment and returns value of MSE
def runExperiment(net,t,dt):
    # see: http://nengo.ca/docs/html/nef.Network.html
    net.reset()
    net.run(t,dt)
    mse = net.get('MSE')    # get MSE measuring node
    return mse.getMse()     # read MSE

def evalInd(ind):
    net = buildExperiment(ind);
    error = runExperiment(net,t,dt);
    ind.setFitness(error[0]);
    return error[0]
    

# experiment setup - constants
mr = 25;
ii = 0;

INdim = 2;
OUTdim = 1;
minw = -0.0;
maxw = 0.3;


t = 3;
dt = 0.001;

# which setup to use?
config=4

if config == 1: # this works pretty well (approximates sum)
    useRecurrent =True
    pMut = 0.1
    pCross = 0.9;
    popsize = 15;
    maxgen = 10;
    N=3

elif (config ==2 ):
    useRecurrent =True
    pMut = 0.6;
    pCross = 1;
    N=5
    popsize = 30;
    maxgen = 20;

elif (config ==3 ): # this is 4x4 fully connected ANN
    useRecurrent =True
    pMut = 0.1
    pCross = 0.9;
    popsize = 25;
    maxgen = 70;
    N=4
elif (config ==4):
    useRecurrent = True
    pMut = 0.15
    pCross = 0.9
    popsize = 25;
    maxgen = 70;
    N=4;

else: 
    useRecurrent =False
    pMut = 0.6;
    pCross = 1;
    N=5
    popsize = 4;
    maxgen = 10;

numRuns=10;
for expNo in range(numRuns):

    print '----------------------- experiment number %d'%expNo
    # init EA
    ea = EA(INdim, OUTdim, N, maxgen,popsize,minw,maxw);
    ea.setProbabilities(pMut,pCross);
    ea.initPop();
    f = open('data/ea_%d.txt'%expNo, 'w');

    # evolution insert here
    while ea.wantsEval():
        print 'Gen: '+repr(ea.generation())+'/'+repr(maxgen)+' actual ind is ' +repr(ea.actualOne())+'/'+repr(popsize)+' best so far: '+repr(ea.getBestFitness());
        
        ind = ea.getInd();
        #ind.printMatrix();        
    
        error = evalInd(ind);
        ind.getFitness().setError(error);
    
        print 'Ind: '+repr(ea.actualOne())+' Error is: '+repr(error) +' fitness is: '+repr(ind.getFitness().get());
    
        print ea.getActualWeights();
    
        # evaluated the last individual in the generatio? write stats
        if (ea.actualOne() == (popsize-1)):
            print 'check: '+repr(ea.generation())
            fit = ea.getBestInd().getFitness().get();
            er = ea.getBestInd().getFitness().getError();
            print '%d %.5f %.5f\n' % (ea.generation(),fit,er)
            f.write('%d %.8f %.8f\n' % (ea.generation(),fit,er))
            f.flush()
            os.fsync(f.fileno()) # just write it to disk

        # poc++ and check end of ea
        ea.nextIndividual();


    f.close()

# load the best one found
ind = ea.getIndNo(ea.getBest());
net = buildExperiment(ind);
print 'best fitness is:'
print ind.getFitness().get();

print 'done\n\n\n'

