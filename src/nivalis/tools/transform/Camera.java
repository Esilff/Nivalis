package nivalis.tools.transform;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private Matrix4f projection;
    private Matrix4f scale;
    private int scaleValue;

    public Camera(int width, int height) {
        this.position = new Vector3f(0,0,0);
        this.projection = new Matrix4f().ortho2D(-width/2, width/2, -height/2, height/2);
        this.scaleValue = 64;
        this.scale = new Matrix4f().scale(scaleValue);
    }

    /**
     * Return the projection of the camera, it is used everywhere since rendering requires a model view projection matrix.
     * @return The projection of the camera.
     */

    public Matrix4f getProjection() {
        Matrix4f target = new Matrix4f();
        Matrix4f pos = new Matrix4f().setTranslation(position);
        target = projection.mul(pos, target);
        target.mul(scale, target);
        return target;
    }

    public void updateProjection(int width, int height) {
        this.projection = new Matrix4f().ortho2D(-width/2, width/2, -height/2, height/2);
    }

    public int getScaleValue() {
        return scaleValue;
    }

    public void zoom() {
        scaleValue +=1;
        scale = new Matrix4f().scale(scaleValue);
    }

    public void dezoom() {
        scaleValue -=1;
        scale = new Matrix4f().scale(scaleValue);
    }
}
