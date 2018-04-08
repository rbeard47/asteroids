package messaging;

import org.joml.Vector3f;

public class MessageSmallSaucerDestroyed extends Message {

    private Vector3f position;

    public MessageSmallSaucerDestroyed(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }
}
