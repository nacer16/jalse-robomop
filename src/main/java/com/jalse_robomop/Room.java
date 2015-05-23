package com.jalse_robomop;

import com.jalse_robomop.entities.RoboMop;
import jalse.JALSE;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Room {
    private int width;
    private int height;
    private boolean[][] cleanedFloorSquares;
    JALSE jalse;

    public Room(int width, int height, JALSE jalse) {
        this.width = width;
        this.height = height;
        cleanedFloorSquares = new boolean[this.width][this.height];
        this.jalse = jalse;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Mark the given position in the room as clean.
     *
     * @param position Position that is now clan.
     */
    public void markPositionAsClean(Point2D.Double position) {
        Point roundedRoboMopPosition = getRoundedPosition(position);
        cleanedFloorSquares[roundedRoboMopPosition.x][roundedRoboMopPosition.y] = true;
    }

    /**
     * Get a position rounded to the nearest floor tile.
     *
     * @param position A non rounded position.
     * @return A rounded position.
     */
    public Point getRoundedPosition(Point2D.Double position) {
        int x = (int)Math.round(position.x);
        if(x < 0) {
            x = 0;
        } else if (x == this.width) {
            x = this.width - 1;
        }
        int y = (int)Math.round(position.y);
        if(y < 0) {
            y = 0;
        } else if (y == this.height) {
            y = this.height - 1;
        }
        return new Point(x, y);
    }

    /**
     * Checks if the floor is clean. If it is then ends the current simulation.
     */
    public void checkIfFloorIsClean() {
        boolean wholeFloorClean = true;
        for(int x = 0; x < this.width - 1; x++) {
            for(int y = 0; y < this.height - 1; y++) {
                wholeFloorClean = wholeFloorClean && cleanedFloorSquares[x][y];
                if(!wholeFloorClean) break;
            }
        }
        if(wholeFloorClean) {
            System.out.println("The floor is clean.");
            printFloor();
            jalse.stop();
        } else {
            System.out.println("The floor is dirty.");
            printFloor();
        }
    }

    /**
     * Print the current state of the floor. X = dirty, ' ' = clean, R = RoboMop.
     */
    public void printFloor() {
        java.util.List<Point> roundedRoboMopPositions = new ArrayList<>();
        jalse.streamEntitiesOfType(RoboMop.class).forEach(roboMop -> {
            roundedRoboMopPositions.add(getRoundedPosition(roboMop.getPosition()));
        });

        String floor = "";
        for(int y = 0; y < this.width; y++) {
            for(int x = 0; x < this.height; x++) {
                boolean tileHasRoboMop = false;
                for(Point roboMopPosition : roundedRoboMopPositions) {
                    if(roboMopPosition.x == x && roboMopPosition.y == y) {
                        tileHasRoboMop = true;
                        break;
                    }
                }

                String tileContents;
                if(tileHasRoboMop) {
                    tileContents = "R";
                } else if(cleanedFloorSquares[x][y]) {
                    tileContents = " ";
                } else {
                    tileContents = "X";
                }

                floor += "[" + tileContents + "]";
            }
            floor += "\n";
        }
        System.out.println(floor);
    }
}
