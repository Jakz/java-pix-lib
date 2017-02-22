package com.pixbits.lib.ui.table;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.pixbits.lib.functional.TriConsumer;

public class ColumnSpec<T, V>
{
  final String name;
  final Class<V> type;
  BiFunction<Integer, T, V> getter;
  Optional<TriConsumer<Integer, T, V>> setter;
  Optional<TableCellRenderer> renderer;
  Optional<TableCellEditor> editor;
  boolean active;
  boolean editable;
  
  private TableModel<T> model;
  
  public ColumnSpec(String name, Class<V> type, BiFunction<Integer, T, V> getter, TriConsumer<Integer, T, V> setter)
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
  
  public ColumnSpec(String name, Class<V> type)
  {
    this(name, type, null, null);
  }

  public ColumnSpec(String name, Class<V> type, Function<T, V> getter)
  {
    this(name, type, (i,t) -> getter.apply(t), null);
  }
  
  public void setGetter(Function<T, V> getter) { this.getter = (i,t) -> getter.apply(t); }
  public void setGetter(BiFunction<Integer, T, V> getter) { this.getter = getter; }
  public void setSetter(BiConsumer<T, V> setter) { this.setter = Optional.of((i, t, v) -> setter.accept(t,v)); }
  public void setSetter(TriConsumer<Integer, T, V> setter) { this.setter = Optional.of(setter); }
  
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