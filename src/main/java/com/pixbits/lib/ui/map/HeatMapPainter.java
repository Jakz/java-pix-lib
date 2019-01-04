package com.pixbits.lib.ui.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import com.pixbits.lib.io.xml.gpx.Coordinate;
import com.pixbits.lib.ui.color.ColorGenerator;
import com.pixbits.lib.ui.color.GradientColorGenerator;

public class HeatMapPainter implements MapPainter
{
  private class Zone
  {
    private int x, y;
    
    public Zone(int x, int y) { this.x = x; this.y = y; }
    public int x() { return x; }
    public int y() { return y; }
    
    public int hashCode() { return Long.hashCode(x << 32 | y); }
    public boolean equals(Object o) { return (o instanceof Zone) && ((Zone)o).x == x && ((Zone)o).y == y; }
  }
  
  private final double zoneWidth, zoneHeight;
  private final Map<Zone, Integer> heatMap;
  private int maxFrequency;
  
  public boolean enabled = true;
  
  private final Map<Zone, Rectangle2D> cache;
  
  private Zone zoneForCoordinate(Coordinate c)
  {
    int zoneX = (int)(c.lat() / zoneWidth);
    int zoneY = (int)(c.lng() / zoneHeight);
    
    Zone zone = new Zone(zoneX, zoneY);
    
    //System.out.printf("%2.4f, %2.4f -> zone(%d, %d) -> rect(%2.4f, %2.4f)\n", c.lat(), c.lng(), zone.x, zone.y, zone.x() * zoneWidth, zone.y() * zoneHeight);
    
    return zone;
  }
  
  public HeatMapPainter(double zoneWidth, double zoneHeight, boolean adjustedForSquare)
  {
    if (adjustedForSquare)
      zoneHeight *= 1.30;
    
    this.zoneWidth = zoneWidth;
    this.zoneHeight = zoneHeight;
    this.heatMap = new HashMap<>();
    this.cache = new HashMap<>();
    this.maxFrequency = 0;
  }
  
  public void invalidate()
  {
    cache.clear();
  }
  
  public void clear()
  {
    heatMap.clear();
    cache.clear();
  }
  
  public void addData(Iterable<Coordinate> coordinates)
  {
    synchronized (heatMap)
    {  
      for (Coordinate c : coordinates)
      {
        Zone zone = zoneForCoordinate(c);
        heatMap.merge(zone, 1, Integer::sum);
      }
      
      maxFrequency = Math.max(maxFrequency, heatMap.values().stream().mapToInt(i -> i).max().getAsInt());

      cache.clear();
    }
  }
  
  private void cacheData(Graphics2D g, JXMapViewer map)
  {
    synchronized (heatMap)
    {
      synchronized (cache)
      {
        Set<Zone> copy = new HashSet<>(heatMap.keySet());
        for (Zone zone : copy)
        {
          GeoPosition tl = new GeoPosition(zone.x() * zoneWidth, (zone.y()-1) * zoneHeight);
          GeoPosition br = new GeoPosition((zone.x()+1) * zoneWidth, (zone.y()) * zoneHeight);
          
          Point2D tlp = map.getTileFactory().geoToPixel(tl, map.getZoom());
          Point2D brp = map.getTileFactory().geoToPixel(br, map.getZoom());
          
          Rectangle2D.Double rect = new Rectangle2D.Double(tlp.getX(), tlp.getY(), brp.getX() - tlp.getX(), tlp.getY() - brp.getY());
          //System.out.println(rect);
          
          cache.put(zone, rect);
        }
      }
    }
  }
  
  GradientColorGenerator generator = new GradientColorGenerator(Color.RED, Color.YELLOW);

  @Override
  public void paint(Graphics2D g, JXMapViewer map, int width, int height)
  {
    if (enabled)
    {
      if (cache.isEmpty())
        cacheData(g, map);
      
      g = (Graphics2D) g.create();
  
      // convert from viewport to world bitmap
      Rectangle rect = map.getViewportBounds();
      g.translate(-rect.x, -rect.y);
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      
      //g.setColor(Color.RED);
      g.setPaint(Color.RED);
  
      synchronized (heatMap) {
      synchronized (cache)
      {
        for (Map.Entry<Zone, Rectangle2D> e : cache.entrySet())
        {
          float frequency = heatMap.get(e.getKey()).intValue() / ((float)maxFrequency+1);
          g.setColor(generator.getColor(frequency).toAWT());
          g.fill(e.getValue());
        };
      } }
      
      g.dispose();
    }
  }
}
