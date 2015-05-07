package design.ea.algorithm.nengorosHeadless;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EvolutionLogger {

	private String fitnessName, bestIndsName;

	BufferedWriter fOut, bestOut;

	public EvolutionLogger(String fitnessName, String bestIndsName) throws IOException{
		this.fitnessName = fitnessName;
		this.bestIndsName = bestIndsName;

		FileWriter fitnessStream = new FileWriter(this.fitnessName, false);
		FileWriter bestIndStream = new FileWriter(this.bestIndsName, false);

		fOut = new BufferedWriter(fitnessStream);
		bestOut = new BufferedWriter(bestIndStream);
	}

	public void writefitness(int gen, double fitness){
		try {
			fOut.write(gen+", "+fitness+"\n");
			fOut.flush();
		} catch (IOException e) {
			System.err.println("cannot write to file "+this.fitnessName+" !");
			e.printStackTrace();
		}
	}

	public void writeBestInd(int gen, double fitness, Float[] genome){
		try {
			bestOut.write(gen+", "+fitness+",\t"+toStr(genome)+"\n");
			bestOut.flush();
		} catch (IOException e) {
			System.err.println("cannot write to file "+this.bestIndsName+" !");
			e.printStackTrace();
		}
	}

	public void exit(){
		try {
			bestOut.flush();
			bestOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try{
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String toStr(Float[] genome){
		String out = "";
		for(int i=0; i<genome.length; i++){
			if(i==genome.length-1){
				out += genome[i];
			}else{
				out += genome[i]+", ";
			}
		}
		return out;
	}
}
