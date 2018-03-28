import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;

public class Mesh {

    public final static int VERTEX_INDEX = 0;
    private AIMesh mesh;
    private int vertexArrayObject;
    private int vertexBufferIndex;

    public Mesh(AIMesh mesh) {

        this.mesh = mesh;

        // Create vao
        vertexArrayObject = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vertexArrayObject);

        // Load vertices
        vertexBufferIndex = GL15.glGenBuffers();
        GL15.glBindBuffer(GL_ARRAY_BUFFER, vertexBufferIndex);
        AIVector3D.Buffer vertices = mesh.mVertices();
        nglBufferData(GL_ARRAY_BUFFER, AIVector3D.SIZEOF * vertices.remaining(), vertices.address(), GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(VERTEX_INDEX, 3, GL_FLOAT, false, 0, 0);

        // Prevent further mods to vao
        GL30.glBindVertexArray(0);
    }


    public AIMesh getMesh() {
        return mesh;
    }

    public int getVertexBufferIndex() {
        return vertexBufferIndex;
    }

    public int getVerticesCount() {
        return mesh.mNumFaces();
    }

    public int getVertexArrayObject() {
        return vertexArrayObject;
    }
}