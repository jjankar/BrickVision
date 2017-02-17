package janis;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ObjectDetection {

public Mat makeImgLeb(Mat mat){
	Imgproc.connectedComponents(mat, mat);
	
	return mat;
	
}
	
	
	
}
