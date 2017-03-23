package com.pixbits.lib.workflow;

import java.util.function.Supplier;

public interface Source<T extends WorkflowData> extends Supplier<T>
{
  
}