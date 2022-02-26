import nivalis.engine.render.Model;
import nivalis.engine.render.Shader;
import nivalis.engine.render.ShaderException;
import nivalis.engine.render.Texture;
import nivalis.engine.window.Window;
import nivalis.tools.controls.Key;
import nivalis.tools.controls.Mouse;
import nivalis.tools.game.Scene;
import nivalis.tools.transform.Transform;
import org.joml.Vector3fc;

import static nivalis.engine.render.shapes.Square.SQUARE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.opengles.GLES20.glClearColor;

public class TestScene implements Scene {

    public TestScene(Window window) { this.window = window;}

    private Window window;
    private Texture texture;
    private Shader shader;
    private Model model;
    int x, y =0;

    @Override
    public void preprocess() {
        model = SQUARE;
        try {
            shader = new Shader("./res/shader/textureshader.txt");
        } catch (ShaderException e) {
            e.printStackTrace();
        }
        texture = new Texture("./res/texture/0.png");
    }

    @Override
    public void loop() {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", new Transform(x,y).getProjection(window.getCamera().getProjection()));
        model.render();
        texture.bind(0);
        if (Key.isKeyPressed(GLFW_KEY_D)) x += 1;
        if ((Mouse.getX() > window.getWidth()/2 - 32)&& (Mouse.getX() < window.getWidth()/2 + 32) &&
         (Mouse.getY() > window.getHeight()/2 - 32 && (Mouse.getY() < window.getHeight()/2 + 32))){
            System.out.println("hover");
        }


    }
}
