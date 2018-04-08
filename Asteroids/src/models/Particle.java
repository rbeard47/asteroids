package models;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import program.DisplayManager;

public class Particle implements IDrawableGameComponent, IGameComponent {

    private static Model particleModel = ModelLoader.loadModel("BULLET");
    private Matrix4f localTransform;
    private Vector3f position;
    private float angle;
    private Vector3f color = new Vector3f(1, 1, 1);
    private Vector3f scale = new Vector3f(15, 15, 1);
    private float velocity;
    private float lifetime = 0.8f;

    public Particle(Vector3f position, float angle) {
        this.position = position;
        this.angle = angle;
        localTransform = new Matrix4f();
        velocity = (float) java.lang.Math.random() * 60f + 25;
    }

    @Override
    public Matrix4f getTransform() {
        localTransform.identity();
        localTransform.translate(position).scale(scale);

        return localTransform;
    }

    @Override
    public Vector3f getColor() {
        return color;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public void draw() {
        particleModel.render();
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.PARTICLE;
    }

    @Override
    public void update(DisplayManager manager, float msec) {
        position.x += -Math.sin(angle) * velocity * msec;
        position.y += Math.cos(angle) * velocity * msec;

        if (position.x > 800) {
            position.x = 0.1f;
        } else if (position.x <= 0) {
            position.x = 800;
        }

        if (position.y > 600) {
            position.y = 0.1f;
        } else if (position.y <= 0) {
            position.y = 600;
        }

        color.x -= 0.5f * msec;
        color.y -= 0.5f * msec;
        color.z -= 0.5f * msec;

        lifetime -= msec;

        if (lifetime <= 0) {
            manager.removeGameComponent(this);
        }
    }
}
