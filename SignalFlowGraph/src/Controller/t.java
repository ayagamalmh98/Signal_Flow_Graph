package Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.QuadCurve2D;

public class t extends JFrame {
    private JPanel panell;
    private JButton clickButton;
    private QuadCurve2D myArc;

    public t()  {
        clickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myArc = new QuadCurve2D.Double(200 ,200, 300, 300,
                        400, 400);


            }
        });
    }

    public static void main(String[] args) {
        t tt=new t();
        tt.setContentPane(new t().panell);
        tt.setVisible(true);


    }
}
