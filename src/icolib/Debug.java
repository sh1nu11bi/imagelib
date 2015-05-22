package icolib;

import java.io.File;

public class Debug {
	
	public static void main(String[] args) {
		try {
			IcoDecoder ico = new IcoDecoder(new File("test.ico"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
