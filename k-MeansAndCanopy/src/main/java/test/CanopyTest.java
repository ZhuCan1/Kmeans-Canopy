package test;

import com.hust.algorithm.Canopy;
import com.hust.convertor.TFIDFConvertor;
import com.hust.segmentation.AnsjSegmentation;
import com.hust.utils.ClusterUtil;
import com.hust.utils.Config;
import com.hust.utils.ExcelReader;
import com.hust.utils.ExcelWriter;

import java.util.List;

public class CanopyTest {
    public static void main(String[] args) {
        System.out.println("开始执行");
        //1.获取数据源
        List<String> dataList = ExcelReader.read("F:/file/excelcrawl/source.xlsx", 1);
        System.out.println(dataList);
        //2.对数据源进行分词操作
        AnsjSegmentation ansjSegmentation = new AnsjSegmentation();
        ansjSegmentation.setWordList(dataList);
        ansjSegmentation.segment();

        //3.得到分词后的集合
        List<String[]> segList = ansjSegmentation.getSegList();
        /*for (String[] strings : segList){
            for (String s : strings){
                System.out.print(s + ",");
            }
            System.out.println();
            System.out.println("======");
        }*/

        //向量转化
        TFIDFConvertor tfidfConvertor = new TFIDFConvertor(segList);
        List<double[]> vectors = tfidfConvertor.getVector();

        //Canopy操作
        Canopy canopy = new Canopy();
        canopy.setVectors(vectors);
        canopy.setT(0.015);
        //开始聚类
        canopy.cluster();
        //聚类结果显示在控制台
        ClusterUtil.showResult(canopy.getResultIndex(), dataList);
        System.out.println("聚类个数：" + canopy.getCanopy());

        //将聚类结果转化为List<String>文本集合
        List<List<String>> clusterList = ClusterUtil.getClusters(canopy.getResultIndex(), dataList);

        //清空聚类结果文件夹
        ClusterUtil.delFolder(Config.CANOPY_RESULT_PATH);

        //将一个聚类结果保存在一个excel文件中
        for (int i = 0; i < clusterList.size(); i++) {
            String fileName = ClusterUtil.stringFilter(clusterList.get(i).get(0));
            ExcelWriter.colListToExcel(Config.CANOPY_RESULT_PATH + fileName + ".xls", clusterList.get(i));
        }
        System.out.println("运行结束");
    }
}
