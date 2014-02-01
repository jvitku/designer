package design.ea.encoding;

/**
 * Encoding transforms genotype to phenotype and the other way. 
 * 
 * Typically, it converts one data structure structure to another (e.g. vector of real 
 * values to a weight matrix).
 * 
 * @author Jaroslav Vitku
 *
 */
public interface Encoding {
	
	/**
	 * Encode phenotype to genotype
	 * @param phenotype representation of solution (architecture)
	 * @return genotype representation of given phenotype (to be used by the EA)
	 */
	public Structure encode(Structure phenotype);
	
	/**
	 * Decode genotype and return a representation of the solution
	 * @param genotype genotype to be decoded
	 * @return representation of some solution, e.g. architecture
	 */
	public Structure decode(Structure genotype);

}
