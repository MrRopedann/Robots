package gui;

public class Robot {
    public RobotDraw robotDraw;
    public RobotMove robotMove;

    public Robot(){
        robotMove = new RobotMove();
        robotDraw = new RobotDraw(robotMove);
    }

    public void someMethod(){
        this.robotMove.onModelUpdateEvent(this);
    }
}
