package janis;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.io.File;
import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MatInOut {
   public static void main( String[] args ) { 
   
      try {
         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
         File input = new File("dice5.jpg");
         BufferedImage image = ImageIO.read(input);	

         byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
         Mat matImg = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
         matImg.put(0, 0, data);
          
         
         
         
         BufferedImage imgTempl = image.getSubimage(40, 40, 40, 40);
         byte[] dataT = ((DataBufferByte) imgTempl.getRaster().getDataBuffer()).getData();
         Mat matTempl = new Mat(imgTempl.getHeight(), imgTempl.getWidth(), CvType.CV_8UC3);
         matTempl.put(0, 0, dataT);
        

         Mat mat1 = new Mat();
         Imgproc.matchTemplate(matImg, matTempl, mat1, Imgproc.TM_CCOEFF_NORMED);
         
         System.out.println("Image Height: "+image.getHeight());
         System.out.println("Image Width: "+image.getWidth());
         
     
         
         System.out.println("Colons: "+mat1.cols());
         System.out.println("Rows: "+mat1.rows());
         
         
         for (int row = 0; row < mat1.rows(); row++) {
			for (int col = 0; col < mat1.cols(); col++) {
				double [] arry =mat1.get(row, col);
				
				for (double x:arry) {
					System.out.println(arry.length);
					System.out.println("row: " + row + " col: " + col + " rezult: " + x);
				}
				
			} 
		}
         
        /* byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int)(mat1.elemSize())];
         mat1.get(0, 0, data1);
         BufferedImage image1 = new BufferedImage(mat1.cols(),mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
         image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

         File ouptut = new File("YY.jpeg");
         ImageIO.write(image1, "jpeg", ouptut);*/
         
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
   }
}