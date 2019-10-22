package com.pixbits.lib.ui.map;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.util.List;

import com.pixbits.lib.io.xml.gpx.Coordinate;

public class PolylineElement implements Positionable
{
  private List<Coordinate> track;
  
  private int width;
  private Color color;
  private Path2D path;
  
  
  PolylineElement(List<Coordinate> track, Color color)
  {
    this.track = track;
    this.color = color;
    this.width = 2;
  }
  
  public List<Coordinate> track() { return track; }
  public Path2D path() { return path; }
  public Color color() { return color; }
  public int width() { return width; }
  
  public void setPath(Path2D path) { this.path = path; }
  public void setWidth(int width) { this.width = width; }

  @Override
  public Iterable<Coordinate> coordinates() { return track; }
}