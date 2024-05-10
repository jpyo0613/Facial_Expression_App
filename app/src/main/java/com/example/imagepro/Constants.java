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
    public static final boolean CAMERA_BOX = true;

    // 표정 결과 산정 설정값
    // 몇초 안에 몇번 연속이면 결정할것인가?

    // 예) CAMERA_RESULT_TIME 초가 지나고 같은 표정이 CAMERA_RESULT_COUNT_MIN 이상이면 결과 도출
    // 예) CAMERA_RESULT_TIME 지나지 않아도 CAMERA_RESULT_COUNT_LIMIT 이상이면 결과 도출
    // 위 상황에서 표정이 바뀌면 초기화하고 처음부터 다시 시작
    public static final long CAMERA_RESULT_TIME = 1500;
    public static final int CAMERA_RESULT_COUNT_MIN = 6;
    public static final int CAMERA_RESULT_COUNT_LIMIT = 20;

    /**
     * 기타
     */
    public static final String KEY_CAMERA_IS_BASIC_MODE = "isBasicMode";
}
