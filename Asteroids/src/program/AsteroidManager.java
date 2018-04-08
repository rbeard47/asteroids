package program;

import messaging.*;
import models.Asteroid;
import models.IGameComponent;
import org.joml.Random;
import org.joml.Vector3f;

import static models.Asteroid.AsteroidSize.*;

public class AsteroidManager implements IGameComponent, IMessageReceiver {

    private DisplayManager manager;

    AsteroidManager(DisplayManager manager) {
        this.manager = manager;
        MessageManager.getInstance().RegisterForMessage(this);
    }

    private void AddAsteroid(Asteroid.AsteroidSize size, Vector3f position) {
        Random r = new Random(343254353);

        Asteroid asteroid = new Asteroid(size, position, (float) (Math.random() * 2 * Math.PI),
                r.nextFloat() * 10,
                (float) (Math.random() * 2 * Math.PI), (float) Math.max(Math.random() * 100, 40));
        asteroid.setColor(new Vector3f(1.0f, r.nextFloat(), 0.5f));

        this.manager.addGameComponent(asteroid);
    }

    void StartRound() {
        for (int i = 0; i < 3; i++) {
            Vector3f position = new Vector3f(
                    (float) Math.random() * 30,
                    (float) Math.random() * 30,
                    0);
            AddAsteroid(large, position);
        }

        for (int i = 0; i < 3; i++) {
            Vector3f position = new Vector3f(
                    (float) Math.min(manager.getScreenWidth() - 30, Math.max(manager.getScreenWidth() - Math.random() * 30, manager.getScreenWidth())),
                    (float) Math.min(manager.getScreenHeight() - 30, Math.max(manager.getScreenHeight() - Math.random() * 30, manager.getScreenHeight())),
                    0);

            AddAsteroid(large, position);
        }
    }


    @Override
    public ObjectType getObjectType() {
        return ObjectType.MESSAGEMANAGER;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

        boolean asteroidsLeft = false;

        for (int i = manager.components().size() - 1; i >= 0; i--) {
            IGameComponent component = manager.components().get(i);
            if (component.getObjectType() == ObjectType.ASTEROID) {
                asteroidsLeft = true;
                break;
            }
        }

        if (!asteroidsLeft) {
            StartRound();
        }
    }

    @Override
    public void Receive(Message message) {

        if (message instanceof MessageRoundBegin) {
            StartRound();
        } else if (message instanceof AsteroidDestroyed) {
            AsteroidDestroyed a = (AsteroidDestroyed) message;
            Asteroid.AsteroidSize size = a.getSize();

            if (size == small) {
                return;
            }

            if (size == large) {
                size = medium;
            } else {
                size = small;
            }

            manager.addGameComponent(new Asteroid(size, new Vector3f(a.getPosition()),
                    (float) (Math.random() * 2 * Math.PI), (float) (Math.random() * 2f),
                    (float) (a.getDirection() - Math.random() * Math.PI / 3), (float) Math.max(Math.random() * 160, 50)));

            manager.addGameComponent(new Asteroid(size, new Vector3f(a.getPosition()),
                    (float) (Math.random() * 2 * Math.PI), (float) (Math.random() * 2f),
                    (float) (a.getDirection() + Math.random() * Math.PI / 3), (float) Math.max(Math.random() * 160, 50)));
        }
    }
}
