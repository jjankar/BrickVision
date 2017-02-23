package lv.brick_vision.video.edge;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import lv.brick_vision.video.Utils;

/**
 * The application logic is implemented here. It handles the button for
 * starting/stopping the camera and button to use image, the acquired video stream or image, the relative
 * controls.
 *
 * @author Original program author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>; Modified by Kaspars Bulindzs,
 *  Janis Karklins, Dmitrijs Ozerskis, Andrejs Paramonovs, Andrejs Derevjanko, Antons Kalcevs.  
 * @version 1.0 (2017-02-24)
 * @since 1.0 (2017-02-24)
 *
 */
public class ObjRecognitionController {
    // camera and picture buttons
    @FXML
    private Button cameraButton;
    @FXML
    private Button pictureButton;
    // the FXML area for showing the current frame
    @FXML
    private ImageView originalFrame;
    // the FXML area for showing the output of the canny operations
    @FXML
    private ImageView cannyImage;
    // FXML slider for parameters
    @FXML
    private Slider dpSlider;
    @FXML
    private Slider minDistSlider;
    @FXML
    private Slider dilateSlider;
    @FXML
    private Slider erodeSlider;
    @FXML
    private Slider accumulatorSlider;
    @FXML
    private Slider minRadiusSlider;
    @FXML
    private Slider maxRadiusSlider;
    // FXML label to show the current values set with the sliders
    @FXML
    private Label parameterValues;
    @FXML
    private Label radiusValues;
    @FXML
    private Label resultValue;
    
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that performs the video capture
    private VideoCapture capture = new VideoCapture();
    // a flag to change the button behavior
    private boolean cameraActive;
    private boolean pictureActive;
    
    // property for object binding
    private ObjectProperty<String> parameterValuesProp;
    private ObjectProperty<String> radiusValuesProp;
    private ObjectProperty<String> resultValueProp;
   	 
    /**
     * The action triggered by pushing the button "Select Camera" on the GUI
     */
    @FXML
    private void startCamera() {
   	 // bind a text property with the string containing the current properties
   	 parameterValuesProp = new SimpleObjectProperty<>();
   	 radiusValuesProp = new SimpleObjectProperty<>();
   	 resultValueProp = new SimpleObjectProperty<>();
   	 this.parameterValues.textProperty().bind(parameterValuesProp);
   	 this.radiusValues.textProperty().bind(radiusValuesProp);
   	 this.resultValue.textProperty().bind(resultValueProp);
   			 
   	 // set a fixed width for all the image to show and preserve image ratio
   	 this.imageViewProperties(this.originalFrame, 400);
   	 this.imageViewProperties(this.cannyImage, 400);
   	 
   	 if (!this.cameraActive) {
   		 // start the video capture
   		 this.capture.open(0);
   		 
   		 // is the video stream available?
   		 if (this.capture.isOpened()) {
   			 this.cameraActive = true;
   			 
   			 this.pictureActive = false;
   			 this.pictureButton.setText("Select Picture");
   			 
   			 // grab a frame
   			 Runnable frameGrabber = new Runnable() {
   				 
   				 @Override
   				 public void run() {
   					 Image imageToShow = grabFrame();
   					 originalFrame.setImage(imageToShow);
   				 }
   			 };
   			 this.timer = Executors.newSingleThreadScheduledExecutor();
   			 this.timer.scheduleAtFixedRate(frameGrabber, 0, 500, TimeUnit.MILLISECONDS);
   			 
   			 // update the button content
   			 this.cameraButton.setText("Stop Camera");
   		 }
   		 else {
   			 // log the error
   			 System.err.println("Failed to open the camera connection...");
   		 }
   	 }
   	 else {
   		 // the camera is not active at this point
   		 this.cameraActive = false;
   		 // update again the button content
   		 this.cameraButton.setText("Start Camera");
   		 
   		 
   		 // stop the timer
   		 try {
   			 this.timer.shutdown();
   			 this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
   		 }
   		 catch (InterruptedException e) {
   			 // log the exception
   			 System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
   		 }
   		 // release the camera
   		 this.capture.release();
   	 }
    }
    
    /**
     * The action triggered by pushing the button "Select Picture" on the GUI
     */    
    @FXML
    private void selectPicture() {
   	 // bind a text property with the string containing the current properties
   	 parameterValuesProp = new SimpleObjectProperty<>();
   	 radiusValuesProp = new SimpleObjectProperty<>();
   	 resultValueProp = new SimpleObjectProperty<>();
   	 this.parameterValues.textProperty().bind(parameterValuesProp);
   	 this.radiusValues.textProperty().bind(radiusValuesProp);
   	 this.resultValue.textProperty().bind(resultValueProp);
   	 
   	 // set a fixed width for all the image to show and preserve image ratio
   	 this.imageViewProperties(this.originalFrame, 400);
   	 this.imageViewProperties(this.cannyImage, 400);
   	 
   	 if (!this.pictureActive) {
   		 this.pictureActive = true;
   		 
   		 FileChooser chooser = new FileChooser();
   		 FileChooser.ExtensionFilter filterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
   		 chooser.getExtensionFilters().addAll(filterPNG);
   		 final File file = chooser.showOpenDialog(null);
   		 
   		 // grab a frame
   		 Runnable frameGrabber = new Runnable() {
   			 
   			 @Override
   			 public void run() {
   				 Image imageToShow = grabImg(file);
   				 originalFrame.setImage(imageToShow);
   			 }
   		 };
   		 this.timer = Executors.newSingleThreadScheduledExecutor();
   		 this.timer.scheduleAtFixedRate(frameGrabber, 0, 1000, TimeUnit.MILLISECONDS);
   		 
   		 // update the button content
   		 this.pictureButton.setText("Pause");
   	 }
   	 else {
   		 // the camera is not active at this point
   		 this.pictureActive = false;
   		 // update again the button content
   		 this.pictureButton.setText("Select Picture");
   		 
   		 // stop the timer
   		 try {
   			 this.timer.shutdown();
   			 this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
   		 }
   		 catch (InterruptedException e) {
   			 System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
   		 }
   		 // release the camera
   		 this.capture.release();
   	 }   	 
    }
    
    /**
     * change image width and height, call method to find circles in image
     * @param file
     *        	input file selected png image
     * @return
     *        	image with found circles
     */
    private Image grabImg(File file) {
   	 Mat frame = new Mat();
   	 Image imageToShow = null;
   	 
   	 try {
   		 String path = file.getAbsolutePath();
   		 frame = Imgcodecs.imread(path);
   		 
   		 int originWidth = frame.width();
   		 int originHeight = frame.height();
   		 
   		 if (originWidth > 640 && originHeight > 480){
   			 if (originWidth > originHeight || originWidth == originHeight) {
   				 Imgproc.resize(frame, frame, new Size(640, 640*originHeight/originWidth));
   			 }
   			 else if (originWidth < originHeight) {
   				 Imgproc.resize(frame, frame, new Size(480*originWidth/originHeight, 480));
   			 }
   		 }
   		 // find circles and show them
   		 frame = this.findCircles(frame);
   		 // find Rectangle
   		 frame = this.findRect(frame);
   		 
   		 // convert the Mat object (OpenCV) to Image (JavaFX)
   		 imageToShow = Utils.mat2Image(frame);
   	 } catch (Exception e) {
   		 System.err.print("ERROR");
   		 e.printStackTrace();
   	 }
   	 return imageToShow;
    }
    
    /**
     * change frame width and height, call method to find circles in frame
     * @return
     *        	frame with found circles
     */
    private Image grabFrame() {
   	 Image imageToShow = null;
   	 Mat frame = new Mat();
   	 
   	 int originWidth = frame.width();
   	 int originHeight = frame.height();
   	 
   	 if (originWidth > 640 && originHeight > 480){
   		 if (originWidth > originHeight || originWidth == originHeight) {
   			 Imgproc.resize(frame, frame, new Size(640, 640*originHeight/originWidth));
   		 }
   		 else if (originWidth < originHeight) {
   			 Imgproc.resize(frame, frame, new Size(480*originWidth/originHeight, 480));
   		 }
   	 }
   	 
   	 if (this.capture.isOpened()) {
   		 try {
   			 // read the current frame
   			 this.capture.read(frame);
   			 
   			 // if the frame is not empty, process it
   			 if (!frame.empty()) {
   				 // count circles and show them
   				 frame = this.findCircles(frame);
   				 frame = this.findRect(frame);
   				 
   				 // convert the Mat object (OpenCV) to Image (JavaFX)
   				 imageToShow = Utils.mat2Image(frame);
   			 }
   		 }
   		 catch (Exception e) {
   			 System.err.print("ERROR");
   			 e.printStackTrace();
   		 }
   	 }
   	 return imageToShow;
    }
    
    /**
     *
     * @param frame
     *       	 
     * @return
     *       	 
     */
    private Mat findCircles(Mat frame) {
   	 Mat gray = new Mat();
   	 Mat circles = new Mat();
   	 Mat edges = new Mat();
   	 Mat morph = new Mat();
   	 
   	 // vector to count all circles
   	 Vector<Mat> circlesList = new Vector<Mat>();
   	 
   	 double lowThreshold = 240;
   	 double highThreshold = 255;
   	 
   	 // inverse ratio of the accumulator resolution to the image resolution
   	 double dp = this.dpSlider.getValue();
   	 // min distance between circle centers
   	 double minDist = this.minDistSlider.getValue();
   	 
   	 // separate out regions of an image corresponding to objects which we want to analyze
   	 
   	 // based on the variation of intensity between object and background pixels
   	 double dilateElem = this.dilateSlider.getValue();
   	 double erodeElem = this.erodeSlider.getValue();
   	 // threshold for the circle centers at the detection stage
   	 double accumulator = this.accumulatorSlider.getValue();   	 
   	 // min/max Radius
   	 double minRadius = this.minRadiusSlider.getValue();
   	 double maxRadius = this.maxRadiusSlider.getValue();
   	 
   	 
   	 
   	 // make frame 8-bit single-channel, blur it
   	 Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
   	 Imgproc.medianBlur(gray, gray, 3);
   					 
   	 // find edges
   	 Imgproc.Canny(gray, edges, lowThreshold, highThreshold);
   	 
   	 // morphological operators, dilate with large element, erode with small ones
   	 Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(dilateElem, dilateElem));
   	 Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(erodeElem, erodeElem));
   	 
   	 Imgproc.erode(edges, morph, erodeElement);
   	 Imgproc.erode(edges, morph, erodeElement);
   	 Imgproc.erode(edges, morph, erodeElement);
   	 Imgproc.erode(edges, morph, erodeElement);
   	 
   	 Imgproc.dilate(edges, morph, dilateElement);
   	 Imgproc.dilate(edges, morph, dilateElement);
   	 Imgproc.dilate(edges, morph, dilateElement);
   	 Imgproc.dilate(edges, morph, dilateElement);
   	 
   	 
   	 // display canny image
   	 Utils.onFXThread(this.cannyImage.imageProperty(), Utils.mat2Image(morph));
   	 
   	 // find circles
   	 Imgproc.HoughCircles(morph, circles, Imgproc.CV_HOUGH_GRADIENT, dp, minDist, 255, accumulator,
   			 (int) minRadius, (int) maxRadius);
   	 
   	 // coordinates of circle center and circle radius
   	 double x = 0.0;
   	 double y = 0.0;
   	 int r = 0;
   	 
   	 for (int i = 0; i < circles.cols(); i++) {
   		 double data[] = circles.get(0, i);
   		 if (data == null)
   			 break;
   		 
   		 for (int j = 0; j < data.length; j++) {
   			 x = data[0];
   			 y = data[1];
   			 r = (int) data[2];
   		 }
   		 Point center = new Point(x, y);
   		 // draw circle center
   		 Imgproc.circle(frame, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
   		 // draw circle outline
   		 Imgproc.circle(frame, center, r, new Scalar(0, 0, 255), 1, 16, 0);
   		 
   		 System.out.println("r: " + r);
   		 // count all circles
   		 circlesList.add(circles);
   	 }
   	 
   	 // display values
   	 String valuesToPrint = "dp: " + String.format("%.1f", dp) + "\tmin Dist: " + String.format("%.1f", minDist) +
   			 "\tdilate: " + String.format("%.0f", dilateElem) + "\terode: " +
   			 String.format("%.0f", erodeElem) + "\taccumulator: " + String.format("%.1f", accumulator);
   	 String radiusToPrint = "min Radius: " + String.format("%.1f", minRadius) + "\tmax Radius: " + String.format("%.1f", maxRadius);
   	 String resultToPrint = "Result: " + String.format("%d", circlesList.size());
   	 Utils.onFXThread(this.parameterValuesProp, valuesToPrint);
   	 Utils.onFXThread(this.radiusValuesProp, radiusToPrint);
   	 Utils.onFXThread(this.resultValueProp, resultToPrint);
   	 
   	 System.out.println("\t");
   	 return frame;
    }
    
    @SuppressWarnings("unused")
	private Mat findRect(Mat frame) {
   	 
   	 Mat gray = new Mat();
   	 Mat edges = new Mat();
   	 Mat hierarchy = new Mat();
   	 Mat morph = new Mat();
   	 
   	 List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
   	 MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
   	 MatOfPoint2f approxCurve = new MatOfPoint2f();

   	 // based on the variation of intensity between object and background pixels
   	 double dilateElem = this.dilateSlider.getValue();
   	 double erodeElem = this.erodeSlider.getValue();
   	 
   	 

   	 // make frame 8-bit single-channel, blur it
   			 Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
   			 Imgproc.medianBlur(gray, gray, 3);
   			 // Imgproc.blur(gray, gray, new Size(7, 7));
   			 // Imgproc.equalizeHist(gray, gray);
   			 // Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0, 0, Core.BORDER_DEFAULT);
   	 
  	//display Canny image
 			 Imgproc.Canny(gray, edges, 50, 255);
   	 
   	 
	// morphological operators, dilate with large element, erode with small ones
   	 Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(dilateElem, dilateElem));
   	 Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(erodeElem, erodeElem));
   	 
   	 Imgproc.erode(edges, morph, erodeElement);
   	 Imgproc.erode(edges, morph, erodeElement);
   	 Imgproc.erode(edges, morph, erodeElement);
   	 Imgproc.erode(edges, morph, erodeElement);
   	 
   	 Imgproc.dilate(edges, morph, dilateElement);
   	 Imgproc.dilate(edges, morph, dilateElement);
   	 Imgproc.dilate(edges, morph, dilateElement);
   	 Imgproc.dilate(edges, morph, dilateElement);
 
    
   		 
   	 
   	 
   	 
 		 //find contours    
   	 Utils.onFXThread(this.cannyImage.imageProperty(), Utils.mat2Image(morph));
   						 
   	 Imgproc.findContours(morph, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
   	 
   	 
    	// Detect all angles from contours and try to stick them together
   	 for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
   	 	MatOfPoint contour = contours.get(idx);
   	 	Rect rect = Imgproc.boundingRect(contour);
   	 	double contourArea = Imgproc.contourArea(contour);
   	 	matOfPoint2f.fromList(contour.toList());
   	 	Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
   	 	long total = approxCurve.total();
   			 
   		 
   	 	if (total >= 3 && total <= 6) {
   	     	List<Double> cos = new ArrayList<>();
   	     	Point[] points = approxCurve.toArray();
   	     	for (int j = 2; j < total + 1; j++) {
   	         	cos.add(angle(points[(int) (j % total)], points[j - 2], points[j - 1]));
   	     	}    
   	     	if (total == 3) {
   	    		 Imgproc.line(frame, points[0], points[1], new Scalar(255, 255, 25), 5, 8, 0);
   	    		 Imgproc.line(frame, points[1], points[2], new Scalar(255, 255, 25), 5, 8, 0);
   	    		 Imgproc.line(frame, points[2], points[0], new Scalar(255, 255, 25), 5, 8, 0);
   		     	}
   	   	 
   	     	if (total == 4) {
   		     	//    Any FourAngle figure
   	     	//    Imgproc.rectangle(frame, points[0], points[2], new Scalar(255, 255, 25), 5, 8, 0);
   	    		 
   		     	}    
   	  	 
   	    	 
   	     	// Calculate Angles degrees for Rectangles and Plygons
   	     	Collections.sort(cos);
   	     	Double minCos = cos.get(0);
   	     	Double maxCos = cos.get(cos.size() - 1);
   	     	boolean isRect = total == 4 && minCos >= -0.8 && maxCos <= 0.8;
   	     	boolean isPolygon = (total == 5 && minCos >= -0.34 && maxCos <= -0.27) || (total == 6 && minCos >= -0.55 && maxCos <= -0.45);
   	     	if (isRect) {
   	    	//      Draw rectangle
   	     	//    Imgproc.rectangle(frame, points[0], points[2], new Scalar(255, 255, 25), 5, 8, 0);
   	    		 Imgproc.line(frame, points[0], points[1], new Scalar(255, 255, 25), 5, 8, 0);
   	    		 Imgproc.line(frame, points[1], points[2], new Scalar(255, 255, 25), 5, 8, 0);
   	    		 Imgproc.line(frame, points[2], points[3], new Scalar(255, 255, 25), 5, 8, 0);
   	    		 Imgproc.line(frame, points[3], points[0], new Scalar(255, 255, 25), 5, 8, 0);
   	    		 
   	     	}
   	    	 
   	     	if (isPolygon){
   	    		 // Some code for polygon
   	     	}
   	 	}
   	 }
   	 return frame;
}   	 
    
    /**
     * Set typical {@link ImageView} properties: a fixed width and the
     * information to preserve the original image ration
     *
     * @param image
     *        	the {@link ImageView} to use
     * @param dimension
     *        	the width of the image to set
     */
    private void imageViewProperties(ImageView image, int dimension) {
   	 // set a fixed width for the given ImageView
   	 image.setFitWidth(dimension);
   	 // preserve the image ratio
   	 image.setPreserveRatio(true);
    }
    
    
    // Angle point calculation
   	 private double angle(Point pt1, Point pt2, Point pt0) {
   	 	double dx1 = pt1.x - pt0.x;
   	 	double dy1 = pt1.y - pt0.y;
   	 	double dx2 = pt2.x - pt0.x;
   	 	double dy2 = pt2.y - pt0.y;
   	 	return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
   	 }
    
}