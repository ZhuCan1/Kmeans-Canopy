package com.hust.convertor;

import java.util.ArrayList;
import java.util.List;

public class TFIDFConvertor {
    //分词结果集合
    private List<String[]> segList;
    //所有单词的集合(不重复)
    private List<String> vectorBase;

    public TFIDFConvertor(){
        super();
    }

    public TFIDFConvertor(List<String[]> list){
        super();
        this.segList = list;
        Init();
    }

    //初始化向量单词模板的集合
    public void Init(){
        vectorBase = new ArrayList<String>();
        for (String[] strings : segList){
            for (String s : strings){
                //如果集合中不存在这个单词，则添加进去
                if (!vectorBase.contains(s)){
                    vectorBase.add(s);
                }
            }
        }
    }
    //得到分词后每个文本的向量,每个向量的长度都为vectorBase.size()
    public List<double[]> getVector(){
        List<double[]> list = new ArrayList<double[]>();
        //因为后面要用到segList，所以重新复制segList
        List<String[]> filterList = segList;
        //对于每一条记录，都生成一个长度为vectorBase.size()向量
        for (String[] array : filterList){
            //构造模板向量
            double[] vector = new double[vectorBase.size()];
            for (String s : array){
                //计算tf=一条记录出现的次数/这条记录单词的总个数
                double tf = (double) getCountInOne(s,array)/(double) array.length;
                //计算idf=记录的总数/出现该单词记录的个数
                double idf = Math.log((float)filterList.size()/((float)getCountInAll(s)+1));
                //将tf*idf存入模板向量
                //按顺序存储
                vector[vectorBase.indexOf(s)] = tf * idf;
            }
            //添加向量到集合中（存放顺序与文本分词存放顺序一致，方便后面的数据处理）
            list.add(vector);
        }
        return list;
    }
//计算单词在一篇文章中出现的次数
    private int getCountInOne(String word,String[]array){
        int count = 0;
        for (String s : array){
            if (s.equals(word)){
                count++;
            }
        }
        return count;
    }
//计算单词在所有文章中出现的文章的次数
    private int getCountInAll(String word){
        int count = 0;
        for (String[] strings : segList){
            for (String s : strings){
                if (s.equals(word)){
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}
