package com.hust.distance;

import com.hust.utils.VectorUtils;

import java.util.List;

public class CosDistance {
    //向量集合
    private List<double[]> vectors;
    //保存相似度的矩形（以下标为坐标存储两向量间的相似度如：matrix[i][j]就是i向量与j向量的相似度，其中i，j为向量集合vectors中向量存放的顺序）
    private double[][] matrix;

    public CosDistance(List<double[]> list) {
        this.vectors = list;
        init();
    }

    //初始化相似度矩形，对角线上的设为1，其余的为计算后向量间的相似度值，<i,j>与<j,i>相似度值一样所以该矩阵是一个对称矩阵
    public void init() {
        matrix = new double[vectors.size()][vectors.size()];
        for (int i = 0; i < vectors.size(); i++) {
            for (int j = i; j < vectors.size(); j++) {
                //当是同一个向量，则为1
                if (i == j) {
                    matrix[i][j] = 1;
                    continue;
                }
                //计算向量i与j的相似度
                matrix[i][j] = caculate(vectors.get(i), vectors.get(j));
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    //计算两个向量的相似度
    public double caculate(double[] v1, double[] v2) {
        return VectorUtils.multiply(v1, v2) / (VectorUtils.module(v1) * VectorUtils.module(v2));

    }

    //根据下标返回两个向量的相似度
    public double getDistance(int i, int j) {
        return matrix[i][j];
    }

    //计算一个向量与一个向量集合相似度的平均值(根据向量与向量之间的索引)
    public double getDistance(int index, List<Integer> list) {
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += getDistance(index, list.get(i));
        }
        return sum / list.size();
    }

    //计算一个向量与一个向量集合相似度的平均值
    public double getDistance(double[] vector, List<double[]> list) {
        double sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += caculate(vector,list.get(i));
        }
        return sum/list.size();
    }

    //获取阈值,即所有向量相似度的平均值
    public double getThreshold(){
        double sum = 0f;
        int n = matrix.length;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                sum += matrix[i][j];
            }
        }
        return sum/(n * (n-1)/2);
    }


}
