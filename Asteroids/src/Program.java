import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Vector;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Program {

    public static void main(String[] args) {

        DisplayManager display = new DisplayManager();
        display.createDisplay();

        Model spaceship_model = ModelLoader.loadModel("spaceship");
        Model thrust_model = ModelLoader.loadModel("thrust");

        Spaceship spaceship = new Spaceship(spaceship_model, thrust_model);
        display.registerForKeyboardEvents(spaceship);

        Asteroid asteroid = new Asteroid(new Vector3f(300, 200, 0), 0f, .5f, new Vector2f(0.5f, 0.5f));
        Asteroid asteroid2 = new Asteroid(new Vector3f(100, 20, 0), 50f, .8f, new Vector2f( -0.1f, 0.4f));
        Asteroid asteroid3 = new Asteroid(new Vector3f(100, 20, 0), 100f, 1f, new Vector2f( 0.2f, -0.3f));

        display.addGameComponent(spaceship);

        display.addGameComponent(asteroid);
        display.addGameComponent(asteroid2);
        display.addGameComponent(asteroid3);

        StaticShader shader = new StaticShader("vertex", "fragment");

        OrthographicCamera camera = new OrthographicCamera(0, display.getWidth(), display.getHeight(), 0, -1f, 1000f);

        glClearColor(0.1f, 0.1f, 0.2f, 1);

        double lastTime = glfwGetTime();
        double dt = 1.0 / 120.0;

        while (!glfwWindowShouldClose(display.getWindow())) {
            glClear(GL_COLOR_BUFFER_BIT);

            double now = glfwGetTime();
            double frameTime = now - lastTime;
            lastTime = now;

            while (frameTime > 0.0f) {
                float deltaTime = (float) Math.min(frameTime, dt);

                for (int i = 0; i < display.components().size(); i++) {
                    IGameComponent component = display.components().get(i);
                    component.update(display, deltaTime);
                }

                frameTime -= deltaTime;
            }

            shader.start();

            shader.loadProjectionMatrix(camera.getViewMatrix());

            for (int i = 0; i < display.components().size(); i++) {
                IGameComponent component = display.components().get(i);
                if (component instanceof IDrawableGameComponent) {
                    shader.loadModelMatrix(((IDrawableGameComponent) component).getTransform());
                    ((IDrawableGameComponent) component).draw();
                }
            }

            shader.stop();

            glfwSwapBuffers(display.getWindow());
            glfwPollEvents();
        }

        display.closeDisplay();
    }
}
