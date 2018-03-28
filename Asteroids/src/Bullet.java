import com.sun.istack.internal.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL15;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Bullet implements IGameComponent, IDrawableGameComponent {

    public Vector3f position;
    public int vbo;
    public int vao;
    public int ibo;
    IBulletEvent callback;

    float bullet_points[] = {0.1f, 0.1f, 0,
            0.1f, -0.1f, 0,
            -0.1f, -0.1f, 0,
            -0.1f, 0.1f, 0};

    int indices[] = {0, 2, 1, 3};  // small X shape

    float velocity = 300.0f;
    float angle;
    float lifetime = 1.5f;
    private boolean isDead;

    public Bullet(Vector3f position, float angle, @Nullable IBulletEvent callback) {

        this.callback = callback;
        this.position = position;
        this.angle = angle;
        this.velocity = velocity;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        GL15.glBindBuffer(GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL_ARRAY_BUFFER, bullet_points, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    @Override
    public void update(DisplayManager manager, float msec) {
        position.x += -Math.sin(angle) * velocity * msec;
        position.y += Math.cos(angle) * velocity * msec;

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

        lifetime -= msec;

        if (lifetime <= 0 && !isDead) {
            isDead = true;
            manager.removeGameComponent(this);

            if(callback != null) {
                callback.eventCallback();
            }
        }
    }

    @Override
    public void draw() {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glDrawElements(GL_LINES, indices.length, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public Matrix4f getTransform() {
        Matrix4f temp = new Matrix4f();
        temp.translate(position)
                .scale(new Vector3f(10f, 10f, 1f));
        return temp;
    }

    public interface IBulletEvent {
        void eventCallback();
    }
}
