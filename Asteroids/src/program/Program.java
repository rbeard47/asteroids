package program;

import messaging.MessageManager;
import messaging.MessageRoundBegin;
import models.*;
import org.joml.Math;
import org.joml.Vector3f;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Program {

    public static void main(String[] args) {

        DisplayManager display = new DisplayManager();
        display.createDisplay();

        SoundManager.getInstance().Init();

        CollisionDetector collisionDetector = new CollisionDetector();
        AsteroidManager generator = new AsteroidManager(display);
        ScoreKeeper scoreKeeper = new ScoreKeeper(display);
        ShipManager shipManager = new ShipManager(display);
        EnemyShipManager enemyManager = new EnemyShipManager(display);
        ParticleManager particleManager = new ParticleManager(display);

        Spaceship spaceship = new Spaceship(new Vector3f(display.getScreenWidth() / 2,
                display.getScreenHeight() / 2, 0));

        display.registerForKeyboardEvents(spaceship);
        display.addGameComponent(spaceship);
        display.addGameComponent(generator);
        display.addGameComponent(scoreKeeper);
        display.addGameComponent(shipManager);
        display.addGameComponent(enemyManager);
        //display.addGameComponent(particleManager);

        StaticShader shader = new StaticShader("vertex", "fragment");

        OrthographicCamera camera = new OrthographicCamera(0, display.getWidth(), display.getHeight(), 0,
                -1000f, 1000f);

        glClearColor(0.1f, 0.1f, 0.2f, 1);

        double lastTime = glfwGetTime();
        double dt = 1.0 / 60.0;

        MessageManager.getInstance().PostMessage(new MessageRoundBegin());

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
                    shader.loadVector3(shader.getUniformLocation("color"),
                            ((IDrawableGameComponent) component).getColor());
                    ((IDrawableGameComponent) component).draw();
                }
            }

            shader.stop();

            glfwSwapBuffers(display.getWindow());
            glfwPollEvents();
        }

        SoundManager.getInstance().cleanup();
        display.closeDisplay();
    }
}
