package gui;

import log.Logger;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    GameWindow()
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        setJMenuBar(generateMenuBar());
        getContentPane().add(panel);
        pack();
    }
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(generateRobot());
        return menuBar;
    }

    private JMenu generateRobot() {

        JMenu otherMenu = new JMenu("Создать робота");
        ActionListener act = (event) -> {
            m_visualizer.robots.add(new Robot());
            Logger.debug("Создан новый робот");
            Logger.debug("Роботов на поле: " + m_visualizer.robots.size());
        };
        addSubMenu(otherMenu, "новый робот", act, KeyEvent.VK_A);
        return otherMenu;
    }

    private void addSubMenu(JMenu bar, String name, ActionListener listener, int keyEvent){
        JMenuItem subMenu = new JMenuItem(name, keyEvent);
        subMenu.addActionListener(listener);
        bar.add(subMenu);
    }

    void addObstacle(Obstacle o){
        m_visualizer.currentRobot.robotMove.obstacles.add(o);
    }

    ArrayList<Obstacle> getObstacles(){
        return m_visualizer.robots.get(0).robotMove.obstacles;
    }

    void addObs(RobotCoordWindow window){
        m_visualizer.robots.get(0).robotMove.observable.add(window);
    }

    JInternalFrame getObserver(){
        return  (JInternalFrame) m_visualizer.robots.get(0).robotMove.observable.get(0);
    }

    boolean isNoObs(){
        return  m_visualizer.robots.get(0).robotMove.observable.isEmpty();
    }

    void removeObs(RobotCoordWindow window){
        m_visualizer.robots.get(0).robotMove.observable.remove(window);
    }

    double getRobotX(){
        return m_visualizer.robots.get(0).robotMove.m_robotPositionX;
    }

    double getRobotY(){
        return m_visualizer.robots.get(0).robotMove.m_robotPositionY;
    }

    double getDirection(){
        return m_visualizer.robots.get(0).robotMove.m_robotDirection;
    }

    void setDirection(double direction){
        m_visualizer.robots.get(0).robotMove.m_robotDirection = direction;
    }

    void setRobotPosition(Point point){
        m_visualizer.robots.get(0).robotMove.m_robotPositionX=point.x; m_visualizer.robots.get(0).robotMove.m_robotPositionY=point.y;
    }

    Point getTargetPosition(){
        return m_visualizer.robots.get(0).robotMove.getTargetPosition();
    }

    void setTargetPosition(Point position){
        m_visualizer.robots.get(0).robotMove.setTargetPosition(position);
    }
}
