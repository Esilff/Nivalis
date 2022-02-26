package nivalis.engine.render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;

/**
 * @author Esilff
 */

public class Model {
    private int drawCount;
    private int vertexID;
    private int textureID;
    private int indiceID;

    /**
     * Create a buffer for each parameter of the constructor. 2 array buffers for the vertices and the textures
     * coordinates and one array element buffer for the indices. The constructor initialize the data part of the
     * texture that we want to shape.
     * @param vertices An array of floats that contains the vertices. It's a matrix of coordinates basically.
     * @param textureCoords An array that contains the texture coordinates.
     * @param indices An array of indices. Indices are pointers to lines of the vertices matrix and they are used
     *                to generate complex shapes.
     */

    public Model(float[]vertices, float[] textureCoords, int[] indices) {
        drawCount = indices.length;

        vertexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexID);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(vertices), GL_STATIC_DRAW);

        textureID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureID);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(textureCoords),GL_STATIC_DRAW);

        indiceID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indiceID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createIntegerBuffer(indices), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * Render the actual data created in the constructor. It's the most important method since it's our base to actually
     * render things to the screen.
     */

    public void render() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, vertexID);
        glVertexAttribPointer(0,3,GL_FLOAT, false,0 ,0);

        glBindBuffer(GL_ARRAY_BUFFER, textureID);
        glVertexAttribPointer(1,2,GL_FLOAT, false,0,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indiceID);

        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    /**
     * Take an array of float in order to return a float buffer that can be used to initialize a model. In our
     * case it is used to convert the vertices and texture coordinates array.
     * @param data A float array.
     * @return A float buffer.
     */

    private FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    /**
     * Take an array of integer in order to return a float buffer that can be used to initialize a model. In our
     * case it is used to convert the indices array.
     * @param data An integer array.
     * @return An integer buffer.
     */

    private IntBuffer createIntegerBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
