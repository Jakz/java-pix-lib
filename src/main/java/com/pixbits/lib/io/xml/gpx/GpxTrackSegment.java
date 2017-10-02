package com.pixbits.lib.io.xml.gpx;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlElement;

import com.pixbits.lib.lang.Pair;
import com.pixbits.lib.ui.color.ColorGenerator;
import com.pixbits.lib.ui.color.PleasantColorGenerator;
import com.pixbits.lib.ui.table.DataSource;

public class GpxTrackSegment implements DataSource<GpxWaypoint>
{
  @XmlElement(name = "trkpt") List<GpxWaypoint> points;
  @XmlElement GpxExtension extensions;
  
  private double[] distanceCache;
  private double totalLength;
  
  GpxTrackSegment(List<GpxWaypoint> points)
  {
    this.points = points;
    totalLength = Double.NaN;
  }
  
  GpxTrackSegment()
  {
    points = new ArrayList<>();
    totalLength = Double.NaN;
  }
  
  public double distanceBetweenPoints(int index)
  {
    if (distanceCache == null)
    {
      distanceCache = new double[points.size()];
      Arrays.fill(distanceCache, Double.NaN);
    }
    
    if (Double.isNaN(distanceCache[index]))
      distanceCache[index] = points.get(index).coordinate.distance(points.get(index+1).coordinate);
    
    return distanceCache[index];
  }
  
  public double totalLength()
  {
    if (!Double.isNaN(totalLength))
      return totalLength;
    
    totalLength = 0.0;
    for (int i = 0; i < points.size() - 1; ++i)
      totalLength += distanceBetweenPoints(i);
    
    return totalLength;
  }
  
  public List<GpxWaypoint> points() { return points; }
  @Override public Iterator<GpxWaypoint> iterator() { return points.iterator(); }
  @Override public GpxWaypoint get(int index) { return points.get(index); }
  @Override public int size() { return points.size(); }
  @Override public int indexOf(GpxWaypoint object) { return points.indexOf(object); }
  
  private Color color;
  private static final ColorGenerator colorGenerator = new PleasantColorGenerator();
  
  public Pair<GpxTrackSegment, GpxTrackSegment> splitAtWaypoint(int index)
  {
    List<GpxWaypoint> list1 = new ArrayList<>(points.subList(0, index));
    List<GpxWaypoint> list2 = new ArrayList<>(points.subList(index, points.size()));
    
    Pair<GpxTrackSegment, GpxTrackSegment> pair = new Pair<>(
        new GpxTrackSegment(list1),
        new GpxTrackSegment(list2)
    );  
    
    pair.first.extensions = this.extensions;     
    return pair;
  }
  
  public Color color()
  { 
    if (color == null)
      color = colorGenerator.getColor();
    return color; 
  }
  
  public Stream<GpxWaypoint> stream() { return points().stream(); }
}
