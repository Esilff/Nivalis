package nivalis.tools.controls;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Key {
    private static Key instance;
    private boolean[] keyPressed = new boolean[350];


    private Key() {

    }

    public static Key get() {
        if (instance == null) instance = new Key();
        return instance;
    }

    public static void keyCallback(long window, int key, int scancode,int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
}
