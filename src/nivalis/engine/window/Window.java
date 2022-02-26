package nivalis.engine.window;

import nivalis.engine.utils.Time;
import nivalis.tools.controls.Key;
import nivalis.tools.controls.Mouse;
import nivalis.tools.game.Scene;
import nivalis.tools.transform.Camera;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static nivalis.engine.utils.WindowContext.Transparent2DContext;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author Esilff
 */

public class Window {
    private long window;
    private int width, height;
    private String title;
    private boolean isFullscreen;
    private double inputCooldown = 0.0;

    private Scene scene;
    private boolean hasPreprocessed = false;

    private Camera camera;

    public Window(String name) {
        title = name;
        width = 900;
        height = 900;
        isFullscreen = false;
        camera = new Camera(width,height);
    }

    /**
     * Run the window.
     */

    public void run() {
        System.out.println("Using LWJGL " + Version.getVersion() + ".\n");
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Initialize a window and make the context for using GLFW and OpenGL.
     */

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) throw new IllegalStateException("Unable to initialize LWJGL");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); //This method hide the window
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(width, height, title, 0, NULL);
        if (window == NULL) throw new RuntimeException("Failed to create the window");
        setCallbacks();
        glfwMakeContextCurrent(window); //Make the OpenGL context
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    /**
     * The gameloop where everything process, it can be rendering, detecting pollEvents or mesuring FPS.
     */

    private void loop() {
        GL.createCapabilities();
        glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
        Transparent2DContext();
        double frameCap = 1.0 / 60;
        double time = Time.getTime();
        double unprocessed = 0;
        double frameTime = 0;
        double frames = 0;
        while (!glfwWindowShouldClose(window)) {
            if (!hasPreprocessed) {
                scene.preprocess();
                hasPreprocessed = true;
            }
            boolean canRender = false;
            double secondTime = Time.getTime();
            double passed = secondTime - time;
            unprocessed += passed;
            frameTime += passed;
            time = secondTime;
            if (inputCooldown > 0.0) {
                inputCooldown -= passed;
            }
            while (unprocessed >= frameCap) {
                unprocessed -= frameCap;
                canRender = true;
            }
            checkWindowInputs();
            if (canRender) {


                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                scene.loop();
                glfwSwapBuffers(window);
                frames++;
                glfwPollEvents();
                if (frameTime >= 1.0) {
                    frameTime = 0;
                    System.out.println("FPS : " + frames);
                    frames = 0;
                }
            }
        }
    }

    /**
     * Set the callbacks in order to detect the mouse position, inputs and scrolling, but also the keyboard inputs.
     */

    private void setCallbacks() {
            glfwSetCursorPosCallback(window, Mouse::mousePosCallback);
            glfwSetMouseButtonCallback(window, Mouse::mouseButtonCallback);
            glfwSetScrollCallback(window, Mouse::mouseScrollCallback);
            glfwSetKeyCallback(window, Key::keyCallback);
    }

    /**
     * Execute actions on the window if the right inputs are pressed.
     */

    private void checkWindowInputs() {
        if (Key.isKeyPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window, true);
        }
        if (Key.isKeyPressed(GLFW_KEY_F11)) {
            if (!isFullscreen && inputCooldown <= 0) {
                glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0,0,getPrimaryMonitor().width(),getPrimaryMonitor().height(),getPrimaryMonitor().refreshRate());
                isFullscreen = true;
                inputCooldown = 1.0;
            }
            else if (isFullscreen && inputCooldown <= 0){
                glfwSetWindowMonitor(window, 0, 900/2, 900/2, 900, 900, getPrimaryMonitor().refreshRate());
                isFullscreen = false;
                inputCooldown = 1.0;
            }
        }
    }

    /**
     * Give access to the parameter of the primary monitor.
     * @return The class containing the monitor parameters.
     */

    public static GLFWVidMode getPrimaryMonitor() {
        GLFWVidMode video = glfwGetVideoMode(glfwGetPrimaryMonitor());
        return video;
    }

    /**
     * Change the scene that has to be rendered
     * @param scene The new scene
     */

    public void setScene(Scene scene) {
        this.scene = scene;
        hasPreprocessed = false;
    }

    public Camera getCamera() { return camera;}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
