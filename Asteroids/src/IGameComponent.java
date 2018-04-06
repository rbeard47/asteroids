import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface IGameComponent {
    ObjectType getObjectType();

    void update(DisplayManager manager, float msec);

    public enum ObjectType {
        spaceship,
        bullet,
        asteroid
    }
}
