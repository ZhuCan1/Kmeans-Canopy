package com.hust.algorithm;

import com.hust.distance.CosDistance;
import com.hust.utils.ClusterUtil;
import com.hust.utils.VectorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans {
    //原始文本聚类
    private List<double[]> vectors;
    //聚类结果对应下标集合
    private List<List<Integer>> resultIndex;
    //K个聚类中心点
    private List<double[]> centers;
    //新的聚类中心点
    private List<double[]> newcenters;

    //KMeans初始K值
    private int K = 0;

    //迭代次数
    private int iterTimes = 10;

    //用于相似度计算的类
    private CosDistance cosDistance;

    public KMeans(int k, List<double[]> vectors, int times) {
        setK(k);
        setVectors(vectors);
        setIterTimes(times);

        centers = new ArrayList<double[]>();
        resultIndex = new ArrayList<List<Integer>>();
        cosDistance = new CosDistance(vectors);
    }


    /**
     * KMeans聚类
     */
    public void cluster() {
        //初始化聚类中心和结果集
        init();
        //记录最大的相似度
        double maxSim = 0f;
        int tmpIndex = 0;
        //簇心集合的变化
        newcenters = centers;
        //开始迭代
        while (iterTimes > 0) {
            iterTimes--;
            for (int i = 0; i < vectors.size(); i++) {
                double[] vector = vectors.get(i);
                maxSim = 0;
                //一次计算每个文本向量集合和簇心的相似度，找出最大相似度
                for (int j = 0; j < K; j++) {
                    if (cosDistance.caculate(vector, newcenters.get(j)) > maxSim) {
                        maxSim = cosDistance.caculate(vector, newcenters.get(j));
                        tmpIndex = j;
                    }
                }
                //把向量集合添加到相似度最大的簇心集合中
                resultIndex.get(tmpIndex).add(i);
            }

            //更新簇心
            for (int i = 0; i < resultIndex.size(); i++) {
                List<Integer> indexList = resultIndex.get(i);
                double[] sum = new double[vectors.get(0).length];
                for (int j : indexList) {
                    sum = VectorUtil.add(sum, vectors.get(j));
                }
                newcenters.set(i, VectorUtil.center(sum, indexList.size()));
            }

            //根据簇心移动的距离判断是否继续迭代
            if (!centerMove(centers, newcenters)) {
                centers = newcenters;
                break;
            }
            //更新簇心
            centers = newcenters;
            resultIndex.clear();
        }

        resultIndex = ClusterUtil.delNullCluster(resultIndex);
    }

    /**
     * 判断聚类中心点是否移动
     * 新中心和旧中心相似度小于T=95%，return true
     * 新中心和旧中心相似度大于等于T=95%，return false
     *
     * @param centers
     * @param newcenters
     * @return true--移动
     */
    public boolean centerMove(List<double[]>centers, List<double[]> newcenters) {
        double T = 0.95f;
        double sum = 0f;
        for (int i = 0; i < K; i++) {
            sum += cosDistance.caculate(centers.get(i), newcenters.get(i));
        }
        if (sum / centers.size() < T) {
            return true;
        }
        return false;
    }

    public void init() {
        //随机生成K个随机数作为K个簇心向量的下标
        List<Integer> randomArray = generateRandom(getK(), vectors.size());
        for (int i = 0; i < K; i++) {
            centers.add(vectors.get(randomArray.get(i)));
            //每生成一个簇心，就生成一个其对应的list
            List<Integer> cluster = new ArrayList<Integer>();
            resultIndex.add(cluster);
        }
    }

    public List<Integer> generateRandom(int k, int size) {
        Random random = new Random();
        int count = 0;
        List<Integer> list = new ArrayList<Integer>();
        while (count < k) {
            int num = random.nextInt(size);
            if (!list.contains(num)) {
                list.add(num);
                count++;
            }
        }
        return list;
    }

    public int getIterTimes() {
        return iterTimes;
    }

    public void setIterTimes(int iterTimes) {
        this.iterTimes = iterTimes;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public List<double[]> getVectors() {
        return vectors;
    }

    public void setVectors(List<double[]> vectors) {
        this.vectors = vectors;
    }

    public List<List<Integer>> getResultIndex() {
        return resultIndex;
    }

    public void setResultIndex(List<List<Integer>> resultIndex) {
        this.resultIndex = resultIndex;
    }

    public List<double[]> getCenters() {
        return centers;
    }

    public void setCenters(List<double[]> centers) {
        this.centers = centers;
    }

    public List<double[]> getNewcenters() {
        return newcenters;
    }

    public void setNewcenters(List<double[]> newcenters) {
        this.newcenters = newcenters;
    }

    public CosDistance getCosDistance() {
        return cosDistance;
    }

    public void setCosDistance(CosDistance cosDistance) {
        this.cosDistance = cosDistance;
    }
}
