package models;

import messaging.MessageManager;
import messaging.MessageSpaceshipDestroyed;
import org.joml.Intersectionf;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import program.DisplayManager;

public class EnemyBullet implements IDrawableGameComponent, IGameComponent {

    private static Model enemyBullet = ModelLoader.loadModel("BULLET");
    private float angle;
    private float velocity = 300;
    private float lifetime = 1.6f;
    private Vector3f position;
    private Vector3f color;
    private Matrix4f localTransform;
    private Vector3f scale = new Vector3f(15, 15, 1);

    public EnemyBullet(Vector3f position, float angle, Bullet.IBulletEvent callback) {
        this.position = position;
        this.angle = angle;
        color = new Vector3f(1f, 0.0f, 1f);
        localTransform = new Matrix4f();
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.ENEMY_BULLET;
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

        if (lifetime <= 0) {
            manager.removeGameComponent(this);
        }

        Spaceship spaceship = null;
        boolean destroyed = false;

        for (int i = manager.components().size() - 1; i >= 0; i--) {
            IGameComponent component = manager.components().get(i);
            ObjectType type = component.getObjectType();

            if (type == ObjectType.SPACESHIP) {
                spaceship = (Spaceship) component;
                Vector3f spaceshipPosition = spaceship.getPosition();
                if (Intersectionf.testCircleCircle(position.x, position.y, .1f, spaceshipPosition.x,
                        spaceshipPosition.y, 5f)) {
                    destroyed = true;
                }

                break;
            }
        }

        if (destroyed) {
            manager.removeGameComponent(spaceship);
            MessageManager.getInstance().PostMessage(new MessageSpaceshipDestroyed(((Spaceship) spaceship)
                    .getPosition()));
        }
    }

    @Override
    public Matrix4f getTransform() {
        localTransform.identity();
        localTransform.translate(position)
                .scale(scale);

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
        enemyBullet.render();
    }
}
