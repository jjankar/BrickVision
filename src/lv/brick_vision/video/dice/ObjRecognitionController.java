package lv.brick_vision.video.dice;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * The application logic is implemented here. It handles the button for
 * starting/stopping the camera and button to use image, the acquired video stream or image, the relative
 * controls.
 *
 * @author Original program author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>; Modified by 
 * Kaspars Bulindzs, Janis Karklins, Dmitrijs Ozerskis, Andrejs Paramonovs, Andrejs Derevjanko, Antons Kalcevs.  
 * @version 1.0 (2017-02-24)
 * @since 1.0 (2017-02-24)
 *
 */
public class ObjRecognitionController {
	// camera and image buttons
	@FXML
	private Button cameraButton;
	@FXML
	private Button imageButton;
	// the FXML area for showing the current frame
	@FXML
	private ImageView originalFrame;
	// the FXML area for showing the output of the canny operations
	@FXML
	private ImageView cannyImage;
	// FXML slider for parameters
	@FXML
	private Slider dilateSlider;
	@FXML
	private Slider dpSlider;
	@FXML
	private Slider minDistSlider;
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
	private Label resultValue;
	
	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that performs the video capture
	private VideoCapture capture = new VideoCapture();
	// a flag to change the button behavior
	private boolean cameraActive;
	private boolean imageActive;
	
	// property for object binding
	private ObjectProperty<String> parameterValuesProp;
	private ObjectProperty<String> resultValueProp;
		
	/**
	 * The action triggered by pushing the button "Select Camera" on the GUI
	 */
	@FXML
	private void startCamera() {
		// bind a text property with the string containing the current properties
		parameterValuesProp = new SimpleObjectProperty<>();
		resultValueProp = new SimpleObjectProperty<>();
		this.parameterValues.textProperty().bind(parameterValuesProp);
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
				
				// grab a frame
				Runnable frameGrabber = new Runnable() {
					
					@Override
					public void run() {
						Image imageToShow = grabFrame();
						originalFrame.setImage(imageToShow);
					}
				};
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 200, TimeUnit.MILLISECONDS);
				
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
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
			// release the camera
			this.capture.release();
		}
	}
	
	/**
	 * The action triggered by pushing the button "Select Image" on the GUI
	 */	
	@FXML
	private void selectImage() {
		// bind a text property with the string containing the current properties
		parameterValuesProp = new SimpleObjectProperty<>();
		resultValueProp = new SimpleObjectProperty<>();
		this.parameterValues.textProperty().bind(parameterValuesProp);
		this.resultValue.textProperty().bind(resultValueProp);
		
		// set a fixed width for all the image to show and preserve image ratio
		this.imageViewProperties(this.originalFrame, 400);
		this.imageViewProperties(this.cannyImage, 400);
		
		if (!this.imageActive) {
			this.imageActive = true;
			
			FileChooser chooser = new FileChooser();
			FileChooser.ExtensionFilter filterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
			chooser.getExtensionFilters().addAll(filterPNG);
			final File file = chooser.showOpenDialog(null);
			
			if (!(file == null)) {
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
				this.imageButton.setText("Pause");
			}
			else if (file == null) {
				// the camera is not active at this point
				this.imageActive = false;
				// update again the button content
				this.imageButton.setText("Select Image");
				this.timer.shutdown();
				// release the camera
				this.capture.release();
			}
		}
		else {
			// the camera is not active at this point
			this.imageActive = false;
			// update again the button content
			this.imageButton.setText("Select Image");
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
	 * change frame width and height, call method to find circles in frame
	 * @return
	 *            frame with found circles
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
					
					// convert the Mat object (OpenCV) to Image (JavaFX)
					imageToShow = mat2Image(frame);
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
	 * change image width and height, call method to find circles in image
	 * @param file
	 *            input file selected png image
	 * @return
	 *            image with found circles
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
			
			// convert the Mat object (OpenCV) to Image (JavaFX)
			imageToShow = mat2Image(frame);
		} catch (Exception e) {
			System.err.print("ERROR");
			e.printStackTrace();
		}
		return imageToShow;
	}
	
	/**
	 * method controls parameters with GUI sliders; finds circles after given parameters,
	 * count all found circles and draw them on the original frame/image
	 * @param frame
	 *            frame/image to analyze
	 * @return
	 *            original frame/image with drawn circles
	 */
	private Mat findCircles(Mat frame) {
		Mat gray = new Mat();
		Mat circles = new Mat();
		Mat edges = new Mat();
		Mat morph = new Mat();
		
		// vector to count all circles
		Vector<Mat> circlesList = new Vector<Mat>();
		
		// make frame 8-bit single-channel, blur it
		Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.medianBlur(gray, gray, 3);
		
		// morphological operators
		double dilateElem = this.dilateSlider.getValue();
		// inverse ratio of the accumulator resolution to the image resolution
		double dp = this.dpSlider.getValue();
		// min distance between circle centers
		double minDist = this.minDistSlider.getValue();
		// threshold for the circle centers at the detection stage
		double accumulator = this.accumulatorSlider.getValue();		
		// min/max Radius
		double minRadius = this.minRadiusSlider.getValue();
		double maxRadius = this.maxRadiusSlider.getValue();
		
		double lowThreshold = 240;
		double highThreshold = 255;
		// find edges
		Imgproc.Canny(gray, edges, lowThreshold, highThreshold);
		
		// dilate canny image
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(dilateElem, dilateElem));
		Imgproc.dilate(edges, morph, dilateElement);
				
		// display canny image
		this.onFXThread(this.cannyImage.imageProperty(), this.mat2Image(morph));
		
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
		String valuesToPrint = "dilate: " + String.format("%.0f", dilateElem) + "\tdp: " + String.format("%.1f", dp) + 
				"\tmin Dist: " + String.format("%.1f", minDist) + "\taccumulator: " + String.format("%.1f", accumulator) +
				"\tmin Radius: " + String.format("%.1f", minRadius) + "\tmax Radius: " + String.format("%.1f", maxRadius);
		String resultToPrint = "Result: " + String.format("%d", circlesList.size());
		this.onFXThread(this.parameterValuesProp, valuesToPrint);
		this.onFXThread(this.resultValueProp, resultToPrint);
		
		System.out.println("\t");
		return frame;
	}
	
	/**
	 * Set typical {@link ImageView} properties: a fixed width and the
	 * information to preserve the original image ration
	 * 
	 * @param image
	 *            the {@link ImageView} to use
	 * @param dimension
	 *            the width of the image to set
	 */
	private void imageViewProperties(ImageView image, int dimension) {
		// set a fixed width for the given ImageView
		image.setFitWidth(dimension);
		// preserve the image ratio
		image.setPreserveRatio(true);
	}
	
	/**
	 * Convert a {@link Mat} object (OpenCV) in the corresponding {@link Image}
	 * for JavaFX
	 * 
	 * @param frame
	 *            the {@link Mat} representing the current frame
	 * @return the {@link Image} to show
	 */
	private Image mat2Image(Mat frame) {
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer, according to the PNG format
		Imgcodecs.imencode(".png", frame, buffer);
		// build and return an Image created from the image encoded in the buffer
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
	
	/**
	 * Generic method for putting element running on a non-JavaFX thread on the
	 * JavaFX thread, to properly update the UI
	 * 
	 * @param property
	 *            a {@link ObjectProperty}
	 * @param value
	 *            the value to set for the given {@link ObjectProperty}
	 */
	private <T> void onFXThread(final ObjectProperty<T> property, final T value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				property.set(value);
			}
		});
	}
}


