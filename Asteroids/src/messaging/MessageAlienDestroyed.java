package messaging;

import org.joml.Vector3f;

public class MessageAlienDestroyed extends Message {

    private Vector3f position;

    public MessageAlienDestroyed(Vector3f position, int size){
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }
}
