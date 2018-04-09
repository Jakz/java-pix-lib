package com.pixbits.lib.ui.table;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

public class ColumnVisibilityMenu extends JPopupMenu
{
  private final Map<JCheckBoxMenuItem, ColumnSpec<?, ?>> mapping;
  
  public ColumnVisibilityMenu(ColumnSpec<?, ?>... specs)
  {
    mapping = new HashMap<>();
    
    ActionListener listener = e -> {
      JCheckBoxMenuItem src = (JCheckBoxMenuItem)e.getSource();
      ColumnSpec<?, ?> column = mapping.get(src);
      
      if (column != null)
      {
        if (src.isSelected())
          column.show();
        else
          column.hide();
      }     
    };    
    
    for (ColumnSpec<?, ?> spec : specs)
    {
      JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(spec.name);
      mapping.put(menuItem, spec);
      menuItem.setSelected(true);
      menuItem.addActionListener(listener);
      this.add(menuItem);
    }
  }
}
