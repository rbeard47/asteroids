import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface IDrawableGameComponent {
    Matrix4f getTransform();

    Vector3f getColor();

    Vector3f getPosition();

    void draw();
}
