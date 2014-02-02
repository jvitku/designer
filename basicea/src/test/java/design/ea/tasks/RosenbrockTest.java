package design.ea.tasks;

import static org.junit.Assert.*;

import org.junit.Test;

public class RosenbrockTest {

	@Test
	public void optimum(){
		System.out.println("Rosenbrock fcn. value in [1,1] is: "
				+Rosenbrock.eval(0f, 0f));
		
		assertTrue(Rosenbrock.eval(1f, 1f)==0);
		
		assertTrue(Rosenbrock.eval(0f, 0f)==1);
		assertTrue(Rosenbrock.eval(2f, 2f)==401);
		assertTrue(Rosenbrock.eval(-2f, -2f)==3609);
		assertTrue(Rosenbrock.eval(-2f, 2f)==409);
	}

}
