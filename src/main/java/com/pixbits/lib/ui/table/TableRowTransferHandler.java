package com.pixbits.lib.ui.table;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

public class TableRowTransferHandler<T> extends TransferHandler 
{
  private final TableModel<T> model;
  
  private final DataFlavor localObjectFlavor = new DataFlavor(Integer.class, "Integer Row Index");
  private final JTable table;

  public TableRowTransferHandler(TableModel<T> model) 
  {
    this.model = model;
    this.table = model.table;
  }

  @Override
  protected Transferable createTransferable(JComponent c) 
  {
    assert (c == table);
    return new DataHandler(Integer.valueOf(table.getSelectedRow()), localObjectFlavor.getMimeType());
  }

  @Override
  public boolean canImport(TransferHandler.TransferSupport info) 
  {
    boolean b = info.getComponent() == table && info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
    table.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
    return b;
  }

  @Override
  public int getSourceActions(JComponent c) 
  {
    return TransferHandler.COPY_OR_MOVE;
  }

  @Override
  public boolean importData(TransferHandler.TransferSupport info) 
  {
    JTable target = (JTable) info.getComponent();
    JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
    
    int index = dl.getRow();
    int max = table.getModel().getRowCount();
    
    if (index < 0 || index > max)
      index = max;
    
    target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    try 
    {
      Integer rowFrom = (Integer) info.getTransferable().getTransferData(localObjectFlavor);
      
      if (rowFrom != -1 && rowFrom != index) 
      {
        int[] rows = table.getSelectedRows();
        int dist = 0;
        for (int row : rows) {
          if (index > row) {
            dist++;
          }
        }
        index -= dist;

        ModifiableDataSource<T> data = (ModifiableDataSource<T>)model.data();
        JTable table = model.table;
        List<Integer> indices = Arrays.stream(model.table.getSelectedRows())
          .map(table::convertRowIndexToModel)
          .mapToObj(Integer::valueOf)
          .collect(Collectors.toList());
        
        List<T> values = indices.stream()
          .map(model.data()::get)
          .collect(Collectors.toList());
        
        indices.stream().forEach(data::remove);
        
        int cindex = index;
        for (T value : values)
          data.add(cindex++, value);
        
        table.getSelectionModel().setSelectionInterval(index, cindex-1);

        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  protected void exportDone(JComponent c, Transferable t, int act)
  {
    if (act == TransferHandler.MOVE)
    {
      table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }
}