import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.*;

public class Spaceship implements IGameComponent, IDrawableGameComponent, Bullet.IBulletEvent {

    private final static int max_bullet_count = 6;
    private Vector3f position;
    private Vector3f heading;
    private Vector2f velocity;
    private float rotationAngle = 0;
    private float rotationVelocity = 0;
    private Matrix4f rotationMatrix;
    private Matrix4f translationMatrix;
    private Matrix4f scaleMatrix;
    private LinkedList<KeyboardInput> keysPressed;

    // The fire shooting out the back
    private IGameComponent thruster;
    private Model spaceshipMesh;
    private Model thrusterMesh;

    private AtomicInteger bulletCount;
    private boolean isFiring = false;
    private float ship_mesh[] = {0, 0.5f, 0, 0.3f, -0.5f, 0, 0, -0.3f, 0, -0.3f, -0.5f, 0};
    private int ship_indices[] = {0, 1, 1, 2, 2, 3, 3, 0};
    private int vao;
    private int vertexBufferIndex;
    private int elementBufferIndex;

    public Spaceship(Model spaceshipMesh, Model thrusterMesh) {

        keysPressed = new LinkedList<>();
        bulletCount = new AtomicInteger();
        bulletCount.set(0);
        scaleMatrix = new Matrix4f();
        translationMatrix = new Matrix4f();
        rotationMatrix = new Matrix4f();

        this.spaceshipMesh = spaceshipMesh;
        this.thrusterMesh = thrusterMesh;
        position = new Vector3f(400, 300, 0);
        velocity = new Vector2f();
        heading = new Vector3f(0, 1, 0);

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        // Load vertices
        vertexBufferIndex = GL15.glGenBuffers();
        GL15.glBindBuffer(GL_ARRAY_BUFFER, vertexBufferIndex);
        glBufferData(GL_ARRAY_BUFFER, ship_mesh, GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        IntBuffer index_buffer = BufferUtils.createIntBuffer(ship_indices.length);
        index_buffer.put(ship_indices).flip();
        elementBufferIndex = GL15.glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferIndex);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, index_buffer, GL_STATIC_DRAW);

        // Prevent further mods to vao
        GL30.glBindVertexArray(0);
    }

    public Matrix4f getTransform() {
        Matrix4f temp = new Matrix4f();
        temp.translate(position)
                .scale(new Vector3f(20f, 20f, 1))
                .rotateZ(rotationAngle);

        return temp;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

        double dx = 0.0;
        double dy = 0.0;
        double rx = 0.0;

        long window = manager.getWindow();

        if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
            dx += -Math.sin(rotationAngle) * 3f * msec;
            dy += Math.cos(rotationAngle) * 3f * msec;
        }

        if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
            rx += 200 * msec;
        }

        if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
            rx -= 200 * msec;
        }

        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS && !isFiring) {
            isFiring = true;

            if (bulletCount.get() < max_bullet_count) {
                bulletCount.incrementAndGet();
                manager.addGameComponent(new Bullet(new Vector3f(position.x, position.y, 1),
                        rotationAngle, this::eventCallback));
            }
        }

        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_RELEASE) {
            isFiring = false;
        }

        velocity.x += dx;
        velocity.y += dy;

        if(velocity.x > 1) { velocity.x = 1;}
        if(velocity.y > 1) { velocity.y = 1;}

        rotationVelocity += rx;

        position.x += velocity.x;
        position.y += velocity.y;

        if (position.x > 800) {
            position.x = 0.1f;
        } else if (position.x <= 0) {
            position.x = 800;
        }

        if (position.y > 600) {
            position.y = 0.1f;
        } else if (position.y <= 0) {
            position.y = 600;
        }

        float rotationAmount = rotationVelocity * msec;

        if (rotationAmount > Math.PI) {
            rotationAmount = (float) -Math.PI;
        } else if (rotationAmount < (float) -Math.PI) {
            rotationAmount = (float) Math.PI;
        }

        rotationAngle += rotationAmount;
        rotationVelocity *= 0.4f;
        velocity.x *= 0.99f;
        velocity.y *= 0.99f;
    }

    @Override
    public void draw() {
        GL30.glBindVertexArray(vao);
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawElements(GL11.GL_LINES, ship_indices.length, GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void eventCallback() {
        if (bulletCount.decrementAndGet() < 0) {
            bulletCount.set(0);
        }
    }

    private class KeyboardInput {
        private int key;
        private int scancode;
        private int action;
        private int mods;

        public KeyboardInput(int key, int scancode, int action, int mods) {
            this.key = key;
            this.scancode = scancode;
            this.action = action;
            this.mods = mods;
        }
    }
}
