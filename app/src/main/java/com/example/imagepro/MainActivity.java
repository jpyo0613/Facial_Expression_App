package com.example.imagepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import org.opencv.android.OpenCVLoader;

import java.util.Locale;

// 메인 화면
public class MainActivity extends AppCompatActivity {
    static {
        if (OpenCVLoader.initDebug()) {
            Log.d("MainActivity: ", "Opencv is loaded");
        } else {
            Log.d("MainActivity: ", "Opencv failed to load");
        }
    }


    // 경고문구 보여줬는지 여부
    private volatile boolean warningShown = false;
    private ConstraintLayout clWarning;

    // 음성안내 나와야하는지 여부
    private volatile boolean isMainForeground = false;
    private Thread ttsGuideThread;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        clWarning = findViewById(R.id.cl_warning);
        FrameLayout basicModeBtn = findViewById(R.id.fl_mode_basic);
        FrameLayout nonModeBtn = findViewById(R.id.fl_mode_non);

        // 기본 모드 눌렀을 시
        basicModeBtn.setOnClickListener(v -> {
            startCamera(true);
        });

        // 비시각장애인 모드 눌렀을 시
        nonModeBtn.setOnClickListener(v -> {
            startCamera(false);
        });

        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.KOREAN);
                tts.setPitch(Constants.TTS_PITCH);
                tts.setSpeechRate(Constants.TTS_RATE);
            }
        });
    }

    private void startCamera(boolean isBasicMode) {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        intent.putExtra(Constants.KEY_CAMERA_IS_BASIC_MODE, isBasicMode);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (warningShown) {
            clWarning.setVisibility(View.GONE);
        } else {
            clWarning.setVisibility(View.VISIBLE);
            clWarning.postDelayed(() -> {
                warningShown = true;
                clWarning.setVisibility(View.GONE);
            }, Constants.WARNING_DELAY);
        }
        startGuideTTS();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isMainForeground = false;
        stopGuideTTS();
    }

    @Override
    protected void onDestroy() {
        warningShown = false;
        tts.stop();
        tts.shutdown();
        super.onDestroy();
    }

    private void startGuideTTS() {
        if (!isMainForeground) {
            isMainForeground = true;
            ttsGuideThread = new Thread(ttsRunnable);
            ttsGuideThread.start();
        }
    }

    private void stopGuideTTS() {
        if (ttsGuideThread != null) {
            ttsGuideThread.interrupt();
        }
    }

    private final Runnable ttsRunnable = () -> {
        try {
            if (clWarning.getVisibility() == View.VISIBLE) {
                // 경고문구가 끝나고 가이드 음성이 나오기 위한 sleep
                Thread.sleep(Constants.WARNING_DELAY);
            }

            while (isMainForeground) {
                tts.speak(getString(R.string.guide_tts), TextToSpeech.QUEUE_FLUSH, null, "");
                Thread.sleep(Constants.GUIDE_TTS_DELAY);
            }
        } catch (InterruptedException ignored) {
        } finally {
            isMainForeground = false;
            tts.stop();
        }
    };
}
