/*
 * Created on Feb 19, 2005
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

import javax.media.MediaLocator;
import javax.media.Time;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullBufferDataSource;
import javax.media.protocol.PullBufferStream;

/**
 * @author derry
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ImageDataSource extends PullBufferDataSource 
{
	private double
	  m_frameRate;
	
	ImageFrameSource 
	  m_source;
	
  private ImageSourceStream 
	  m_streams[];

  public ImageDataSource(float frameRate, ImageFrameSource source) 
  {
  	m_frameRate = frameRate;
  	m_source = source;
  	
    m_streams = new ImageSourceStream[1];
    m_streams[0] = new ImageSourceStream(frameRate, source);
  }

  public void setLocator(MediaLocator source) 
  {
  } 

  public MediaLocator getLocator() 
  {
    return null;
  }

  public String getContentType() 
  {
    return ContentDescriptor.RAW;
  }

  public void connect() 
  {
    m_streams[0].setCurFrame(0);
  }

  public void disconnect() 
  {
  }

  public void start() 
  {
//    m_streams[0].setCurFrame(0);
  }

  public void stop() 
  {
  }

  public PullBufferStream[] getStreams() 
  {
    return m_streams;
  }

  public Time getDuration() 
  {
//    return DURATION_UNKNOWN;
  	return new Time((long)(m_source.getCount() * 1000000 / m_frameRate));
  }

  public Object[] getControls() 
  {
  	Object controls[] = new Object[1];
  	controls[0] = new ISSFramePosControl((float)m_frameRate, m_source, m_streams[0]);
    return controls;
  }

  public Object getControl(String type) 
  {
  	if(ISSFramePosControl.class.getName().equals(type))
  		return new ISSFramePosControl((float)m_frameRate, m_source, m_streams[0]);
  	
    return null;
  }
}
