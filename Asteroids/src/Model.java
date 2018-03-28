import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Vector;

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
            GL30.glBindVertexArray(m.getVertexArrayObject());
            GL20.glEnableVertexAttribArray(m.VERTEX_INDEX);
            GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, m.getVerticesCount());
            GL20.glDisableVertexAttribArray(m.VERTEX_INDEX);
            GL30.glBindVertexArray(0);
        }
    }
}