package com.pixbits.lib.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.pixbits.lib.lang.Size;

public class UIUtils
{
  public static enum OperatingSystem
  { 
    WIN, 
    OSX, 
    LINUX 
    
    ;
    
    public boolean isWindows() { return this == WIN; }
    
    public Path convertPathIfNeeded(Path path)
    {
      if (this != OperatingSystem.WIN)
        return Paths.get(path.toString().replaceAll("\\\\", "/"));
      else
        return Paths.get(path.toString().replaceAll("\\/", "\\"));
    }
    
    public static OperatingSystem get() { return getOperatingSystem(); }
  }
  
  private static OperatingSystem operatingSystem = null;
  public static OperatingSystem getOperatingSystem()
  {
    if (operatingSystem == null)
    {
      String system = java.lang.System.getProperty("os.name").toLowerCase();
      
      if (system.indexOf("win") >= 0)
        operatingSystem = OperatingSystem.WIN;
      else if (system.indexOf("mac") >= 0)
        operatingSystem = OperatingSystem.OSX;
      else
        operatingSystem = OperatingSystem.LINUX;
    }
    
    return operatingSystem;
  }
  
  public static void resizeTableColumn(TableColumn column, int width)
  {
    column.setMinWidth(width);
    column.setMaxWidth(width);
    column.setPreferredWidth(width);
  }
  
  public static void packTableColumn(JTable table, TableColumn column, int spacing)
  {
    int width = 0;
    for (int i = 0; i < table.getRowCount(); ++i)
    {
      TableCellRenderer renderer = table.getCellRenderer(i, column.getModelIndex());
      Component component = table.prepareRenderer(renderer, i, column.getModelIndex());
      width = Math.max(component.getPreferredSize().width, width);
    }
    
    TableCellRenderer headerRenderer = column.getHeaderRenderer();
    if (headerRenderer == null) table.getTableHeader().getDefaultRenderer();
    
    if (headerRenderer != null)
    {
      Component headerComponent = headerRenderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1, column.getModelIndex());
      width = Math.max(headerComponent.getPreferredSize().width, width);
    }

    resizeTableColumn(column, width+spacing);
  }
  
  public static <T extends JPanel> WrapperFrame<T> buildFrame(T panel, String title)
  {
    WrapperFrame<T> frame = new WrapperFrame<T>(panel);
    frame.setTitle(title);
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.pack();
    
    return frame;
  }
  
  public static <T extends Component> JPanel buildFillPanel(T component, Size.Int size)
  {
    JPanel panel = new JPanel(new BorderLayout());
    JScrollPane inner = new JScrollPane(component);
    inner.setPreferredSize(new Dimension(size.w, size.h));
    panel.add(inner, BorderLayout.CENTER);
    return panel;
  }
  
  public static <T extends Component> JPanel buildFillPanel(T component, boolean scrollPane)
  {
    JPanel panel = new JPanel(new BorderLayout());
    Component inner = scrollPane ? new JScrollPane(component) : component;
    panel.add(inner, BorderLayout.CENTER);
    return panel;
  }
  
  public static void setNimbusLNF()
  {
    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
          if ("Nimbus".equals(info.getName())) {
            
            UIManager.setLookAndFeel(info.getClassName());
            break;
          }
      }
    } catch (Exception e) {
      e.printStackTrace();
        // If Nimbus is not available, you can set the GUI to another look and feel.
    }
  }
  
  public static void enableAntiAliasing(Graphics g)
  {
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); 
  }
  
  public static void showErrorDialog(Container parent, String title, String text)
  {
    JOptionPane.showMessageDialog(
        parent,
        text,
        title,
        JOptionPane.ERROR_MESSAGE
    );
  }
  
  public static boolean showConfirmDialog(Container parent, String title, String text)
  {
    int result = JOptionPane.showConfirmDialog(
        parent,
        text,
        title,
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );
    return result == JOptionPane.YES_OPTION;
  }
}
