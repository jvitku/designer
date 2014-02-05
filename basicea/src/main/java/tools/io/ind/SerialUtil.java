package tools.io.ind;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerialUtil {

	public static void save(String filename, Object o){
		try{
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(o);
			out.close();
			fileOut.close();
			//System.out.println("Serialized data is saved in "+filename);
		}catch(IOException i){
			i.printStackTrace();
		}
	}

	public static Object read(String filename){
		try{
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Object e = in.readObject();
			in.close();
			fileIn.close();
			return e;
		}catch(IOException i){
			i.printStackTrace();
			return null;
		}catch(ClassNotFoundException c){
			//System.out.println("Object class not found");
			c.printStackTrace();
			return null;
		}
	}
}
