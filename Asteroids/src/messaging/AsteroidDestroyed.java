package messaging;

import models.Asteroid;
import org.joml.Vector3f;

public class AsteroidDestroyed extends Message {

    private Vector3f position;

    public Vector3f getPosition() {
        return position;
    }

    public Asteroid.AsteroidSize getSize() {
        return size;
    }

    public float getDirection() {
        return direction;
    }

    private Asteroid.AsteroidSize size;
    private float direction;

    public AsteroidDestroyed(Vector3f position, Asteroid.AsteroidSize size, float direction) {
        this.position = position;
        this.size = size;
        this.direction = direction;
    }
}
