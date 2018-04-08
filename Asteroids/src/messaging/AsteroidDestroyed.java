package messaging;

import models.Asteroid;
import org.joml.Vector3f;

public class AsteroidDestroyed extends Message {
    private Asteroid asteroid;

    public AsteroidDestroyed(Asteroid asteroid) {
        this.asteroid = asteroid;
    }

    public Asteroid getAsteroid() {
        return asteroid;
    }
}
