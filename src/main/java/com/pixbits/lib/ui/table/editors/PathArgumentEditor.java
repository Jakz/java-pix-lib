package com.pixbits.lib.ui.table.editors;

import java.awt.Component;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import com.pixbits.lib.ui.elements.ComponentBorder;

public class PathArgumentEditor implements TableCellEditor
{
  private final DefaultCellEditor inner;
  private final JTextField field;
  private final JButton browse;
  private final ComponentBorder cb;
  
  private JTable table;
  private int row;
  private Path startPath;
    
  public PathArgumentEditor(int type)
  {
    inner = new DefaultCellEditor(new JTextField());
    field = (JTextField)inner.getComponent();
    field.setBorder(null);
    browse = new JButton("...");
    
    cb = new ComponentBorder(browse);
    cb.setEdge(ComponentBorder.Edge.RIGHT);
    cb.setAdjustInsets(false);
    cb.install(field);
    
    browse.addActionListener( e -> {
      final JFileChooser jfc = new JFileChooser();
      jfc.setFileSelectionMode(type);
      
      if (startPath != null)
        jfc.setCurrentDirectory(startPath.toFile());

      int response = jfc.showOpenDialog(table);
      
      if (response == JFileChooser.APPROVE_OPTION)
      {
        File f = jfc.getSelectedFile();
        Path path = f.toPath();
        table.getModel().setValueAt(path, row, 1);
        cancelCellEditing();
      }
      
    });
  }

  @Override public boolean isCellEditable(EventObject e) { return inner.isCellEditable(e); }
  @Override public boolean shouldSelectCell(EventObject e) { return inner.shouldSelectCell(e); }
  @Override public boolean stopCellEditing() { return inner.stopCellEditing(); }
  @Override public void cancelCellEditing() { inner.cancelCellEditing(); }
  @Override public void addCellEditorListener(CellEditorListener l) { inner.addCellEditorListener(l); }
  @Override public void removeCellEditorListener(CellEditorListener l) { inner.removeCellEditorListener(l); }

  @Override public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
  {
    this.table = table;
    this.row = row;
    
    Path path = (Path)value;
    
    if (path != null && !Files.isDirectory(path))
      path = path.getParent();
    
    startPath = path != null && Files.exists(path) ? path : null;

    return inner.getTableCellEditorComponent(table, value, isSelected, row, column);
  }
  
  @Override
  public Object getCellEditorValue()
  {
    Object value = inner.getCellEditorValue();
    
    try
    {
      Path path = Paths.get((String)value);
      return path;
    }
    catch (InvalidPathException e)
    {
      return null;
    }  
  }
}
