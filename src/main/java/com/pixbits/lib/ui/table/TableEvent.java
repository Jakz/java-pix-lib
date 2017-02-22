package com.pixbits.lib.ui.table;

public class TableEvent
{
   public static enum Type
   {
     COLUMN_HIDDEN,
     COLUMN_SHOWN,
     COLUMN_ADDED,
     DATA_SOURCE_CHANGED,
   };
   
   public final Type type;
   
   TableEvent(Type type)
   {
     this.type = type;
   }
}
