package com.example.imagepro;

import android.content.Context;

import org.opencv.core.Mat;

public class Result {
    public ResultType type;
    public Mat mat;

    public Result(ResultType type, Mat mat) {
        this.type = type;
        this.mat = mat;
    }
    
    public String getResultString(Context context) {
        switch (type) {
            case SURPRISE:
                return context.getString(R.string.surprise);
            case FEAR:
                return context.getString(R.string.fear);
            case ANGRY:
                return context.getString(R.string.angry);
            case NEUTRAL:
                return context.getString(R.string.neutral);
            case SAD:
                return context.getString(R.string.sad);
            case DISGUST:
                return context.getString(R.string.disgust);
            default:
                return context.getString(R.string.happy);
        }
    }
}
