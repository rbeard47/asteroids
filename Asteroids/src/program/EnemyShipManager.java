package program;

import messaging.IMessageReceiver;
import messaging.Message;
import models.AlienShip;
import models.IGameComponent;

public class EnemyShipManager implements IMessageReceiver, IGameComponent {

    private float appearanceTimer;
    private float nextAppearance;
    private DisplayManager manager;

    public EnemyShipManager(DisplayManager manager) {
        this.manager = manager;
        nextAppearance = (float)Math.min(30, Math.max(Math.random() * 60, 60));
    }

    @Override
    public void Receive(Message message) {

    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.ENEMYSHIPMANAGER;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

        appearanceTimer += msec;

        if(appearanceTimer > nextAppearance) {
            nextAppearance = (float)Math.max(20, Math.min(Math.random() * 60, 60));
            appearanceTimer = 0;

            manager.addGameComponent(new AlienShip());
        }
    }
}
