package gui;

import log.Logger;

import java.awt.*;
import java.awt.event.*;
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

    //RobotDraw currentRobot = robots.get(0);

    RobotMove robotMove = new RobotMove();
    TargetDraw targetDraw = new TargetDraw();
    RobotDraw robotDraw = new RobotDraw(robotMove);
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
                robotMove.onModelUpdateEvent();
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                for (RobotDraw robotDraw : robots) {
                    if (e.getButton() == MouseEvent.BUTTON2 && ((Math.pow(e.getX() - robotDraw.round(robotMove.m_targetPositionX), 2) / 30 * 30
                            + Math.pow(e.getY() - robotDraw.round(robotMove.m_targetPositionY), 2)) / 10 * 10) <= 300) {
                        //currentRobot = robotDraw;
                        break;
                    }
                }
                if (e.getButton()==MouseEvent.BUTTON1) {
                    robotMove.setTargetPosition(e.getPoint());
                    Logger.debug("Робот начал движение в точку: " + "(" + e.getX() + ";" + e.getY() + ")");
                    repaint();
                }
                if(e.getButton()==MouseEvent.BUTTON3){
                    obstacles.add(new Obstacle(e.getPoint()));
                    Logger.debug("Создано препятсвие в координатах: " + "(" + e.getX() + ";" + e.getY() + ")");
                }
                /*
                if (e.getButton()==MouseEvent.BUTTON2){
                    for (Obstacle o:obstacles){
                        if (o.hasInBorder(e.getPoint())){
                            obstacles.remove(o);
                            Logger.debug("Удаленно препятсвие в координатах: " + "(" + e.getX() + ";" + e.getY() + ")");
                        }
                    }
                }
                 */
            }
        });
        setDoubleBuffered(true);
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        targetDraw.drawTarget(g2d, robotMove.m_targetPositionX, robotMove.m_targetPositionY);
        robotDraw.paint(g2d);

        for (int i = 0; i<robots.size(); i++){
            robots.get(i).paint(g2d);
        }
        for(int i=0; i<obstacles.size(); i++){
            obstacles.get(i).paint(g2d);
        }
    }
}