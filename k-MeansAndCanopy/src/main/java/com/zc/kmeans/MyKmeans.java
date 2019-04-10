package com.zc.kmeans;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MyKmeans {
    //定义点的集合
    static List<Point> points = new ArrayList<Point>();
    //定义簇的集合，每个簇占据一个list保存簇心和该组元素
    static List<List<Point>> listCenter = new ArrayList<List<Point>>();
    //设置簇的数量
    private final static Integer k = 2;
    //设置迭代的终点，也就是当簇心的移动距离小于某个阈值时迭代完成
    private final static Double converage = 0.001;

    //读取数据
    public static final void readFi1e() throws IOException {
        String filePath = "F:\\code\\k-MeansTest\\src\\main\\java\\data\\data.txt";
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath)));
        for (String line = bufferedReader.readLine();
             line != null; line = bufferedReader.readLine()) {
            if (line.equals("") || line.length() == 0) {
                continue;
            }
            String[] str = line.split(" ");
            Point point = new Point();
            point.setX(Double.valueOf(str[0]));
            point.setY(Double.valueOf(str[1]));
            points.add(point);
        }
    }

    //计算两点间的欧式距离
    public static Double getDistance(Point p1, Point p2) {
        return Math.sqrt(StrictMath.pow(p2.getX() - p1.getX(), 2) + StrictMath.pow(p2.getY() - p1.getY(), 2));
    }

    //将点分到每一组簇上
    public static void allocatePoint() {
        for (int i = k; i < points.size(); i++) {
            Point point = new Point();
            point = points.get(i);
            int index = -1;
            double minCenter = Double.MAX_VALUE;
            //每个点依次和每个簇心比较距离，决定属于哪个簇
            for (int j = 0; j < k; j++) {
                Point center = listCenter.get(j).get(0);
                double dist = getDistance(point, center);
                if (dist < minCenter) {
                    minCenter = dist;
                    index = j;
                }
            }
            System.out.println("Center" + index + "的点为：" + point.getX() + "," + point.getY());
            listCenter.get(index).add(point);
        }
    }

    //第一次迭代，并且设置簇心
    public static void kluster() {
        //设置前两个点为第一次迭代的两个簇心
        for (int i = 0; i < k; i++) {
            List<Point> center = new ArrayList<Point>();
            Point point = new Point();
            point = points.get(i);
            center.add(point);
            listCenter.add(center);
        }
        //打印第一次迭代的簇心
        for (int i = 0; i < k; i++) {
            Point center = listCenter.get(i).get(0);
            System.out.println("C" + i + "的簇心：" + center.getX() + "," + center.getY());
        }
        System.out.println("第一次迭代:");
        //簇心位于每一个list的第一个元素中
        /*for (int i = k; i < points.size(); i++) {
            Point point = new Point();
            point = points.get(i);
            int index = -1;
            double minCenter = Double.MAX_VALUE;
            //每个点依次和每个簇心比较距离，决定属于哪个簇
            for (int j = 0; j < k; j++) {
                Point center = listCenter.get(j).get(0);
                double dist = getDistance(point, center);
                if (dist < minCenter) {
                    minCenter = dist;
                    index = j;
                }
            }
            System.out.println("Center" + index + "的点为：" + point.getX() + "," + point.getY());
            listCenter.get(index).add(point);
        }*/
        allocatePoint();
    }

    //计算新的簇心,并返回簇心移动的最小距离
    public static Double changeCenter() {
        System.out.println("--------------");
        Double movelist = Double.MAX_VALUE;//定义簇心移动的距离
        for (int i = 0; i < listCenter.size(); i++) {
            List<Point> sublist = listCenter.get(i);
            Double sumx = 0.0;
            Double sumy = 0.0;
            Double centerLen = Double.valueOf(sublist.size());
            for (int j = 0; j < centerLen; j++) {
                Point point = sublist.get(j);
                sumx += point.getX();
                sumy += point.getY();
            }
            Point targetPoint = new Point();//定义新的簇心
            targetPoint.setX(sumx / centerLen);
            targetPoint.setY(sumy / centerLen);
            //计算新旧簇心见得聚类
            Double dis = getDistance(sublist.get(0), targetPoint);
            //返回移动的距离最小值
            if (dis < movelist) movelist = dis;
            //清空该簇的list,并保存新的簇心
            sublist.clear();
            sublist.add(targetPoint);
            System.out.println("C" + i + "的簇心为：" + targetPoint.getX() + "," + targetPoint.getY());
        }
        return movelist;
    }

    private static Double move = Double.MAX_VALUE;//移动距离

    //不断迭代,直至收敛
    public static void recursionKluster() {
        for (int times = 2; move > converage; times++) {
            System.out.println("第" + times + "次迭代");
            /*for (int i = 0; i < points.size(); i++) {
                Point point = new Point();
                point = points.get(i);
                int index = -1;
                double neardist = Double.MAX_VALUE;
                for (int j = 0; j < k; j++) {
                    Point center = listCenter.get(j).get(0);
                    double distance = getDistance(center, point);
                    if (distance < neardist) {
                        neardist = distance;
                        index = j;
                    }
                }
                System.out.println("C"+index+":的点为："+point.getX()+","+point.getY());
                listCenter.get(index).add(point);
            }*/
            allocatePoint();

            //分组完成后，重新计算簇心，并且返回簇心移动的最小距离
            move = changeCenter();
            System.out.println("各个簇心移动中最小的距离为，move=" + move);
        }
    }

    public static void main(String[] args) throws IOException {
        //读取文件
        readFi1e();
        //第一次迭代
        kluster();
        //第一次迭代后计算簇心
        changeCenter();
        //不断迭代，直至收敛
        recursionKluster();
        //System.out.println("Hello world");
    }

}
