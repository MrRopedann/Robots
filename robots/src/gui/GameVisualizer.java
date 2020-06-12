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
    ArrayList<RobotDraw> robots = new ArrayList<>();
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

    RobotDraw robotDraw = new RobotDraw();

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawTarget(g2d, robot.m_targetPositionX, robot.m_targetPositionY);
        robots.add(new RobotDraw());
        for (int i = 0; i<robots.size(); i++){
            robots.get(i).paint(g2d);
        }
        for(int i=0; i<obstacles.size(); i++){
            obstacles.get(i).paint(g2d);
        }
    }

    private void drawRect(Graphics g, int x, int y){
        g.drawRect(x-50,y-50, 100, 100);
    }

    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        robotDraw.fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        robotDraw.drawOval(g, x, y, 5, 5);
    }
}