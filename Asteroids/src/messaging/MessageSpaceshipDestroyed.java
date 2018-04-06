package messaging;

import org.joml.Vector3f;

public class MessageSpaceshipDestroyed extends Message {

    private Vector3f position;

    public MessageSpaceshipDestroyed(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }
}
