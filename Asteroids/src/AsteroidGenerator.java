import org.joml.Random;
import org.joml.Vector3f;

public class AsteroidGenerator implements IGameComponent {

    private DisplayManager manager;
    private Model asteroidModel;

    AsteroidGenerator(DisplayManager manager) {
        this.manager = manager;
        asteroidModel = ModelLoader.loadModel("large_asteroid");
    }

    private void AddAsteroid(Asteroid.AsteroidSize size, Vector3f position) {
        Random r = new Random(343254353);
        Asteroid asteroid = new Asteroid(asteroidModel, size, position, (float) (Math.random() * 2 * Math.PI),
                r.nextFloat() * 10,
                (float) (Math.random() * 2 * Math.PI), (float) Math.max(Math.random() * 100, 40));
        asteroid.setColor(new Vector3f(1.0f, r.nextFloat(), 0.5f));

        this.manager.addGameComponent(asteroid);
    }

    void StartRound() {

        for (int i = 0; i < 5; i++) {
            AddAsteroid(Asteroid.AsteroidSize.large, new Vector3f((float) Math.random() * manager.getScreenWidth(),
                    (float) Math.random() * manager.getScreenHeight(), 0));
        }
    }

    @Override
    public ObjectType getObjectType() {
        return null;
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
}
