package pathfinding.algos;

import pathfinding.dto.ASPoint;
import pathfinding.dto.Path;
import pathfinding.dto.PathPoint;
import pathfinding.exception.TargetUnreachableException;
import pathfinding.interfaces.IPathfinder;

import java.util.*;

public class AStarFinder implements IPathfinder
{
    private ASPoint[][] originalMap;
    private ASPoint[][] map;
    private PathPoint target;
    private List<ASPoint> open;
    private Map<ASPoint, ASPoint> cameFrom;
    private boolean[][] closed;

    public AStarFinder(ASPoint[][] map)
    {
        this.originalMap = map;
    }

    @Override
    public Path calculatePath(PathPoint origin, PathPoint target, int maxCost) throws TargetUnreachableException
    {
        synchronized (this) // Neccesaire
        {
            this.map = new ASPoint[originalMap.length][originalMap[0].length];

            for (int i = 0; i < this.map.length; i++)
            {
                for (int j = 0; j < this.map[0].length; j++)
                {
                    if (this.originalMap[i][j] == null)
                    {
                        System.out.println("caca");
                    }

                    this.map[i][j] = new ASPoint(i, j, this.originalMap[i][j].isWalkable());
                }
            }


            this.target = target;
            this.closed = new boolean[map.length][map[0].length];

            this.open = new LinkedList<ASPoint>();
            this.cameFrom = new HashMap<ASPoint, ASPoint>();

            // Initialise le point de départ
            map[origin.getX()][origin.getY()].setgCost(0);
            map[origin.getX()][origin.getY()].setfCost(origin.distance(target));

            ASPoint originPrime = new ASPoint(origin.getX(), origin.getY(), true);
            originPrime.setgCost(0);
            this.open.add(originPrime);

            while (!this.open.isEmpty()) // Tant qu'on peux explorer de nouveaux points
            {
                // On récupère le point avec le fCost le plus petit (donc théoriquement le plus proche de la solution)
                ASPoint best = popBestPoint();

                if (best.getX() == target.getX() && best.getY() == target.getY()) // On a trouvé l'arivée
                {
                    return recontructPath(best);
                }

                if (best.getgCost() >= maxCost) // Si le point le plus "proche" est déja trop loin du départ alors ca ne sert a rien de chercher plus
                {
                    throw new TargetUnreachableException("Pas assez de points de mouvements !");
                }

                // Sinon on regarde autour
                neighbor(best);
            }

            throw new TargetUnreachableException("Aucun chemin !");
        }
    }

    /**
     * Cherche le point avec le fcost le plus petit
     * @return point avec le fcost le plus petit
     */
    private ASPoint popBestPoint()
    {
        // On trie la liste de point par leur fcost
        Collections.sort(this.open, new Comparator<ASPoint>()
                {
                    @Override
                    public int compare(ASPoint p1, ASPoint p2)
                    {
                        return p1.getfCost() - p1.getfCost();
                    }
                }
        );

        // On récupère le premier (donc le plus petit fcost)
        ASPoint p = open.remove(0);
        open.remove(p);
        return p;
    }

    /**
     * Reconstruit le chemin après execution de l'algorithme
     * @return Chemin de points
     */
    private Path recontructPath(ASPoint p)
    {
        Path result = new Path();
        result.add(target);

        ASPoint temp = p;
        while ((temp = this.cameFrom.get(temp)) != null)
        {
            result.add(temp);
        }
        result.reverse();
        return result;
    }

    private void neighbor(ASPoint p)
    {
        int x = p.getX();
        int y = p.getY();
        int tentativegCost = p.getgCost() + 1;

        // Pour i = -1 et 1
        for (int i = -1; i<=1; i = i+2)
        {
            try
            {
                ASPoint neighbor = this.map[x+i][y];

                if (neighbor.isWalkable() && !closed[x+i][y])
                {
                    if (tentativegCost < neighbor.getgCost())
                    {
                        // On a trouvé un meilleur chemin
                        this.cameFrom.put(neighbor, p);
                        neighbor.setgCost(tentativegCost);
                        neighbor.setfCost(tentativegCost + neighbor.distance(this.target));
                        if (!this.open.contains(neighbor))
                        {
                            open.add(neighbor);
                        }
                    }
                }
            }
            catch (IndexOutOfBoundsException e)
            {
                // Si on sort du tableau bah on continue
                continue;
            }
        }

        // Pour i = -1 et 1
        for (int i = -1; i<=1; i = i+2)
        {
            try
            {
                ASPoint neighbor = this.map[x][y+i];

                if (neighbor.isWalkable())
                {
                    if (tentativegCost < neighbor.getgCost() && !closed[x][y+i])
                    {
                        // On a trouvé un meilleur chemin
                        this.cameFrom.put(neighbor, p);
                        neighbor.setgCost(tentativegCost);
                        neighbor.setfCost(tentativegCost + neighbor.distance(this.target));
                        if (!this.open.contains(neighbor))
                        {
                            open.add(neighbor);
                        }
                    }
                }
            }
            catch (IndexOutOfBoundsException e)
            {
                // Si on sort du tableau bah on continue
                continue;
            }
        }

        this.closed[x][y] = true;
    }
}
