package pathfinding.dto;

public class JPSPoint extends PathPoint
{
    // Ordonnées pour +1 => Sens horaire et -1 anti horaire
    public static final int NORTH = 0;
    public static final int NORTH_EAST = 1;
    public static final int EAST = 2;
    public static final int SOUTH_EAST = 3;
    public static final int SOUTH = 4;
    public static final int SOUTH_WEST = 5;
    public static final int WEST = 6;
    public static final int NORTH_WEST = 7;

    private int direction;

    public JPSPoint(int x, int y, int direction)
    {
        // Un point JPS est forcement traversable sinon aucun interet
        super(x, y, true);

        if (direction < 0 || direction > 7)
        {
            throw new RuntimeException("Direction invalide !");
        }

        this.direction = direction;
    }

    public int getDirection()
    {
        return this.direction;
    }

    /**
     * Calcul coordonées x depuis le point dans sa direction
     * @param n valeur de l'offset
     * @return x
     */
    public int getXOffset(int n)
    {
        int x = this.x;

        if (this.direction == 7 || this.direction <= 1) // Vers le haut
        {
            x = x - n;
        }
        else if (this.direction >= 3 && this.direction <= 5) // Vers le bas
        {
            x = x + n;
        }

        return x;
    }

    /**
     * Calcul coordonées y depuis le point dans sa direction
     * @param n valeur de l'offset
     * @return y
     */
    public int getYOffset(int n)
    {
        int y = this.y;

        if (this.direction >= 1 && this.direction <= 3) // Vers la droite
        {
            y = y + n;
        }
        else if (this.direction >= 5 && this.direction <= 7) // Vers la gauche
        {
            y = y - n;
        }

        return y;
    }

    /**
     * Décale les coordonées du point dans sa direction
     */
    public void shiftPoint()
    {
        this.x = getXOffset(1);
        this.y = getYOffset(1);
    }

    // Methodes statiques

    /**
     * Calcul coordonées x depuis le point avec une autre direction
     * @param dir direction
     * @return x
     */
    public static int getXOffsetDir(int x, int dir)
    {
        if (dir == 7 || dir <= 1)
        {
            return x-1;
        }
        else if (dir >= 3 && dir <= 5)
        {
            return x+1;
        }
        else
        {
            return x;
        }
    }

    /**
     * Calcul coordonées y depuis le point avec une autre direction
     * @param dir direction
     * @return x
     */
    public static int getYOffsetDir(int y, int dir)
    {
        if (dir >= 1 && dir <= 3)
        {
            return y+1;
        }
        else if (dir >= 5 && dir <= 7)
        {
            return y-1;
        }
        else
        {
            return y;
        }
    }

    /**
     * Permet de calculer la rotation d'une direction
     * @param dir direction de base
     * @param n rotation de la direction (positive = sens horaire)
     * @return direction rotationée
     */
    public static int rotateDir(int dir, int n)
    {
        int calcDir = (dir-n)%8;

        if (calcDir < 0)
        {
            calcDir += 8;
        }

        return calcDir;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) {
            return true;
        }

        if (!(o instanceof JPSPoint)) {
            return false;
        }

        JPSPoint p = (JPSPoint) o;

        return p.x == this.x && p.y == this.y && p.direction == this.direction;
    }
}
