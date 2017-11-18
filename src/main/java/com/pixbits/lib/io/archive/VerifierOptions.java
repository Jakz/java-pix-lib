package com.pixbits.lib.io.archive;

import java.util.function.Function;

public class VerifierOptions
{
  public final boolean matchSize;
  public final boolean matchSHA1;
  public final boolean matchMD5;
  public final boolean checkNestedArchives;
  public Function<VerifierEntry, VerifierEntry> transformer;

  
  public VerifierOptions(boolean matchSize, boolean matchSHA1, boolean matchMD5, boolean checkNestedArchives)
  {
    this.matchSize = matchSize;
    this.matchMD5 = matchMD5;
    this.matchSHA1 = matchSHA1;
    this.checkNestedArchives = checkNestedArchives;
    this.transformer = null;
  }
  
  public VerifierOptions()
  {
    this(true, false, false, true);
  }
  
  public boolean verifyJustCRC() { return !(matchSHA1 || matchMD5); }
}
