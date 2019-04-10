package com.hust.segmentation;

import com.hust.utils.Config;
import com.hust.utils.TxtReader;
import org.ansj.domain.Result;
import org.ansj.library.DicLibrary;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.DicAnalysis;

import java.util.ArrayList;
import java.util.List;

public class AnsjSegmentation {
    //源文档集合
    private List<String> wordList;
    //分词后存放的集合
    private List<String[]> segList;
    //停词表存放的路径
    private static String stopWordsPath = Config.stopWordPath;
    //用户自定义词典存放的路径
    private static String userWordsPath = Config.userWordsPath;
    //没有使用停词表后分词后存放的集合
    private List<String[]> listWithoutFilter = new ArrayList<String[]>();
    //加载停词表的过滤器
    private static StopRecognition filter;

    //加载停用词
    static {
        //获取停用词保存在list中
        List<String> list = new TxtReader().getDataFromTxt(stopWordsPath);
        filter = new StopRecognition();
        filter.insertStopWords(list);
        System.out.println("停用词加载成功！");
    }

    //添加用户自定义词典
    static {
        List<String> list = new TxtReader().getDataFromTxt(userWordsPath);
        for (String s : list) {
            DicLibrary.insert(DicLibrary.DEFAULT, s);
        }
        System.out.println("用户自定义词典加载成功");
    }

    //实现分词
    public void segment() {
        //依次对每一条数据进行处理
        for (String word : wordList) {
            if (word == null){
                //如果当前记录为空，则不进行分词处理，跳出当前循环
                segList.add(new String[]{});
                continue;
            }
            //result用来存放分词后的结果集，为List<>类型
            Result result = new Result(null);
            //使用精准分词模式进行分词
            //result:[让/v, 战士/n, 们/k, 过/ug, 一个/m, 欢乐/a, 祥和/a, 的/uj, 新春/t, 佳节/n, 。/w]
            result = DicAnalysis.parse(word);
            if (result.size() == 0){
                segList.add(new String[]{});
                continue;
            }
            //将不使用停用词的结果保存起来,并且结果去掉词性
            listWithoutFilter.add(result.toStringWithOutNature().split(","));
            //System.out.println(result.toString());
            //使用停用词
            result = result.recognition(filter);
            if (result.size() == 0){
                segList.add(new String[]{});
                continue;
            }
            segList.add(result.toStringWithOutNature().split(","));
        }
    }
    //初始化变量
    public AnsjSegmentation() {
        wordList = new ArrayList<String>();
        segList = new ArrayList<String[]>();
    }

    public List<String[]> getListWithoutFilter() {
        return listWithoutFilter;
    }

    // 设置要分词的文档集合
    public void setWordList(List<String> wordList) {
        this.wordList = wordList;
    }

    // 获取分词后的词语集合
    public List<String[]> getSegList() {
        return segList;
    }


}
