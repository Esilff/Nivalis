package nivalis.engine.render.shapes;

import nivalis.engine.render.Model;

public class Square {
    private static final float[] vertices = new float[] {
            -0.5f,0.5f,0f,
            0.5f,0.5f,0f,
            0.5f,-0.5f,0f,
            -0.5f,-0.5f,0f
    };

    private static final float[] texCoords = new float[] {
            0,0,
            1,0,
            1,1,
            0,1
    };

    private static final int[] indices = new int[] {
            0,1,2,
            2,3,0
    };

    public static final Model SQUARE = new Model(vertices, texCoords, indices);
}
