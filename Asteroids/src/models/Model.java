package models;

import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;

import java.util.List;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Model {

    List<Mesh> meshes;

    public Model(AIScene scene) {

        meshes = new Vector<>(scene.mNumMeshes());

        for (int i = 0; i < scene.mNumMeshes(); i++) {
            meshes.add(new Mesh(AIMesh.create(scene.mMeshes().get(i))));
        }
    }

    public void render() {
        for (Mesh m : meshes) {
            glBindVertexArray(m.getVao());
            glEnableVertexAttribArray(Mesh.VERTEX_INDEX);
            glDrawElements(GL_LINES, m.getIndicesCount(), GL_UNSIGNED_INT, 0);
            glDisableVertexAttribArray(Mesh.VERTEX_INDEX);
            glBindVertexArray(0);
        }
    }
}