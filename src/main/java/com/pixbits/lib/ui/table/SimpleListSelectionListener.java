package com.pixbits.lib.ui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public abstract class SimpleListSelectionListener implements ListSelectionListener
{

  public SimpleListSelectionListener()
  {
    super();
  }

  protected abstract void commonActionBefore();
  protected abstract void commonActionAfter();
  
  protected abstract void multipleSelection(List<Integer> indices);
  protected abstract void singleSelection(int index);
  protected abstract void clearSelection();

  @Override
  public void valueChanged(ListSelectionEvent e)
  {
    if (!e.getValueIsAdjusting())
    {
      commonActionBefore();
      
      ListSelectionModel model = (ListSelectionModel)e.getSource();
      
      if (model.isSelectionEmpty())
        clearSelection();
      else if (model.getMinSelectionIndex() == model.getMaxSelectionIndex())
        singleSelection(model.getMinSelectionIndex());
      else
      {
        int min = model.getMinSelectionIndex();
        int max = model.getMaxSelectionIndex();
        
        List<Integer> indices = new ArrayList<>();
        
        for (int i = min; i <= max; ++i)
          if (model.isSelectedIndex(i))
            indices.add(i);
        
        multipleSelection(indices);
      }
      
      commonActionAfter();
    }
  }

}