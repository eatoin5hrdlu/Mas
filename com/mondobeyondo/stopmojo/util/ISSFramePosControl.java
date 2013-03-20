/*
 * Created on Feb 20, 2005
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

import java.awt.Component;

import javax.media.Time;
import javax.media.control.FramePositioningControl;

/**
 * @author derry
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ISSFramePosControl implements FramePositioningControl 
{
    @SuppressWarnings("unused")
	private long
	  m_frameDuration;
	
//	private ImageFrameSource
//	  m_source;
	
	private ImageSourceStream
	  m_stream;
	
	public ISSFramePosControl(float frameRate, ImageFrameSource source, ImageSourceStream stream)
	{
    m_frameDuration = (long)(source.getCount() / frameRate);
  //  m_source = source;
    m_stream = stream;
	}
	
	public int seek(int frame) 
	{
//System.out.println("seek to " + frame);		
		return m_stream.setCurFrame(frame);
	}

	public int skip(int frames) 
	{
//System.out.println("skipping " + frames);		
		return m_stream.setCurFrame(m_stream.getCurFrame() + frames);
	}

	public Time mapFrameToTime(int frame) 
	{
//		return new Time(m_frameDuration * frame);
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.media.control.FramePositioningControl#mapTimeToFrame(javax.media.Time)
	 */
	public int mapTimeToFrame(Time time) 
	{
//		return (int)(time.getNanoseconds() / m_frameDuration);
		return 0;
	}

	public Component getControlComponent() 
	{
		return null;
	}
}
