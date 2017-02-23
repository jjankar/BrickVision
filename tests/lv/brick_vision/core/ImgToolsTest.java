package lv.brick_vision.core;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ImgToolsTest {

	@Test
	public final void testImg2Mat() throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		ImgTools tools = new ImgTools();
		tools.img2Mat("GG.png");
		tools.img2Mat("IMG_5737.png");
		tools.img2Mat("dice5.jpg");
	}

	@Test
	public final void testImg2Mat_CV_8UC3() throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		ImgTools tools = new ImgTools();
		tools.img2Mat("GG.png");
		tools.img2Mat("IMG_5737.png");
		tools.img2Mat("dice5.jpg");
	}

	@Test
	public final void testMat2BuffImgGray() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		ImgTools tools = new ImgTools();
		Mat mat1 = new Mat(100, 100, CvType.CV_8UC1);

		BufferedImage img = tools.mat2BuffImgGray(mat1);
		Mat mat2 = tools.buffImg2Mat_CV_8UC1(img);
		for (int x = 0; x < mat1.cols(); x++) {
			for (int y = 0; y < mat1.rows(); y++) {
				double[] d1 = mat1.get(x, y);
				double[] d2 = mat2.get(x, y);
				assertTrue(d1[0] == d2[0]);
			}
		}

	}

	@Test
	public final void testRemoveBackGround() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testBuffImg2Mat_CV_8UC3() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testBuffImg2Mat_CV_8UC1() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		ImgTools tools = new ImgTools();
		Mat mat1 = new Mat(100, 100, CvType.CV_8UC1);

		BufferedImage img = tools.mat2BuffImgGray(mat1);
		Mat mat2 = tools.buffImg2Mat_CV_8UC1(img);
		for (int x = 0; x < mat1.cols(); x++) {
			for (int y = 0; y < mat1.rows(); y++) {
				double[] d1 = mat1.get(x, y);
				double[] d2 = mat2.get(x, y);
				assertTrue(d1[0] == d2[0]);
			}
		}
	}

	@Test
	public final void testMakeBinaryImg() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetLebelsArray() {
		fail("Not yet implemented"); // TODO
	}

}
