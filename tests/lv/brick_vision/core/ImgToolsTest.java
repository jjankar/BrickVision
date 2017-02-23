package lv.brick_vision.core;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

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
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		ImgTools tools = new ImgTools();
		int[][] intArray = new int[][]{{5,7},{8,9}};
		Mat mat = new Mat(2,2,CvType.CV_8UC1);
		for(int row=0;row<2;row++){
		   for(int col=0;col<2;col++)
		        mat.put(row, col, intArray[row][col]);
		}
		int[][] intArray1 = new int[][]{{7,5},{9,8}};
		Mat mat1 = new Mat(2,2,CvType.CV_8UC1);
		for(int row=0;row<2;row++){
		   for(int col=0;col<2;col++)
		        mat1.put(row, col, intArray1[row][col]);
		}
		mat = tools.removeBackGround(mat, mat1);
		
		
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

	@SuppressWarnings("rawtypes")
	@Test
	public final void testGetLebelsArray() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		ImgTools tools = new ImgTools();
		int[][] intArray = new int[][]{{0,0,1},{2,2,1},{2,2,3}};
		Mat mat = new Mat(3,3,CvType.CV_8UC1);
		for(int row=0;row<3;row++){
		   for(int col=0;col<3;col++)
		        mat.put(row, col, intArray[row][col]);
		}
		List list = tools.getLebelsArray(4, mat);
		//-----------------------------------------------------
		//pixel sum in mat
		int [] array = (int[]) list.get(0);
		assertEquals(2, array[0]);
		assertEquals(2, array[1]);
		assertEquals(4, array[2]);
		assertEquals(1, array[3]);
		//------------------------------------------------------
		//x centr
		array = (int[]) list.get(1);
		assertEquals(0, array[0]);
		assertEquals(2, array[1]);
		assertEquals(0, array[2]);
		assertEquals(2, array[3]);
		//-------------------------------------------------------
		//y centr
		array = (int[]) list.get(2);
		assertEquals(0, array[0]);
		assertEquals(0, array[1]);
		assertEquals(1, array[2]);
		assertEquals(2, array[3]);
		//-------------------------------------------------------
		//x sum
		array = (int[]) list.get(3);
		assertEquals(1, array[0]);
		assertEquals(4, array[1]);
		assertEquals(2, array[2]);
		assertEquals(2, array[3]);
		//-------------------------------------------------------
		//y sun
		array = (int[]) list.get(4);
		assertEquals(0, array[0]);
		assertEquals(1, array[1]);
		assertEquals(6, array[2]);
		assertEquals(2, array[3]);
		/*for (int i = 0; i < list.size(); i++) {
	    	int[] area=(int[]) list.get(i);
			for (int j = 0; j < area.length; j++) {
				System.out.println("Value " + j + " is " + area[j] + " val");
			}
			System.out.println("-----------------------------------------------------------");
		}	*/
	}

}
