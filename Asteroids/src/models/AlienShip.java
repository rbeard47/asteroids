package models;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import program.DisplayManager;
import program.SoundManager;

public class AlienShip implements IGameComponent, IDrawableGameComponent {

    private static Model enemyShip = ModelLoader.loadModel("enemy_ship");
    private static Vector3f scale = new Vector3f(35, -30, 1);
    private static Vector3f color = new Vector3f(1, 1, 1);

    private Matrix4f localTransform;

    private Vector3f position;

    private float direction;
    private float velocity;
    private float rotationAngle = 0;
    private float time = 0f;
    private float gunTimer = 0;

    public AlienShip() {
        localTransform = new Matrix4f();
        position = new Vector3f(-10, (float) Math.random() * 300 + 30, 0);
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
    public Vector3f getColor() {
        return color;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public void draw() {
        enemyShip.render();
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.ENEMY_SHIP;
    }

    @Override
    public void update(DisplayManager manager, float msec) {
        position.x += 80f * msec;

        if (position.x > manager.getScreenWidth()) {
            manager.removeGameComponent(this);
        }

        time += msec;

        if (time > .14f) {
            SoundManager.getInstance().PlaySound(SoundManager.SoundEffect.SAUCERBIG);
            time = 0;
        }

        gunTimer += msec;

        if (gunTimer > 1) {
            for (IGameComponent component : manager.components()) {
                if (component.getObjectType() == ObjectType.SPACESHIP) {
                    Spaceship ship = (Spaceship) component;
                    Vector3f enemyPosition = ship.getPosition();

                    float angleToEnemy = (float) (Math.atan2(enemyPosition.y - position.y,
                            enemyPosition.x - position.x) - Math.PI / 2)  + (float)Math.random() * 0.1f - 0.05f;

                    manager.addGameComponent(new EnemyBullet(new Vector3f(position), angleToEnemy, null));
                    break;
                }
            }

            gunTimer = 0;
        }
    }
}
