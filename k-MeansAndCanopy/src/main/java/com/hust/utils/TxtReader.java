package com.hust.utils;

import com.sleepycat.je.tree.IN;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TxtReader {

    public List<String> getDataFromTxt(String path) {
        List<String> list = new ArrayList<String>();
        //获取指定路径的文件
        File file = new File(path);
        try {
            //如果file存在并且是文件，则直接读取
            if (file.exists() && file.isFile()) {
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String lineContent = null;
                while ((lineContent = reader.readLine()) != null) {
                    //将读取的每行内容放入文本集合中（实际每行都是一个词语）
                    list.add(lineContent);
                }
                reader.close();
                inputStreamReader.close();
            } else {
                System.out.println("文件不存在！请确认输入的文件路径是否正确！");
            }
        } catch (Exception e) {
            System.out.println("文件读取出错！");
            e.printStackTrace();
        }
        return list;
    }
}
