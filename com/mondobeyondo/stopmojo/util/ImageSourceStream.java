/*
 * Created on Feb 19, 2005
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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullBufferStream;


/**
 * @author derry
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ImageSourceStream implements PullBufferStream 
{
	@SuppressWarnings("unused")
 	private Vector<BufferedImage> 
	  m_images;
 	
  private int 
		m_curFrame;
//	  m_nextImage = 0;	// index of the next image to be read.
  
  private VideoFormat 
	  m_format;

  private boolean 
	  m_ended = false;
  
  private ImageFrameSource
	  m_source;
  
 // private static final boolean
//	  s_intArray = true;
  
  private int
	  m_data[];
  
 // private float
//	  m_frameRate;
  
  private long
	  m_frameDuration;

  public ImageSourceStream(float frameRate, ImageFrameSource source)               
  {
  	m_source = source;
  	m_curFrame = 0;
  //	m_frameRate = frameRate;
  	BufferedImage img = source.getImage(0);
   	m_format = new RGBFormat(new Dimension(img.getWidth(), img.getHeight()), img.getWidth() * img.getHeight(), Format.intArray, frameRate, 24, 0x00ff0000, 0x0000ff00, 0x000000ff, 1, img.getWidth(), Format.FALSE, Format.NOT_SPECIFIED);
   	m_data = new int[img.getWidth() * img.getHeight()];
   	m_frameDuration = (long)(((long)source.getCount() * 1000000) / (long)frameRate);
  }

  public boolean willReadBlock() 
  {
    return false;
  }

 	public void read(Buffer buf) throws IOException 
	{
 		if(m_curFrame < 0 || m_curFrame >= m_source.getCount())
 		{
//  		System.err.println("Done reading all images.");
  		buf.setEOM(true);
  		buf.setOffset(0);
  		buf.setLength(0);
  		m_curFrame = 0;
//  		m_ended = true;
  		return;
  	}

 		buf.setSequenceNumber(m_curFrame);
 		buf.setTimeStamp(m_curFrame * m_frameDuration);
 		buf.setDuration(m_frameDuration);
 		BufferedImage img = m_source.getImage(m_curFrame++);
 		buf.setData(img.getRGB(0, 0, img.getWidth(), img.getHeight(), m_data, 0, img.getWidth()));
 		buf.setLength(m_data.length);
 		buf.setOffset(0);
 		buf.setFlags(buf.getFlags() | Buffer.FLAG_KEY_FRAME);
 		buf.setFormat(m_format);
 	}

 	public Format getFormat() 
 	{
    return m_format;
 	}

  public ContentDescriptor getContentDescriptor() 
  {
    return new ContentDescriptor(ContentDescriptor.RAW);
  }

  public long getContentLength() 
  {
    return 0;
 	}

 	public boolean endOfStream() 
 	{
    return m_ended;
  }

  public Object[] getControls() 
  {
    return new Object[0];
  }

  public Object getControl(String type) 
  {
    return null;
  }
  
  public int getCurFrame()
  {
  	return m_curFrame;
  }
  
  public int setCurFrame(int frame)
  {
//System.out.println("Setting frame to " + frame);  	
  	if(frame >= 0 && frame < m_source.getCount())
      m_curFrame = frame;
  	
  	m_ended = false;
  	return m_curFrame;
  }
}
