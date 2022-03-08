package pathfinding.interfaces;

import pathfinding.dto.Path;
import pathfinding.dto.PathPoint;
import pathfinding.exception.TargetUnreachableException;

public interface IPathfinder
{
    Path calculatePath(PathPoint origin, PathPoint target, int maxCost) throws TargetUnreachableException;
}
