package com.pixbits.lib.io.archive;

import com.pixbits.lib.io.archive.handles.Handle;

public class VerifierResult<T extends Verifiable>
{
  public final T element;
  public final Handle handle;
  
  public VerifierResult(T element, Handle handle)
  {
    this.element = element;
    this.handle = handle;
  }
}
