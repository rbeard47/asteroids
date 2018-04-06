package models;

import com.sun.istack.internal.Nullable;
import messaging.AsteroidDestroyed;
import messaging.MessageManager;
import org.joml.Intersectionf;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import program.DisplayManager;

import java.util.Vector;

public class Bullet implements IGameComponent, IDrawableGameComponent {

    public Vector3f position;
    public int vbo;
    public int cbo;
    public int vao;
    public int ibo;
    IBulletEvent callback;
    float velocity = 300.0f;
    float angle;
    float lifetime = 1.5f;
    private Matrix4f localTransform;
    private Vector3f color;
    private boolean isDead;
    private Model bulletModel;

    public Bullet(Vector3f position, float angle, @Nullable IBulletEvent callback) {

        bulletModel = ModelLoader.loadModel("bullet");

        this.callback = callback;
        this.position = position;
        this.angle = angle;
        this.velocity = velocity;

        localTransform = new Matrix4f();

        color = new Vector3f(1, 0.1f, 0.1f);
    }

    @Override
    public IGameComponent.ObjectType getObjectType() {
        return IGameComponent.ObjectType.bullet;
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

        lifetime -= msec;

        if (lifetime <= 0 && !isDead) {
            manager.removeGameComponent(this);
            destroy();
        }


        Vector<Asteroid> destroyedAsteroids = new Vector<>();

        for (int i = manager.components().size() - 1; i >= 0; i--) {
            IGameComponent component = manager.components().get(i);
            if (component.getObjectType() == IGameComponent.ObjectType.asteroid) {
                Asteroid asteroid = (Asteroid) component;
                Vector3f asteroidPosition = asteroid.getPosition();

                float radius = 0f;
                Asteroid.AsteroidSize size = asteroid.getSize();

                switch (size) {
                    case small:
                        radius = 8;
                        break;
                    case medium:
                        radius = 25;
                        break;
                    case large:
                        radius = 36;
                        break;
                }

                if (Intersectionf.testCircleCircle(position.x, position.y, .1f, asteroidPosition.x,
                        asteroidPosition.y, radius)) {
                    destroy();
                    manager.removeGameComponent(this);
                    destroyedAsteroids.add(asteroid);
                }
            }
        }

        for (Asteroid asteroid : destroyedAsteroids) {
            Asteroid.AsteroidSize size = asteroid.getSize();
            Vector3f position = asteroid.getPosition();
            float direction = asteroid.getDirection();
            manager.removeGameComponent(asteroid);
            MessageManager.getInstance().PostMessage(new AsteroidDestroyed(asteroid.getPosition(),
                    asteroid.getSize(), asteroid.getDirection()));
        }
    }

    @Override
    public void draw() {
        bulletModel.render();
    }

    public Matrix4f getTransform() {
        localTransform.identity();
        localTransform.translate(position)
                .scale(new Vector3f(10f, 10f, 1f));

        return localTransform;
    }

    @Override
    public Vector3f getPosition() {
        return this.position;
    }

    @Override
    public Vector3f getColor() {
        return color;
    }

    public void destroy() {
        isDead = true;

        if (callback != null) {
            callback.eventCallback();
        }
    }

    public interface IBulletEvent {
        void eventCallback();
    }
}
