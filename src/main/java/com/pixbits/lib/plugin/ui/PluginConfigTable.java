package com.pixbits.lib.plugin.ui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.pixbits.lib.plugin.Plugin;
import com.pixbits.lib.plugin.PluginArgument;
import com.pixbits.lib.ui.table.editors.PathArgumentEditor;
import com.pixbits.lib.ui.table.renderers.AlternateColorTableCellRenderer;

public class PluginConfigTable extends JTable
{
  private List<Class<?>> types;
  private List<TableCellEditor> editors;
  private List<TableCellRenderer> renderers;
  private final PluginArgumentTableModel model;
  
  class PluginArgumentTableModel extends AbstractTableModel
  {
    private final String[] names = { "Name", "Value" };
    
    List<PluginArgument> arguments = new ArrayList<>();
    
    @Override public int getColumnCount() { return 2; }
    @Override public String getColumnName(int i) { return names[i]; }
    @Override public int getRowCount() { return arguments.size(); }
    @Override public boolean isCellEditable(int r, int c) { return c == 1; }
    
    @Override public Object getValueAt(int r, int c)
    {
      PluginArgument arg = arguments.get(r);
      return c == 0 ? arg.getName() : arg.get();
    }
    
    @Override public void setValueAt(Object v, int r, int c)
    {
      if (v != null)
      {
        PluginArgument arg = arguments.get(r);
        arg.set(v);
      }
    }
  }
  
  public PluginConfigTable()
  {
    super();
    editors = new ArrayList<>();
    renderers = new ArrayList<>();
    
    setDefaultRenderer(Boolean.class, new AlternateColorTableCellRenderer(getDefaultRenderer(Boolean.class)));
    
    model = new PluginArgumentTableModel();
    setModel(model);
  }
  
  @Override public TableCellEditor getCellEditor(int r, int c)
  {
    if (c == 0)
      return null;
    else
    {
      return editors.get(r);
    }
  }
  
  @Override public TableCellRenderer getCellRenderer(int r, int c)
  {
    if (c == 0)
      return this.getDefaultRenderer(String.class);
    else
      return renderers.get(r);
  }
  
  @Override public String getToolTipText(MouseEvent e)
  {
    int r = this.rowAtPoint(e.getPoint());
    if (r != -1)
      return model.arguments.get(r).getDescription();
    else
      return null;
  }
  
  public void prepare(Plugin plugin)
  {
    renderers.clear();
    editors.clear();
  
    if (plugin == null)
    {
      model.arguments.clear();
      setEnabled(false);
    }
    else
    {
      setEnabled(true);

      model.arguments = plugin.getArguments();
      
      types = model.arguments.stream().map( a -> a.getType() ).collect(Collectors.toList());
 
      model.arguments.stream().map( a -> {
        Class<?> t = a.getType();
        if (t.equals(Integer.class) || t.equals(Integer.TYPE))
          return new PluginArgumentEditor(t, this.getDefaultEditor(Integer.class));
        else if (t.equals(Boolean.class) || t.equals(Boolean.TYPE))
          return new PluginArgumentEditor(t, this.getDefaultEditor(Boolean.class));
        else if (t.equals(java.nio.file.Path.class))
        {
          int type = JFileChooser.FILES_AND_DIRECTORIES;
          
          String params = a.getParams();
          
          if (params.equals("directories"))
            type = JFileChooser.DIRECTORIES_ONLY;
          else if (params.equals("files"))
            type = JFileChooser.FILES_ONLY;
          
          return new PathArgumentEditor(type);
        }
        else
          return this.getDefaultEditor(Object.class);
      }).forEach(editors::add);
            
      types.stream().map( t -> {
        if (t.equals(Integer.class) || t.equals(Integer.TYPE))
          return getDefaultRenderer(Integer.class);
        else if (t.equals(Boolean.class) || t.equals(Boolean.TYPE))
          return getDefaultRenderer(Boolean.class);
        else
          return getDefaultRenderer(t);
      }).forEach(renderers::add);
      
      model.fireTableDataChanged();
    }
  }
}