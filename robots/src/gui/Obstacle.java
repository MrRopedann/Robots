package gui;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Obstacle implements Serializable {

    private static final int GAP = 20;
    private Point leftUp;
    private Point rightUp;
    private Point leftDown;
    private Point rightDown;

    public Obstacle(Point p) {
        leftUp = new Point(p.x - 7, p.y - 7);
        leftDown = new Point(p.x + 7, p.y - 7);
        rightUp = new Point(p.x - 7, p.y + 7);
        rightDown = new Point(p.x + 7, p.y + 7);
    }

    void paint(Graphics g) {//рисовка робота
        g.setColor(Color.BLACK);
        g.fillRect(this.leftUp.x + 1, this.leftUp.y + 1, 14, 14);
        g.setColor(Color.BLACK);
        g.drawRect(this.leftUp.x, this.leftUp.y, 15, 15);
    }

    boolean hasInBorder(Point p) {
        return (p.x > this.leftUp.x && p.y > this.leftUp.y &&
                p.x < this.rightDown.x && p.y < this.rightDown.y);
    }

    public Rectangle getRectangle() {
        int weight = rightDown.x - leftUp.x + GAP;
        int height = rightDown.y - leftUp.y + GAP;
        return new Rectangle(leftUp.x - GAP / 2, leftUp.y - GAP / 2, weight, height);
    }

    public ArrayList<Point> getPoints() {
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point((int) rightDown.getX() + GAP, (int) rightDown.getY() + GAP));
        points.add(new Point((int) rightUp.getX() - GAP, (int) rightUp.getY() + GAP));
        points.add(new Point((int) leftUp.getX() - GAP, (int) leftUp.getY() - GAP));
        points.add(new Point((int) leftDown.getX() + GAP, (int) leftDown.getY() - GAP));

        return points;
    }
}