package janis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class RunApp {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
	}

	public static void main(String[] args) throws IOException {
		ImgTools tool=new ImgTools();
		
		Mat matImg = tool.makeMat("objects.jpg");
		Mat matBack = tool.makeMat("background.jpg");
		Imgproc.medianBlur(matImg, matImg, 3);
		Imgproc.medianBlur(matBack, matBack, 3);
		Imgproc.cvtColor( matImg, matImg, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor( matBack, matBack, Imgproc.COLOR_RGB2GRAY);
		matImg = tool.removeBackGround(matImg, matBack);
		
		BufferedImage image = tool.makeBuffImageFromMat(matImg);
		image = tool.makeBinaryImg(image);
		File ouptut = new File("YY.jpg");
        ImageIO.write(image, "jpg", ouptut);
        System.out.println("Process is done!");
	}

}
