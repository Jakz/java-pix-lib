package com.pixbits.lib.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class UIUtils
{
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
}
