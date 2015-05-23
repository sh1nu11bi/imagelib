package imagelib.ico;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.common.io.LittleEndianDataInputStream;

public class IcoDecoder {
	
	public static List<Image> decode(byte[] buffer) throws Exception {
		return decode(new ByteArrayInputStream(buffer));
	}
	
	/**
	 * Decode image from file
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static List<Image> decode(File file) throws Exception {
		return decode(new BufferedInputStream(new FileInputStream(file)));
	}
	
	/**
	 * Decode image from InputStream
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static List<Image> decode(InputStream in) throws Exception {
		return decode(new LittleEndianDataInputStream(in));
	}
	
	/**
	 * Decode image from LittleEndianInputStream
	 * @param dis
	 * @return
	 * @throws Exception
	 */
	public static List<Image> decode(LittleEndianDataInputStream dis) throws Exception {
		dis.mark(Integer.MAX_VALUE);
		
		short first = dis.readShort();
		assert first == 0; // always 0

		short format = dis.readShort();
		
		if (format != 1) {
			throw new Exception("No icon format detected" + (format == 2 ? ", is .cur" : ""));
		}
		
		short totalImages = dis.readShort();
				
		List<Image> images = new ArrayList<Image>();
		
		for (short i = 0; i < totalImages; i++) {
			int width = dis.readUnsignedByte();
			int height = dis.readUnsignedByte();
						
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
			
			images.add(new Image(length, offset));
		}
		
		for (int i = 0; i < images.size(); i++) {
			Image image = images.get(i);
			dis.reset();
			dis.skip(image.offset);		
			
			int info = dis.readInt();
			
			if (info == 0x28) {
				System.out.println("bitmap");
				byte[] bImage = new byte[image.length - 4];
				dis.readFully(bImage, 0, image.length - 4);
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream(image.length);
				DataOutputStream dos = new DataOutputStream(baos);
				dos.writeInt(info);
				dos.write(bImage);
				
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				BufferedImage bufferedImage = ImageIO.read(bais);
				
				image.setImage(bufferedImage);
				
				if (bufferedImage != null) {
					ImageIO.write(bufferedImage, "png", new File("test" + i + ".png"));
				}
			} else if (info == 0x474E5089) {
				System.out.println("png");
			} else {
				System.out.println(Integer.toHexString(info));
			}
		}
		
		dis.close();
		
		return images;
	}
	
	public static byte[] toBytes(int i) {
		byte[] result = new byte[4];

		result[0] = (byte) (i >> 24);
		result[1] = (byte) (i >> 16);
		result[2] = (byte) (i >> 8);
		result[3] = (byte) (i /* >> 0 */);

		return result;
	}
	
	public static class Image {
		
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
