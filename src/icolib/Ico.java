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
		dis.mark(Integer.MAX_VALUE);
		
		assert dis.readShort() == 0; // always 0
		assert dis.readShort() == 1; // .ico
		
		short images = dis.readShort();
		
		for (short i = 0; i < images; i++) {
			int width = dis.readByte();
			int height = dis.readByte();
			
			if (width == 0) {
				width = 256;
			}
			
			if (height == 0) {
				height = 256;
			}
			
			byte palette = dis.readByte();
			
			assert dis.readByte() == 0; // always 0
			
			short colorpanes = dis.readShort();
			short bitsperpixel = dis.readShort();
			
			int length = dis.readInt();
			int offset = dis.readInt();
			dis.reset();
			dis.skip(offset);
			
			byte[] image = new byte[length];
			dis.readFully(image);
		}
	}

}
