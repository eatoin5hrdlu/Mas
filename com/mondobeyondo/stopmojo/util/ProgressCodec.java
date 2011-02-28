/*
 * Created on Mar 27, 2005
 *
 * Copyright (c) 2005 Derone Bryson
 *
 * This program is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation; either version 2, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this software; see the file COPYING. If not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * As a special exception, Derone Bryson and the StopMojo Project gives 
 * permission for additional uses of the text contained in its release of 
 * StopMojo.
 *
 * The exception is that, Derone Bryson and the the StopMojo Project hereby 
 * grants permission for non-GPL compatible modules (jar files, libraries, 
 * codecs, etc.) to be used and distributed together with StopMojo. This 
 * permission is above and beyond the permissions granted by the GPL license 
 * StopMojo is covered by.
 *
 * This exception does not however invalidate any other reasons why the 
 * executable file might be covered by the GNU General Public License.
 *
 * This exception applies only to the code released by Derone Bryson and/or the
 * StopMojo Project under the name StopMojo. If you copy code from other Free 
 * Software Foundation releases into a copy of StopMojo, as the General Public 
 * License permits, the exception does not apply to the code that you add in 
 * this way. To avoid misleading anyone as to the status of such modified files, 
 * you must delete this exception notice from them.
 *
 * If you write modifications of your own for StopMojo, it is your choice 
 * whether to permit this exception to apply to your modifications. If you do 
 * not wish that, delete this exception notice.  
 */
package com.mondobeyondo.stopmojo.util;

import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.format.VideoFormat;
import javax.swing.ProgressMonitor;

/**
 * @author derry
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ProgressCodec implements Codec 
{
	private ProgressMonitor
		m_monitor;
	
	private int
	  m_curFrame = 0;
	
	public ProgressCodec(ProgressMonitor monitor)
	{
		m_monitor = monitor;
	}
	
  /**
   * Callback to access individual video frames.
   */
  void accessFrame(Buffer frame) 
  {
  	m_curFrame++;
  	m_monitor.setNote("Processing frame " + m_curFrame);
  	m_monitor.setProgress(m_curFrame);
  	
    // For demo, we'll just print out the frame #, time &
    // data length.

//    long t = (long)(frame.getTimeStamp()/10000000f);

//    System.err.println("Pre: frame #: " + frame.getSequenceNumber() + 
//    ", time: " + ((float)t)/100f + 
//	  ", len: " + frame.getLength());
  }

  /**
	 * The code for a pass through codec.
   */

  // We'll advertize as supporting all video formats.
  protected Format supportedIns[] = new Format [] 
	{
    new VideoFormat(null)
  };

  // We'll advertize as supporting all video formats.
  protected Format supportedOuts[] = new Format [] 
	{
    new VideoFormat(null)
  };

  Format input = null, output = null;

  public String getName() 
  {
    return "Progress Codec";
  }

  // No op.
  public void open() 
  {
  }

  // No op.
  public void close() 
  {
  }

  // No op.
  public void reset() 
  {
  }

  public Format [] getSupportedInputFormats() 
  {
    return supportedIns;
  }

  public Format [] getSupportedOutputFormats(Format in) 
  {
    if (in == null)
	    return supportedOuts;
    else 
    {
	    // If an input format is given, we use that input format
	    // as the output since we are not modifying the bit stream
	    // at all.
	    Format outs[] = new Format[1];
	    outs[0] = in;
	    return outs;
    }
  }

  public Format setInputFormat(Format format) 
  {
    input = format;
    return input;
  }

  public Format setOutputFormat(Format format) 
  {
    output = format;
    return output;
  }

  public int process(Buffer in, Buffer out) 
  {
    // This is the "Callback" to access individual frames.
    accessFrame(in);

    // Swap the data between the input & output.
    Object data = in.getData();
    in.setData(out.getData());
    out.setData(data);

    // Copy the input attributes to the output
    out.setFormat(in.getFormat());
    out.setLength(in.getLength());
    out.setOffset(in.getOffset());

    return BUFFER_PROCESSED_OK;
  }

  public Object[] getControls() 
  {
    return new Object[0];
  }

  public Object getControl(String type) 
  {
    return null;
  }
}

