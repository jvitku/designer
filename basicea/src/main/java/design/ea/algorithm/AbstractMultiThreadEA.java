package design.ea.algorithm;

/**
 * Multi-threaded version of the AbstractSingleThreadEA
 * 
 * @author Jaroslav Vitku
 *
 */
public class AbstractMultiThreadEA extends AbstractSingleThreadEA{

	public AbstractMultiThreadEA(int vectorLength, int generations, int popSize) {
		super(vectorLength, generations, popSize);
	}

	public AbstractMultiThreadEA(int vectorLength, boolean minimize, int generations, int popSize) {
		super(vectorLength, minimize, generations, popSize);
	}

}
