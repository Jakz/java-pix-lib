package com.pixbits.lib.io.archive;

import com.pixbits.lib.io.archive.handles.Handle;

public interface VerifierEntry
{
  Handle getVerifierHandle();
  
  default int verifierEntryCount() { return 1; }
  default boolean isSingleVerifierEntry() { return true; }
  default VerifierEntry getVerifierEntry(int index) { return null; }
  default void preloadForVerification(boolean requiresRealAccess, int index) { }
  default void unloadAfterVerification() { }
}
