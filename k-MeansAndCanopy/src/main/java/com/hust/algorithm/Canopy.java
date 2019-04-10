package com.hust.algorithm;

import com.hust.distance.CosDistance;
import com.hust.utils.ClusterUtil;
import org.apache.poi.hssf.util.HSSFColor;

import java.util.ArrayList;
import java.util.List;

public class Canopy {
    //原始文本向量化的集合
    private List<double[]> vectors;

    //聚类结果对应的下标集合
    private List<List<Integer>> resultIndex;

    //canopy初始阈值
    private double T = 0f;

    //聚类结果类别数量
    private int canopy = 0;

    //用于计算相似度的类
    private CosDistance cosDistance;

    //开始canopy算法
    public void cluster() {
        //初始化相似度的类
        cosDistance = new CosDistance(vectors);
        //如果阈值为零，则去相似度的平均值
        if (T == 0) {
            setT(cosDistance.getThreshold());
        }
        //聚类结果集
        resultIndex = new ArrayList<List<Integer>>();
        List<Integer> tempIndex = null;
        //遍历向量集合
        for (int i = 0; i < vectors.size(); i++) {
            //刚开始,没有聚集类，则直接生成
            if (i == 0) {
                tempIndex = new ArrayList<Integer>();
                tempIndex.add(i);
                resultIndex.add(tempIndex);
            }
            //找到符合相似度要求的类的标志
            boolean isFind = false;
            //这是通过余弦值比较，值越大，越相似
            //与所有的聚集类进行比较
            for (int j = 0; j < resultIndex.size(); j++) {
                //如果distanc大于T,则加入该结果集
                if (cosDistance.getDistance(i, resultIndex.get(j)) > T) {
                    //获取当前
                    tempIndex = resultIndex.get(j);
                    //加入新的结果
                    tempIndex.add(i);
                    //更新结果集
                    resultIndex.remove(j);
                    resultIndex.add(tempIndex);
                    isFind = true;
                    break;
                }
            }
            //如果为false,则新建一个类别
            if (!isFind){
                tempIndex = new ArrayList<Integer>();
                tempIndex.add(i);
                resultIndex.add(tempIndex);
            }
        }
        //删除空类
        resultIndex = ClusterUtil.delNullCluster(resultIndex);
        //获得聚类的数量
        canopy = resultIndex.size();
    }

    public List<List<Integer>> getResultIndex() {
        return resultIndex;
    }

    public void setResultIndex(List<List<Integer>> resultIndex) {
        this.resultIndex = resultIndex;
    }

    public List<double[]> getVectors() {
        return vectors;
    }

    public void setVectors(List<double[]> vectors) {
        this.vectors = vectors;
    }

    public double getT() {
        return T;
    }

    public void setT(double T) {
        this.T = T;
    }

    public int getCanopy() {
        return canopy;
    }

}
