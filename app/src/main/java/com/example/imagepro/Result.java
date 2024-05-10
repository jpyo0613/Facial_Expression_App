package com.example.imagepro;

import org.opencv.core.Mat;

public class Result {
    public ResultType type;
    public Mat mat;

    public Result(ResultType type, Mat mat) {
        this.type = type;
        this.mat = mat;
    }
}
