package nivalis.tools.transform;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    private Vector3f position;
    private Vector3f scale;
    private int rotation;
    private final float Z = 0.0f;

    public Transform(float x, float y, int rotation) {
        this.position = new Vector3f(x,y,Z);
        scale = new Vector3f(1,1,1);
        this.rotation = rotation;
    }

    public Matrix4f getProjection(Matrix4f target) {
        target.scale(scale);
        target.translate(position);
        target.rotate((float)Math.toRadians(rotation),0,0,1);
        return target;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getScale () {
        return scale;
    }
}
