package pathfinding.render;

import nivalis.engine.render.*;
import nivalis.tools.transform.Camera;
import nivalis.tools.transform.Transform;

import static nivalis.engine.render.shapes.Square.SQUARE;

public class RenderedPoint {
    private Transform position;
    private String spritePath;
    private Model model = SQUARE;
    private Shader shader;

    {
        try {
            shader = new Shader("./res/shader/textureshader.txt");
        } catch (ShaderException e) {
            e.printStackTrace();
        }
    }
    private Texture texture;


    public RenderedPoint(Transform position, String spritePath) {
        this.position = position;
        this.spritePath = spritePath;
        this.texture = new Texture(this.spritePath);
    }

    public void render(Camera camera) {

        shader.bind();
        try {
            texture.bind(1);
        } catch (InvalidTextureSlotException e) {
            e.printStackTrace();
        }
        shader.setUniform("sampler", 1);
        shader.setUniform("projection", position.getProjection(camera.getProjection()));
        model.render();

    }
}
