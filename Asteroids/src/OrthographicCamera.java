import org.joml.Matrix4f;
import org.joml.Vector2f;

public class OrthographicCamera {

    float left;
    float right;
    float bottom;
    float top;
    float near;
    float far;
    private Vector2f position;
    private Matrix4f cameraTransform;

    public OrthographicCamera(float left, float right, float bottom, float top, float near, float far) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.near = near;
        this.far = far;

        cameraTransform = new Matrix4f();
        cameraTransform.setOrtho(left, right, bottom, top, near, far);
    }

    public Matrix4f getViewMatrix() {
        return cameraTransform;
    }
}