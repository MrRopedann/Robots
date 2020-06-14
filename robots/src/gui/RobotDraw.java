package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class RobotDraw extends JPanel {

    private RobotMove robotMove;

    public RobotDraw()
    {
        this.robotMove = new RobotMove();
    }

    public static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    public static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    public static int round(double value)
    {
        return (int)(value + 0.5);
    }

    public void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        int robotCenterX = round(robotMove.m_robotPositionX);
        int robotCenterY = round(robotMove.m_robotPositionY);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        //new RobotDraw(robotMove);

    }

    public void paint(Graphics2D g2d){
        drawRobot(g2d, round(robotMove.m_robotPositionX), round(robotMove.m_robotPositionY), robotMove.m_robotDirection);
    }
}
