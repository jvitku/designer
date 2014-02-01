package design.ea.ind.genome;

import design.ea.encoding.Encoding;
import design.ea.encoding.Structure;

public interface Genome extends Structure{

	public Encoding getMyEncoding();
	
	public void setEncoding(Encoding e);
	
}
