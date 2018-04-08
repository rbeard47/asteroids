package models;

import messaging.*;
import org.joml.Vector3f;
import program.DisplayManager;

public class ParticleManager implements IMessageReceiver {

    private DisplayManager manager;

    public ParticleManager(DisplayManager manager) {
        MessageManager.getInstance().RegisterForMessage(this);
        this.manager = manager;
    }

    private void CreateExplosion(Vector3f position) {
        for (int i = 0; i < 15; i++) {
            manager.addGameComponent(new Particle(new Vector3f(position), (float) (Math.random() * 2 * Math.PI)));
        }
    }

    @Override
    public void Receive(Message message) {
        if (message instanceof AsteroidDestroyed) {
            CreateExplosion(new Vector3f(((AsteroidDestroyed) message).getAsteroid().getPosition()));
        } else if (message instanceof MessageSmallSaucerDestroyed) {
            CreateExplosion(new Vector3f(((MessageSmallSaucerDestroyed) message).getPosition()));
        }
    }
}
