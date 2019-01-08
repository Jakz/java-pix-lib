package com.pixbits.lib.ui.map;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import com.pixbits.lib.io.xml.gpx.Bounds;
import com.pixbits.lib.io.xml.gpx.Coordinate;

public class MapPanel extends JPanel implements JXMap
{
  JXMapViewer viewer;
  TileFactoryInfo info;

  private final List<MapPainter> painters;
  private CompoundPainter<JXMapViewer> compoundPainter;
  
  private final MouseListener mouseListener;
  
  public MapPanel(int width, int height)
  {
    viewer = new JXMapViewer();
    viewer.setPreferredSize(new Dimension(width, height));
    
    info = new OSMTileFactoryInfo();
    DefaultTileFactory tileFactory = new DefaultTileFactory(info);
    viewer.setTileFactory(tileFactory);

    painters = new ArrayList<>();
    compoundPainter = new CompoundPainter<>();
    viewer.setOverlayPainter(compoundPainter);

    mouseListener = new MouseListener();
    addMouseListener(mouseListener);
    addMouseWheelListener(mouseListener);
    addMouseMotionListener(mouseListener);
    
    setLayout(new BorderLayout());
    add(viewer, BorderLayout.CENTER);
  }
  
  public void addPainter(MapPainter painter)
  {
    painters.add(painter);
    compoundPainter.addPainter(painter);
  }
  
  public void removePainter(MapPainter painter)
  {
    painters.remove(painter);
    compoundPainter.removePainter(painter);
  }
  
  public JXMapViewer viewer() { return viewer; }

  @Override
  public void zoomToFit(Bounds bounds, float maxFraction)
  {
    viewer.zoomToBestFit(
    Arrays.asList(new Coordinate[] { bounds.ne(), bounds.sw() })
    .stream()
    .map(c -> new GeoPosition(c.lat(), c.lng()))
    .collect(Collectors.toSet()
        ), maxFraction);
    
    painters.forEach(MapPainter::invalidate);
  }
  
  
  private class MouseListener extends MouseAdapter
  {
    Point2D.Float base;
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
      int dx = e.getWheelRotation();
      int z = viewer.getZoom();
      int min = info.getMinimumZoomLevel();
      int max = info.getMaximumZoomLevel();
      
      if (dx < 0)
      {
        viewer.setZoom(Math.max(min, z + dx));
        Rectangle bounds = viewer.getViewportBounds();
        viewer.setCenter(new Point2D.Float(bounds.x + e.getX(), bounds.y + e.getY()));
        painters.forEach(MapPainter::invalidate);

      }
      else if (dx > 0)
      {
        viewer.setZoom(Math.min(max, z + dx));
        painters.forEach(MapPainter::invalidate);
      }
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
      if (base != null)
      {
        double dx = e.getX() - base.getX();
        double dy = e.getY() - base.getY();
        
        Point2D center = viewer.getCenter();
        viewer.setCenter(new Point2D.Double(center.getX() - dx, center.getY() - dy));
      }
      
      base = new Point2D.Float(e.getX(), e.getY());
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
      base = null;
    }
  }
}