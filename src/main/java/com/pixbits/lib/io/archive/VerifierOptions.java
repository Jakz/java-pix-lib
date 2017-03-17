package com.pixbits.lib.io.archive;

public class VerifierOptions
{
  public boolean matchSize;
  public boolean matchSHA1;
  public boolean matchMD5;
  public boolean checkNestedArchives;

  
  public VerifierOptions(boolean matchSize, boolean matchSHA1, boolean matchMD5, boolean checkNestedArchives)
  {
    this.matchSize = matchSize;
    this.matchMD5 = matchMD5;
    this.matchSHA1 = matchSHA1;
    this.checkNestedArchives = checkNestedArchives;
    
  }
  
  public VerifierOptions()
  {
    this(true, true, true, true);
  }
  
  public boolean verifyJustCRC() { return !(matchSHA1 || matchMD5); }
}
