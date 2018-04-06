package models;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import program.DisplayManager;

public class Particle implements IGameComponent, IDrawableGameComponent {

    private Vector3f particles[];

    public Particle() {
        particles = new Vector3f[8];
    }

    @Override
    public ObjectType getObjectType() {
        return null;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

    }

    @Override
    public Matrix4f getTransform() {
        return null;
    }

    @Override
    public Vector3f getColor() {
        return null;
    }

    @Override
    public Vector3f getPosition() {
        return null;
    }

    @Override
    public void draw() {

    }
}
