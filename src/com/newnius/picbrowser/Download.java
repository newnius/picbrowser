package com.newnius.picbrowser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Newnius
 */

//下载图片的线程
public class Download extends Thread {

    String pic;
    String f;
    MyPanel pa;

    public Download(String str,MyPanel pane) {
        pic = str;
        pa=pane;
    }

    @Override
    public void run() {
        try {
            //图片的http全路径
            URL url = new URL(pic);
            long no = System.currentTimeMillis();
            String type = pic.substring(pic.lastIndexOf(".") + 1, pic.length());
            f = no + "." + type;
            File outFile = new File("downloaded files/" + f);
            OutputStream os = new FileOutputStream(outFile);
            InputStream is = url.openStream();
            byte[] buff = new byte[1024];
            while (true) {
                int readed = is.read(buff);
                if (readed == -1) {
                    break;
                }
                byte[] temp = new byte[readed];
                System.arraycopy(buff, 0, temp, 0, readed);
                os.write(temp);
            }
            is.close();
            os.close();

            //假设网络很慢
            sleep(500);
            pa.setImage("downloaded files/" +f);
            pa.setInfo("downloaded files/" +f);
            pa.updateUI();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("wrong at line:58");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("wrong at line:61");
        } catch (Exception e) {
            System.out.println("wrong at line:63");
        }
    }
}
