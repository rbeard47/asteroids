package models;

import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;

public class Mesh {

    public final static int VERTEX_INDEX = 0;

    private AIMesh mesh;

    private int vao;
    private int vbo;
    private int ibo;
    private int index_count;

    public Mesh(AIMesh mesh) {

        this.mesh = mesh;

        // Create vao
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        // Load vertices
        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL_ARRAY_BUFFER, vbo);
        AIVector3D.Buffer vertices = mesh.mVertices();
        nglBufferData(GL_ARRAY_BUFFER, AIVector3D.SIZEOF * vertices.remaining(), vertices.address(), GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(VERTEX_INDEX, 3, GL_FLOAT, false, 0, 0);

        if (mesh.mNumFaces() > 0) {
            index_count = mesh.mNumFaces() * 2;

            IntBuffer buffer = BufferUtils.createIntBuffer(index_count);

            int indices[] = new int[index_count];

            for (int i = 0; i < mesh.mNumFaces(); i++) {
                AIFace face = mesh.mFaces().get(i);

                buffer.put(face.mIndices());
            }
            buffer.flip();

            ibo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            GL15.glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        }

        // Prevent further mods to vao
        GL30.glBindVertexArray(0);
    }


    public AIMesh getMesh() {
        return mesh;
    }

    public int getVbo() {
        return vbo;
    }

    public int getVerticesCount() {
        return mesh.mNumFaces();
    }

    public int getIndicesCount() {
        return index_count;
    }

    public int getVao() {
        return vao;
    }
}