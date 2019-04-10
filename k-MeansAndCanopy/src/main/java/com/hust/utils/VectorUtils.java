package com.hust.utils;

import java.util.Arrays;

public class VectorUtils {
    //计算每一个向量的平方和
    public static double module(double[] vector) {
        if (vector == null || vector.length == 0) {
            return 0;
        }
        double sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += Math.pow(vector[i], 2);
        }
        return Math.sqrt(sum);
    }

    //计算两个向量直接乘积和
    public static double multiply(double[] vector1, double[] vector2) {
        if (vector1 == null || vector1.length == 0 || vector2 == null || vector2.length == 0) {
            return 0;
        }
        //如果向量长度不同(这个相同),以长的为标准
        double[] v1, v2;
        if (vector1.length > vector2.length) {
            v2 = new double[vector1.length];
            //v2 = Arrays.copyOf(vector2, vector2.length);
            v2 = VectorUtils.copyArray(vector2, v2);
            v1 = vector1.clone();
        } else if (vector1.length < vector2.length) {
            v1 = new double[vector2.length];
            // v1 = Arrays.copyOf(vector1, vector1.length);
            v1 = VectorUtils.copyArray(vector1, v1);
            v2 = vector2.clone();
        } else {
            v1 = vector1.clone();
            v2 = vector2.clone();
        }
        double sum = 0;
        for (int i = 0; i < v1.length; i++) {
            sum += v1[i] * v2[i];

        }
        return sum;
    }

    public static double[] copyArray(double[] source, double[] target) {
        for (int i = 0; i < source.length; i++) {
            target[i] = source[i];
        }
        return target;

    }


}
