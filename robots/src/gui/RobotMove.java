package gui;

import logistic.PathFinder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observer;

public class RobotMove extends java.util.Observable {
    private static final double maxVelocity = 0.1;
    volatile double m_robotPositionX;
    volatile double m_robotPositionY;
    volatile double m_robotDirection;
    volatile int m_targetPositionX;
    volatile int m_targetPositionY;
    ArrayList<Observer> observable = new ArrayList<>();
    private Iterator<Point> targets;
    private Point currentTarget;

    public RobotMove() {
        m_robotPositionX = 100;
        m_robotPositionY = 100;
        m_robotDirection = 0;
        m_targetPositionX = 150;
        m_targetPositionY = 100;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    void setTargetPosition(Point targetPoint, PathFinder pathFinder) {
        m_targetPositionX = targetPoint.x;
        m_targetPositionY = targetPoint.y;

        targets = pathFinder.findPathTo(m_robotPositionX, m_robotPositionY, targetPoint);
        if (targets.hasNext()) {
            currentTarget = targets.next();
        }
    }

    private boolean isDistanceMinimal() {
        if (currentTarget == null) {
            ArrayList <Point> Points = new ArrayList<>();
            currentTarget = new Point(m_targetPositionX, m_targetPositionY);
            Points.add(currentTarget);
            targets = Points.iterator();

        }
        double distance = distance(currentTarget.getX(), currentTarget.getY(), m_robotPositionX, m_robotPositionY);
        return distance <= 0.5;
    }

    void onModelUpdateEvent() {

        while (isDistanceMinimal()) {
            if (!targets.hasNext()) {
                return;
            }
            currentTarget = targets.next();
        }

        m_robotDirection = angleTo(m_robotPositionX, m_robotPositionY, currentTarget.getX(), currentTarget.getY());
        moveRobot(maxVelocity, 10);
        setChanged();
        //notifyObservers();
    }

    private void moveRobot(double velocity, double duration)//шаг робота
    {
        double newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        double newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        m_robotPositionX = newX;
        m_robotPositionY = newY;
    }

    public void notifyObservers() {//обновление данных наблюдателей
        for (Observer o : observable) {
            o.update(this, null);
        }
    }

}