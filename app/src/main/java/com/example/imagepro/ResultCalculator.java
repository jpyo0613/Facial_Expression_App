package com.example.imagepro;

public class ResultCalculator {
    private Listener listener;

    public interface Listener {
        void onResult(ResultType type);
    }

    private ResultType lastResultType = null;
    private long lastChangedTime = 0L;
    private int sameCount = 0;
    private boolean sentCallback = false;

    public ResultCalculator(Listener listener) {
        this.listener = listener;
    }

    public void setResult(ResultType resultType) {
        long currentTime = System.currentTimeMillis();
        if (lastResultType == resultType) {
            if (lastChangedTime == 0L) {
                lastChangedTime = currentTime;
            }
            sameCount++;
            if (!sentCallback && (sameCount >= Constants.CAMERA_RESULT_COUNT_LIMIT ||
                    (lastChangedTime >= currentTime - Constants.CAMERA_RESULT_TIME &&
                    sameCount >= Constants.CAMERA_RESULT_COUNT_MIN))
            ) {
                sentCallback = true;
                listener.onResult(lastResultType);
            }
        } else {
            sentCallback = false;
            sameCount = 0;
            lastChangedTime = 0;
            lastResultType = resultType;
        }
    }
}
