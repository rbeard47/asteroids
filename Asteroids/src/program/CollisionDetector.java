package program;

import models.Asteroid;
import models.Bullet;
import models.IGameComponent;
import org.joml.Vector3f;

import java.util.Vector;

public class CollisionDetector {


    public CollisionDetector() {

    }

    public void detectCollisions(DisplayManager manager, AsteroidManager generator) {

        Vector<IGameComponent> asteroids = new Vector<>();
        Vector<IGameComponent> bullets = new Vector<>();
        IGameComponent ship;

        for (int i = 0; i < manager.components().size(); i++) {

            IGameComponent component = manager.components().get(i);

            if (component.getObjectType() == IGameComponent.ObjectType.asteroid) {
                asteroids.add(component);
            } else if (component.getObjectType() == IGameComponent.ObjectType.spaceship) {
                ship = component;
            } else if (component.getObjectType() == IGameComponent.ObjectType.bullet) {
                bullets.add(component);
            }
        }

        for (int i = bullets.size() - 1; i >= 0; i--) {
            boolean collision = false;
            for (int j = asteroids.size() - 1; j >= 0; j--) {

                Asteroid asteroid = (Asteroid) asteroids.get(j);
                Vector3f asteroidPosition = asteroid.getPosition();

                Bullet bullet = (Bullet) bullets.get(i);
                Vector3f bulletPosition = bullet.getPosition();

                if (org.joml.Intersectionf.testCircleCircle(bulletPosition.x, bulletPosition.y, .1f, asteroidPosition.x, asteroidPosition.y, 36)) {
                    manager.removeGameComponent(bullet);
                    asteroid.destroy();
                    bullet.destroy();
                    asteroids.remove(j);
                    collision = true;
                }
            }

            if (collision) {
                bullets.remove(i);
            }
        }
    }
}
