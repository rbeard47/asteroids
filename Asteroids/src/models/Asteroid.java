package models;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import program.DisplayManager;

public class Asteroid implements IGameComponent, IDrawableGameComponent {

    public static Vector3f SmallSize = new Vector3f(15, 15, 1);
    public static Vector3f MediumSize = new Vector3f(35, 35, 1);
    public static Vector3f LargeSize = new Vector3f(50, 50, 1);
    private static Model asteroidModel1 = ModelLoader.loadModel("large_asteroid1");
    private static Model asteroidModel2 = ModelLoader.loadModel("large_asteroid2");
    private float direction;
    private boolean destroyed = false;
    private Vector3f position;
    private Vector3f color;
    private Vector3f scale;
    private Matrix4f localTransform;
    private float velocity;
    private float rotationAngle;
    private float rotationVelocity;
    private AsteroidSize size;
    private Model asteroidModel;

    public Asteroid(AsteroidSize size, Vector3f position, float rotationAngle, float rotationVelocity,
                    float direction, float velocity) {

        if (Math.random() > 0.5f) {
            asteroidModel = asteroidModel1;
        } else {
            asteroidModel = asteroidModel2;
        }

        this.size = size;
        this.rotationVelocity = rotationVelocity;
        this.velocity = velocity;
        this.rotationAngle = rotationAngle;
        this.position = position;
        this.direction = direction;

        localTransform = new Matrix4f();

        color = new Vector3f(.5f, .5f, .5f);

        switch (size) {
            case small:
                scale = SmallSize;
                break;
            case medium:
                scale = MediumSize;
                break;
            case large:
                scale = LargeSize;
                break;
        }
    }

    public AsteroidSize getSize() {
        return size;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public void destroy() {
        destroyed = true;
    }

    public float getVelocity() {
        return velocity;
    }

    @Override
    public void draw() {
        asteroidModel.render();
    }

    @Override
    public Matrix4f getTransform() {
        localTransform.identity();
        localTransform.translate(position)
                .rotateZ(rotationAngle)
                .scale(scale);

        return localTransform;
    }

    @Override
    public IGameComponent.ObjectType getObjectType() {
        return IGameComponent.ObjectType.ASTEROID;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

        position.y += Math.sin(direction) * velocity * msec;
        position.x += Math.cos(direction) * velocity * msec;

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

        float rotationAmount = rotationVelocity * msec;

        if (rotationAmount > Math.PI) {
            rotationAmount = (float) -Math.PI;
        } else if (rotationAmount < (float) -Math.PI) {
            rotationAmount = (float) Math.PI;
        }

        rotationAngle += rotationAmount;
    }

    public float getDirection() {
        return direction;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    public enum AsteroidSize {
        small,
        medium,
        large
    }
}
