package com.pixbits.lib.ui.canvas;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import com.pixbits.lib.lang.Rect;

public class Gfx
{
  private final Graphics2D g;
  private int margin;
  private boolean useMargin;
  private Color color;
  private final int w, h;
  private Font font;
  
  public Gfx(Graphics g, boolean antialias)
  {
    this.g = (Graphics2D)g;
    this.useMargin = true;
    this.color = g.getColor();
    
    java.awt.Rectangle r = g.getClipBounds();
    this.w = r.width;
    this.h = r.height;
    
    if (antialias)
      this.g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    
    this.g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    
    this.font = g.getFont();
  }
  
  protected Graphics2D g() { return g; }
  
  public int x(float x) { return (int) (useMargin ? (x + margin) : x); }
  public int y(float y) { return (int) (useMargin ? (y + margin) : y); }
  
  void saveColor(Color nc) { g.setColor(nc); } 
  void restoreColor() { g.setColor(color); }

  public void fillRect(Rect r) { fillRect(r.x, r.y, r.w, r.h); }
  public void fillRect(float x, float y, float w, float h) { g.fillRect(x(x), y(y), (int)w, (int)h); }
  public void fillRect(float x1, float y1, float w, float h, int r, int g, int b) { fillRect(x1,y1,w,h,new Color(r,g,b)); }
  public void fillRect(float x, float y, float w, float h, Color c) 
  { 
    saveColor(c);
    fillRect(x, y, w, h);
    restoreColor();
  }
  
  public void rect(Rect r) { rect(r.x, r.y, r.w, r.h); }
  public void rect(float x, float y, float w, float h) { g.drawRect(x(x), y(y), (int)w, (int)h); }
  public void rect(float x1, float y1, float w, float h, int r, int g, int b) { rect(x1,y1,w,h,new Color(r,g,b)); }
  public void rect(float x, float y, float w, float h, Color c) 
  { 
    saveColor(c);
    rect(x, y, w, h);
    restoreColor();
  }
  
  public void fillOval(float x, float y, float w, float h) { g.fillOval(x(x), y(y), (int)w, (int)h); }
  public void fillOval(float x1, float y1, float w, float h, int r, int g, int b) { fillOval(x1,y1,w,h,new Color(r,g,b)); }
  public void fillOval(float x, float y, float w, float h, Color c) 
  { 
    saveColor(c);
    fillOval(x, y, w, h);
    restoreColor();
  }
  
  public void line(float x1, float y1, float x2, float y2) { g.drawLine(x(x1), y(y1), x(x2), y(y2)); }
  public void line(float x1, float y1, float x2, float y2, int r, int g, int b) { line(x1,y1,x2,y2,new Color(r,g,b)); }
  public void line(float x1, float y1, float x2, float y2, Color c)
  { 
    saveColor(c);
    g.drawLine(x(x1), y(y1), x(x2), y(y2));
    restoreColor();
  }
  
  public void image(Image img, float x, float y) { image(img, x, y, img.getWidth(null), img.getHeight(null)); }
  public void image(Image img, float x, float y, float w, float h)
  {
    g.drawImage(img, x(x), y(y), x(x+w), y(y+h), 0, 0, img.getWidth(null), img.getHeight(null), null);
  }
  
  public void clear(int r, int g, int b) { clear(new Color(r,g,b)); }
  public void clear(Color c)
  {
    saveColor(c);
    boolean tmp = isAbsolute();
    setAbsolute(true);
    fillRect(0,0,w,h,c);
    setAbsolute(tmp);
    restoreColor();
  }
  
  public void string(String string, float x, float y) { g.drawString(string, x(x), y(y)); }
  public void string(String string, float x, float y, int r, int g, int b) { string(string, x, y, new Color(r,g,b)); }
  public void string(String string, float x, float y, Color c)
  {
    saveColor(c);
    string(string,x,y);
    restoreColor();
  }
  
  public void stringCentered(String string, int x, int y, Color color)
  {
    FontMetrics metrics = this.g.getFontMetrics();
    int width = metrics.stringWidth(string);

    x -= width/2;
    y += metrics.getAscent()/2;
    
    string(string, x, y, color);
  }
  public void stringCentered(String string, int x, int y, int r, int g, int b) { stringCentered(string, x, y, new Color(r,g,b)); }


  public void setColor(Color c) { this.color = c; }
  public void setAbsolute(boolean absolute) { this.useMargin = !absolute; }
  public boolean isAbsolute() { return !useMargin; }
  public void setMargin(int margin) { this.margin = margin; }
  public int margin() { return margin; }
  
  public void setFont(Font font) { g.setFont(font); }
  public void restoreFont() { g.setFont(this.font); }
  
  public int fontHeight() { return g.getFontMetrics().getHeight(); }
  
  public Font font() { return font; }
  public Font font(float sizeDelta) { return font.deriveFont(font.getSize()+sizeDelta); }
  public Font font(float sizeDelta, int style) { return font.deriveFont(style, font.getSize()+sizeDelta); }
  
  public int w() { return w; }
  public int h() { return h; }
  
}
