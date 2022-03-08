package pathfinding.render;

import nivalis.tools.rendering.Renderable;
import nivalis.tools.transform.Camera;
import nivalis.tools.transform.Transform;
import pathfinding.dto.Path;
import pathfinding.dto.PathPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderedPath implements Renderable {

    private List<RenderedPoint> path;
    private int counter, max;

    public RenderedPath() {
        path = new ArrayList<>();
    }

    @Override
    public void render(Camera camera) {
        path.forEach(renderedPoint -> {
            renderedPoint.render(camera);
        });
    }

    public void init(Path path) {
        clear();
        max = path.getList().size() - 1;
        counter = 0;
        path.getList().forEach(pathPoint -> {
            boolean isStraight = false;
            if (counter != max && counter != 0) {
                if (path.getList().get(counter - 1).getX() != pathPoint.getX() && path.getList().get(counter - 1).getY() == pathPoint.getY()
                        && path.getList().get(counter + 1).getX() != pathPoint.getX() && path.getList().get(counter + 1).getY() == pathPoint.getY()) {
                    this.path.add(new RenderedPoint(new Transform(pathPoint.getX(), pathPoint.getY(), 0), "./res/texture/straight.png"));
                    isStraight = true;
                }
                if (path.getList().get(counter - 1).getX() == pathPoint.getX() && path.getList().get(counter - 1).getY() != pathPoint.getY()
                        && path.getList().get(counter + 1).getX() == pathPoint.getX() && path.getList().get(counter + 1).getY() != pathPoint.getY()) {
                    this.path.add(new RenderedPoint(new Transform(pathPoint.getX(), pathPoint.getY(), 90), "./res/texture/straight.png"));
                    isStraight = true;
                }
                if (path.getList().get(counter +1).getX() - path.getList().get(counter - 1).getX() > 0 && !isStraight) {
                    int rotation = (path.getList().get(counter + 1).getY() - path.getList().get(counter -1).getY() >0)?0:270;
                    this.path.add(new RenderedPoint(new Transform(pathPoint.getX(), pathPoint.getY(), rotation),"./res/texture/turn.png"));
                }
                if (path.getList().get(counter +1).getX() - path.getList().get(counter - 1).getX() <= 0 && !isStraight) {
                    int rotation = (path.getList().get(counter + 1).getY() - path.getList().get(counter -1).getY() >0)?90:180;
                    this.path.add(new RenderedPoint(new Transform(pathPoint.getX(), pathPoint.getY(), rotation),"./res/texture/turn.png"));
                }
                if (path.getList().get(counter +1).getY() - path.getList().get(counter - 1).getY() > 0 && !isStraight) {
                    int rotation = (path.getList().get(counter + 1).getX() - path.getList().get(counter -1).getX() >0)?0:270;
                    this.path.add(new RenderedPoint(new Transform(pathPoint.getX(), pathPoint.getY(), rotation),"./res/texture/turn.png"));
                }
                if (path.getList().get(counter +1).getY() - path.getList().get(counter - 1).getY() <= 0 && !isStraight) {
                    int rotation = (path.getList().get(counter + 1).getX() - path.getList().get(counter -1).getX() >0)?90:180;
                    this.path.add(new RenderedPoint(new Transform(pathPoint.getX(), pathPoint.getY(), rotation),"./res/texture/turn.png"));
                }
            } else if (counter == max || counter ==0) {
                if (counter == 0) {
                    int rotation = (path.getList().get(counter +1).getX() == pathPoint.getX())?(path.getList().get(counter+1).getY() > pathPoint.getY())?90:270:(path.getList().get(counter + 1).getX() > pathPoint.getX())?0:180;
                    this.path.add(new RenderedPoint(new Transform(pathPoint.getX(), pathPoint.getY(), rotation), "./res/texture/source.png"));
                }
                if (counter == max) {
                    int rotation = (path.getList().get(counter -1).getX() == pathPoint.getX())?(path.getList().get(counter-1).getY() > pathPoint.getY())?270:90:(path.getList().get(counter - 1).getX() > pathPoint.getX())?180:0;
                    this.path.add(new RenderedPoint(new Transform(pathPoint.getX(), pathPoint.getY(), rotation), "./res/texture/end.png"));
                }
            }
            counter++;
        });

    }

    public void clear() {
        this.path.clear();
    }
}
