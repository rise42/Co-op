package com.forsenboyz.rise42.server.objects.projectiles;

import com.forsenboyz.rise42.server.objects.Object;
import com.forsenboyz.rise42.server.objects.RotatableObject;

public class Projectile extends RotatableObject {

    private final int type;

    private final int maxMovementRange;
    private final int moveSpeed;

    private int movementAngle;
    private int currentRangeMoved;

    public Projectile(int type, float x, float y, int width, int height,
                      int maxMovementRange, int moveSpeed, int movementAngle) {
        super(x, y, width, height, movementAngle);
        this.type = type;
        this.maxMovementRange = maxMovementRange;
        this.moveSpeed = moveSpeed;
        this.movementAngle = movementAngle;
        this.currentRangeMoved = 0;
    }

    public void move(){
        this.x += this.moveSpeed * Math.cos(Math.toRadians(movementAngle));
        this.y += this.moveSpeed * Math.sin(Math.toRadians(movementAngle));
        currentRangeMoved += moveSpeed;
    }

    public boolean hasReachedDestination(){
        return currentRangeMoved - maxMovementRange > 0;
    }

    public int getType() {
        return type;
    }
}