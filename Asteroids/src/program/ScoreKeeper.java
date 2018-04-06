package program;

import messaging.*;
import models.IDrawableGameComponent;
import models.IGameComponent;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ScoreKeeper implements IMessageReceiver, IDrawableGameComponent, IGameComponent {

    private int score = 0;
    private Matrix4f localTransform;
    private Vector3f color;

    public ScoreKeeper() {
        MessageManager.getInstance().RegisterForMessage(this);
        localTransform = new Matrix4f();
        color = new Vector3f(1,1,1);
    }

    @Override
    public void Receive(Message message) {

        if(message instanceof AsteroidDestroyed) {

            AsteroidDestroyed a = (AsteroidDestroyed)message;

            switch(a.getSize()) {
                case small:
                    score += 50;
                    break;
                case medium:
                    score += 30;
                    break;
                case large:
                    score += 10;
            }
        } else if(message instanceof MessageAlienDestroyed) {
            // Add score...
        }
    }

    @Override
    public Matrix4f getTransform() {
        localTransform.identity();
        return localTransform;
    }

    @Override
    public Vector3f getColor() {
        return color;
    }

    @Override
    public Vector3f getPosition() {
        return null;
    }

    @Override
    public void draw() {

    }

    @Override
    public ObjectType getObjectType() {
        return null;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

    }
}
