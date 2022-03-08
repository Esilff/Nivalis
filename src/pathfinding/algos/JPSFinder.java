package pathfinding.algos;

import pathfinding.dto.JPSPoint;
import pathfinding.dto.Path;
import pathfinding.dto.PathPoint;
import pathfinding.exception.TargetUnreachableException;
import pathfinding.interfaces.IPathfinder;

import java.util.*;

public class JPSFinder implements IPathfinder
{
    private PathPoint[][] map;
    private int mapSizeX;
    private int mapSizeY;
    private List<JPSPoint> open;
    private List<JPSPoint> close;
    private Map<PathPoint, JPSPoint> cameFrom;
    private PathPoint target;

    public JPSFinder(PathPoint[][] map)
    {
        this.map = map;
        this.mapSizeX = map.length;
        this.mapSizeY = map[0].length;
        this.open = new ArrayList<JPSPoint>();
        this.close = new ArrayList<JPSPoint>();
        this.cameFrom = new HashMap<PathPoint, JPSPoint>();
    }

    @Override
    public Path calculatePath(PathPoint origin, PathPoint target, int maxCost) throws TargetUnreachableException
    {
        this.target = target;

        // Init origine
        for (int i = 0; i <= 7; i++)
        {
            try
            {
                PathPoint p = map[JPSPoint.getXOffsetDir(origin.getX(), i)][JPSPoint.getYOffsetDir(origin.getY(), i)];

                if (p.isWalkable())
                {
                    // Si le point est traversable alors on ajoute la direction à explorer
                    open.add(new JPSPoint(origin.getX(), origin.getY(), i));
                }
            }
            catch (IndexOutOfBoundsException e)
            {
                // Si on sort de la grille alors on ignore
                continue;
            }
        }

        // Tant qu'on peux explorer des points on continue
        while (!this.open.isEmpty())
        {
            System.out.println("\n\nNew Loop");

            JPSPoint p = this.popBestPoint();

            System.out.println("x:" + p.getX() + " y:" + p.getY() + " dir:" + p.getDirection());

            if (p.getX() == target.getX() && p.getY() == target.getY())
            {
                return recontructPath(p);
            }

            if (p.getDirection()%2 == 0)
            {
                // Verification lineaire
                linearScan(p);
            }
            else
            {
                // Verification diagonale
                diagonalScan(p);
            }
        }

        throw new TargetUnreachableException("Erreur");
    }

    private JPSPoint popBestPoint()
    {
        Collections.sort(this.open, new Comparator<JPSPoint>()
        {
            @Override
            public int compare(JPSPoint o1, JPSPoint o2)
            {
                return o1.distance(target) - o2.distance(target);
            }
        });

        // Le premier donc le plus proche de la destination
        JPSPoint p = open.remove(0);
        return p;
    }

    private Path recontructPath(PathPoint p)
    {
        return null;
    }

    /**
     * Lance une recherche lineaire depuis un point
     * @param p Point de départ
     * @return Point ajouté dans la liste ouverte
     */
    private boolean linearScan(JPSPoint p)
    {
        System.out.println("Linear " + p.getDirection());

        boolean foundInterestPoint = false;
        int direction = p.getDirection();
        int currentX = p.getX();
        int currentY = p.getY();

        if (this.close.contains(p))
        {
            return false; // Pour eviter de réexplorer des chemins déja faits
        }
        this.close.add(p);

        while (currentX >= 0 && currentX < mapSizeX && currentY >= 0 && currentY < mapSizeY)
        {
            System.out.println("Linear step x:" + currentX + " y:" + currentY);

            if (!map[currentX][currentY].isWalkable())
            {
                break; // On a touché un mur on arrete
            }

            // Verif à "gauche"
            try
            {
                if (!map[JPSPoint.getXOffsetDir(currentX, JPSPoint.rotateDir(direction, -2))][JPSPoint.getYOffsetDir(currentY, JPSPoint.rotateDir(direction, -2))].isWalkable())
                {
                    // Si on a un mur à "gauche" et que on peux avancer, il faut vérifier si on peux aller sur la diagonale gauche
                    PathPoint tempp = map[JPSPoint.getXOffsetDir(currentX, JPSPoint.rotateDir(direction, -1))][JPSPoint.getYOffsetDir(currentY, JPSPoint.rotateDir(direction, -1))];
                    if (tempp.isWalkable())
                    {
                        // On a trouvé un point interessant donc on le garde
                        this.open.add(new JPSPoint(currentX, currentY, JPSPoint.rotateDir(direction, -1)));
                        foundInterestPoint = true;
                        System.out.println("Trouvé " + JPSPoint.rotateDir(direction, -1));
                    }
                }
            }
            catch (IndexOutOfBoundsException e)
            {
                // Rien on a du sortir du tableau
            }

            // Verif à "droite"
            try
            {
                if (!map[JPSPoint.getXOffsetDir(currentX, JPSPoint.rotateDir(direction, 2))][JPSPoint.getYOffsetDir(currentY, JPSPoint.rotateDir(direction, 2))].isWalkable())
                {
                    // Si on a un mur à "droite" et que on peux avancer, il faut vérifier si on peux aller sur la diagonale droite
                    if (map[JPSPoint.getXOffsetDir(currentX, JPSPoint.rotateDir(direction, 1))][JPSPoint.getYOffsetDir(currentY, JPSPoint.rotateDir(direction, 1))].isWalkable())
                    {
                        // On a trouvé un point interessant donc on le garde
                        this.open.add(new JPSPoint(currentX, currentY, JPSPoint.rotateDir(direction, 1)));
                        foundInterestPoint = true;
                        System.out.println("Trouvé " + JPSPoint.rotateDir(direction, 1));
                    }
                }
            }
            catch (IndexOutOfBoundsException e)
            {
                // Rien on a du sortir du tableau
            }

            currentX = JPSPoint.getXOffsetDir(currentX, direction);
            currentY = JPSPoint.getYOffsetDir(currentY, direction);
        }


        return foundInterestPoint;
    }

    private void diagonalScan(JPSPoint p)
    {
        System.out.println("Diago " + p.getDirection());

        int direction = p.getDirection();

        int currentX = JPSPoint.getXOffsetDir(p.getX(), direction);
        int currentY = JPSPoint.getYOffsetDir(p.getY(), direction);

        boolean leftSearch = false;
        boolean rightSearch = false;

        boolean foundInterestPoint = false;

        if (this.close.contains(p))
        {
            return; // Pour eviter de réexplorer des chemins déja faits
        }
        this.close.add(p);

        while (currentX >= 0 && currentX < mapSizeX && currentY >= 0 && currentY < mapSizeY)
        {
            if (!map[currentX][currentY].isWalkable())
            {
                break; // On a touché un mur on arrete
            }

            leftSearch = linearScan(new JPSPoint(currentX, currentY, JPSPoint.rotateDir(direction, -1)));
            rightSearch = linearScan(new JPSPoint(currentX, currentY, JPSPoint.rotateDir(direction, 1)));

            if (leftSearch || rightSearch)
            {
                foundInterestPoint = true;
            }

            // Verif à "gauche"
            try
            {
                int checkDir = JPSPoint.rotateDir(direction, -3);
                PathPoint tempp = map[JPSPoint.getXOffsetDir(currentX, checkDir)][JPSPoint.getYOffsetDir(currentY, checkDir)];
                System.out.println("wait");
                if (!tempp.isWalkable())
                {
                    // Si on a un mur à "gauche" et que on peux avancer, il faut vérifier si on peux aller sur la diagonale gauche
                    if (map[JPSPoint.getXOffsetDir(currentX, JPSPoint.rotateDir(direction, -2))][JPSPoint.getYOffsetDir(currentY, JPSPoint.rotateDir(direction, -2))].isWalkable())
                    {
                        // On a trouvé un point interessant donc on le garde
                        this.open.add(new JPSPoint(currentX, currentY, (direction-2)%8));
                        foundInterestPoint = true;
                    }
                }
            }
            catch (IndexOutOfBoundsException e)
            {
                // Rien on a du sortir du tableau
            }

            // Verif à "droite"
            try
            {
                if (!map[JPSPoint.getXOffsetDir(currentX, JPSPoint.rotateDir(direction, 3))][JPSPoint.getYOffsetDir(currentY, JPSPoint.rotateDir(direction, 3))].isWalkable())
                {
                    // Si on a un mur à "gauche" et que on peux avancer, il faut vérifier si on peux aller sur la diagonale gauche
                    if (map[JPSPoint.getXOffsetDir(currentX, JPSPoint.rotateDir(direction, 2))][JPSPoint.getYOffsetDir(currentY, JPSPoint.rotateDir(direction, 2))].isWalkable())
                    {
                        // On a trouvé un point interessant donc on le garde
                        this.open.add(new JPSPoint(currentX, currentY, JPSPoint.rotateDir(direction, 2)));
                        foundInterestPoint = true;
                    }
                }
            }
            catch (IndexOutOfBoundsException e)
            {
                // Rien on a du sortir du tableau
            }

            if (foundInterestPoint)
            {
                // On à trouvé un point interresant on va donc l'explorer avant, mais on sauvegarde
                this.open.add(new JPSPoint(currentX, currentY, direction));
                break;
            }

            // Si on doit avancer et continuer alors on se déplace
            currentX = JPSPoint.getXOffsetDir(currentX, direction);
            currentY = JPSPoint.getYOffsetDir(currentY, direction);
            System.out.println("Diago Step\n");
        }
        System.out.println("DiagoEnd\n");
    }
}
