package test;

import com.hust.algorithm.KMeans;
import com.hust.convertor.TFIDFConvertor;
import com.hust.segmentation.AnsjSegmentation;
import com.hust.utils.ClusterUtil;
import com.hust.utils.Config;
import com.hust.utils.ExcelReader;
import com.hust.utils.ExcelWriter;

import java.util.List;

public class KMeansTest {
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

        //向量转化
        TFIDFConvertor tfidfConvertor = new TFIDFConvertor(segList);
        List<double[]> vectors = tfidfConvertor.getVector();

        //初始化KMeans聚类参数 （K值--Canopy聚类的个数，向量集合，迭代次数）
        KMeans kmeans = new KMeans(20, vectors, 20);

        //进行KMeans聚类
        kmeans.cluster();

        //聚类结果显示到控制台
        ClusterUtil.showResult(kmeans.getResultIndex(), dataList);

        //聚类结果下标集合转化为String集合
        List<List<String>> clusterlist = ClusterUtil.getClusters(kmeans.getResultIndex(), dataList);

        //
        ClusterUtil.delFolder(Config.KMEANS_RESULT_PATH);

       //把每个类的结果输出到一个Excel文件
        for (int i = 0; i < clusterlist.size(); i++) {
            //
            String fileName = ClusterUtil.stringFilter(clusterlist.get(i).get(0));

            ExcelWriter.colListToExcel(Config.KMEANS_RESULT_PATH +
                    fileName + ".xls", clusterlist.get(i));

        }
    }

}
