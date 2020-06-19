package logistic;

import dijkstra.Dijkstra;
import gui.Obstacle;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class PathFinder {
    public HashMap<Point, ArrayList<Point>> graph;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Point> points;

    public PathFinder(ArrayList<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public Iterator<Point> findPathTo(double m_robotPositionX, double m_robotPositionY, Point targetPoint) {
        points = allPoints(obstacles);
        graph = getGraph();

        Point currentPoint = new Point((int) m_robotPositionX, (int) m_robotPositionY);
        HashMap<Point, ArrayList<Point>> copyGraph = copyGraph();//Может его копировать не надо

        copyGraph.put(currentPoint, new ArrayList<>());
        copyGraph.put(targetPoint, new ArrayList<>());

        LinkedList<Point> path;
        if (isIntersect(new Line2D.Double(currentPoint, targetPoint))) {
//            copyGraph.get(currentPoint).add(targetPoint);
//            copyGraph.get(targetPoint).add(currentPoint);


            for (Point p : points) {

                if (!isIntersect(new Line2D.Double(currentPoint, p))) {
                    copyGraph.get(currentPoint).add(p);
                    copyGraph.get(p).add(currentPoint);
                }

                if (!isIntersect(new Line2D.Double(p, targetPoint))) {
                    copyGraph.get(p).add(targetPoint);
                    copyGraph.get(targetPoint).add(p);
                }
            }

            path = Dijkstra.find(copyGraph, currentPoint, targetPoint);
        }
        else {
            path = new LinkedList<>();
            path.add(targetPoint);
        }
        return path.iterator();
    }

    private boolean isIntersect(Line2D line) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getRectangle().intersectsLine(line)) {
                return true;
            }
        }
        return false;
    }

    private HashMap<Point, ArrayList<Point>> getGraph() {
        HashMap<Point, ArrayList<Point>> graph = new HashMap<>();
        for (Point x : points) {
            graph.put(x, new ArrayList<>());
        }

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point fir = points.get(i);
                Point sec = points.get(j);

                Line2D line = new Line2D.Double(fir, sec);

                if (!isIntersect(line)) {
                    graph.get(fir).add(sec);
                    graph.get(sec).add(fir);
                }
            }
        }
        return graph;
    }

    private ArrayList<Point> allPoints(Iterable<Obstacle> obstacles) {
        ArrayList<Point> points = new ArrayList<>();

        for (Obstacle obstacle : obstacles)
            points.addAll(obstacle.getPoints());

        return points;
    }

    private HashMap<Point, ArrayList<Point>> copyGraph() {
        HashMap<Point, ArrayList<Point>> copy = new HashMap<>();

        for (Point key : graph.keySet()) {
            copy.put(key, new ArrayList<>());
        }

        for (Point key : graph.keySet()) {
            copy.put(key, (ArrayList<Point>) graph.get(key).clone());
        }

        return copy;
    }
}
