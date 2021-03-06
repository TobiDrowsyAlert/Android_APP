/*
 * Copyright 2016-present Tzutalin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tzutalin.dlibtest;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;
import com.tzutalin.dlibtest.Utility.AlertUtility;
import com.tzutalin.dlibtest.Utility.AppState;
import com.tzutalin.dlibtest.domain.FaceLandmark;
import com.tzutalin.dlibtest.domain.FaceRect;
import com.tzutalin.dlibtest.domain.RequestAnalyzeSleepDTO;
import com.tzutalin.dlibtest.domain.StrechDataDTO;
import com.tzutalin.dlibtest.user.model.User;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that takes in preview frames and converts the image to Bitmaps to process with dlib lib.
 */
public class OnGetImageListener implements OnImageAvailableListener {

    public static int isBlue;   // 1 = blue , 2 = red , 3 = yellow
    private static final boolean SAVE_PREVIEW_BITMAP = false;

    private static final int INPUT_SIZE = 300; //224
    private static final String TAG = "OnGetImageListener";

    private int mScreenRotation = 90;

    private int mPreviewWdith = 0;
    private int mPreviewHeight = 0;
    private byte[][] mYUVBytes;
    private int[] mRGBBytes = null;
    private Bitmap mRGBframeBitmap = null;
    private Bitmap mCroppedBitmap = null;

    private boolean mIsComputing = false;
    private Handler mInferenceHandler;

    private Context mContext;
    private FaceDet mFaceDet;
    private TrasparentTitleView mTransparentTitleView;
    private FloatingCameraWindow mWindow;
    private Paint mFaceLandmardkPaint;

    private static AlertUtility alertUtility;
    private User user;


    private Boolean isActivateNetwork;
    private int sleep_step;

    public void initialize(
            final Context context,
            final AssetManager assetManager,
            final TrasparentTitleView scoreView,
            final Handler handler) {
        this.mContext = context;
        this.mTransparentTitleView = scoreView;
        this.mInferenceHandler = handler;
        mFaceDet = new FaceDet(Constants.getFaceShapeModelPath());
        mWindow = new FloatingCameraWindow(mContext);

        //CameraActivity.setColor(1);
        user = User.getInstance();

        //dialogBox = new DialogBox(mContext);
        alertUtility = new AlertUtility(CameraActivity.getContext());
        isActivateNetwork = CameraActivity.getIsActivateNetwork();

        mFaceLandmardkPaint = new Paint();
        mFaceLandmardkPaint.setColor(Color.GREEN);
        mFaceLandmardkPaint.setStrokeWidth(2);
        mFaceLandmardkPaint.setStyle(Paint.Style.STROKE);
    }


    static public AlertUtility getAlertUtility(){
        return alertUtility;
    }


    public void deInitialize() {
        synchronized (OnGetImageListener.this) {
            if (mFaceDet != null) {
                mFaceDet.release();
            }

            if (mWindow != null) {
                mWindow.release();
            }
        }
    }

    // 사각형 만드는 함수
    private void drawResizedBitmap(final Bitmap src, final Bitmap dst) {

        Display getOrient = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        Point point = new Point();
        getOrient.getSize(point);
        int screen_width = point.x;
        int screen_height = point.y;
        Log.d(TAG, String.format("screen size (%d,%d)", screen_width, screen_height));
        if (screen_width < screen_height) {
            orientation = Configuration.ORIENTATION_PORTRAIT;
            //mScreenRotation = 90; // 기존 오픈 소스 코드
            //mScreenRotation = 270; // 변경된 소스 (작은 사각형 회전)
            mScreenRotation = -90; // 변경된 소스 (작은 사각형 회전)
        } else {
            orientation = Configuration.ORIENTATION_LANDSCAPE;
            mScreenRotation = 0;
        }

        final float minDim = Math.min(src.getWidth(), src.getHeight());

        final Matrix matrix = new Matrix();

        // We only want the center square out of the original rectangle.
        final float translateX = -Math.max(0, (src.getWidth() - minDim) / 2);
        final float translateY = -Math.max(0, (src.getHeight() - minDim) / 2);
        matrix.preTranslate(translateX, translateY);

        final float scaleFactor = dst.getHeight() / minDim;
        matrix.postScale(scaleFactor, scaleFactor);

        // Rotate around the center if necessary.
        if (mScreenRotation != 0) {
            matrix.postTranslate(-dst.getWidth() / 2.0f, -dst.getHeight() / 2.0f);
            matrix.postRotate(mScreenRotation);
            matrix.postTranslate(dst.getWidth() / 2.0f, dst.getHeight() / 2.0f);
        }

        final Canvas canvas = new Canvas(dst);
        canvas.drawBitmap(src, matrix, null);
    }

    @Override
    public void onImageAvailable(final ImageReader reader) {
        Image image = null;
        try {
            image = reader.acquireLatestImage();

            if (image == null) {
                return;
            }

            // No mutex needed as this method is not reentrant.
            if (mIsComputing) {
                image.close();
                return;
            }
            mIsComputing = true;

            Trace.beginSection("imageAvailable");

            final Plane[] planes = image.getPlanes();

            // Initialize the storage bitmaps once when the resolution is known.
            if (mPreviewWdith != image.getWidth() || mPreviewHeight != image.getHeight()) {
                mPreviewWdith = image.getWidth();
                mPreviewHeight = image.getHeight();

                Log.d(TAG, String.format("Initializing at size %dx%d", mPreviewWdith, mPreviewHeight));
                mRGBBytes = new int[mPreviewWdith * mPreviewHeight];
                mRGBframeBitmap = Bitmap.createBitmap(mPreviewWdith, mPreviewHeight, Config.ARGB_8888);
                //	createBitmap(int width, int height, Bitmap.Config config)
                mCroppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Config.ARGB_8888);

                mYUVBytes = new byte[planes.length][];
                for (int i = 0; i < planes.length; ++i) {
                    mYUVBytes[i] = new byte[planes[i].getBuffer().capacity()];
                }
            }

            for (int i = 0; i < planes.length; ++i) {
                planes[i].getBuffer().get(mYUVBytes[i]);
            }

            final int yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();
            ImageUtils.convertYUV420ToARGB8888(
                    mYUVBytes[0],
                    mYUVBytes[1],
                    mYUVBytes[2],
                    mRGBBytes,
                    mPreviewWdith,
                    mPreviewHeight,
                    yRowStride,
                    uvRowStride,
                    uvPixelStride,
                    false);

            image.close();
        } catch (final Exception e) {
            if (image != null) {
                image.close();
            }
            Log.e(TAG, "Exception!", e);
            Trace.endSection();
            return;
        }
        //setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height)
        mRGBframeBitmap.setPixels(mRGBBytes, 0, mPreviewWdith, 0, 0, mPreviewWdith, mPreviewHeight);
        drawResizedBitmap(mRGBframeBitmap, mCroppedBitmap);   // 작은 사각형 만듬

        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(mCroppedBitmap);
        }

        mInferenceHandler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (!new File(Constants.getFaceShapeModelPath()).exists()) {
                            mTransparentTitleView.setText("Copying landmark model to " + Constants.getFaceShapeModelPath());
                            FileUtils.copyFileFromRawToOthers(mContext, R.raw.shape_predictor_68_face_landmarks, Constants.getFaceShapeModelPath());
                        }

                        List<VisionDetRet> results;
                        // 비트맵에서 디텍트!
                        synchronized (OnGetImageListener.this) {
                            results = mFaceDet.detect(mCroppedBitmap);
                        }

                        // Draw on bitmap
                        RequestAnalyzeSleepDTO requestAnalyzeDTO = new RequestAnalyzeSleepDTO();
                        if (results != null) {  // 랜드마크 사각형 그리기 위함(얼굴 좌표를 이용하여)

                            if(CameraActivity.getCurrentColor() != 2)
                            {
                                CameraActivity.setColor(1);
                            }

                            for (final VisionDetRet ret : results) {
                                if(!CameraActivity.getIsActivateNetwork()){
                                    Log.e("OnGetImageListener", "Camera Pause");
                                    break;
                                }

                                float resizeRatio = 1.0f;
                                int rect[] = new int[4];
                                Rect bounds = new Rect();
                                bounds.left = (int) (ret.getLeft() * resizeRatio);
                                bounds.top = (int) (ret.getTop() * resizeRatio);
                                bounds.right = (int) (ret.getRight() * resizeRatio);
                                bounds.bottom = (int) (ret.getBottom() * resizeRatio);

                                FaceRect faceRect = new FaceRect(bounds);

                                Canvas canvas = new Canvas(mCroppedBitmap);
                                canvas.drawRect(bounds, mFaceLandmardkPaint);

                                // Draw landmark
                                ArrayList<Point> landmarks = ret.getFaceLandmarks();
                                Log.e(TAG, landmarks.toString());
                                FaceLandmark faceLandmark = new FaceLandmark(landmarks, resizeRatio);

                                int landmark[][] = new int[landmarks.size()][2];
                                int count = 0;
                                for (Point point : landmarks) {
                                    int pointX = (int) (point.x);
                                    int pointY = (int) (point.y);
                                    canvas.drawCircle(pointX, pointY, 2, mFaceLandmardkPaint);
                                    landmark[count][0] = pointX;
                                    landmark[count][1] = pointY;
                                    count++;
                                 }

                                requestAnalyzeDTO.setRequestAnalyzeSleepDTO(faceRect.getRect(),
                                        true, faceLandmark.getLandmark(), 50, true);
                                requestAnalyzeDTO.setUserId(user.getUserId());

                                if(AppState.getInstance().getIsStrecthing()){
                                    alertUtility.requestStreching(requestAnalyzeDTO);
                                }else{
                                    alertUtility.requestSleepAnalyze(requestAnalyzeDTO);
                                }

                            }

                            if(results.size() == 0) {

                                if(CameraActivity.getCurrentColor() != 2)
                                {
                                    CameraActivity.setColor(3);
                                }

                                Log.e("Debug","Nothing in here");
                                Log.e("OnGetImageListener", "현재 pause 진리값 : " + CameraActivity.getIsActivateNetwork().toString());

                                if(!CameraActivity.getIsActivateNetwork()){
                                    Log.e("OnGetImageListener", "Camera Pause");
                                }
                                else {
                                    requestAnalyzeDTO.setRequestAnalyzeSleepDTO(null,false,null,0,true);
                                    requestAnalyzeDTO.setUserId(user.getUserId());
                                    if(AppState.getInstance().getIsStrecthing()){
                                        alertUtility.requestStreching(requestAnalyzeDTO);
                                    }else{
                                        alertUtility.requestSleepAnalyze(requestAnalyzeDTO);
                                    }
                                }
                            }
                        }
                        mWindow.setRGBBitmap(mCroppedBitmap);
                        mIsComputing = false;
                    }
                });


        Trace.endSection();



    }





}
