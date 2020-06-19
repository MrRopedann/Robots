package gui;

import log.Logger;
import logistic.PathFinder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

public class GameVisualizer extends JPanel {
    private Robot currentRobot;
    private DefaultListModel<Robot> robots;
    private TargetDraw targetDraw = new TargetDraw();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private PathFinder pathFinder = new PathFinder(obstacles);

    public GameVisualizer() {
        robots = new DefaultListModel<>();
        robots.addElement(new Robot());
        currentRobot = robots.get(0);
        Timer m_timer = initTimer();
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
                currentRobot.onModelUpdateEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentRobot.onModelUpdateEvent();

            }
        }, 0, 10);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Logger.debug("Робот" + robots.indexOf(currentRobot) + " начал движение в точку: " + "(" + e.getX() + ";" + e.getY() + ")");
                    currentRobot.getRobotMove().setTargetPosition(e.getPoint(), pathFinder);
                    repaint();
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    obstacles.add(new Obstacle(e.getPoint()));
                    Logger.debug("Создано препятсвие в координатах: " + "(" + e.getX() + ";" + e.getY() + ")");
                }

                if (e.getButton() == MouseEvent.BUTTON2) {
                    ListIterator<Obstacle> obstacleIterator = obstacles.listIterator();
                    while (obstacleIterator.hasNext()) {
                        if (obstacleIterator.next().hasInBorder(e.getPoint())) {
                            obstacleIterator.remove();
                            Logger.debug("Удаленно препятсвие в координатах: " + "(" + e.getX() + ";" + e.getY() + ")");
                        }
                    }
                }
            }

        });
        setDoubleBuffered(true);
    }

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    void setCurrentRobot(Robot currentRobot) {
        this.currentRobot = currentRobot;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    private void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < robots.size(); i++) {
            robots.get(i).getRobotDraw().paint(g2d);
            targetDraw.drawTarget(g2d, robots.get(i).getRobotMove().m_targetPositionX, robots.get(i).getRobotMove().m_targetPositionY);
        }

        for (Obstacle obstacle : obstacles) {
            obstacle.paint(g2d);
        }
    }

    DefaultListModel<Robot> getRobots() {
        return robots;
    }

}