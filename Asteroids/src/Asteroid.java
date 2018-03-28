import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Asteroid implements IGameComponent, IDrawableGameComponent {

    int vao;
    int vbo;
    int ibo;

    private Vector3f position;
    private Vector3f heading;
    private Vector2f velocity;
    private float rotationAngle;
    private float rotationVelocity;

    private float collision_box1[] = {
            0.042422f, -0.958023f, 0.0f,
            0.960078f, -0.958023f, 0.0f,
            0.042422f, 0.500000f, 0.0f,
            0.960078f, 0.500000f, 0.0f};

    private float collision_box2[] = {
            -0.867839f, 0.241657f, 0.0f,
            0.439855f, 0.241657f, 0.0f,
            0.439855f, 1.136579f, 0.0f,
            -0.867839f, 1.136579f, 0.0f};

    private float collision_box3[] = {
            -1.035313f, -0.835313f, 0.0f,
            0.035313f, -0.835313f, 0.0f,
            -1.035313f, 0.235313f, 0.0f,
            0.035313f, 0.235313f, 0.0f};

    private List<float[]> collisionBoxes;

    private float mesh[] = {-0.722061f, -0.722061f, 0.0f,
            0.822061f, -0.822061f, 0.0f,
            -0.822061f, 0.622061f, 0.0f,
            0.522061f, 0.522061f, 0.0f,
            -1.062570f, 0.0f, 0.0f,
            0.0f, -0.662570f, 0.0f,
            0.962570f, 0.0f, 0.0f,
            0.0f, 1.162570f, 0.0f,
            -0.974075f, -0.444896f, 0.0f,
            0.444896f, -0.974075f, 0.0f,
            0.874075f, 0.444896f, 0.0f,
            -0.444896f, 1.074075f, 0.0f,
            -0.774075f, 0.344896f, 0.0f,
            -0.244896f, -0.874075f, 0.0f,
            0.674075f, -0.444896f, 0.0f,
            0.444896f, 1.074075f, 0.0f};

    private int indices[] = {8, 0, 9, 1, 10, 3, 11, 2, 12, 4, 13,
            5, 14, 6, 15, 7, 4, 8, 5, 9, 6, 10, 7, 11, 2, 12,
            0, 13, 1, 14, 3, 15};

//    private int indices[] = {0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7,
//            8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15, 0};

    public Asteroid(Vector3f position, float rotationAngle, float rotationVelocity, Vector2f velocity) {

        collisionBoxes = new Vector<>();

        collisionBoxes.add(collision_box1);
        collisionBoxes.add(collision_box2);
        collisionBoxes.add(collision_box3);

        this.rotationVelocity = rotationVelocity;
        this.velocity = velocity;
        this.rotationAngle = rotationAngle;
        this.position = position;

        vao = GL30.glGenVertexArrays();
        glBindVertexArray(vao);

        // Load vertices
        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, mesh, GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        IntBuffer index_buffer = BufferUtils.createIntBuffer(indices.length);
        index_buffer.put(indices).flip();
        ibo = GL15.glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, index_buffer, GL_STATIC_DRAW);

        // Prevent further mods to vao
        glBindVertexArray(0);
    }

    @Override
    public void draw() {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        GL11.glDrawElements(GL11.GL_LINES, indices.length, GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    @Override
    public Matrix4f getTransform() {
        Matrix4f temp = new Matrix4f();
        temp.translate(position)
                .scale(new Vector3f(30f, 30f, 1))
                .rotateZ(rotationAngle);

        return temp;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

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
    }
}
