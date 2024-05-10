package com.example.imagepro;

public class Constants {
    // 메인페이지 경고 노출시간
    public static final long WARNING_DELAY = 1000 * 4;

    // 메인페이지 N초 후 음성안내
    public static final long GUIDE_TTS_DELAY = 1000 * 15;

    // 음성안내 음성 톤 높이 (%) ex) 1.2 -> 120%
    public static final float TTS_PITCH = 1.2f;

    // 음성안내 음성 속도 (%) ex) 0.9 -> 90%
    public static final float TTS_RATE = 0.9f;

    public static final String KEY_CAMERA_IS_BASIC_MODE = "isBasicMode";
}
