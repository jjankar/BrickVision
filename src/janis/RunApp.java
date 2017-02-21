package janis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class RunApp {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
	}

	public static void main(String[] args) throws IOException {
		ImgTools tool=new ImgTools();
		
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Mat matImg = tool.img2Mat("objects6(3).png");
		
		Mat matBack = tool.img2Mat("background.png");
		System.out.println("col: "+ matImg.cols()+" row: "+matImg.rows());
		Imgproc.blur(matImg, matImg, new Size(7,7));
		Imgproc.blur(matBack, matBack, new Size(7,7));
		
		//--------------------------------------------------------------------------//
		//removing noise COLOR_RGB2GRAY
		/*Imgproc.cvtColor( matImg, matImg, Imgproc.COLOR_BGR5652GRAY);
		Imgproc.cvtColor( matBack, matBack, Imgproc.COLOR_BGR5652GRAY);*/
		//-------------------------------------------------------------------------//
		//make gray scale
		matImg = tool.removeBackGround(matImg, matBack);
		Imgproc.findContours(matImg, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		System.out.println("Contures: "+contours.size());
		BufferedImage image1 = tool.makeBuffImageFromMat(matImg);
		File ouptut = new File("YY.png");
        ImageIO.write(image1, "png", ouptut);
		//binary matrice
		 System.out.println("Binary image is created!");
		//-------------------------------------------------------------------------//
		//remove background
		Imgproc.threshold(matImg, matImg, 10, 255, 0);
		//------------------------------------------------------------------------//
		BufferedImage image = tool.makeBuffImageFromMat(matImg);
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
		List list = tool.getLebelsArray(num, matLeb);
	    
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
