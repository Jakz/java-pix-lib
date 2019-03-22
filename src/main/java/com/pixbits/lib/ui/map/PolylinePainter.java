package com.pixbits.lib.ui.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import com.pixbits.lib.io.xml.gpx.Coordinate;
import com.pixbits.lib.lang.Pair;
import com.pixbits.lib.ui.color.ColorGenerator;
import com.pixbits.lib.ui.color.ColorUtils;
import com.pixbits.lib.ui.color.PastelColorGenerator;
import com.pixbits.lib.ui.color.PleasantColorGenerator;
import com.pixbits.lib.ui.color.RandomColorGenerator;

import org.jxmapviewer.painter.Painter;

public class PolylinePainter implements MapPainter
{
  private final JXMap map;
  private boolean antiAlias = true;

  private final List<MapElement<PolylineElement>> tracks;


  public PolylinePainter(JXMap map)
  {
    this.map = map;
    this.tracks = new ArrayList<>();
  }

  ColorGenerator generator = new PleasantColorGenerator(0.99f, 0.99f);
  public PolylineElement add(List<Coordinate> points, Color color)
  {
    synchronized (tracks)
    {
      PolylineElement element = new PolylineElement(points, generator.getColor());
      element.setWidth(2);
      tracks.add(new MapElement<>(map, element));
      return element;
    }
  }

  @Override
  public void paint(Graphics2D g, JXMapViewer map, int w, int h)
  {
    g = (Graphics2D) g.create();

    // convert from viewport to world bitmap
    Rectangle rect = map.getViewportBounds();
    g.translate(-rect.x, -rect.y);

    if (antiAlias)
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    drawPolylines(g, map);

    g.dispose();
  }
  
  private void generatePolyline(Graphics2D g, JXMapViewer map, PolylineElement track)
  {
    Path2D path = new Path2D.Double();
    
    boolean first = true;
    for (int i = 0; i < track.track().size(); ++i)
    {
      Coordinate point = track.track().get(i);
      GeoPosition gpoint = new GeoPosition(point.lat(), point.lng());
      Point2D pt = map.getTileFactory().geoToPixel(gpoint, map.getZoom());
      
      //System.out.println(pt);
      
      if (first)
        path.moveTo(pt.getX(), pt.getY());
      else
        path.lineTo(pt.getX(), pt.getY());
      
      first = false;
    }
    
    track.setPath(path);
  }

  private void drawPolyLine(Graphics2D g, JXMapViewer map, PolylineElement track)
  {
    if (track.path() == null)
      generatePolyline(g, map,track);
    
    g.draw(track.path());
  }
  
  public void invalidate()
  {
    tracks.forEach(track -> track.element().setPath(null));
  }

  private void drawPolylines(Graphics2D g, JXMapViewer map)
  {
    synchronized (tracks)
    {
      for (MapElement<PolylineElement> element : tracks)
      {
        if (element.isVisible())
        {
          PolylineElement polyline = element.element();
          Color color = ColorUtils.withAlpha(polyline.color(), 220);
  
          g.setColor(color);
          g.setStroke(new BasicStroke(polyline.width(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
          drawPolyLine(g, map, polyline);
        }
  
      }
    }
  }
}