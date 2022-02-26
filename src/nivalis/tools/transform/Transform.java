package nivalis.tools.transform;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    private Vector3f position;
    private Vector3f scale;

    public Transform(float x, float y) {
        this.position = new Vector3f(x,y,0f);
        scale = new Vector3f(1,1,1);
    }

    public Matrix4f getProjection(Matrix4f target) {
        target.scale(scale);
        target.translate(position);
        return target;
    }
}
