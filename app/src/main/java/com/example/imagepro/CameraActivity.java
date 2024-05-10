package com.example.imagepro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, ResultCalculator.Listener {
    private static final String TAG = "CameraActivity";
    private Mat mRgba;
    private CameraBridgeViewBase mOpenCvCameraView;
    private facialExpressionRecognition facialExpressionRecognition;

    private final ResultCalculator resultCalculator = new ResultCalculator(this);

    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface
                    .SUCCESS) {
                Log.i(TAG, "OpenCv Is loaded");
                mOpenCvCameraView.enableView();
            }
            super.onManagerConnected(status);
        }
    };

    private boolean isModeBasic = true;
    private TextView resultTv;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        int MY_PERMISSIONS_REQUEST_CAMERA = 0;
        // if camera permission is not given it will ask for it on device
        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }

        setContentView(R.layout.activity_camera);

        // 모드 가져오기
        Intent intent = getIntent();
        if (intent != null) {
            isModeBasic = intent.getBooleanExtra(Constants.KEY_CAMERA_IS_BASIC_MODE, true);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        mOpenCvCameraView = findViewById(R.id.frame_Surface);
        resultTv = findViewById(R.id.tv_result);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        // 비시각장애인 모드 색상
        if (!isModeBasic) {
            int backgroundColor = ContextCompat.getColor(this, R.color.light_blue);
            toolbar.setBackgroundColor(backgroundColor);
            resultTv.setBackgroundColor(backgroundColor);
        }

        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.KOREAN);
                tts.setPitch(Constants.TTS_PITCH);
                tts.setSpeechRate(Constants.TTS_RATE);
            }
        });

        try {
            // input size of model is 48
            int inputSize = 48;
            facialExpressionRecognition = new facialExpressionRecognition(
                    getAssets(),
                    CameraActivity.this,
                    "newmodel.tflite",
                    inputSize
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            //if load success
            Log.d(TAG, "Opencv initialization is done");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            //if not loaded
            Log.d(TAG, "Opencv is not loaded. try again");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
        tts.stop();
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
        tts.stop();
        tts.shutdown();
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
//        mGray = new Mat(height, width, CvType.CV_8UC1);
    }

    public void onCameraViewStopped() {
        mRgba.release();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
//        mGray = inputFrame.gray();

        //output                                         input
        Result result = facialExpressionRecognition.recognizeImage(mRgba);
        mRgba = result.mat;

        // 결과 누적 및 계산
        resultCalculator.setResult(result.type);

        return mRgba;
    }

    @Override
    public void onResult(@NonNull ResultType type) {
        runOnUiThread(() -> {
            String result = "";
            switch (type) {
                case SURPRISE:
                    result = getString(R.string.surprise);
                    break;
                case FEAR:
                    result = getString(R.string.fear);
                    break;
                case ANGRY:
                    result = getString(R.string.angry);
                    break;
                case NEUTRAL:
                    result = getString(R.string.neutral);
                    break;
                case SAD:
                    result = getString(R.string.sad);
                    break;
                case DISGUST:
                    result = getString(R.string.disgust);
                    break;
                case HAPPY:
                    result = getString(R.string.happy);
                    break;
                default:
                    result = getString(R.string.unknown);
                    break;
            }

            resultTv.setText(result);
            if (isModeBasic) {
                tts.speak(result, TextToSpeech.QUEUE_FLUSH, null, "1");
            }
        });
    }
}
