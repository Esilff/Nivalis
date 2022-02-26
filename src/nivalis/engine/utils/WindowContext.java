package nivalis.engine.utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

/**
 * @author Esilff
 */

public class WindowContext {

    /**
     * Make a gl context to render 2D textures with pixels where the alpha channel equals to 0. Quite useful for rendering
     * characters.
     */

    public static void Transparent2DContext() {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
}
