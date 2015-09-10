package com.newnius.picbrowser;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
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

//显示图片的区域
public class MyPanel extends JPanel implements MouseListener {

    private JLabel jimg;
    private JLabel jinfo;

    public MyPanel() {
        try {
            setSize(220, 125);
            setBackground(Color.white);

            ImageIcon p = new ImageIcon("img/wait.gif");
            Image pt = p.getImage().getScaledInstance(200, 100, 0);

            jimg = new JLabel();
            jimg.setSize(200, 100);
            jimg.setIcon(new ImageIcon(pt));
            jimg.setToolTipText("双击以查看大图");
            add(jimg);

            jinfo = new JLabel();
            jinfo.setSize(0, 0);
            jinfo.setText("");

            //这里是用来存放图片的本地位置的
            jinfo.setVisible(false);
            add(jinfo);

            jimg.addMouseListener((MouseListener) this);

        } catch (Exception e) {
            System.out.println("wrong at line:49");
        }
    }

    //显示大图（原图）
    @Override
    public void mouseClicked(MouseEvent evt) {
        try {
            if (evt.getClickCount() == 2) {
                String str = ((MyPanel) ((JLabel) evt.getSource()).getParent()).jinfo.getText();
                new detail(str).setVisible(true);
            }
        } catch (Exception e) {
            System.out.println("wrong at line:62");
        }

    }

    public void setImage(String pic) {
        try{
            if(pic==null)pic="img/null.gif";
        ImageIcon p = new ImageIcon(pic);
        Image pt = p.getImage().getScaledInstance(200, 100, 0);
        jimg.setIcon(new ImageIcon(pt));
        }catch(Exception e){
        }
    }

    public void setInfo(String str) {
        jinfo.setText(str);
    }

    @Override
    public void mousePressed(MouseEvent me) {

    }

    @Override
    public void mouseReleased(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }

}
