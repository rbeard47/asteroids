package models;

import org.lwjgl.assimp.AIScene;

import static org.lwjgl.assimp.Assimp.aiImportFile;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;

public class ModelLoader {

    public static Model loadModel(String modelName) {

        String modelFilePath = String.format("res/models/%s.obj", modelName);

        try (AIScene scene = aiImportFile(modelFilePath, aiProcess_Triangulate)) {
            if (scene == null) {
                throw new AssertionError();
            }
            return new Model(scene);
        }
    }
}
