package program;

import messaging.*;
import models.Asteroid;
import models.IGameComponent;
import models.Spaceship;
import org.joml.Intersectionf;
import org.joml.Vector3f;

public class ShipManager implements IMessageReceiver, IGameComponent {

    private DisplayManager manager;
    private int lifeCount = 3;
    private boolean createShip = false;

    public ShipManager(DisplayManager manager) {
        this.manager = manager;
        MessageManager.getInstance().RegisterForMessage(this);
    }

    @Override
    public void Receive(Message message) {
        if (message instanceof MessageSpaceshipDestroyed) {
            createShip = true;
            lifeCount--;
        } else if (message instanceof MessageNewLife) {
            lifeCount++;
        }

    }

    @Override
    public ObjectType getObjectType() {
        return null;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

        boolean collision = false;

        if (createShip) {
            for (int i = manager.components().size() - 1; i >= 0; i--) {
                IGameComponent component = manager.components().get(i);

                if(component.getObjectType() == ObjectType.ENEMY_SHIP) {
                    collision = true;
                    break;
                }

                if (component.getObjectType() == IGameComponent.ObjectType.ASTEROID) {
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

                    if (Intersectionf.testCircleCircle(manager.getScreenWidth() / 2, manager.getScreenHeight() / 2, 50f, asteroidPosition.x,
                            asteroidPosition.y, radius)) {
                        collision = true;
                    }
                }
            }

            if (collision == false) {
                manager.addGameComponent(new Spaceship(new Vector3f(manager.getScreenWidth() / 2, manager.getScreenHeight() / 2, 0)));
                createShip = false;
            }
        }
    }
}
