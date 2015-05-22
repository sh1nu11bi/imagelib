package imagelib;

import java.io.File;

public class Debug {
	
	public static void main(String[] args) {
		try {
			IcoDecoder.decode(new File("test.ico"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
