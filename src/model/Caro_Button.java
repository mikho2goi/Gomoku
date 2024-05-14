/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.MetalBorders;

public class Caro_Button extends JButton {

    public int value = 0;
    private final ImageIcon xImageIcon = new ImageIcon("src/resource_caro/x.png");
    private final ImageIcon oImageIcon = new ImageIcon("src/resource_caro/o.png");
    Caro_Button _this = this;

    public Caro_Button(int x, int y, boolean state) {
        this.setBackground(Color.WHITE);
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (_this.isEnabled()) {
                    _this.setBorder(new BasicBorders.ButtonBorder(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (_this.isEnabled()) {
                    _this.setBorder(new MetalBorders.ButtonBorder());
                    _this.setIcon(null);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }
        });
    }

    public void setState(boolean isHumanPlay) {
        if (this.isEnabled()) {
            if (isHumanPlay) {
                this.value = 2;
                setIcon(xImageIcon);
                setDisabledIcon(xImageIcon);
                this.setBorder(new MetalBorders.ButtonBorder());
                this.setEnabled(false);
            } else {
                this.value = 1;
                setIcon(oImageIcon);
                setDisabledIcon(oImageIcon);
                this.setEnabled(false);
                this.setBorder(new MetalBorders.ButtonBorder());
            }
        } else {
            return;
        }

    }

    public void resetState() {
        this.value = 0;
        this.setEnabled(true);
        this.setIcon(null);
        this.setBorder(new MetalBorders.ButtonBorder());
    }
}
