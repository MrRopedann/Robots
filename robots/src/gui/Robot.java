package gui;

public class Robot {
    public RobotDraw robotDraw;
    public RobotMove robotMove;

    public Robot(){
        this.robotMove = new RobotMove();
        this.robotDraw = new RobotDraw();
    }
}
