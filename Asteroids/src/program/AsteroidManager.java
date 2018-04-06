package program;

import messaging.AsteroidDestroyed;
import messaging.IMessageReceiver;
import messaging.Message;
import messaging.MessageManager;
import models.Asteroid;
import models.IGameComponent;
import models.Model;
import models.ModelLoader;
import org.joml.Random;
import org.joml.Vector3f;

import static models.Asteroid.AsteroidSize.*;

public class AsteroidManager implements IGameComponent, IMessageReceiver {

    private DisplayManager manager;
    private Model asteroidModel;

    AsteroidManager(DisplayManager manager) {
        this.manager = manager;
        asteroidModel = ModelLoader.loadModel("large_asteroid");
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
        for (int i = 0; i < 5; i++) {
            AddAsteroid(large, new Vector3f((float) Math.random() * manager.getScreenWidth(),
                    (float) Math.random() * manager.getScreenHeight(), 0));
        }
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.messagemanager;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

        boolean asteroidsLeft = false;

        for (int i = manager.components().size() - 1; i >= 0; i--) {
            IGameComponent component = manager.components().get(i);
            if (component.getObjectType() == ObjectType.asteroid) {
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

        if (message instanceof AsteroidDestroyed) {
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
                    (float) (a.getDirection() - Math.PI / 4), (float) Math.max(Math.random() * 100, 40)));

            manager.addGameComponent(new Asteroid(size, new Vector3f(a.getPosition()),
                    (float) (Math.random() * 2 * Math.PI), (float) (Math.random() * 2f),
                    (float) (a.getDirection() + Math.PI / 4), (float) Math.max(Math.random() * 100, 40)));
        }
    }
}
