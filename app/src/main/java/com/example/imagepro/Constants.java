package com.example.imagepro;

public class Constants {
    /**
     * 메인 페이지
     */
    // 경고 노출시간
    public static final long WARNING_DELAY = 1000 * 4;

    // N초 후 음성안내
    public static final long GUIDE_TTS_DELAY = 1000 * 15;

    // 음성안내 음성 톤 높이 (%) ex) 1.2 -> 120%
    public static final float TTS_PITCH = 1.2f;

    // 음성안내 음성 속도 (%) ex) 0.9 -> 90%
    public static final float TTS_RATE = 0.9f;

    /**
     * 카메라 페이지
     */
    // 카메라에 얼굴 인식 박스 유무
    public static final boolean CAMERA_BOX = false;

    /**
     * 기타
     */
    public static final String KEY_CAMERA_IS_BASIC_MODE = "isBasicMode";
}
