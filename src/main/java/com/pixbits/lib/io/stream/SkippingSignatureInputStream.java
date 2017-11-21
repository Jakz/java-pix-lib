package com.pixbits.lib.io.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class SkippingSignatureInputStream extends InputStream
{
  private final int signatureOffset;
  private final byte[] signature;
  private final int totalSignatureLength;
  
  private final int bytesToSkip;
  
  private final byte[] buffer;
  
  private final InputStream is;
  
  private Status status;
  private int read;
  
  private enum Status
  {
    JUST_OPENED,
    READY,
    INVALID,
  };
  
  public SkippingSignatureInputStream(InputStream is, final byte[] signature, final int signatureOffset, final int bytesToSkip)
  {
    this.signatureOffset = signatureOffset;
    this.signature = signature;
    this.bytesToSkip = bytesToSkip;
    this.totalSignatureLength = signature.length + signatureOffset;
    this.is = is;
    
    this.buffer = new byte[signature.length + signatureOffset];
    
    this.status = Status.JUST_OPENED;
    this.read = 0;
  }

  public int read(byte[] buffer, int o, int l) throws IOException
  {
    if (status == Status.READY)
      return super.read(buffer, o, l);
    else
    {
      int i = 0;
      for (i = 0; i < l; ++i)
      {
        int c = read();
        
        if (c == -1 && i == 0)
          return -1;
        else if (c == -1)
          break;
        else
        {
          buffer[o + i] = (byte)c;
        }
      }
      
      return i;
    }
  }
  
  int counter = 0;
  
  public int read() throws IOException
  {
    switch (this.status)
    {
      case READY:
        int c = is.read();
        //System.out.printf("SKIPPING read %d bytes (%d) (READY)\n", counter++, c);
        return c;
        
      case JUST_OPENED:
      {
        /* read enough bytes to be able to check magic number */
        while ((read += is.read(buffer, read, totalSignatureLength - read)) < totalSignatureLength);
        
        //System.out.printf("SKIPPING read %d bytes (signature length: %d, offset: %d)\n", read, signature.length, signatureOffset);
               
        /* magic number is correct, then we apply skip */
        boolean valid = false;
        
        if (signatureOffset == 0)
          valid = Arrays.equals(signature, buffer);
        else
          valid = Arrays.equals(signature, Arrays.copyOfRange(buffer, signatureOffset, signatureOffset + signature.length));
        
        /* if check is valid then we should 
         * skip all the remaining bytes from header 
         * and return the next one
         */
        if (valid)
        {
          while (read < bytesToSkip)
          {
            is.read();
            ++read;
          }
          
          status = Status.READY;
          return is.read();
        }
        else
        {
          /* otherwise we should return the 
           * buffer and then keep with the stream 
           */
          
          //System.out.println("SKIPPING Signature invalid");
          
          status = Status.INVALID;
          int value = buffer[totalSignatureLength - read] & 0xFF;
          --read;
          
          //System.out.printf("SKIPPING read %d bytes (%d) (JUST_OPENED)\n", counter++, value);

          return value;
        }
      }
      case INVALID:
      {
        int value = buffer[totalSignatureLength - read] & 0xFF;
        --read;
        if (read == 0)
          status = Status.READY;
        //System.out.printf("SKIPPING read %d bytes (%d) (INVALID)\n", counter++, value);

        return value;
      }
      default: return -1;
    } 
  }
}
