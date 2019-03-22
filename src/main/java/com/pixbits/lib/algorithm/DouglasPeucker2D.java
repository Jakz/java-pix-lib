package com.pixbits.lib.algorithm;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.ToDoubleFunction;

import com.pixbits.lib.lang.IntRange;

public class DouglasPeucker2D<T>
{
  public ToDoubleFunction<T> mapperX;
  public ToDoubleFunction<T> mapperY;
  private T[] data;

  public DouglasPeucker2D(T[] data, ToDoubleFunction<T> mx, ToDoubleFunction<T> my)
  {
    this.data = data; 
    this.mapperX = mx;
    this.mapperY = my;
  }

  public double getSquareDistance(T p1, T p2) {

    double dx = mapperX.applyAsDouble(p1) - mapperX.applyAsDouble(p2);
    double dy = mapperY.applyAsDouble(p1) - mapperY.applyAsDouble(p2);

    return dx * dx + dy * dy;
  }

  public double getSquareSegmentDistance(T p0, T p1, T p2) {
    double x0, y0, x1, y1, x2, y2, dx, dy, t;

    x1 = mapperX.applyAsDouble(p1);
    y1 = mapperY.applyAsDouble(p1);
    x2 = mapperX.applyAsDouble(p2);
    y2 = mapperY.applyAsDouble(p2);
    x0 = mapperX.applyAsDouble(p0);
    y0 = mapperY.applyAsDouble(p0);

    dx = x2 - x1;
    dy = y2 - y1;

    if (dx != 0.0d || dy != 0.0d)
    {
      t = ((x0 - x1) * dx + (y0 - y1) * dy)
          / (dx * dx + dy * dy);

      if (t > 1.0d)
      {
        x1 = x2;
        y1 = y2;
      } 
      else if (t > 0.0d)
      {
        x1 += dx * t;
        y1 += dy * t;
      }
    }

    dx = x0 - x1;
    dy = y0 - y1;

    return dx * dx + dy * dy;
  }

  public T[] simplify(T[] points, double tolerance, boolean highestQuality)
  {
    final double sqTolerance = tolerance * tolerance;

    if (!highestQuality)
      points = simplifyRadialDistance(points, sqTolerance);

    points = simplifyDouglasPeucker(points, sqTolerance);

    return points;
  }

  T[] simplifyRadialDistance(T[] points, double sqTolerance)
  {
    T point = null;
    T prevPoint = points[0];

    List<T> newPoints = new ArrayList<T>();
    newPoints.add(prevPoint);

    for (int i = 1; i < points.length; ++i) {
      point = points[i];

      if (getSquareDistance(point, prevPoint) > sqTolerance) {
        newPoints.add(point);
        prevPoint = point;
      }
    }

    if (prevPoint != point) {
      newPoints.add(point);
    }

    return newPoints.toArray(data);
  }

  T[] simplifyDouglasPeucker(T[] points, double sqTolerance)
  {
    BitSet bitSet = new BitSet(points.length);
    bitSet.set(0);
    bitSet.set(points.length - 1);

    List<IntRange> stack = new ArrayList<IntRange>();
    stack.add(new IntRange(0, points.length - 1));

    while (!stack.isEmpty())
    {
      IntRange range = stack.remove(stack.size() - 1);

      int index = -1;
      double maxSqDist = 0f;

      // find index of point with maximum square distance from first and last point
      for (int i = range.first() + 1; i < range.last(); ++i) {
        double sqDist = getSquareSegmentDistance(points[i], points[range.first()], points[range.last()]);

        if (sqDist > maxSqDist)
        {
          index = i;
          maxSqDist = sqDist;
        }
      }

      if (maxSqDist > sqTolerance)
      {
        bitSet.set(index);

        stack.add(new IntRange(range.first(), index));
        stack.add(new IntRange(index, range.last()));
      }
    }

    List<T> newPoints = new ArrayList<T>(bitSet.cardinality());
    for (int index = bitSet.nextSetBit(0); index >= 0; index = bitSet.nextSetBit(index + 1))
    {
      newPoints.add(points[index]);
    }

    return newPoints.toArray(data);
  }
}
