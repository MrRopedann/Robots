package gui;

import java.awt.*;
import java.io.Serializable;

public class Obstacle implements Serializable {

    private Point leftUp;
    private Point rightUp;
    private Point leftDown;
    private Point rightDown;

    private Obstacle(){}

    public Obstacle(Point p) {
        leftUp = new Point(p.x-7,p.y-7);
        leftDown = new Point(p.x+7, p.y-7);
        rightUp = new Point(p.x-7, p.y+7);
        rightDown = new Point(p.x+7,p.y+7);
    }

    void paint(Graphics g){//рисовка робота
        g.setColor(Color.BLACK);
        g.fillRect(this.leftUp.x+1,this.leftUp.y+1,14,14);
        g.setColor(Color.BLACK);
        g.drawRect(this.leftUp.x,this.leftUp.y,15,15);
    }

    boolean hasInBorder(Point p){
        return  (p.x>this.leftUp.x&&p.y>this.leftUp.y&&
                p.x<this.rightDown.x&&p.y<this.rightDown.y);
    }

    public Point getLeftUp() {
        return leftUp;
    }

    public Point getRightUp() {
        return rightUp;
    }

    public Point getLeftDown() {
        return leftDown;
    }

    public Point getRightDown() {
        return rightDown;
    }
}