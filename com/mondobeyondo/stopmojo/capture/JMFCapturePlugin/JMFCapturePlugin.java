/*
 * Created on Apr 30, 2005
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
package com.mondobeyondo.stopmojo.capture.JMFCapturePlugin;

import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.ConfigureCompleteEvent;
import javax.media.Control;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.MediaLocator;
import javax.media.NoDataSourceException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.ResourceUnavailableEvent;
import javax.media.StartEvent;
import javax.media.bean.playerbean.MediaPlayer;
import javax.media.control.FormatControl;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.util.BufferToImage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.mondobeyondo.stopmojo.plugin.capture.CapturePlugin;
import com.mondobeyondo.stopmojo.plugin.capture.CapturePluginException;
import com.mondobeyondo.stopmojo.util.CDSWrapper;
import com.mondobeyondo.stopmojo.util.Util;

/**
 * @author derry
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JMFCapturePlugin implements CapturePlugin, ControllerListener 
{
	private static final String
	  ID = "JMFCapture1",
		DESC = "JMF Capture",
		VENDOR = "StopMojo Project",
		VERSION = "1.0";
	
	private boolean 	  m_ok = false;
	
  private String	  m_devName = null;	
  
  private DataSource    m_capDS = null;
  private MediaPlayer    m_capMediaPlayer = null;
  private FrameGrabbingControl    m_grabber = null;
  
  private Object 
	  m_waitSync = new Object();
  
  private boolean 
	  m_stateTransitionOK = true;
  
  private JFrame
	  m_parent;

	public JMFCapturePlugin()
	{
		int
		  devCount = 0;
		
		//
		//  do a simple check to see if JMF is installed and we have some
		//  capture devices available.
		try
		{
  		Vector
	      devs = CaptureDeviceManager.getDeviceList(null);
  		
  		for(int i = 0; i < devs.size(); i++)
  		{
  			CaptureDeviceInfo
  			  c = (CaptureDeviceInfo)devs.elementAt(i);
  				
  			Format[]
  		    formats = c.getFormats();
  				
  			for(int j = 0; j < formats.length; j++)
  				if(formats[j] instanceof VideoFormat)
  				{
  					devCount++;
  					break;
  				}
  		}
  		m_ok = devs != null && devCount > 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.capture.CapturePlugin#selectCaptureDevice(java.lang.String, boolean)
	 */
	public void closeCaptureDevice()
	{
		System.out.println("closeCaptureDevice");
	    if(m_capDS != null)
	    {
	    	try {
				m_capDS.stop();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	m_capDS.disconnect();
	    	m_capDS = null;
	    }
	
	    if(m_capMediaPlayer != null)
	    {
		    m_capMediaPlayer.close();
		    m_capMediaPlayer.deallocate();
		    m_capMediaPlayer.disable();
		    m_capMediaPlayer = null;
	    }
		dispose();
	}

	public boolean selectCaptureDevice(JFrame parent, String dev, boolean showDialog) throws CapturePluginException 
	{
		boolean retval = false;
		
		// dispose();
		System.out.println("JMF:selectCaptureDevice: ["+dev+"]");
		m_devName = dev;
		m_parent = parent;
		
		if(showDialog)
		{
			try
			{
  	    CaptureDeviceDialog
	        d = new CaptureDeviceDialog(m_parent, dev);
	
	      if(d.showModal())
	      {
	    	  m_devName = d.getDevName();
	      }
	      else
	      {
	    	  m_devName = "";
	      }
	      System.out.println("Device name ["+ m_devName + "]");
		}
			catch (Exception e)
			{
				throw new CapturePluginException("Unable to select capture device!", e);
			}
		}
	      System.out.println("After showdialog Device name ["+ m_devName + "]");
	      
	      if(!m_devName.trim().equals(""))
	      {
	    	  Cursor oldCursor = m_parent.getCursor();
	    	  m_parent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    	
	    	  String devName = "";
	    	  int  formatIndex = -1;
    	
	    	  StringTokenizer  st = new StringTokenizer(m_devName, CaptureDeviceDialog.DELIMITER);
    	
    	if(st.hasMoreTokens())
    		devName = st.nextToken();
    	if(st.hasMoreTokens())
    		formatIndex = Util.atoi(st.nextToken());
    	System.out.println("substrings devname "+devName+"  formatindex "+formatIndex);
	    if(m_capDS != null)
		    m_capDS.disconnect();
	
	    if(m_capMediaPlayer != null)
	    {
		    m_capMediaPlayer.close();
	    }
	    try
			{
	      retval = setCapDev(devName, formatIndex);
			}
		catch(IOException eo)
			{
				System.out.println("\n\nIS THE CAMERA PLUGGED IN???\n");
			}
	    catch(CapturePluginException e1)
			{
	      m_parent.setCursor(oldCursor);
			  throw e1;
			}
	    catch(Exception e2)
			{
	    		m_parent.setCursor(oldCursor);
	    		System.out.println("This happens when there is no jmf.properties file");
	    		throw new CapturePluginException("Unable to set capture device!", e2);
			}
      
      m_parent.setCursor(oldCursor);
    }
		return retval;
	}
	
	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.capture.CapturePlugin#getCaptureDeviceName()
	 */
	public String getCaptureDeviceName()
	{
		return m_devName;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.capture.CapturePlugin#startCapture()
	 */
	public void startCapture() throws CapturePluginException
	{
		if(m_capMediaPlayer != null && m_capMediaPlayer.getState() == Processor.Prefetched)
		{
	    m_capMediaPlayer.start();
  	  if(!waitForState(m_capMediaPlayer, Processor.Started)) 
  	  {
  		  throw new CapturePluginException("Unable to start player!");
  	  }
		}
		else
			throw new CapturePluginException("Player in invalid state!");
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.capture.CapturePlugin#stopCapture()
	 */
	public void stopCapture() throws CapturePluginException 
	{
		if(m_capMediaPlayer != null /*&& m_capMediaPlayer.getState() == Processor.Started*/)
		{
			m_capMediaPlayer.stop();
		}
		else
			throw new CapturePluginException("Player in invalid state!");
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.capture.CapturePlugin#grabImage()
	 */
	public BufferedImage grabPreviewImage() throws CapturePluginException
	{
		return grabImage();
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.capture.CapturePlugin#grabImage()
	 */
	public BufferedImage grabImage() throws CapturePluginException
	{
    Buffer          
  	  bufferFrame;

    BufferToImage   
      bufferToImage;

    BufferedImage           
      image = null;

    bufferFrame = m_grabber.grabFrame();
    bufferToImage = new BufferToImage ((VideoFormat)bufferFrame.getFormat());
    image = (BufferedImage)bufferToImage.createImage ( bufferFrame );
    return image;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.util.Plugin#getID()
	 */
	public String getID() 
	{
		return ID;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.util.Plugin#getDesc()
	 */
	public String getDesc() 
	{
		return DESC;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.util.Plugin#getVendor()
	 */
	public String getVendor() 
	{
		return VENDOR;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.util.Plugin#getVersion()
	 */
	public String getVersion() 
	{
		return VERSION;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.util.Plugin#isOk()
	 */
	public boolean isOk() 
	{
		return m_ok;
	}
  
  private boolean setCapDev(String devName, int format) throws Exception
  {
    //  	dispose();
  	m_capDS = createCaptureDataSource(devName, format);
  	if (m_capDS == null) 	{ return false;	}
   	try { m_capDS.connect();  }
    catch(java.io.IOException ioe) 
	  {
     	ioe.printStackTrace();
     	m_capDS = null;
	  }
    catch(Exception e)
    	{
    		e.printStackTrace();
    		m_capDS = null;
    	}
    if(m_capDS != null)
    {
  	  m_capMediaPlayer = createMediaPlayer(m_capDS);

  	  if(m_capMediaPlayer != null)
  	  {
  	    m_capMediaPlayer.addControllerListener(this);
//  	    m_capMediaPlayer.setControlPanelVisible(true);
  	    m_capMediaPlayer.realize();
  	  	if(!waitForState(m_capMediaPlayer, Processor.Configured)) 
  	  	{
  	  		throw new CapturePluginException("Unable to configure player!");
  	  	}
  		  m_capMediaPlayer.prefetch();
  	  	if(!waitForState(m_capMediaPlayer, Processor.Prefetched)) 
  	  	{
  	  		throw new CapturePluginException("Unable to prefetch player!");
  	  	}
  		  m_grabber = (FrameGrabbingControl) m_capMediaPlayer.getControl("javax.media.control.FrameGrabbingControl");
  	  	return true;
  	  }
    }
    return false;
  }
  
  public DataSource createCaptureDataSource(String devName, int formatIndex)
  {
    MediaLocator      deviceURL;   
    CaptureDeviceInfo cdi;
    DataSource 		  ds = null;
    Format			  format, formats[];
    FormatControl 	  formatControls[];
    
    cdi = CaptureDeviceManager.getDevice(devName);
    format = cdi.getFormats()[formatIndex];
    
    if(cdi != null)
    {
      deviceURL = cdi.getLocator();
      try 
      	{
    	  ds = javax.media.Manager.createDataSource(deviceURL);
      	} 
      catch (NoDataSourceException ndse) 
      	{
    	  System.out.println("\n\nIS THE CAMERA CONNECTED? " + devName);
    	  JOptionPane.showMessageDialog(m_parent,
    			  "IS THE CAMERA CONNECTED?? [" + devName + "]",
    			  "Error",  JOptionPane.ERROR_MESSAGE);
    	 // ndse.printStackTrace();
      	} 
      catch (java.io.IOException ioe) 
      	{
      		ioe.printStackTrace();
      		return null;
      	}
    }
    if(!(ds instanceof CaptureDevice))
    {
    	JOptionPane.showMessageDialog(m_parent, devName + " is not a capture device!", "Error", JOptionPane.ERROR_MESSAGE);
    	ds = null;
    }
    if(ds != null && format != null)
    {
      formatControls = ((CaptureDevice)ds).getFormatControls();
      for(int i = 0; i < formatControls.length; i++)
      {
      	formats = formatControls[i].getSupportedFormats();
      	for(int j = 0; j < formats.length; j++)
      		if(formats[j].matches(format))
          {
          	formatControls[i].setFormat(format);
          	break;
          }
      }
    }
    return new CDSWrapper((PushBufferDataSource)ds);
  }

  private MediaPlayer createMediaPlayer(DataSource dataSource) 
  {
    MediaPlayer     
		  mediaPlayer = null;

    if(dataSource == null) 
    {
    	JOptionPane.showMessageDialog(m_parent, "Datasource is null: " + dataSource, "Error", JOptionPane.ERROR_MESSAGE);
      return(null);
    }

    mediaPlayer = new MediaPlayer();
    mediaPlayer.setDataSource(dataSource);
    if(mediaPlayer.getPlayer() == null) 
    {
    	JOptionPane.showMessageDialog(m_parent, "Unable to create MediaPlayer for: " + dataSource, "Error", JOptionPane.ERROR_MESSAGE);
      return(null);
    }

    return(mediaPlayer);
  }
  
  private void setMediaPlayer(MediaPlayer player)
  {
  	m_capMediaPlayer = player;
	  m_grabber = (FrameGrabbingControl) m_capMediaPlayer.getControl("javax.media.control.FrameGrabbingControl");
	  
	  Control[] controls = player.getControls();
  }

  /**
   * Block until the processor has transitioned to the given state.
   * Return false if the transition failed.
   */
  boolean waitForState(Player p, int state) 
  {
    synchronized(m_waitSync) 
		{
      try 
			{
	      while(p.getState() < state && m_stateTransitionOK)
	        m_waitSync.wait();
      } catch (Exception e) {}
    }
    return m_stateTransitionOK;
  }

  public void controllerUpdate(ControllerEvent evt) 
  {
    if(evt instanceof ConfigureCompleteEvent ||
       evt instanceof RealizeCompleteEvent ||
       evt instanceof PrefetchCompleteEvent || 
       evt instanceof StartEvent) 
    {
      synchronized(m_waitSync) 
			{
	      m_stateTransitionOK = true;
	      m_waitSync.notifyAll();
      }
    } 
    else if(evt instanceof ResourceUnavailableEvent) 
    {
      synchronized(m_waitSync) 
		  {
	      m_stateTransitionOK = false;
	      m_waitSync.notifyAll();
      }
    } 
    else if(evt instanceof EndOfMediaEvent) 
    {
      evt.getSourceController().stop();
      evt.getSourceController().close();
    }
  }
  
  public void dispose()
  {
		if(m_capDS != null)
		{
			try
			{
		    m_capDS.stop();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
  		m_capDS.disconnect();
  		m_capDS = null;
		}
		
		if(m_capMediaPlayer != null)
		{
			m_capMediaPlayer.stop();
			m_capMediaPlayer.close();
			m_capMediaPlayer.deallocate();
			m_capMediaPlayer = null;

		}
		m_grabber = null;
  }
}
