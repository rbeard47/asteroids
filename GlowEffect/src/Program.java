import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Program {

    public static void main(String[] args) {

        int width = 800;
        int height = 600;
        long window;

        float vertices[] = {
                0.5f, 0, 0,
                0, 0.5f, 0,
                -0.5f, 0, 0};


        if (!glfwInit()) {

        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);


        window = glfwCreateWindow(width, height, "OpenGL", NULL, NULL);

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            if (vidmode == null) {
                throw new RuntimeException("Failed to get the video mode of the primary monitor");
            }

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );

        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(GLFW_TRUE);

        glfwShowWindow(window);

        GL.createCapabilities();

        glClearColor(.1f, 0.1f, 0.3f, 1);

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        StaticShader shader = new StaticShader("vertex", "fragment");

        Matrix4f cameraTransform = new Matrix4f();
        cameraTransform.setOrtho(0, 800, 600, 0, -1, 1);

        Matrix4f modelTransform = new Matrix4f();
        modelTransform.translate(new Vector3f(400, 300, 0))
                .scale(new Vector3f(200, -200, 1));

        Vector3f color = new Vector3f(1, 1, 1);
        int colorUniform = shader.getUniformLocation("color");

        while (!glfwWindowShouldClose(window)) {

            glClear(GL_COLOR_BUFFER_BIT);

            shader.start();

            shader.loadModelMatrix(modelTransform);
            shader.loadProjectionMatrix(cameraTransform);
            shader.loadVector3(colorUniform, color);

            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            glDrawArrays(GL_TRIANGLES, 0, 3);

            shader.stop();

            glfwSwapBuffers(window);
            glfwPollEvents();

        }

    }

}
