package com.pixbits.lib.ui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TableModel<T> extends AbstractTableModel
{
  private List<ColumnSpec<T,?>> allColumns;
  private List<ColumnSpec<T,?>> columns;
  private TableDataSource<T> data;
  
  TableModel(TableDataSource<T> data)
  {
    this.data = data;
    this.allColumns = new ArrayList<>();
    this.columns = new ArrayList<>();
  }
  
  private void rebuildVisibleColumns()
  {
    columns.clear();
    
    allColumns.stream()
      .filter(ColumnSpec::isActive)
      .forEach(columns::add);
  }
  
  private void rebuildRenderersAndEditors()
  {
    
  }
   
  void notifyEvent(TableEvent event)
  {
    switch (event.type)
    {
      case COLUMN_HIDDEN:
      case COLUMN_SHOWN:
      {
        rebuildVisibleColumns();
        this.fireTableStructureChanged();
        break;
      }
    }
  }
  
  @Override public String getColumnName(int col) { return columns.get(col).name; }
  @Override public Class<?> getColumnClass(int col) { return columns.get(col).type; }
  
  @Override public int getRowCount() { return data.size(); }
  @Override public int getColumnCount() { return columns.size(); }

  @Override
  public Object getValueAt(int row, int col)
  {
    return columns.get(col).getter.get(data.get(row));
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void setValueAt(Object value, int row, int col)
  {
    ((TableDataSetter<T,Object>)columns.get(col).setter).set(data.get(row), value);
  }
}
