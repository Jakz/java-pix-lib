package com.pixbits.lib.io.archive;

import java.io.IOException;
import java.io.InputStream;

public interface Compressible
{
  String fileName();
  long size();
  InputStream getInputStream() throws IOException;
}
