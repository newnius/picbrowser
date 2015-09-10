package com.newnius.picbrowser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static javax.swing.SwingUtilities.windowForComponent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Newnius
 */

//负责加载网页并管理download线程下载图片，然后显示在窗口上
//
//
public class Go extends Thread {

    JPanel jPanel2;
    String domain;
    public boolean state = true;

    public void init(String dom, JPanel jp) {
        jPanel2 = jp;
        domain = dom;
    }

    @Override
    public void run() {
        try {
            //网址的http全路径
            URL url = new URL(domain);
            String source = "";
            String tmp;
            
             mainWindow.mySetInfo("连接中，网络似乎有点慢。。。");

            try (BufferedReader is = new BufferedReader(new InputStreamReader(url.openStream()))) {
                while ((tmp = is.readLine()) != null) {
                    source += tmp;
                    //source += "\n";
                }
            }
           
            
            mainWindow.mySetInfo("成功连接到网址，正在提取图片信息");
            System.out.println("成功连接到网址，正在提取图片信息");
            //提取网页标题
            mainWindow.mySetUrl(domain);

            String[] tit = source.split("</title>");
            String localTitle = "未找到标题";
            if (tit.length > 1) {//说明该网页有标题
                tit = tit[0].split("<title>");
                if (tit.length > 1) {
                    localTitle = tit[1];
                } else {
                    localTitle = tit[0];//title为第一个标签，可能性不大^-^
                }
            }
            mainWindow.mySetTitle(localTitle);
            System.out.println("获得标题成功");

            //改变窗口标题
            ((JFrame) windowForComponent(jPanel2)).setTitle(localTitle);

            //提取全部图片地址
            //
            //用正则表达式来改进
            String list;
            String[] rs = source.split("\"");

            int localCnt = 0;
            Set<String> set = new HashSet<>();

            String realUrl = domain; 
            
            //得到当前页所在的位置，不含文件名  形式如：http://www.oracle.com/
            
            if (realUrl.lastIndexOf("/") < realUrl.lastIndexOf(".")) {
                realUrl = realUrl.substring(0, realUrl.lastIndexOf("/") - 1);
            }
            
            //排除http://www.oracle.com的情况会被上述处理
            if(!(realUrl.startsWith("http://")||realUrl.startsWith("https://"))){
                realUrl=domain;
            }
            
            if(realUrl.charAt(realUrl.length()-1)!='/')realUrl+='/';
            
            //System.out.println(realUrl);

            for (int i = 0; state == true && i < rs.length; i++) {
                list = rs[i];
                
                //这里还要添加规则
                if (!(list.endsWith(".jpg") || list.endsWith(".png") || list.endsWith(".gif"))) {//其他后缀基本不是文件
                    continue;
                }
                
                if (list.startsWith("/")) {//有的图片给的是相对路径，要改成绝对路径
                    list = realUrl + list.substring(1, list.length());
                }else{
                    if(!(list.startsWith("http://"))||list.startsWith("https://"))list=realUrl+list;
                } 
                if (!(list.startsWith("http://") || list.startsWith("https://"))) {//不是绝对路径
                    continue;
                }
                
                //System.out.println(list);
                
                if (set.add(list)) {
                    mainWindow.mySetInfo("已找到" + ++localCnt + "张图片。");
                }
            }
            
            System.out.println("成功获得所有图片信息");
            
            int cnt=0;

            
            //开启6个线程同时下载图片
            //这里没有处理线程死住的情况
            
            Download [] d={null,null,null,null,null,null};
            mainWindow.mySetInfo("正在下载图片，" + "共" + localCnt + " 张");
            for (Iterator<String> localIt = set.iterator(); state == true && localIt.hasNext();) {
                for(int j=0;j<6 && localIt.hasNext();j++){
                    if(d[j]!=null&&d[j].isAlive())continue;
                    list = localIt.next();
                    MyPanel pa = new MyPanel();
                    jPanel2.add(pa);
                    jPanel2.updateUI();
                    d[j]=new Download(list,pa);
                    d[j].start();
                    cnt++;
                }
                //分配完6条线程任务后，休息一会，否则会浪费循环周期
                sleep(100);
            }

//            for (Iterator<String> localIt = set.iterator(); state == true && localIt.hasNext();) {
//                list = localIt.next();
//                i++;
//                mainWindow.mySetInfo("正在下载第 " + i + "张图片，" + "共" + localCnt + " 张");
//                MyPanel pa = new MyPanel();
//                jPanel2.add(pa);
//                jPanel2.updateUI();
//                Download d = new Download(list,pa);
//                d.start();
//                
//                
//                
////                synchronized (d) {
////                    try {
////                        d.wait();
////                    } catch (Exception e) {
////                        System.out.println("wrong at line:141");
////                    }
////                }
////                String ad = d.f;
////                pa.setImage(ad);
////                pa.setInfo(ad);
////                jPanel2.updateUI();
//            }
            
            //这里应该将按钮置为GO ，但是目前并未处理
            
            if (cnt==localCnt) {
                mainWindow.mySetInfo("全部"+localCnt+"张图片下载完成，请手动将按钮置为GO状态（我没时间改了。。。）");
            } else {
                mainWindow.mySetInfo("下载已暂停");
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            System.out.println("wrong at line:157");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            mainWindow.mySetInfo("网址不存在或网络异常，请检查后重试");
            System.out.println("wrong at line:161");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("wrong at line:164");
        }
    }
}
