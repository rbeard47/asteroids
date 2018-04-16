import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Program {

    public static void main(String[] args) {

        int width = 800;
        int height = 600;
        long window;

        // Texture coordinates and colors interleaved
        float quadVertices[] = {
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                -1.0f, 1.0f, 0.0f,
                -1.0f, 1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.0f, 1.0f, 0.0f
        };

        float uvs[] = {
                1, 1, -1, 1, -1, -1, 1, -1
        };

        float vertices[] = {
                0.5f, 0, 0,
                0, 0.5f, 0,
                -0.5f, 0, 0,
                -0.5f, 0, 0,
                0, -0.5f, 0,
                0.5f, 0, 0
        };

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

        int quad_vao = glGenVertexArrays();
        glBindVertexArray(quad_vao);

        int quad_vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, quad_vbo);
        glBufferData(GL_ARRAY_BUFFER, quadVertices, GL_STATIC_DRAW);

        glBindVertexArray(0);

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        int uvbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, uvbo);
        glBufferData(GL_ARRAY_BUFFER, uvs, GL_STATIC_DRAW);

        StaticShader sceneShader = new StaticShader("vertex", "fragment");
        StaticShader processShader = new StaticShader("texture_vertex", "processfrag");
        StaticShader plainShader = new StaticShader("plain", "fragment");

        Matrix4f cameraTransform = new Matrix4f();
        cameraTransform.setOrtho(0, 800, 600, 0, -1, 1);

        Matrix4f modelTransform = new Matrix4f();
        modelTransform.translate(new Vector3f(400, 300, 0))
                .scale(new Vector3f(400, 300, 1));

        Matrix4f quadTransform = new Matrix4f();
//        quadTransform.translate(300, 400, 0)
//                .scale(new Vector3f(320, -240, 1));

        Vector3f color = new Vector3f(1, 1, 1);
        int colorUniform = sceneShader.getUniformLocation("color");

        //glEnable(GL_TEXTURE_2D);
        int bufferColorTex = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, bufferColorTex);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glBindTexture(GL_TEXTURE_2D, 0);

        int bufferFBO = glGenFramebuffers();

        while (!glfwWindowShouldClose(window)) {

            // BEGIN - DRAW LINES ONLY
            // Render to the framebuffer

            GL30.glBindFramebuffer(GL_FRAMEBUFFER, bufferFBO);
            glViewport(0,0,800,600);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, bufferColorTex, 0);

            glClear(GL_COLOR_BUFFER_BIT);
            glEnable(GL_LINE_SMOOTH);

            sceneShader.start();  // Use scene shader

            sceneShader.loadModelMatrix(modelTransform);
            sceneShader.loadProjectionMatrix(cameraTransform);
            sceneShader.loadVector3(colorUniform, color);

            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            //glLineWidth(120.0f);
            glDrawArrays(GL_TRIANGLES, 0, 6);

            sceneShader.stop();

// **************************************************


// **************************************************

            // Turn off rendering to framebuffer
            glBindFramebuffer(GL_FRAMEBUFFER, 0);

            glClear(GL_COLOR_BUFFER_BIT);

            // Use process shader
            processShader.start();

            processShader.loadModelMatrix(modelTransform);
            processShader.loadProjectionMatrix(cameraTransform);

            // Set active texture - Texture0
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, bufferColorTex);

            // Set texture sampler to texture0
            processShader.loadInt(processShader.getUniformLocation("diffuseTex"), 0);
            processShader.loadFloat(processShader.getUniformLocation("time"), (float)(glfwGetTime()*10.0f));

            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, quad_vbo);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            glDrawArrays(GL_TRIANGLES, 0, 6);

            glDisableVertexAttribArray(0);

            processShader.stop();

            plainShader.start();  // Use scene shader

            plainShader.loadModelMatrix(modelTransform);
            plainShader.loadProjectionMatrix(cameraTransform);
            plainShader.loadVector3(colorUniform, color);

            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            glDrawArrays(GL_LINES, 0, 6);

            plainShader.stop();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}
