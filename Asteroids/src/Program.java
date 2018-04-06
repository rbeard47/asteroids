import org.joml.Math;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Program {

    public static void main(String[] args) {

        DisplayManager display = new DisplayManager();
        display.createDisplay();

        AsteroidGenerator generator = new AsteroidGenerator(display);

        Spaceship spaceship = new Spaceship();

        display.registerForKeyboardEvents(spaceship);

        display.addGameComponent(spaceship);
        display.addGameComponent(generator);

        generator.StartRound();

        StaticShader shader = new StaticShader("vertex", "fragment");

        OrthographicCamera camera = new OrthographicCamera(0, display.getWidth(), display.getHeight(), 0,
                -1000f, 1000f);

        glClearColor(0.1f, 0.1f, 0.2f, 1);

        double lastTime = glfwGetTime();
        double dt = 1.0 / 120.0;

        CollisionDetector collisionDetector = new CollisionDetector();

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
                    shader.loadModelMatrix(((IDrawableGameComponent)component).getTransform());
                    shader.loadVector3(shader.getUniformLocation("color"),
                            ((IDrawableGameComponent)component).getColor());
                    ((IDrawableGameComponent)component).draw();
                }
            }

            shader.stop();

            glfwSwapBuffers(display.getWindow());
            glfwPollEvents();
        }

        display.closeDisplay();
    }
}
