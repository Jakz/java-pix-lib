package com.pixbits.lib.ui.table;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ColumnSpec<T, V>
{
  final String name;
  final Class<V> type;
  final TableDataGetter<T, V> getter;
  TableDataSetter<T, V> setter;
  TableCellRenderer renderer;
  TableCellEditor editor;
  boolean active;
  boolean editable;
  
  private TableModel<T> model;
  
  ColumnSpec(String name, Class<V> type, TableDataGetter<T, V> getter, TableDataSetter<T, V> setter)
  {
    this.name = name;
    this.type = type;
    this.getter = getter;
    this.setter = setter;
    this.active = true;
    this.editable = false;
  }
  
  void setModel(TableModel<T> model)
  {
    this.model = model;
  }

  boolean isEditable() { return editable && setter != null; }
  void setEditable(boolean editable) { this.editable = editable; }
  
  boolean isActive() { return active; }
  
  public void hide()
  {
    if (active)
    {
      active = false;
      model.notifyEvent(new TableEvent(TableEvent.Type.COLUMN_HIDDEN));
    }
  }
  
  public void show()
  {
    if (!active)
    {
      active = true;
      model.notifyEvent(new TableEvent(TableEvent.Type.COLUMN_SHOWN));
    }
  }
}