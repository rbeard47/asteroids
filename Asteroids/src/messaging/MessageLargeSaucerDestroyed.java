package messaging;

import org.joml.Vector3f;

public class MessageLargeSaucerDestroyed extends Message {

    private Vector3f position;

    public MessageLargeSaucerDestroyed(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }
}
