package gui;

import log.Logger;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import java.util.ArrayList;

public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();

    private ArrayList<Obstacle> obstacles=new ArrayList<>();
    ArrayList<RobotMove> robots = new ArrayList<>();
    private int drawnObstacles;

    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

        RobotMove robot = new RobotMove();
    public GameVisualizer(){
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                robot.onModelUpdateEvent();
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton()==MouseEvent.BUTTON1) {
                    robot.setTargetPosition(e.getPoint());
                    Logger.debug("Робот начал движение в точку: " + "(" + e.getX() + ";" + e.getY() + ")");
                    repaint();
                }
                if(e.getButton()==MouseEvent.BUTTON3){
                    obstacles.add(new Obstacle(e.getPoint()));
                    Logger.debug("Создано препятсвие в координатах: " + "(" + e.getX() + ";" + e.getY() + ")");
                }
                if (e.getButton()==MouseEvent.BUTTON2){
                    for (Obstacle o:obstacles){
                        if (o.hasInBorder(e.getPoint())){
                            obstacles.remove(o);
                            Logger.debug("Удаленно препятсвие в координатах: " + "(" + e.getX() + ";" + e.getY() + ")");
                        }
                    }
                }
            }
        });
            setDoubleBuffered(true);
    }
    
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    private static int round(double value)
    {
        return (int)(value + 0.5);
    }

    void addRobot() {
        LoadClass lc = new LoadClass("C:\\Users\\skalo\\Desktop\\Java\\Robots-master\\robots\\out\\production\\Robots-master\\algs");
        String[] choices = new String[lc.classes.size()];
        choices = lc.classes.keySet().toArray(choices);
        String in = (String) JOptionPane.showInputDialog(null, "Выберите алгоритм", "Выбор алгоритма", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        if (in != null) {
            RobotMove robotMove = new RobotMove();
            robotMove.cls = lc.classes.get(in);
            robots.add(robotMove);
            robotMove.obstacles = robot.obstacles;
        }
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawRobot(g2d, round(robot.m_robotPositionX), round(robot.m_robotPositionY), robot.m_robotDirection);
        drawTarget(g2d, robot.m_targetPositionX, robot.m_targetPositionY);
        for(int i=0; i<obstacles.size(); i++){
            obstacles.get(i).paint(g2d);
        }
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        int robotCenterX = round(robot.m_robotPositionX);
        int robotCenterY = round(robot.m_robotPositionY);
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
    }

    private void drawRect(Graphics g, int x, int y){
        g.drawRect(x-50,y-50, 100, 100);
    }
    
    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
