package nivalis.engine.render;

import nivalis.engine.render.utils.Buffers;
import nivalis.tools.transform.Camera;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;


public class RenderBatch {
    private int vertexID;
    private int indiceID;

    private final int POS_SIZE = 3; //x,y,z
    private final int COLOR_SIZE = 4; //r,g,b,a <-> x,y,z,w as a vector
    private final int TEX_COORD_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORD_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORD_OFFSET + TEX_COORD_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORD_SIZE + TEX_ID_SIZE; //x,y,z,r,g,b,a
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private final int MAX_BATCH_SIZE = 1000;

    private Shader shader;


    private float[] vertices;
    private int[] indices;
    private int[] texSlots = {0,1,2,3,4,5,6,7};

    private Sprite[] sprites;
    private int spriteNumber = 0;

    private List<Texture> textures;

    private boolean hasRoom = true;

    public RenderBatch() {
        vertices = new float[4 * MAX_BATCH_SIZE * VERTEX_SIZE];
        indices = new int[MAX_BATCH_SIZE * 6];
        sprites = new Sprite[MAX_BATCH_SIZE];


        vertexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexID);
        glBufferData(GL_ARRAY_BUFFER, Buffers.createFloatBuffer(vertices), GL_DYNAMIC_DRAW);

        indiceID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indiceID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Buffers.createIntegerBuffer(indices), GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        try {
            shader = new Shader("./res/shader/basic.glsl");
        } catch (ShaderException e) {
            e.printStackTrace();
        }
        textures = new ArrayList<>();
    }

    public void render(Camera camera) {
        vertexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexID);
        glBufferData(GL_ARRAY_BUFFER, Buffers.createFloatBuffer(vertices), GL_DYNAMIC_DRAW);

        indiceID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indiceID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Buffers.createIntegerBuffer(indices), GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glBindBuffer(GL_ARRAY_BUFFER, vertexID);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glVertexAttribPointer(2, TEX_COORD_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORD_OFFSET);
        glVertexAttribPointer(3,TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indiceID);

        shader.bind();
        shader.setUniform("projection", camera.getProjection());
        for (int i = 1; i < textures.size();i++) {
            try {
                textures.get(i).bind(i);
            } catch (InvalidTextureSlotException e) {
                e.printStackTrace();
            }
        }
        shader.setUniform("uTextures", texSlots);

        glDrawElements(GL_TRIANGLES, spriteNumber * 6, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);


    }

    public void addSprite(Sprite sprite) {
        int index = this.spriteNumber;
        this.sprites[index] = sprite;
        this.spriteNumber++;



        if (sprite.getTexture() != null) {
            if (!textures.contains(sprite.getTexture())) {
                textures.add(sprite.getTexture());
            }
        }

        loadVertexProperties(index);
        loadIndicesProperties(index);

        if (spriteNumber > MAX_BATCH_SIZE) {
            hasRoom = false;
        }
    }

    private void loadVertexProperties(int index) {
        Sprite sprite = this.sprites[index];
        int offset = index * 4 * VERTEX_SIZE;
        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();

        int texId = 0;
        if (sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i) == sprite.getTexture()) {
                    texId = i + 1;
                    break;
                }
            }
        }


        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            if (i == 1)
                yAdd = 0.0f;
            else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            vertices[offset] = sprite.getTransform().getPosition().x + (xAdd * sprite.getTransform().getScale().x);
            vertices[offset + 1] = sprite.getTransform().getPosition().y + (yAdd * sprite.getTransform().getScale().y);
            vertices[offset + 2] = sprite.getTransform().getPosition().z + (0 * sprite.getTransform().getScale().z);

            vertices[offset + 3] = color.x;
            vertices[offset + 4] = color.y;
            vertices[offset + 5] = color.z;
            vertices[offset + 6] = color.w;

            vertices[offset + 7] = texCoords[i].x;
            vertices[offset + 8] = texCoords[i].y;

            vertices[offset + 9] = texId;

            offset += VERTEX_SIZE;
        }
    }

    private void loadIndicesProperties(int index) {
        int offsetArray = 6 * index;
        int offset = 4 * index;
        indices[offsetArray] = offset + 3;
        indices[offsetArray + 1] = offset + 2;
        indices[offsetArray + 2] = offset + 0;
        indices[offsetArray + 3] = offset + 0;
        indices[offsetArray + 4] = offset + 2;
        indices[offsetArray + 5] = offset + 1;
    }
}
