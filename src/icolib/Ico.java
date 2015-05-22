package icolib;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Ico {
	
	public Ico(File file) throws Exception {
		this(new FileInputStream(file));
	}
	
	public Ico(InputStream in) throws Exception {
		this(new DataInputStream(in));
	}
	
	public Ico(DataInputStream dis) throws Exception {
		assert dis.readShort() == 0; // always 0
		assert dis.readShort() == 1; // .ico
		
		short images = dis.readShort();
		
		for (short i = 0; i < images; i++) {
			
		}
	}

}
