package icolib;

import java.io.File;

public class Debug {
	
	public static void main(String[] args) {
		try {
			Ico ico = new Ico(new File("test.ico"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
