package program;

import models.IDrawableGameComponent;
import models.IGameComponent;
import models.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Digit implements IGameComponent, IDrawableGameComponent {

    private Model digitModel;
    private Vector3f position;
    private Vector3f scale;
    private Vector3f color;
    private Matrix4f localTransform;

    public Digit(Model digitModel, Vector3f position) {
        this.digitModel = digitModel;
        this.position = position;
        color = new Vector3f(0.6f, 0.6f, 0.6f);
        scale = new Vector3f(11, 13, 1);
        localTransform = new Matrix4f();
    }

    public void setModel(Model model) {
        this.digitModel = model;
    }

    @Override
    public Matrix4f getTransform() {
        localTransform.identity();
        localTransform.translate(position).scale(scale);

        return localTransform;
    }

    @Override
    public Vector3f getColor() {
        return color;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public void draw() {
        digitModel.render();
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.DIGIT;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

    }
}
