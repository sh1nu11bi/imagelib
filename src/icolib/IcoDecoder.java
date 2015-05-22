package icolib;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.common.io.LittleEndianDataInputStream;

public class IcoDecoder {
		
	public IcoDecoder(File file) throws Exception {
		this(new BufferedInputStream(new FileInputStream(file)));
	}
	
	public IcoDecoder(InputStream in) throws Exception {
		this(new LittleEndianDataInputStream(in));
	}
	
	public IcoDecoder(LittleEndianDataInputStream dis) throws Exception {
		dis.mark(Integer.MAX_VALUE);
		
		short first = dis.readShort();
		assert first == 0; // always 0

		short format = dis.readShort();
		assert format == 1; // .ico
		
		short totalImages = dis.readShort();
		
		int pos = 6;
		
		List<Image> images = new ArrayList<Image>();
		
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
			
			images.add(new Image(length, offset));
			
			pos += 8;
		}
		
		for (int i = 0; i < images.size(); i++) {
			Image image = images.get(i);
			dis.reset();
			dis.skip(image.offset);
			
			byte[] bImage = new byte[image.length];
			dis.readFully(bImage);
					
			ByteArrayInputStream bais = new ByteArrayInputStream(bImage);
			BufferedImage bufferedImage = ImageIO.read(bais);
			
			image.setImage(bufferedImage);
			
			if (bufferedImage != null) {
				ImageIO.write(bufferedImage, "png", new File("test" + i + ".png"));
			}
		}
	}
	
	public class Image {
		
		private int length;
		private int offset;
		private BufferedImage image;
		
		public Image(int length, int offset) {
			this.length = length;
			this.offset = offset;
		}
		
		public void setImage(BufferedImage image) {
			this.image = image;
		}
		
		public BufferedImage getImage() {
			return this.image;
		}
	}

}
