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
    ArrayList<Robot> robots = new ArrayList<>();

    private int drawnObstacles;

    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    //RobotDraw currentRobot = robots.get(0);
    TargetDraw targetDraw = new TargetDraw();
    Robot robot = new Robot();
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
                robot.robotMove.onModelUpdateEvent();
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                for (Robot robot : robots) {
                    if (e.getButton() == MouseEvent.BUTTON2 && ((Math.pow(e.getX() - robot.robotDraw.round(robot.robotMove.m_targetPositionX), 2) / 30 * 30
                            + Math.pow(e.getY() - robot.robotDraw.round(robot.robotMove.m_targetPositionY), 2)) / 10 * 10) <= 300) {
                        //currentRobot = robotDraw;
                        break;
                    }
                }
                if (e.getButton()==MouseEvent.BUTTON1) {
                    robot.robotMove.setTargetPosition(e.getPoint());
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

        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e, MouseEvent me) {
                super.keyTyped(e);
                if (e.getKeyCode() == KeyEvent.VK_PLUS)
                {
                    obstacles.add(new Obstacle(me.getPoint()));
                    Logger.debug("Создано препятсвие в координатах: " + "(" + me.getX() + ";" + me.getY() + ")");
                }
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
        targetDraw.drawTarget(g2d, robot.robotMove.m_targetPositionX, robot.robotMove.m_targetPositionY);
        robot.robotDraw.paint(g2d);

        for (int i = 0; i<robots.size(); i++){
            robots.get(i).robotDraw.paint(g2d);
        }
        for(int i=0; i<obstacles.size(); i++){
            obstacles.get(i).paint(g2d);
        }
    }
}