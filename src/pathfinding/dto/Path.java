package pathfinding.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Path
{
    private List<PathPoint> points;

    public Path()
    {
        points = new ArrayList<PathPoint>();
    }

    public void add(PathPoint p)
    {
        this.points.add(p);
    }

    public void reverse()
    {
        Collections.reverse(points);
    }

    public Iterator<PathPoint> getIterator()
    {
        return this.points.iterator();
    }

    public PathPoint getOrigin()
    {
        return this.points.get(0);
    }

    public PathPoint getDestination()
    {
        return this.points.get(this.points.size() - 1);
    }

    public List<PathPoint> getList()
    {
        // TODO Remove this
        return points;
    }

    public String toString() {
        String s = " {";
        boolean first = false;
        for (int i = 0; i < points.size(); i++) {
            if (!first) { s += points.get(i).toString(); first = true;  }
            else s += " ; " + points.get(i).toString();
        }
        s+= "}";
        return s;
    }
}
