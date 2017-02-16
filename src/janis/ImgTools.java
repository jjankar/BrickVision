package janis;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * @author janis
 *
 */
public class ImgTools {
	
	
	
	/**
	 * @param imgName is jpg file name like dice.jpg
	 * @return Mat object from dice.jpg
	 * @throws IOException 
	 * 
	 * 
	 */
	public Mat makeMat (String imgName) throws IOException{
		File input = new File(imgName);
        BufferedImage image = ImageIO.read(input);	

        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat matImg = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        matImg.put(0, 0, data);
		
		return matImg;	
	}
	
	/**
	 * @param mat
	 * @return bufferedImage   TYPE_3BYTE_BGR
	 */
	public BufferedImage makeBuffImageFromMat(Mat mat){
		 byte[] data = new byte[mat.rows() * mat.cols() * (int)(mat.elemSize())];
         mat.get(0, 0, data);
         BufferedImage image = new BufferedImage(mat.cols(),mat.rows(), BufferedImage.TYPE_3BYTE_BGR);
         image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		
		return image;		
	}
	
	
	
	public Mat removeBackGround (Mat img, Mat backGround){
		
		Core.subtract (backGround, img, img);
		
		return img;
		
	}
	
	
	public BufferedImage makeBinaryImg(BufferedImage img){
		
		int iw = 0;
		int ih = 0;
		BufferedImage binary = new BufferedImage(iw, ih, BufferedImage.TYPE_BYTE_BINARY);
       /* Graphics biG = binary.getGraphics();
        Image gray = null;
		biG.drawImage(gray, 0, 0, null);
        biG.dispose();*/
		
		return null;
		
	}
}
