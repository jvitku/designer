# Architecture containing gridWorld simulator and RL nodes. 
# The RL node has configuration connected by weighted connections to bias. These weights are set manually.
#
# Test:
# 	-let the agent lear 
#	-move with the parameter IPORTANCE towards 1 - observe that the agent sticks to one of reward sources and exploits it
# 	-move the parameter IMPORTANCE lower - observe how the agent continues with exploration
#
# by Jaroslav Vitku [vitkujar@fel.cvut.cz]

import nef
from ca.nengo.math.impl import FourierFunction
from ca.nengo.model.impl import FunctionInput
from ca.nengo.model import Units
from ctu.nengoros.modules.impl import DefaultNeuralModule as NeuralModule
from ctu.nengoros.comm.nodeFactory import NodeGroup as NodeGroup
from ctu.nengoros.comm.rosutils import RosUtils as RosUtils
from org.hanns.rl.discrete.ros.sarsa import HannsQLambdaVisProsperity as QLambda

import rl_sarsa
import gridworld

################################################### script goes here:
net=nef.Network('HandWired parameters of RL node to bias')
net.add_to_nengo()  


rl = rl_sarsa.qlambdaProsperity("RL",2,4,20)	# define neural modules
world = gridworld.benchmark("map");
net.add(rl)									# place them into the network
net.add(world)

# connect them together
net.connect(world.getOrigin(QLambda.topicDataIn), rl.newTerminationFor(QLambda.topicDataIn))
net.connect(rl.getOrigin(QLambda.topicDataOut), world.getTermination(QLambda.topicDataOut))

alpha = QLambda.DEF_ALPHA
gamma = QLambda.DEF_GAMMA
lambdaa = QLambda.DEF_LAMBDA
importance = QLambda.DEF_IMPORTANCE

# define the configuration
net.make_input('alpha',[alpha])
net.make_input('gamma',[gamma])
net.make_input('lambda',[lambdaa])
net.make_input('importance',[importance])

# wire it
net.connect('alpha', rl.getTermination(QLambda.topicAlpha))
net.connect('gamma', rl.getTermination(QLambda.topicGamma))
net.connect('lambda', rl.getTermination(QLambda.topicLambda))
net.connect('importance', rl.getTermination(QLambda.topicImportance))


# define parameters of the RL algorithm
#ba = Bias('AlphaBias' ,0.12)
#ba = WeightedBias('AlphaBias')
#ba.setW(0.6)
#bias = net.add(ba)
#bias.setW(0.6)
#net.connect(bias.getOrigin('output'),rl.getTermination(QLambda.topicAlpha));



