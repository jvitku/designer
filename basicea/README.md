Set of Algorithms Purposed to Design Hybrid Artificial Neural Network Systems
====================================================

@author Jaroslav Vitku [vitkujar@fel.cvut.cz]

About
------

This `Designer` is used for optimization of topology of in Hybrid Artificial Neural Network Systems, used in the [Nengoros simulator](http://nengoros.wordpress.com). 


Purpose
---------

This is newer version of `annvshnn` project, which is more domain independent and features nicer implementation. 

The project `annvshnn` was used in [1] for comparing design of ANNs vs Hybrid ANNs. 

The project implements basic Evolutionary Algorithm (EA), which optimizes vector of real-valued numbers. These numbers represent specific connection weights in an agent architecture. By modifying these weights, new HANNS topology is created - new agent architecture is designed. 

[1] J. Vítků and P. Nahodil, “Autonomous Design of Modular Intelligent Systems,” in 27th European Conference on Modelling and Simulation ECMS 2013, Alesund, 2013, pp. 379-389, ISBN: 978-0-9564944-6-7.


Requirements
------------------
Complete list of dependencies can be found in the `build.gradle` file, some of the main requirements are:

* Nengoros [core](https://github.com/jvitku/nengoros).

Installation
------------------

Installation can be obtained by running the following command

	./gradlew installApp

In case of any problems, the best way how to use these nodes is by means of the NengoRos project (see: [https://github.com/jvitku/nengoros](https://github.com/jvitku/nengoros) )

