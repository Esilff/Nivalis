package nivalis.engine.render.utils;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Buffers {
    public static FloatBuffer createFloatBuffer(float[] data) {
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

    public static IntBuffer createIntegerBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
