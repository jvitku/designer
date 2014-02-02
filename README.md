Set of Algorithms Purposed to Design Hybrid Artificial Neural Network Systems
====================================================

Author Jaroslav Vitku [vitkujar@fel.cvut.cz]

About
------

This `Designer` is used for optimization of topology of in Hybrid Artificial Neural Network Systems, used in the [Nengoros simulator](http://nengoros.wordpress.com). 

This project serves as place for first types of algorithms used for designing hybrid systems, that is: designing topologies of Hybrid Artificial Neural Network Systems (HANNS).

The first project - `basicea` implements basic Evolutionary Algorithm (EA), which optimizes vector of real-valued numbers. These numbers represent specific connection weights in an agent architecture. By modifying these weights, new HANNS topology is created - new agent architecture is designed. 

Requirements
------------------
Complete list of dependencies can be found in the `build.gradle` file, some of the main requirements are:

* Rosjava [core](https://github.com/jvitku/nengoros).
* Jros[core](https://github.com/jvitku/jroscore).


Installation
------------------

Installation can be obtained by running the following command

	./gradlew installApp

In case of any problems, the best way how to use these nodes is by means of the NengoRos project (see: [https://github.com/jvitku/nengoros](https://github.com/jvitku/nengoros) )

