import org.joml.Matrix4f;

public interface IDrawableGameComponent {
    void draw();

    Matrix4f getTransform();
}
