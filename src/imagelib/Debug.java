package imagelib;

import imagelib.ico.IcoDecoder;
import imagelib.ico.IcoEncoder;

import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class Debug {
	
	public static void main(String[] args) {
		try {
			IcoEncoder.encode(ImageIO.read(new File("test.png")), new FileOutputStream("testout.ico"));
			IcoDecoder.decode(new File("test.ico"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
