/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.washington.cs.touchfreelibrary.sensors;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * <p><code>CameraGestureSensor</code> takes input data from the camera and uses that to sense
 * four gesture commands: up, down, left, and right.</p>
 * <p>
 * <p><strong>Important: The static function {@link #loadLibrary()} must be called after
 * OpenCV is initiated and before {} is called!</strong></p>
 *
 * @author Leeran Raphaely <leeran.raphaely@gmail.com>
 */
public class CameraGestureSensor extends ClickSensor implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "CameraGestureSensor";

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mCurrentFrame = inputFrame.gray();
        currentIntensityValue = Core.mean(mCurrentFrame).val[0];
        last100Intensities[frameCount] = currentIntensityValue;

        if ((frameCount < 100) && (!bufferFilled)) {
            frameCount++;
        }

        if (frameCount == 100) {
            frameCount = 0;
            bufferFilled = true;
        }

        //DETECT GESTURES
        mdret = DetectMovementPosition(mCurrentFrame.getNativeObjAddr(), mPreviousFrame.getNativeObjAddr());

        //Are we already in a  gesture?
        if (gestureStartDetected) {
            currentGestureLength++;

            //Are we seeing the end of the gesture?
            if (mdret.fractionOfScreenInMotion < MIN_FRACTION_SCREEN_MOTION) {
                //This is now the last gesture so record the time
                lastGestureEndTime = System.currentTimeMillis();
                gestureEndDetected = true;
                mEndPos = mdret.averagePosition;
            }

            //If the gesture lasts too long without finishing then ignore it
            if (currentGestureLength > framesToWaitForGesture) {
                Log.e("CameraGestureSensor", "GESTURE TIMED OUT " + currentGestureLength);

                //Reset everything
                mStartPos = null;
                mEndPos = null;

                gestureStartDetected = false;
                gestureEndDetected = false;
                currentGestureLength = 0;
            }
        } else {
            //Are we seeing a spike in intensity?
            if (mdret.fractionOfScreenInMotion > MIN_FRACTION_SCREEN_MOTION) {
                //Enforce a certain amount of time between gestures
                long time = System.currentTimeMillis();
                if (time - lastGestureEndTime > MIN_MILLISECONDS_BETWEEN_GESTURES) {
                    lastGestureStartTime = time;
                    mStartPos = mdret.averagePosition;
                    gestureStartDetected = true;
                    currentGestureLength++;
                }
            }
        }

        // Did we find a gesture?
        if (gestureEndDetected) {
            //Ignore a gesture if it comes right after a click
            long gestureLength = lastGestureEndTime - lastGestureStartTime;
            Direction movementDirection = getGestureDirection(gestureLength);
            if (movementDirection == Direction.None)
                Log.e("", "NO DIRECTION!");

            // see if we should call a callback based on movementDirection
            if (mGestureListeners.size() != 0 && movementDirection != Direction.None) {
                if (movementDirection == Direction.Left) {
                    onGestureLeft(gestureLength);
                } else if (movementDirection == Direction.Right) {
                    onGestureRight(gestureLength);
                } else if (movementDirection == Direction.Up) {
                    onGestureUp(gestureLength);
                } else if (movementDirection == Direction.Down) {
                    onGestureDown(gestureLength);
                }
            }

            //Reset everything
            mStartPos = null;
            mEndPos = null;

            gestureStartDetected = false;
            gestureEndDetected = false;
            currentGestureLength = 0;
        }

        // set the previous frame to the current frame
        mCurrentFrame.copyTo(mPreviousFrame);


        return null;
    }

    /**
     * To receive messages from CameraGestureSensor, classes must implement the <code>CameraGestureSensor.Listener</code>
     * interface.
     *
     * @author Leeran Raphaely <leeran.raphaely@gmail.com>
     */
    public interface Listener {
        /**
         * Called when an up gesture is triggered
         *
         * @param caller        the CameraGestureSensor object that made the call
         * @param gestureLength the amount of time the gesture took in milliseconds
         */
        void onGestureUp(CameraGestureSensor caller, long gestureLength);

        /**
         * Called when a down gesture is triggered
         *
         * @param caller        the CameraGestureSensor object that made the call
         * @param gestureLength the amount of time the gesture took in milliseconds
         */
        void onGestureDown(CameraGestureSensor caller, long gestureLength);

        /**
         * Called when a left gesture is triggered
         *
         * @param caller        the CameraGestureSensor object that made the call
         * @param gestureLength the amount of time the gesture took in milliseconds
         */
        void onGestureLeft(CameraGestureSensor caller, long gestureLength);

        /**
         * Called when a right gesture is triggered
         *
         * @param caller        the CameraGestureSensor object that made the call
         * @param gestureLength the amount of time the gesture took in milliseconds
         */
        void onGestureRight(CameraGestureSensor caller, long gestureLength);

    }

    private enum Direction {
        Left(0), Down(1), Right(2), Up(3), None(4);

        private int numVal;

        Direction(int numVal) {
            this.numVal = numVal;
        }

        public int toInt() {
            return numVal;
        }
    }

    private List<Listener> mGestureListeners;

    private JavaCameraView mCamera;
    private int mCameraId;
    private Camera.Size mPreviewSize;

    private boolean mIsRunning;

    private static final double MIN_FRACTION_SCREEN_MOTION = 0.1;
    private final static double MIN_MILLISECONDS_BETWEEN_GESTURES = 1000;

    private double mMinDirectionalMotionX;
    private double mMinDirectionalMotionY;
    private double mMinGestureLengthY;
    private double mMinGestureLengthX;

    private Mat mPreviousFrame;
    private Mat mCurrentFrame;

    private Point mStartPos;
    private Point mEndPos;

    private boolean mIsHorizontalScrollEnabled;
    private boolean mIsVerticalScrollEnabled;
    private boolean mIsClickByColorEnabled;

    int frameCount = 0;
    int framesToWaitForGesture = 20;

    boolean bufferFilled = false;

    private double[] last100Intensities = new double[100];

    private double currentIntensityValue = 120;
    private double currentGestureLength = 0;

    boolean gestureStartDetected = false;
    boolean gestureEndDetected = false;

    MotionDetectionReturnValue mdret = null;

    long lastGestureEndTime = 0;
    long lastGestureStartTime = 0;
    //long lastClickTime = 0;

    private Direction previousMovement = Direction.None;

    /**
     * To use a <code>CameraGestureSensor</code> object, this must be called some time after
     * OpenCV is initiated.
     */
    static public void loadLibrary() {
        System.loadLibrary("touch_free_library");
    }

    // a quick utility function to find the camera id
    int getFrontCameraId() {
        CameraInfo ci = new CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == CameraInfo.CAMERA_FACING_FRONT) {
                return i;
            }
        }
        return 0; // No front-facing camera found
    }

    private List<Camera.Size> getCameraPreviewSizes(int cameraId) {
        if (mCamera != null) {
            mCamera.disableView();
        }
        Camera cam = Camera.open(cameraId);
        List<Camera.Size> sizes = cam.getParameters().getSupportedPreviewSizes();
        cam.release();
        return sizes;
    }

    private void reenableCamera(JavaCameraView camera) {
        if (mCamera == null) {
            mCamera = camera;
            mCamera.setCameraIndex(getFrontCameraId());
            mCamera.setAlpha(0);
            mCamera.enableView();
        }
    }

    private void setCameraSettings(int cameraId) {
        Camera mCam = Camera.open(cameraId);
        Camera.Parameters params = mCam.getParameters();

        params.set("iso", "auto"); // values can be "auto", "100", "200", "400", "800", "1600"
        params.setExposureCompensation(2);
        params.setWhiteBalance("fluorescent");

        //mCam.setParameters(params);
        mCam.release();
    }

    /**
     * Creates a new instance of CameraGestureSensor. Remember to call
     */
    public CameraGestureSensor() {
        mIsHorizontalScrollEnabled = true;
        mIsVerticalScrollEnabled = true;
        mIsRunning = false;
        mGestureListeners = new LinkedList<Listener>();

        // find the front facing camera id
        mCameraId = getFrontCameraId();

        //Try manipulate some settings on the camera
        setCameraSettings(mCameraId);
        loadLibrary();

    }

    /**
     * Adds listener to the list of gesture listeners.
     *
     * @param listener This object will have its call-back methods called when a gesture is recognized
     */

    public void addGestureListener(Listener listener) {
        mGestureListeners.add(listener);
    }

    /**
     * Removes listener from the list of gesture listeners
     *
     * @param listener The object will no longer have its call-back mehtods called by this gesture sensor.
     */
    public void removeGestureListener(Listener listener) {
        mGestureListeners.remove(listener);
    }

    /**
     * Removes all gesture listeners.
     */
    public void clearGestureListeners() {
        mGestureListeners.clear();
    }

    // these methods invoke gesture call backs on all listeners
    private void onGestureUp(long gestureLength) {
        for (Listener l : mGestureListeners) {
            l.onGestureUp(this, gestureLength);
        }
    }

    private void onGestureLeft(long gestureLength) {
        for (Listener l : mGestureListeners) {
            l.onGestureLeft(this, gestureLength);
        }
    }

    private void onGestureRight(long gestureLength) {
        for (Listener l : mGestureListeners) {
            l.onGestureRight(this, gestureLength);
        }
    }

    private void onGestureDown(long gestureLength) {
        for (Listener l : mGestureListeners) {
            l.onGestureDown(this, gestureLength);
        }
    }

    /**
     * Enable/disable horizontal scroll.
     *
     * @param enabled When true, onGestureLeft/onGestureRight are called, when false, they are not.
     */
    public void enableHorizontalScroll(boolean enabled) {
        mIsHorizontalScrollEnabled = enabled;
    }

    /**
     * Test if horizontal scroll is enabled.
     *
     * @return true if horizontal scroll is enabled, false otherwise.
     */
    public boolean isHorizontalScrollEnabled() {
        return mIsHorizontalScrollEnabled;
    }

    /**
     * Enable/disable vertical scroll.
     *
     * @param enabled When true, onGestureUp/onGestureDown are called, when false, they are not.
     */
    public void enableVerticalScroll(boolean enabled) {
        mIsVerticalScrollEnabled = enabled;
    }

    /**
     * Test if vertical scroll is enabled.
     *
     * @return true if vertical scroll is enabled, false otherwise.
     */
    public boolean isVerticalScrollEnabled() {
        return mIsVerticalScrollEnabled;
    }

    /**
     * When enabled, an onSensorClick command is sent to any click listeners when a large enough
     * percentage of the screen goes black.
     *
     * @param enabled Set whether click-by-color is enabled
     */
    public void enableClickByColor(boolean enabled) {
        mIsClickByColorEnabled = enabled;
    }

    /**
     * Test if click by color is enabled.
     *
     * @return true if click by color is enabled, false otherwise.
     */
    public boolean isClickByColorEnabled() {
        return mIsClickByColorEnabled;
    }

    /**
     * <p>Causes this to start reading camera input and looking for gestures. The camera must be available
     * for this method to be successful.</p>
     * <p>Warning! CameraGestureSensor will seize control of the front facing camera, even if the activity loses focus.
     * If you would like to let other applications use the camera, you must call stop() when the activity loses
     * focus.</p>
     */
    public void start(JavaCameraView cameraView) {
        Log.i(TAG, "Initializing is it running: " + mIsRunning);
        if (mIsRunning)
            return;

        //mPeakPos = null;
        mStartPos = null;
        mEndPos = null;

        if (mCamera != null) {
            JavaCameraView camera = mCamera;
            mCamera = null; // Make it null before releasing...
            camera.disableView();
        }


        List<Camera.Size> previewSizes = getCameraPreviewSizes(mCameraId);
        reenableCamera(cameraView);
        for (Camera.Size previewSize : previewSizes) {
            mPreviewSize = previewSize;
        }
        mCamera.setMaxFrameSize(mPreviewSize.width, mPreviewSize.height);

        mPreviousFrame = new Mat(mPreviewSize.height, mPreviewSize.width, CvType.CV_8U);
        mCurrentFrame = new Mat(mPreviewSize.height, mPreviewSize.width, CvType.CV_8U);

        //w x h = 320 x 240
        mMinDirectionalMotionX = mPreviewSize.width / 4;
        mMinDirectionalMotionY = mPreviewSize.height / 4;
        mMinGestureLengthY = 80;
        mMinGestureLengthX = 200;
        //mWidthToHeight = mPreviewSize.width / mPreviewSize.height;
        mIsRunning = true;

        // run the frame processor now
        if (!mCamera.isActivated())
            reenableCamera(cameraView);
        mCamera.setCvCameraViewListener(this);

        Log.i(TAG, "Finished Initializing");
    }

    /**
     * Stops this from looking at camera input for gestures, thus freeing the camera for other uses.
     */
    public void stop() {
        if (!mIsRunning)
            return;
        mIsRunning = false;
    }

    private Direction getGestureDirection(double gestureLength) {

        final int MIN_GESTURE_LENGTH = 200;
        final int MIN_RIGHT_GESTURE_LENGTH = 80;
        final int MIN_DOWN_GESTURE_LENGTH = -50;
        final double HORIZONTAL_COEFFICIENT = 1.15;
        final int MIN_X_DIFFERENCE = -180;
        final int MAX_X_DIFFERENCE = 160;
        final int MIN_Y_DIFFERENCE = -60;
        final int MAX_Y_DIFFERENCE = 70;

        Direction movementDirection = Direction.None;

        double diffX = mStartPos.x - mEndPos.x;
        double diffY = mStartPos.y - mEndPos.y;
        Log.i("CGS", "Diff Y: " + diffY + " Min: " + mMinDirectionalMotionY);
        Log.i(TAG, "Diff X: " + diffX + " Min: " + mMinDirectionalMotionX);

        if (mEndPos.x == -1) {
            return Direction.None;
        }

        if (gestureLength > MIN_GESTURE_LENGTH) {
            //equalize screen aspect ratio
            if (Math.abs(diffX) > (Math.abs(diffY) * HORIZONTAL_COEFFICIENT)) {
                if (diffX > MAX_X_DIFFERENCE && Math.abs(gestureLength) > mMinGestureLengthX) {
                    movementDirection = Direction.Right;
                    previousMovement = Direction.Right;
                } else if (diffX < MIN_X_DIFFERENCE && Math.abs(gestureLength) > mMinGestureLengthX) {
                    movementDirection = Direction.Left;
                    previousMovement = Direction.Left;
                }
            } else {
                if (diffY > MAX_Y_DIFFERENCE && Math.abs(gestureLength) > mMinGestureLengthY) {
                    movementDirection = Direction.Up;
                    previousMovement = Direction.Up;
                } else if (diffY < MIN_Y_DIFFERENCE && Math.abs(gestureLength) > mMinGestureLengthY) {
                    movementDirection = Direction.Down;
                    previousMovement = Direction.Down;
                }
            }
        } // in some cases right and down direction gestures can be divided
        // into several segments, check for previous gesture direction
        else if (diffY < MIN_DOWN_GESTURE_LENGTH) {
            if (previousMovement == Direction.Down)
                movementDirection = Direction.Down;
            previousMovement = Direction.Down;
        } else if (diffX > MIN_RIGHT_GESTURE_LENGTH) {
            if (previousMovement == Direction.Right)
                movementDirection = Direction.Right;
            previousMovement = Direction.Right;
        }
        return movementDirection;
    }

    private native MotionDetectionReturnValue DetectMovementPosition(long currentFrame, long previousFrame);

}