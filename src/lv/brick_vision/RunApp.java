package lv.brick_vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import lv.brick_vision.core.ImgTools;

public class RunApp {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
	}

	public static void main(String[] args) throws IOException {
		ImgTools tool=new ImgTools();
		
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Mat matImg = tool.img2Mat("IMG_5737.png");
		//Mat matImg = tool.img2Mat("dice13.png");
		//Mat matBack = tool.img2Mat("GG0.png");
		System.out.println("col: "+ matImg.cols()+" row: "+matImg.rows());
		
		
		
		Imgproc.blur(matImg, matImg, new Size(7,7));
		//Imgproc.medianBlur(matImg, matImg, 7);
		
		int erosion_size = 10;
		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new  Size(2*erosion_size + 1, 2*erosion_size+1));
        Imgproc.erode(matImg, matImg, element);
		
		/*int dilation_size = 3;
		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new  Size(2*dilation_size + 1, 2*dilation_size+1));
        Imgproc.dilate(matImg, matImg, element);*/
		//Imgproc.dia
		//Imgproc.blur(matBack, matBack, new Size(7,7));
		
		//--------------------------------------------------------------------------//
		//removing noise COLOR_RGB2GRAY
		/*Imgproc.cvtColor( matImg, matImg, Imgproc.COLOR_BGR5652GRAY);
		Imgproc.cvtColor( matBack, matBack, Imgproc.COLOR_BGR5652GRAY);*/
		//-------------------------------------------------------------------------//
		//make gray scale
		//matImg = tool.removeBackGround(matImg, matBack);
		Imgproc.findContours(matImg, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		System.out.println("Contures: "+contours.size());
		BufferedImage image1 = tool.mat2BuffImgGray(matImg);
		File ouptut = new File("YY.png");
        ImageIO.write(image1, "png", ouptut);
		//binary matrice
		 System.out.println("Binary image is created!");
		//-------------------------------------------------------------------------//
		//remove background
		Imgproc.threshold(matImg, matImg, 20, 255, 1);
		//------------------------------------------------------------------------//
		BufferedImage image = tool.mat2BuffImgGray(matImg);
		image = tool.makeBinaryImg(image);
		//-------------------------------------------------------------------------//
		//make binary image
		ouptut = new File("GG.png");
        ImageIO.write(image, "png", ouptut);
		//binary matrice
		 System.out.println("Binary image is created!");
	    //-----------------------------------------------------------------------------//
	    //end of binary
		Mat matLeb = new Mat();
		int num = Imgproc.connectedComponents(matImg, matLeb);
		System.out.println("Labeled Matrice is created!");
		System.out.println(num-1);
		
		//-------------------------------------------------------------------------------//
		//get and print object pixel area
		@SuppressWarnings("unchecked")
		List<int []> list = tool.getLebelsArray(num, matLeb);
	    int [] x= list.get(1);
	    int [] y= list.get(2);
	    Mat mat = tool.buffImg2Mat_CV_8UC1(image);
	    for (int j = 0; j < x.length; j++) {
	    	Imgproc.rectangle(mat, new Point(x[j], y[j]), new Point(x[j]+ 20, y[j] + 20), new Scalar(0,0,0), 5);
		
	    }
	    
	    BufferedImage image3 = tool.mat2BuffImgGray(mat);
	    File ouptut1 = new File("rect.png");
        ImageIO.write(image3, "png", ouptut1);
	    
	    
	    for (int i = 0; i < list.size(); i++) {
	    	int[] area=(int[]) list.get(i);
			for (int j = 0; j < area.length; j++) {
				System.out.println("Value " + j + " is " + area[j] + " val");
			}
			System.out.println("-----------------------------------------------------------");
		}	
	    
	    //------------------------------------------------------------------------------//
	    
	
        
        
        
       
        
        
        
        
        
	}

}
