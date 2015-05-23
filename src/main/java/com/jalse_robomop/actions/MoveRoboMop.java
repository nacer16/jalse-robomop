package com.jalse_robomop.actions;

import com.jalse_robomop.Room;
import com.jalse_robomop.entities.RoboMop;
import jalse.JALSE;
import jalse.actions.Action;
import jalse.actions.ActionContext;
import jalse.attributes.Attributes;

import java.awt.geom.Point2D;

public class MoveRoboMop implements Action<JALSE> {
    private static final double MOVE_SPEED = 1;

    @Override
    public void perform(final ActionContext<JALSE> context) {
        context.getActor().streamEntitiesOfType(RoboMop.class).forEach(roboMop -> {
            System.out.println("RoboMop moving.");
            Point2D.Double position = roboMop.getPosition();
            double angle = roboMop.getAngle();

            Room room = context.get("room");

            Point2D.Double newPosition = new Point2D.Double(
                    position.x + (MOVE_SPEED * Math.cos(angle)),
                    position.y + (MOVE_SPEED * Math.sin(angle))
            );
            if (newPosition.x <= 0 || newPosition.x >= room.getWidth() ||
                newPosition.y <= 0 || newPosition.y >= room.getHeight()) {
                System.out.println("Hit a wall, changing direction.");
                roboMop.setAngle(Math.random() * 359);
                roboMop.printRoboMopData();
                return;
            }

            position.setLocation(newPosition.x, newPosition.y);
            roboMop.fireAttributeChanged("position", Attributes.newTypeOf(Point2D.Double.class));
        });
    }
}
