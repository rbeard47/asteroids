import org.joml.Matrix4f;

public class StaticShader extends ShaderProgram {

    private int locationTransformationMatrix;
    private int locationModelMatrix;

    public StaticShader(String vertexShader, String fragmentShader) {
        super(String.format("res/shaders/%s.glsl", vertexShader), String.format("res/shaders/%s.glsl", fragmentShader));
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void GetAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationModelMatrix = super.getUniformLocation("modelMatrix");
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    public void loadModelMatrix(Matrix4f matrix) {
        super.loadMatrix(locationModelMatrix, matrix);
    }
}