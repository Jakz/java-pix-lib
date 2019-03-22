package com.pixbits.lib.io.xml.gpx;

public class Bounds
{
  private Coordinate ne;
  private Coordinate sw;
  
  public Bounds()
  {
    
  }
  
  public Bounds(Coordinate... coords)
  {
    ne = coords[0];
    sw = coords[0];
    
    for (int i = 1; i < coords.length; ++i)
      updateBound(coords[i]);
  }
  
  public Bounds(Coordinate coord)
  {
    ne = coord;
    sw = coord;
  }
  
  public Bounds(Iterable<Coordinate> coords)
  {
    coords.forEach(c -> updateBound(c));
  }
 
  public void updateBound(Iterable<Coordinate> coords)
  {
    coords.forEach(c -> updateBound(c));
  }
  
  public void updateBound(Bounds bounds)
  {
    updateBound(bounds.sw());
    updateBound(bounds.ne());
  }
  
  public Bounds merge(Bounds other)
  {
    Bounds bounds = new Bounds();
    bounds.updateBound(ne);
    bounds.updateBound(sw);
    bounds.updateBound(other.ne);
    bounds.updateBound(other.sw);
    return bounds;
  }
  
  public void updateBound(Coordinate coord)
  {
    if (ne == null)
    {
      ne = new Coordinate(coord);
      sw = new Coordinate(coord);
      return;
    }
    
    double lat = coord.lat(), lng = coord.lng();
    
    sw.setLat(Math.min(sw.lat(), lat));
    sw.setLng(Math.min(sw.lng(), lng));
    
    ne.setLat(Math.max(ne.lat(), lat));
    ne.setLng(Math.max(ne.lng(), lng));
  }
  
  public Coordinate ne() { return ne; }
  public Coordinate sw() { return sw; }
}
