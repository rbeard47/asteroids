package models;

import com.sun.istack.internal.Nullable;
import messaging.AsteroidDestroyed;
import messaging.MessageSmallSaucerDestroyed;
import messaging.MessageManager;
import org.joml.Intersectionf;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import program.DisplayManager;
import program.SoundManager;

import java.util.Vector;

public class Bullet implements IGameComponent, IDrawableGameComponent {

    private static Model bulletModel = ModelLoader.loadModel("BULLET");
    public Vector3f position;
    private Vector3f color;
    private IBulletEvent callback;
    private float velocity = 300f;
    private float angle;
    private float lifetime = 1.5f;
    private Matrix4f localTransform;
    private boolean isDead;

    public Bullet(Vector3f position, float angle, @Nullable IBulletEvent callback) {

        this.callback = callback;
        this.position = position;
        this.angle = angle;

        localTransform = new Matrix4f();

        color = new Vector3f(1, 0.1f, 0.1f);

        SoundManager.getInstance().PlaySound(SoundManager.SoundEffect.FIRE);
    }

    @Override
    public IGameComponent.ObjectType getObjectType() {
        return IGameComponent.ObjectType.BULLET;
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
            ObjectType type = component.getObjectType();

            if (type == IGameComponent.ObjectType.ASTEROID || type == ObjectType.ENEMY_SHIP) {

                if (type == ObjectType.ASTEROID) {
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
                            radius = 38;
                            break;
                    }

                    if (Intersectionf.testCircleCircle(position.x, position.y, .1f, asteroidPosition.x,
                            asteroidPosition.y, radius)) {
                        destroy();

                        switch (size) {
                            case small:
                                SoundManager.getInstance().PlaySound(SoundManager.SoundEffect.SMALLEXPLOSION);
                                break;
                            case medium:
                                SoundManager.getInstance().PlaySound(SoundManager.SoundEffect.MEDIUMEXPLOSION);
                                break;
                            case large:
                                SoundManager.getInstance().PlaySound(SoundManager.SoundEffect.LARGEEXPLOSION);
                                break;
                        }

                        manager.removeGameComponent(this);
                        destroyedAsteroids.add(asteroid);
                    }
                } else if (type == ObjectType.ENEMY_SHIP) {

                    AlienShip enemyShip = (AlienShip) component;
                    Vector3f enemyShipPosition = enemyShip.getPosition();

                    if (Intersectionf.testCircleCircle(position.x, position.y, 0.1f, enemyShipPosition.x,
                            enemyShipPosition.y, 8)) {

                        SoundManager.getInstance().PlaySound(SoundManager.SoundEffect.SMALLEXPLOSION);
                        manager.removeGameComponent(enemyShip);
                        MessageManager.getInstance().PostMessage(new MessageSmallSaucerDestroyed(new Vector3f(position)));
                    }
                }
            }
        }

        for (Asteroid asteroid : destroyedAsteroids) {
            MessageManager.getInstance().PostMessage(new AsteroidDestroyed(asteroid));
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
