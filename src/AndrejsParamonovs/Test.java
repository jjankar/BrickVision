/*package AndrejsParamonovs;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.io.File;
import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Test {
   public static void main( String[] args ) { 
   
      try {
         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
         File input = new File("grayscale.jpeg");
         BufferedImage image = ImageIO.read(input);	

         byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
         Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
         mat.put(0, 0, data);

         Mat mat1 = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC1);
         Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);

         byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int)(mat1.elemSize())];
         mat1.get(0, 0, data1);
         BufferedImage image1 = new BufferedImage(mat1.cols(),mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
         image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

         File output = new File("blackscale.jpeg");

         Imgproc.threshold(mat, mat1,60 ,160 ,Imgproc.THRESH_BINARY);
         BufferedImage image2 = new BufferedImage(mat1.cols(),mat1.rows(),BufferedImage.TYPE_BYTE_INDEXED );
         image1.getRaster().setDataElements(0, 0, mat1.cols(),mat1.rows(),data1 );
         
         ImageIO.write(image2, "jpeg",output);
      //   ImageIO.write(image1, "jpeg", output);
         
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
   }
}*/
package AndrejsParamonovs;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.io.File;
import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Test {
   public static void main( String[] args ) { 
   System.out.println("Hi! OK");
      try {
         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
         File input = new File("dice.jpeg");
         BufferedImage image = ImageIO.read(input);	

         byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
         Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
         mat.put(0, 0, data);

         Mat mat1 = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC1);
         Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);

         byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int)(mat1.elemSize())];
         mat1.get(0, 0, data1);
         BufferedImage image1 = new BufferedImage(mat1.cols(),mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
         image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

         File ouptut = new File("grayscale.jpeg");
         ImageIO.write(image1, "jpeg", ouptut);
         
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
   }
}