package nivalis.tools.rendering;

import nivalis.tools.transform.Camera;

public class Renderer {
    public static void render(Renderable renderable, Camera camera) {
        renderable.render(camera);
    }
}
