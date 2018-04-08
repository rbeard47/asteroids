package program;

import org.joml.Matrix4f;

public class OrthographicCamera {

    private float left;
    private float right;
    private float bottom;
    private float top;
    private float near;
    private float far;
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