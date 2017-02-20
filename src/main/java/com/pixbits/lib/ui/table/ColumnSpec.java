package com.pixbits.lib.ui.table;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ColumnSpec<T, V>
{
  final String name;
  final Class<V> type;
  final Function<T, V> getter;
  final Optional<BiConsumer<T, V>> setter;
  Optional<TableCellRenderer> renderer;
  Optional<TableCellEditor> editor;
  boolean active;
  boolean editable;
  
  private TableModel<T> model;

  public ColumnSpec(String name, Class<V> type, Function<T, V> getter)
  {
    this(name, type, getter, null);
  }
  
  public ColumnSpec(String name, Class<V> type, Function<T, V> getter, BiConsumer<T, V> setter)
  {
    this.name = name;
    this.type = type;
    this.getter = getter;
    this.setter = setter != null ? Optional.of(setter) : Optional.empty();
    this.active = true;
    this.editable = false;
    this.renderer = Optional.empty();
    this.editor = Optional.empty();
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
  
  public void setRenderer(TableCellRenderer renderer)
  {
    this.renderer = Optional.of(renderer);
  }
}