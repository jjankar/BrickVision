package janis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class RunApp {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
	}

	public static void main(String[] args) throws IOException {
		ImgTools tool=new ImgTools();
		
		Mat matImg = tool.makeMat("objects.jpg");
		Mat matBack = tool.makeMat("background.jpg");
		Imgproc.blur(matImg, matImg, new Size(7,7));
		Imgproc.blur(matBack, matBack, new Size(7,7));
		//--------------------------------------------------------------------------//
		//removing noise
		Imgproc.cvtColor( matImg, matImg, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor( matBack, matBack, Imgproc.COLOR_RGB2GRAY);
		//-------------------------------------------------------------------------//
		//make gray scale
		matImg = tool.removeBackGround(matImg, matBack);
		//-------------------------------------------------------------------------//
		//remove background
		Imgproc.threshold(matImg, matImg, 1, 255, 0);
		//------------------------------------------------------------------------//
		BufferedImage image = tool.makeBuffImageFromMat(matImg);
		image = tool.makeBinaryImg(image);
		//-------------------------------------------------------------------------//
		//make binary image
		File ouptut = new File("GG.jpg");
        ImageIO.write(image, "jpg", ouptut);
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
