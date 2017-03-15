package com.pixbits.lib.io.archive;

public class CompressorOptions
{
  public final ArchiveFormat format;
  public final boolean useSolidArchives;
  public final int compressionLevel;
  
  public CompressorOptions(ArchiveFormat format, boolean useSolidArchives, int compressionLevel)
  {
    this.format = format;
    this.useSolidArchives = useSolidArchives;
    this.compressionLevel = compressionLevel;
  }
}
