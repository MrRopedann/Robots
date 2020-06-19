package gui;

public class Robot {
    private RobotDraw robotDraw;
    private RobotMove robotMove;

    public Robot(){
        robotMove = new RobotMove();
        robotDraw = new RobotDraw(robotMove);
    }

    public RobotDraw getRobotDraw() {
        return robotDraw;
    }

    public RobotMove getRobotMove() {
        return robotMove;
    }

    public void onModelUpdateEvent() {
        this.robotMove.onModelUpdateEvent();
    }
}
