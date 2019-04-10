package com.hust.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClusterUtil {
    /**
     * 删除空类
     *
     * @param resultIndex
     * @return
     */
    public static List<List<Integer>> delNullCluster(List<List<Integer>> resultIndex) {
        for (int i = 0; i < resultIndex.size(); i++) {
            if (resultIndex.get(i) == null || resultIndex.get(i).size() == 0) {
                resultIndex.remove(i);
                System.out.println(i + "为空");
            }
        }

        return resultIndex;
    }

    /*
      显示聚类结果
     * */
    public static void showResult(List<List<Integer>> resultIndex, List<String> datalist) {
        for (int i = 0; i < resultIndex.size(); i++) {
            List<Integer> tempIndex = resultIndex.get(i);
            if (tempIndex == null && tempIndex.size() == 0) {
                continue;
            }
            System.out.println("类别" + i + ":" + datalist.get(tempIndex.get(0)));
            for (int j = 0; j < tempIndex.size(); j++) {
                System.out.println(datalist.get(tempIndex.get(j)));
            }
            System.out.println();
        }
    }

    /**
     * 通过聚类结果的下标集合和数据集合得到List<List<String>>类型的数组
     **/
    public static List<List<String>> getClusters(List<List<Integer>> resultIndex, List<String> dataList) {
        List<List<String>> result = new ArrayList<List<String>>();
        for (int i = 0; i < resultIndex.size(); i++) {
            List<Integer> temp = resultIndex.get(i);
            List<String> list = new ArrayList<String>();
            for (int j = 0; j < temp.size(); j++) {
                list.add(dataList.get(temp.get(j)));
            }
            result.add(list);
        }
        return result;
    }

    /**
     * 删除文件夹下所有文件
     **/
    public static void delFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.getName().endsWith("xls")) {
                    f.delete();
                }
            }
        }
    }

    /**
     * 清除特殊字符
     **/
    public static String stringFilter(String s) {
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？0123456789]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return m.replaceAll("").trim();
    }
}
