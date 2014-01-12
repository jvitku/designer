# Architecture containing gridWorld simulator and RL nodes. 
# The RL node has configuration connected by weighted connections to bias. 
#
# The values of the paramters will be determined by the EA, based on the prosperity measure.
# The values of parameters (alpha, gamma, lambda) are searched for the fixed value of importance=0.3
#
# by Jaroslav Vitku [vitkujar@fel.cvut.cz]

import nef
from ca.nengo.math.impl import FourierFunction
from ca.nengo.model.impl import FunctionInput
from ca.nengo.model import Units
from ctu.nengoros.modules.impl import DefaultNeuralModule as NeuralModule
from ctu.nengoros.comm.nodeFactory import NodeGroup as NodeGroup
from ctu.nengoros.comm.rosutils import RosUtils as RosUtils
from org.hanns.rl.discrete.ros.sarsa import HannsQLambdaVis as QLambda

import rl_sarsa
import gridworld


def buildSimulation(alpha, gamma, lambdaa):
	################################################### script goes here:
	net=nef.Network('HandWired parameters of RL node to bias')
	net.add_to_nengo()  

	rl = rl_sarsa.qlambdaDelay("RL", 2, 4, 10, 10)	# define the neural modules
	world = gridworld.benchmark("map", 1000);
	net.add(rl)									    # place them into the network
	net.add(world)

	# connect them together
	net.connect(world.getOrigin(QLambda.topicDataIn), rl.newTerminationFor(QLambda.topicDataIn))
	net.connect(rl.getOrigin(QLambda.topicDataOut), world.getTermination(QLambda.topicDataOut))

	# set the values of the parameters
	#alpha = QLambda.DEF_ALPHA
	#gamma = QLambda.DEF_GAMMA
	#lambdaa = QLambda.DEF_LAMBDA
	importance = QLambda.DEF_IMPORTANCE

	# define the parameter sources (controllable from the simulation window)
	net.make_input('alpha',[alpha])
	net.make_input('gamma',[gamma])
	net.make_input('lambda',[lambdaa])
	net.make_input('importance',[importance])

	# connect signal sources to the RL node
	net.connect('alpha', rl.getTermination(QLambda.topicAlpha))
	net.connect('gamma', rl.getTermination(QLambda.topicGamma))
	net.connect('lambda', rl.getTermination(QLambda.topicLambda))
	net.connect('importance', rl.getTermination(QLambda.topicImportance))
	return net


net = buildSimulation(QLambda.DEF_ALPHA,QLambda.DEF_GAMMA,QLambda.DEF_LAMBDA)
print 'defalpha '+str(QLambda.DEF_ALPHA)




