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
 * @author <a href="mailto:luigi.derussis@polito.it">Janis Karklins</a>
 * @description This class contains core methods to manipulate with mat and
 *              images using opencv library
 *              
 * @version 1.1 (2017-02-18)
 * 
 */
public class ImgTools {

	/**
	 * @param imgName
	 *            is jpg file name like dice.jpg
	 * @return Mat object from dice.jpg CvType.CV_8UC3
	 * 
	 * @throws IOException
	 *             problems could be with file or Mat CvType
	 * 
	 * 
	 */
	public Mat makeMat(String imgName) throws IOException {
		File input = new File(imgName);
		BufferedImage image = ImageIO.read(input);
		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat matImg = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		matImg.put(0, 0, data);

		return matImg;
	}

	/**
	 * @param mat
	 * @return bufferedImage type is set to TYPE_BYTE_GRAY
	 */
	public BufferedImage makeBuffImageFromMat(Mat mat) {
		byte[] data = new byte[mat.rows() * mat.cols() * (int) (mat.elemSize())];
		mat.get(0, 0, data);
		BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_BYTE_GRAY);
		image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);

		return image;
	}

	/**
	 * @param img
	 *            mat with background and objects
	 * @param backGround
	 *            mat only background
	 * @return img - backGround
	 */
	public Mat removeBackGround(Mat img, Mat backGround) {

		Core.subtract(backGround, img, img);

		return img;

	}

	/**
	 * @param image
	 *            buffered image
	 * @return mat with CvType.CV_8UC3
	 */
	public Mat makeMatFromBuffImg(BufferedImage image) {

		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat matImg = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		matImg.put(0, 0, data);

		return matImg;

	}

	/**
	 * @param image
	 *            buffered image
	 * @return mat with CvType.CV_8UC1
	 */
	public Mat makeMatFromBuffImgGrey(BufferedImage image) {

		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat matImg = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
		matImg.put(0, 0, data);

		return matImg;

	}

	/**
	 * @param img
	 *            buffered image
	 * @return mat with threshold 1 set color 255 ant type is binary
	 */
	public BufferedImage makeBinaryImg(BufferedImage img) {
		Mat mat = this.makeMatFromBuffImgGrey(img);
		Imgproc.threshold(mat, mat, 1, 255, 0);

		return this.makeBuffImageFromMat(mat);

	}

	/**
	 * @param objNum
	 *            numberof objects in image including background
	 * @param matLeb
	 *            lebelet mat from methods that counts objects
	 * @return int array that contains all object pixel area
	 */
	public int[] getLebelsArray(int objNum, Mat matLeb) {
		
		int[] area = new int[objNum];
		//int[] centr = new int[3]; //0 - segment num, 1- x cordinate, 2-y cordinate, 3 - xsum, 4-
		double[] d;
		int w = matLeb.cols();
		int h = matLeb.rows();
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				d = matLeb.get(y, x);
				double pixel = d[0];

				for (int i = 0; i <= objNum; i++) {
					if (i == pixel) {
						area[i]++;
					}
				}
			}
		}

		return area;

	}

}
