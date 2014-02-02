package design.ea.tasks;

/**
 * <p>An implementation of the Rosnenbrock benchmark function.</p>
 * 
 * <p>The 2D function with one local extreme with value of 0 on 
 * the coordinates: [1,1]. Typically for range on X and Y axes
 * from [-2,2], the Z falls in range [0,2000].</p>
 * 
 * @see <a href="http://coco.gforge.inria.fr/lib/exe/fetch.php?media=mersmann2010.pdf">
 * Benchmarking Evolutionary Algorithms: Towards Exploratory Landscape Analysis</a>
 * @see <a href="http://mathworld.wolfram.com/RosenbrockFunction.html">
 * Wolfram Alpha - Rosenbrock function</a>
 * 
 * 
 * @author Jaroslav Vitku
 *
 */
public class Rosenbrock{
	
	public static double eval(Float x, Float y){
		double val = (1-x)*(1-x);
		double tmp = (y-x*x)*(y-x*x);
		return val+100*tmp;
	}
}
