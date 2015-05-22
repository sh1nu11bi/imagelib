package imagelib.ico;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.common.io.LittleEndianDataOutputStream;

public class IcoEncoder {

	public static void encode(BufferedImage read, OutputStream os) {
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		images.add(read);
		
		encode(read, os);
	}
	
	public static void encode(List<BufferedImage> images, OutputStream os) throws Exception {
		LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(os);
		
		dos.writeShort(0);
		dos.writeShort(1);
		dos.writeShort(images.size());
		
		List<Image> wImages = new ArrayList<Image>();
		
		for (int i = 0; i < images.size(); i++) {
			BufferedImage image = images.get(i);
			
			int width = image.getWidth();
			
			if (width == 256) {
				width = 0;
			}
			
			if (width > 255) {
				throw new Exception("Width is over 256");
			}
			
			int height = image.getHeight();
			
			if (height == 256) {
				height = 0;
			}
			
			if (height > 255) {
				throw new Exception("Height is over 256");
			}
			
			int size = 8;
			
			dos.writeByte(width);
			dos.writeByte(height);
			
			byte palette = 0; // TODO
			dos.writeByte(palette);
			dos.writeByte(0); // reserved
			
			byte colorpanes = 0; // TODO
			dos.writeShort(colorpanes);
			
			byte bitsperpixel = 0; // TODO
			dos.writeShort(bitsperpixel);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			
			byte[] bImage = baos.toByteArray();
			
			int offset = 6 + size * images.size() + bImage.length;
			
			wImages.add(new Image(offset, bImage));
			
			dos.writeInt(bImage.length);
			dos.writeInt(offset);
		}
		
		for (int i = 0; i < wImages.size(); i++) {
			Image image = wImages.get(i);
			
			dos.write(image.image);
		}
		
		dos.close();
	}
	
	private static class Image {
		
		private int offset;
		private byte[] image;
		
		public Image(int offset, byte[] image) {
			this.offset = offset;
			this.image = image;
		}
	}
}
