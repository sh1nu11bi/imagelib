package icolib;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Ico {
	
	private List<BufferedImage> images = new ArrayList<BufferedImage>();
	
	public Ico(File file) throws Exception {
		this(new BufferedInputStream(new FileInputStream(file)));
	}
	
	public Ico(InputStream in) throws Exception {
		this(new DataInputStream(in));
	}
	
	public Ico(DataInputStream dis) throws Exception {
		dis.mark(Integer.MAX_VALUE);
		
		short first = dis.readShort();
		assert first == 0; // always 0

		short format = dis.readShort();
		assert format == 1; // .ico
		
		short totalImages = dis.readShort();
		
		int pos = 6;
		
		for (short i = 0; i < totalImages; i++) {
			int width = dis.readByte();
			int height = dis.readByte();
			
			pos += 2;
			
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
			
			pos += 6;
			
			int length = dis.readInt();
			int offset = dis.readInt();
			
			pos += 8;
			
			dis.reset();
			dis.skip(offset);
			
			byte[] image = new byte[length];
			dis.readFully(image);
			
			ByteArrayInputStream bais = new ByteArrayInputStream(image);
			images.add(ImageIO.read(bais));
			
			dis.reset();
			dis.skip(pos);
		}
	}

}
