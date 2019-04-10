package com.zc.bean;

import java.util.Arrays;
import java.util.Random;

//https://www.cnblogs.com/8335IT/p/5635965.html
//https://www.cnblogs.com/txq157/p/6067098.html
public class K_MeansTest {
    Point[] points;//定义点的集合作为样本
    Point[] oldCenter;//定义旧的聚类中心
    Point[] newCenter;//定义新的聚类中心

    //产生初始点的集合和聚类中心
    public void productPoint() {
        //假设对count个样本分析
        int count = 50;
        points = new Point[count];
        for (int i = 0; i < count; i++) {
            float x = new Random().nextInt(100);
            float y = new Random().nextInt(100);
            points[i] = new Point();
            points[i].setPoint_x(x);
            points[i].setPoint_y(y);
        }
        //随机产生聚类中心,假设划分5组
        int coreNum = 5;
        this.oldCenter = new Point[coreNum];
        this.newCenter = new Point[coreNum];
        Random random = new Random();
        int[] temp = new int[coreNum];
        temp[0] = random.nextInt(count);
        //必须重新new对象，否则直接赋值将会使oldCenter和points指向同一个坐标.
        oldCenter[0] = new Point();
        //不能写下面这句，否则指向同一个对象
        /*oldCenter[0] = points[temp[0]];*/
        oldCenter[0].point_x = points[temp[0]].point_x;
        oldCenter[0].point_y = points[temp[0]].point_y;
        //产生不重复的聚类中心
        for (int i = 1; i < coreNum; i++) {
            int flag = 0;
            int thistemp = random.nextInt(count);
            for (int j = 0; j < i; j++) {
                if (temp[j] == thistemp) {
                    flag = 1;//说明有重复,重新random
                    break;
                }
            }
            if (flag == 1) {
                i--;//重新random
            } else {
                temp[i] = thistemp;
                oldCenter[i] = new Point();
                //oldCenter[i] = points[thistemp];//这句有问题
                oldCenter[i].point_x = points[thistemp].point_x;
                oldCenter[i].point_y = points[thistemp].point_y;
                oldCenter[i].flag = 0;//0表示聚类中心
            }
        }
        System.out.println("初始聚类中心:");
        for (int i = 0; i < oldCenter.length; i++) {
            System.out.println(oldCenter[i]);
        }
    }

    //确定每个点属于哪个聚类中心
    public void searchBelong() {
        for (int i = 0; i < points.length; i++) {
            double mindis = 10000;
            int label = -1;
            for (int j = 0; j < oldCenter.length; j++) {
                double distance = getDistance(points[i], oldCenter[j]);
                if (distance < mindis) {
                    mindis = distance;
                    label = j;
                }
            }
            //选出距离最近的聚类中心,并把该点的flag = 第几个聚类中心
            points[i].flag = label + 1;
        }
    }

    //更新聚类中心
    public void updateCenter() {
        for (int i = 0; i < oldCenter.length; i++) {
            System.out.println("以" + oldCenter[i] + "为中心的点：");
            int num = 0;
            float sumx = 0;
            float sumy = 0;
            for (int j = 0; j < points.length; j++) {
                if (points[j].flag == (i + 1)) {
                    num += 1;
                    sumx += points[j].point_x;
                    sumy += points[j].point_y;
                    System.out.println(points[j]);
                }
            }
            //更新聚类中心
            newCenter[i] = new Point();
            newCenter[i].point_x = sumx / num;
            newCenter[i].point_y = sumy / num;
            newCenter[i].flag = 0;
            System.out.println("新的聚类中心：" + newCenter[i]);
        }
    }

    //计算两个点之间的距离
    public double getDistance(Point point, Point center) {
        return Math.sqrt(Math.pow((point.point_x - center.point_x), 2) +
                Math.pow((point.point_y - center.point_y), 2));

    }

    //数组复制
    public void change_oldtonew(Point[] old, Point[] news) {
        for (int i = 0; i < old.length; i++) {
            old[i].point_x = news[i].point_x;
            old[i].point_y = news[i].point_y;
            old[i].flag = 0;// 表示为聚类中心的标志。
        }
    }

    //聚类中心变化过程
    public void movecore() {
        //确定每个点属于哪个聚类中心
        this.searchBelong();
        //更新聚类中心
        this.updateCenter();
        //定义样本中心移动距离
        double movedistance = 0;
        int over = -1;//定义结束的标志<0.01
        for (int i = 0; i < oldCenter.length; i++) {
            movedistance = getDistance(oldCenter[i], newCenter[i]);
            System.out.println("distcore:" + movedistance);
            if (movedistance < 0.01) {
                over = 0;//结束
            } else {
                over = 1;
                break;
            }
        }
        if (over == 0) {
            System.out.println("迭代完毕！！！！");
        } else {//否则继续迭代
            change_oldtonew(oldCenter,newCenter);
            this.movecore();
        }
    }
    public static void main(String[] args) {
        K_MeansTest test = new K_MeansTest();
        test.productPoint();
        System.out.println(Arrays.toString(test.points));
        test.movecore();
    }
}
