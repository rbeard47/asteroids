import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.glfw.GLFW.*;

public class Spaceship implements IGameComponent, IDrawableGameComponent, Bullet.IBulletEvent {

    private final static int max_bullet_count = 6;
    private Vector3f position;
    private Vector3f heading;
    private Vector2f velocity;
    private float rotationAngle = 0;
    private float rotationVelocity = 0;
    private Matrix4f localTransform;

    private AtomicInteger bulletCount;
    private boolean isFiring = false;
    private Vector3f color;

    private Model spaceshipModel;
    private Model thrusterModel;

    public Spaceship() {

        spaceshipModel = ModelLoader.loadModel("spaceship");
        thrusterModel = ModelLoader.loadModel("thrust");

        bulletCount = new AtomicInteger();
        bulletCount.set(0);
        localTransform = new Matrix4f();

        position = new Vector3f(400, 300, 0);
        velocity = new Vector2f();
        heading = new Vector3f(0, 1, 0);

        color = new Vector3f(1, 1, 1);
    }

    public Matrix4f getTransform() {
        localTransform.identity();
        localTransform.translate(position)
                .rotateZ(rotationAngle)
                .scale(new Vector3f(10f, 10f, 1));

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
    public ObjectType getObjectType() {
        return ObjectType.spaceship;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

        double dx = 0.0;
        double dy = 0.0;
        double rx = 0.0;

        long window = manager.getWindow();

        if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
            dx += -Math.sin(rotationAngle) * 4f * msec;
            dy += Math.cos(rotationAngle) * 4f * msec;
        }

        if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
            rx += 300 * msec;
        }

        if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
            rx -= 300 * msec;
        }

        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS && !isFiring) {
            isFiring = true;

            if (bulletCount.get() < max_bullet_count) {
                bulletCount.incrementAndGet();
                manager.addGameComponent(new Bullet(new Vector3f(position.x, position.y, 1),
                        rotationAngle, this::eventCallback));
            }
        }

        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_RELEASE) {
            isFiring = false;
        }

        velocity.x += dx;
        velocity.y += dy;

        if (velocity.x > 1) {
            velocity.x = 1;
        }
        if (velocity.y > 1) {
            velocity.y = 1;
        }

        rotationVelocity += rx;

        position.x += velocity.x;
        position.y += velocity.y;

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
        rotationVelocity *= 0.4f;
        velocity.x *= 0.993f;
        velocity.y *= 0.993f;
    }

    @Override
    public void draw() {
        spaceshipModel.render();
    }

    @Override
    public void eventCallback() {
        if (bulletCount.decrementAndGet() < 0) {
            bulletCount.set(0);
        }
    }
}
