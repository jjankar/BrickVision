package lv.brick_vision.core;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * @author <a href="mailto:jjankar@gmail.com">Janis Karklins</a>
 * @description This class contains core methods to manipulate with mat and
 *              images using opencv library
 * 
 * @version 1.0 (2017-02-18)
 * 
 */
public class ImgTools {

	
	/**
	 * @param file file path string. better method for any type of images
	 * @return out Opencv Mat object 
	 * @throws IOException
	 * 
	 */
	public Mat img2Mat(String file) throws IOException {
		
		File input = new File(file);
		BufferedImage in = ImageIO.read(input);
		
        Mat out;
        byte[] data;
        int r, g, b;

        if (in.getType() == BufferedImage.TYPE_INT_RGB) {
            out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
            data = new byte[in.getWidth() * in.getHeight() * (int) out.elemSize()];
            int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
            for (int i = 0; i < dataBuff.length; i++) {
                data[i * 3] = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                data[i * 3 + 2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
            }
        } else {
            out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC1);
            data = new byte[in.getWidth() * in.getHeight() * (int) out.elemSize()];
            int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
            for (int i = 0; i < dataBuff.length; i++) {
                r = (byte) ((dataBuff[i] >> 0) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 16) & 0xFF);
                data[i] = (byte) ((0.21 * r) + (0.71 * g) + (0.07 * b));
            }
        }
        out.put(0, 0, data);
        return out;
    }
	
	
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
	public Mat img2Mat_CV_8UC3(String imgName) throws IOException {
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
	public BufferedImage mat2BuffImgGray(Mat mat) {
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
	 * @return img - without backGround and all colors of object minus background colors at the pixel
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
	public Mat buffImg2Mat_CV_8UC3(BufferedImage image) {

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
	public Mat buffImg2Mat_CV_8UC1(BufferedImage image) {

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
		Mat mat = this.buffImg2Mat_CV_8UC1(img);
		Imgproc.threshold(mat, mat, 1, 255, 0);

		return this.mat2BuffImgGray(mat);

	}

	/**
	 * @param objNum
	 *            Number of objects in image including background
	 * @param matLeb
	 *            Labeled mat from methods that counts objects
	 * @return list
	 * 			  0-area; 1-x; 2-y; 3-xsum; 4-ysum.
	 */
	@SuppressWarnings("rawtypes")
	public List getLebelsArray(int objNum, Mat matLeb) {

		List<int[]> list = new ArrayList<int[]>();
		int[] area = new int[objNum];
		int[] xsum = new int[objNum];
		int[] ysum = new int[objNum];
		int[] xcentr = new int[objNum];
		int[] ycentr = new int[objNum];

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
						xsum[i] = xsum[i] + x;
						ysum[i] = ysum[i] + y;
						break;
					}

				}
			}
		}

		for (int i = 0; i < objNum; i++) {
			xcentr[i] = xsum[i] / area[i];
			ycentr[i] = ysum[i] / area[i];
		}
		list.add(area);
		list.add(xcentr);
		list.add(ycentr);
		list.add(xsum);
		list.add(ysum);

		return list;

	}
	
	/**
	 * @param documenti That way we make documentation.
	 */
	public void testDoc(){
		
	}

}
