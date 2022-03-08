package pathfinding.dto;

public class PathPoint
{
    protected int x;
    protected int y;
    protected boolean walkable;

    public PathPoint(int x, int y, boolean wakable)
    {
        this.x = x;
        this.y = y;
        this.walkable = wakable;
    }

    /**
     * Permet de savoir si une case est traversable ou non.
     * @return L'état d'une case.
     */

    public boolean isWalkable()
    {
        return walkable;
    }

    /**
     * Définit l'état d'une case
     * @param walkable, un état définissant si une case est traversable ou non.
     */

    public void setWalkable(boolean walkable)
    {
        this.walkable = walkable;
    }



    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    /**
     * Calcule la distance entre ce point et un autre
     * @param p Autre point
     * @return distance
     */
    public int distance(PathPoint p)
    {
        return (int)Math.floor(Math.sqrt((this.x - p.x)^2 + (this.y - p.y)^2));
    }

    public String toString() {
        String s = x + ":" + y;
        return s;
    }
}
