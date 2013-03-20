/*
 * Created on Oct 21, 2003
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
package com.mondobeyondo.stopmojo.capture;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.media.CachingControlEvent;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DurationUpdateEvent;
import javax.media.MediaTimeSetEvent;
import javax.media.PrefetchCompleteEvent;
import javax.media.RateChangeEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.SizeChangeEvent;
import javax.media.StartEvent;
import javax.media.StopTimeChangeEvent;
import javax.media.Time;
import javax.media.TransitionEvent;
import javax.media.bean.playerbean.MediaPlayer;
import javax.media.format.FormatChangeEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.mondobeyondo.stopmojo.util.BasicDialog;

/**
 * @author Derry Bryson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")

public class PlayerDialog extends BasicDialog implements ControllerListener 
{
  MediaPlayer
	  m_mediaPlayer;
  
  public PlayerDialog(JFrame frame, MediaPlayer mediaPlayer, String title)
  {
  	super(frame, true);
  	m_mediaPlayer = mediaPlayer;
  	m_mediaPlayer.addControllerListener(this);
  	m_mediaPlayer.setPlaybackLoop(false);
  	m_mediaPlayer.setControlPanelVisible(true);
  	m_mediaPlayer.setMediaLocationVisible(true);
  	m_mediaPlayer.setFixedAspectRatio(true);
	  setTitle(title);
	  
		//setIconImage(EmailApp.s_iconImage);
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent evt)
			{
				onClose(evt);
			}
		});
		
		setSize(500, 500);
		restoreSizeAndPosition();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		m_mediaPlayer.realize();
	}
  
	private void onClose(java.awt.event.WindowEvent evt)
	{
		doFrameClose();
	}
	
	private void doFrameClose()
	{
		if(m_mediaPlayer != null)
		{
			m_mediaPlayer.close();
		}
		saveSizeAndPosition();
		
		dispose();
	}
	
  public synchronized void controllerUpdate(ControllerEvent event) 
  {
System.out.println("controllerUpdate: " + event.toString());  	
    if(event instanceof RealizeCompleteEvent) 
    {
      processRealizeComplete((RealizeCompleteEvent)event);
    }
    else if(event instanceof PrefetchCompleteEvent) 
    {
      processPrefetchComplete((PrefetchCompleteEvent)event);
    }
    else if(event instanceof ControllerErrorEvent) 
    {
      processControllerError((ControllerErrorEvent)event);
    }
    else if(event instanceof ControllerClosedEvent ) 
    {
      processControllerClosed ( (ControllerClosedEvent) event );
    }
    else if(event instanceof DurationUpdateEvent) 
    {
    @SuppressWarnings("unused")
      Time t = ((DurationUpdateEvent)event).getDuration();
    }
    else if(event instanceof CachingControlEvent) 
    {
//      processCachingControl((CachingControlEvent)event);
    }
    else if(event instanceof StartEvent) 
    {
    }
    else if(event instanceof MediaTimeSetEvent) 
    {
    }
    else if(event instanceof TransitionEvent) 
    {
    }
    else if(event instanceof RateChangeEvent) 
    {
    }
    else if(event instanceof StopTimeChangeEvent) 
    {
    }
    else if(event instanceof FormatChangeEvent) 
    {
//      processFormatChange((FormatChangeEvent) event);
    }
    else if(event instanceof SizeChangeEvent) 
    {
    }
    else if(event.getClass().getName().endsWith("ReplaceURLEvent")) 
    {
//      processReplaceURL ( event );
    }
  }

  protected void processRealizeComplete(RealizeCompleteEvent event) 
  {
//System.out.println("processRealizeComplete");  	
    // Wait for visual to show up
    Component compVis = m_mediaPlayer.getVisualComponent();
    if (compVis != null) 
    {
      while (!compVis.isVisible()) 
      {
        try 
		    {
          Thread.sleep(10);
        } 
        catch (InterruptedException ie) 
		    {
        }
      }
    }
    
    // Ask the player to prefetch data and prepare to start.
   	if(m_mediaPlayer != null && m_mediaPlayer.getPlayer() == event.getSourceController())
    {
    	getContentPane().add(new MediaPlayerPanel(m_mediaPlayer, true), BorderLayout.CENTER);
    	validate();
    }
    event.getSourceController().prefetch();
  }

  protected void processPrefetchComplete ( PrefetchCompleteEvent event ) 
  {
//System.out.println("processPrefetchComplete");
  	if(m_mediaPlayer != null && m_mediaPlayer.getPlayer() == event.getSourceController())
	  {
		  m_mediaPlayer.start();
	  }
  }

  protected void processFormatChange ( FormatChangeEvent event ) 
  {
/*	
    killCurrentView ();

    // Get the visual component
    panelVideo = new VideoPanel ( mediaPlayerCurrent );
    panelVideo.setZoom ( dDefaultScale );
    panelVideo.addMenuZoomActionListener ( this );
    panelContent.add ( panelVideo, BorderLayout.CENTER );

    // Get the control component
    compControl = mediaPlayerCurrent.getControlPanelComponent ();
    if ( compControl != null) {
        panelContent.add ( compControl, BorderLayout.SOUTH );
    }
*/    
  }

  protected void processControllerError ( ControllerErrorEvent event ) 
  {
  	JOptionPane.showMessageDialog(this, event.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
  }

  protected void processControllerClosed ( ControllerClosedEvent event ) 
  {
  	m_mediaPlayer = null;
  }

  protected void processCachingControl ( CachingControlEvent event ) 
  {
  }

  protected void processReplaceURL ( ControllerEvent event ) 
  {
  }
  
}
