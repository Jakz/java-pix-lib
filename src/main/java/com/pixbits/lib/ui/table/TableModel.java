package com.pixbits.lib.ui.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableModel<T> extends AbstractTableModel
{  
  private final Map<Class<?>, TableCellRenderer> defaultRenderers;
  private final Map<Class<?>, TableCellEditor> defaultEditors;
  
  private List<ColumnSpec<T,?>> allColumns;
  private List<ColumnSpec<T,?>> columns;
  private Optional<DataSource<T>> data;
  
  final JTable table;
  final JScrollPane scrollPane;
  
  public TableModel(JTable table, DataSource<T> data)
  {
    this(table, null, data);
  }
  
  public TableModel(JTable table, JScrollPane scrollPane)
  {
    this(table, scrollPane, null);
  }
  
  public TableModel(JTable table, JScrollPane scrollPane, DataSource<T> data)
  {
    this.data = Optional.ofNullable(data);
    this.allColumns = new ArrayList<>();
    this.columns = new ArrayList<>();
    
    this.defaultRenderers = new HashMap<>();
    this.defaultEditors = new HashMap<>();
    
    this.table = table;
    this.scrollPane = scrollPane;
    table.setModel(this);
  }
  
  public void addColumn(ColumnSpec<T, ?> spec)
  {
    allColumns.add(spec);
    if (spec.isActive())
      columns.add(spec);
    
    notifyEvent(new TableEvent(TableEvent.Type.COLUMN_ADDED));
  }
  
  @Override
  public void fireTableStructureChanged()
  {
    super.fireTableStructureChanged();
    
    TableColumnModel model = table.getColumnModel();
    
    defaultRenderers.forEach((c, r) -> table.setDefaultRenderer(c, r));
    defaultEditors.forEach((c, e) -> table.setDefaultEditor(c, e));
    
    for (int i = 0; i < model.getColumnCount(); ++i)
    {
      ColumnSpec<T,?> spec = columns.get(i);
      TableColumn column = model.getColumn(i);
      
      spec.renderer.ifPresent(r -> column.setCellRenderer(r));
      spec.editor.ifPresent(e -> column.setCellEditor(e));
    }
  }
  
  private void rebuildVisibleColumns()
  {
    columns.clear();
    
    allColumns.stream()
      .filter(ColumnSpec::isActive)
      .forEach(columns::add);
  }
  
  void notifyEventIfNeeded(TableEvent event)
  {
    if (table.isShowing())
      notifyEvent(event);
  }
   
  void notifyEvent(TableEvent event)
  {
    switch (event.type)
    {
      case COLUMN_HIDDEN:
      case COLUMN_SHOWN:
      case COLUMN_ADDED:
      {
        rebuildVisibleColumns();
        this.fireTableStructureChanged();
        break;
      }
      case DATA_SOURCE_CHANGED:
      {
        this.fireTableDataChanged();
        break;
      }
    }
  }
  
  public void setData(DataSource<T> data)
  {
    this.data = Optional.of(data);
    notifyEventIfNeeded(new TableEvent(TableEvent.Type.DATA_SOURCE_CHANGED));
  }
  
  @Override public String getColumnName(int col) { return columns.get(col).name; }
  @Override public Class<?> getColumnClass(int col) { return columns.get(col).type; }
  
  @Override public int getRowCount() { return data.isPresent() ? data.get().size() : 0; }
  @Override public int getColumnCount() { return columns.size(); }

  @Override
  public Object getValueAt(int row, int col)
  {
    /*JViewport viewport = scrollPane.getViewport();
    Rectangle bounds = table.getCellRect(row, col, true);
    Rectangle position = viewport.getViewRect();

    if (!position.intersects(bounds))
      return null;*/
    
    Object value = columns.get(col).getter.apply(row, data.get().get(row));
    return value;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void setValueAt(Object value, int row, int col)
  {
    ((BiConsumer<T,Object>)columns.get(col).setter.get()).accept(data.get().get(row), value);
  }
}
