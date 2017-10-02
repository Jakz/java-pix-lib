package com.pixbits.lib.io.stream;

import java.io.OutputStream;

public class NullOutputStream extends OutputStream
{
  @Override
  public void write(int b)
  {
    // do nothing
  }

  @Override
  public void write(byte[] b, int off, int len)
  {
    // do nothing
  }
}
