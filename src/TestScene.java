import nivalis.engine.render.*;
import nivalis.engine.window.Window;
import nivalis.tools.controls.Key;
import nivalis.tools.controls.Mouse;
import nivalis.tools.game.Scene;
import nivalis.tools.transform.Transform;
import org.joml.Quaternionf;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import pathfinding.algos.AStarFinder;
import pathfinding.dto.ASPoint;
import pathfinding.dto.Path;
import pathfinding.dto.PathPoint;
import pathfinding.exception.TargetUnreachableException;
import pathfinding.render.RenderedPath;


import static nivalis.engine.render.shapes.Square.SQUARE;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.glClearColor;

public class TestScene implements Scene {

    public TestScene(Window window) {
        this.window = window;
    }

    private Window window;
    private Texture texture;
    private Shader shader;
    private Model model;
    private int x = 0;
    private int y = 0;
    private AStarFinder pathfinder;
    private ASPoint[][] map = new ASPoint[10][10];
    private RenderedPath pathrenderer = new RenderedPath();
    private boolean renderingPath = false;
    private boolean onDrag = false;
    private Path path;
    private int lastX = 0;
    private int lastY = 0;
    //private Renderer renderer;
    private RenderBatch renderbatch;

    @Override
    public void preprocess() {
        renderbatch = new RenderBatch();
        renderbatch.addSprite(new Sprite(0,0,new Texture("./res/texture/0.png")));
        renderbatch.addSprite(new Sprite(1,0,new Texture("./res/texture/end.png")));

        /*float totalWidth = (float) (600  * 2);
        float totalHeight = (float) (300  * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 10; y++) {
                float xPos = (x );
                float yPos =  (y );
                Sprite sprite = new Sprite(xPos, yPos, new Vector4f(xPos / totalWidth, yPos / totalHeight, 0.0f, 1.0f));
                renderbatch.addSprite(sprite);
            }
        }*/
        System.out.println("debug");
        /*renderer = new Renderer();
        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float) (600 - xOffset * 2);
        float totalHeight = (float) (300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++){
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);
                Sprite sprite = new Sprite(xOffset, yOffset, new Vector4f(xPos / totalWidth, yPos / totalHeight, 1.0f, 1.0f));
                renderer.add(sprite);
            }
        }*/

        model = SQUARE;
        try {
            shader = new Shader("./res/shader/textureshader.txt");
        } catch (ShaderException e) {
            e.printStackTrace();
        }
        texture = new Texture("./res/texture/0.png");

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = new ASPoint(i, j, true);
            }
        }
        pathfinder = new AStarFinder(map);

        path = null;

    }

    @Override
    public void loop() {
        //renderer.render(window.getCamera());
        //renderbatch.render(window.getCamera());
            /*if (Key.isKeyPressed(GLFW_KEY_UP)) {
                window.getCamera().zoom();
            }
            if (Key.isKeyPressed(GLFW_KEY_DOWN)) {
                window.getCamera().dezoom();
            }*/


        shader.bind();
        shader.setUniform("sampler", 1);
        shader.setUniform("projection", new Transform(x,y,0).getProjection(window.getCamera().getProjection()));
        model.render();
            try {
                texture.bind(1);
            } catch (InvalidTextureSlotException e) {
                e.printStackTrace();
            }


            if (((Mouse.getX() > window.getWidth()/2 - 32)&& (Mouse.getX() < window.getWidth()/2 + 32) &&
         (Mouse.getY() > window.getHeight()/2 - 32 && (Mouse.getY() < window.getHeight()/2 + 32)) && Mouse.mouseButtonDown(GLFW_MOUSE_BUTTON_1)) || (onDrag && Mouse.mouseButtonDown(GLFW_MOUSE_BUTTON_1))){
            onDrag = true;

            if (Mouse.isDragging()) {

                int xPos = (int)(Mouse.getX()/(window.getCamera().getScaleValue()) + 0.5) - 7;
                int yPos = -((int)(Mouse.getY()/(window.getCamera().getScaleValue()) + 0.5) - 7);
                if (path != null)path.getList().clear();
                try {
                   if ((xPos != lastX)|| (yPos != lastY)) {

                       /*for (int i = 0; i < map.length; i++) {
                           for (int j = 0; j < map[i].length; j++) {
                               map[i][j] = new ASPoint(i, j, true);
                           }
                       }
                       pathfinder = new AStarFinder(map);*/

                       path = pathfinder.calculatePath(new PathPoint(x, y, true), new PathPoint(x + xPos, y + yPos, true), 20);
                       if (path != null) System.out.println(path.toString());
                       if (xPos != 0 || yPos != 0) pathrenderer.init(path);


                   }
                   pathrenderer.render(window.getCamera());

                } catch (TargetUnreachableException e) {
                    e.printStackTrace();
                }
                lastX = xPos;
                lastY = yPos;

            }
            else {
                onDrag = false;
                pathrenderer.clear();
                x = lastX;
                y = lastY;
                lastX = 0;
                lastY = 0;
            }


            }


        }

        /*private void testRenderPath () {
            try {
                if (!renderingPath)
                    pathrenderer.init(pathfinder.calculatePath(new PathPoint(x, y, true), new PathPoint(x + 3, y, true), 20));
                pathrenderer.render(window.getCamera());
                renderingPath = true;
            } catch (TargetUnreachableException e) {
                e.printStackTrace();
            }
        }*/
        }




