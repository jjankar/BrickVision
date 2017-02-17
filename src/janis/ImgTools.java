package janis;



import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

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
         BufferedImage image = new BufferedImage(mat.cols(),mat.rows(), BufferedImage.TYPE_BYTE_GRAY);
         image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		
		return image;		
	}
	
	
	
	/**
	 * @param img
	 * @param backGround
	 * @return
	 */
	public Mat removeBackGround (Mat img, Mat backGround){
		
		Core.subtract (backGround, img, img);
		
		return img;
		
	}
	
	public Mat makeMatFromBuffImg(BufferedImage image){
		
		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat matImg = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        matImg.put(0, 0, data);
        
		return matImg;
		
	}
	
	public Mat makeMatFromBuffImgGrey(BufferedImage image){
		
		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat matImg = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
        matImg.put(0, 0, data);
        
		return matImg;
		
	}
	
	
	
	/**
	 * @param img
	 * @return
	 */
	public BufferedImage makeBinaryImg(BufferedImage img){
		Mat mat = this.makeMatFromBuffImgGrey(img);
		//cvtColor(imageMat, grayscaleMat, CV_RGB2GRAY);
		//threshold(grayscaleMat, binaryMat, 100, 255, CV_THRESH_BINARY);
		Imgproc.threshold(mat, mat, 1, 255, 0);		
		//TODO need to add
		return this.makeBuffImageFromMat(mat);
		
	}
}
