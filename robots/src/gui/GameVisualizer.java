package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameVisualizer extends JPanel {

    private Robot currentRobot;
    private DefaultListModel<Robot> robots;
    private TargetDraw targetDraw = new TargetDraw();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();

    public GameVisualizer() {
        robots = new DefaultListModel<>();
        robots.addElement(new Robot());
        currentRobot = robots.get(0);
        Timer m_timer = initTimer();
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
                currentRobot.someMethod();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentRobot.someMethod();

            }
        }, 0, 10);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    currentRobot.getRobotMove().setTargetPosition(e.getPoint());
                    Logger.debug("Робот" + robots.indexOf(currentRobot) + " начал движение в точку: " + "(" + e.getX() + ";" + e.getY() + ")");
                    repaint();
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    obstacles.add(new Obstacle(e.getPoint()));
                    Logger.debug("Создано препятсвие в координатах: " + "(" + e.getX() + ";" + e.getY() + ")");
                }
                if (e.getButton()==MouseEvent.BUTTON2) {
                    for (Obstacle o : obstacles) {
                        if (o.hasInBorder(e.getPoint())) {
                            obstacles.remove(o);
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

    Robot getCurrentRobot() {
        return currentRobot;
    }

    void setCurrentRobot(Robot currentRobot) {
        this.currentRobot = currentRobot;
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