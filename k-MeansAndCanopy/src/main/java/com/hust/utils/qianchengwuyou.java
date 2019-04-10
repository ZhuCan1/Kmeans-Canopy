package com.hust.utils;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.select.Elements;

public class qianchengwuyou extends BreadthCrawler {
    public qianchengwuyou(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        /*种子页面*/
        //程序的入口,也是第一个爬取的网站
        this.addSeed("https://jobs.51job.com/wuhan-hsq/109400352.html?s=01&t=0");
        //正则规则设置
        //爬取符合https://jobs.51job.com/wuhan-hsq/109400352.html?s=01&t=0
        this.addRegex("https://jobs.51job.com/.*/[0-9]{8,9}.html.*");
        //不要爬取jpg|png|gif*/
        this.addRegex("-.*\\.(jpg|png|gif)");
        //不要爬取含#的URL
        this.addRegex("-.*#.*");
    }

    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {
        String url = page.getUrl();
        if (page.matchUrl("https://jobs.51job.com/.*/[0-9]{8,9}.html.*")) {
            //jsoup：选择器语法查找元素
            String title = page.select("div.cn > h1").text();
            String companyName = page.select("div .com_name").text();
            String jobBenefits = page.select("div.t1").text();
            String salary = page.select("div.cn > strong").text();
            String jobInformation = page.select("div.job_msg").text();

            Elements information = page.select("div p.at");
            String field = information.get(2).text();//公司领域
            String type = information.get(0).text();//公司类型
            String scale = information.get(1).text();//公司规模
            //发布时间
            String[] timeArray = page.select("p.msg").text().split("\\|");
            String place = timeArray[0].trim();
            String time = "";
            for (int i = 0; i < timeArray.length; i++) {
                if (timeArray[i].indexOf("发") != -1) {
                    time = timeArray[i].substring(0, timeArray[i].indexOf("发")).trim();
                }
            }
            //sql插入语句
            String sql = "insert into qianchengwuyou(url,title,companyName,jobBenefits,salary,jobInformation,place,field,type,scale,time) values('"
                    + url + "','" + title + "','" + companyName + "','" + jobBenefits + "','" + salary
                    + "','" + jobInformation + "','" + place + "','" + field + "','" + type + "','" + scale
                    + "','" + time  + "')";
            DB db = new DB();
            db.open(sql);
            db.close();
        }
    }

    public static void main(String[] args) throws Exception {
        /*如果autoParse是true(构造函数的第二个参数)，爬虫会自动抽取网页中符合正则规则的URL，
        作为后续任务，当然，爬虫会去掉重复的URL,不会爬起历史中爬过的URL.
        * */
        qianchengwuyou crawler = new qianchengwuyou("crawl", true);
        //线程数
        crawler.setThreads(500);
        //设置每次迭代爬取数量的上限
        crawler.setTopN(50000);
        /*可以设置每个线程visit的间隔，这里是毫秒。主要用于具有反爬虫机制的网站*/
        //crawler.setExecuteInterval(10000);
        /*设置是否为断点爬取，如果设置为false，任务启动前会清空历史数据。
         如果设置为true,会在已有的crawlPath(构造函数的第一个参数)的基础上继续
         爬取。对于耗时较长的任务，很有可能中断爬虫，也有可能遇到死机，停电等情况，使用断点爬取模式，可以保证爬虫不受这些因素的影响，默认为false
        * */
        //crawler.setRequester(true);

        /*开始深度为4的爬取，这里深度和网站的拓扑结构没有任何关系，可以将深度理解为迭代次数
         * ，往往迭代次数越多，爬取的数据就越多*/
        crawler.start(200);
    }
}
