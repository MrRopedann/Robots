package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameWindow extends JInternalFrame {
    private final GameVisualizer m_visualizer;

    GameWindow() {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        addRobotsList(panel);
        setJMenuBar(generateMenuBar());
        getContentPane().add(panel);
        pack();
    }

    private void addRobotsList(JPanel panel) {
        JList<Robot> list = new JList(m_visualizer.getRobots());
        list.addListSelectionListener(event -> m_visualizer.setCurrentRobot(list.getSelectedValue()));
        panel.add(list, BorderLayout.WEST);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(generateRobot());
        return menuBar;
    }

    private JMenu generateRobot() {

        JMenu otherMenu = new JMenu("Операции");
        ActionListener act = (event) -> {
            m_visualizer.getRobots().addElement(new Robot());
            Logger.debug("Создан новый робот");
            Logger.debug("Роботов на поле: " + m_visualizer.getRobots().size());
        };
        addSubMenu(otherMenu, "Создать робота", act, KeyEvent.VK_N);

        return otherMenu;
    }

    private void addSubMenu(JMenu bar, String name, ActionListener listener, int keyEvent) {
        JMenuItem subMenu = new JMenuItem(name, keyEvent);
        subMenu.addActionListener(listener);
        bar.add(subMenu);
    }

    void addObstacle(Obstacle o) {
        m_visualizer.getObstacles().add(o);
    }

    ArrayList<Obstacle> getObstacles() {
        return m_visualizer.getObstacles();
    }

    void addObs(RobotCoordWindow window) {
        m_visualizer.getRobots().firstElement().getRobotMove().observable.add(window);
    }

    JInternalFrame getObserver() {
        return (JInternalFrame) m_visualizer.getRobots().firstElement().getRobotMove().observable.get(0);
    }

    boolean isNoObs() {
        return m_visualizer.getRobots().firstElement().getRobotMove().observable.isEmpty();
    }

    double getRobotX() {
        return m_visualizer.getRobots().firstElement().getRobotMove().m_robotPositionX;
    }

    double getRobotY() {
        return m_visualizer.getRobots().firstElement().getRobotMove().m_robotPositionY;
    }
}
