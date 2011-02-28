/*
 * Created on May 1, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.mondobeyondo.stopmojo.capture.QTCapturePlugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.Timer;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.qd.PixMap;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.RawEncodedImage;

import com.mondobeyondo.stopmojo.plugin.capture.CapturePlugin;
import com.mondobeyondo.stopmojo.plugin.capture.CapturePluginException;

/**
 * @author derry
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class QTCapturePlugin implements CapturePlugin 
{
	private static final String
    ID = "QTCapture1",
	  DESC = "QuickTime Capture",
	  VENDOR = "StopMojo Project",
	  VERSION = "1.0";

	private boolean
	  m_ok = false,
		m_capturing = false;
	
	private SequenceGrabber
	  m_grabber = null;
	
	private QDGraphics
	  m_gWorld = null;
	
	private SGVideoChannel
	  m_videoChannel;
	
	private Timer
	  m_timer = null;
	
	private QDRect 
	  m_grabBounds;
	
	private String
	  m_devName;
	
	private int
	  m_pixels[] = null,
		m_intsPerRow = 0;

	private BufferedImage
	  m_image;
	
	public QTCapturePlugin()
	{
		try
		{
		  QTSession.open( );
/*		  
      // create shutdown handler
      Runtime.getRuntime().addShutdownHook(new Thread( ) {
              public void run( ) {
//                QTSession.close( );
                QTSession.exitMovies();
            }
        });
*/        
      m_ok = true;
		}
	  catch(UnsatisfiedLinkError er)
		{
	  	
		}
		catch(Exception e)
		{
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.capture.CapturePlugin#selectCaptureDevice(javax.swing.JFrame, java.lang.String, boolean)
	 */
	public void closeCaptureDevice()
	{
		  if (m_grabber != null)
		  {
			  try {
				m_grabber.disposeQTObject();
			} catch (QTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  m_grabber = null;
		  }
		  if (m_videoChannel != null)
		  {
			  try {
				m_videoChannel.disposeQTObject();
			} catch (QTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  m_videoChannel = null;
		  }
		  dispose();
	}
	
	public boolean selectCaptureDevice(JFrame parent, String devName,
			boolean showDialog) throws CapturePluginException 
	{
		boolean
		  retval = false;
		
		dispose();
		m_devName = "";
		
		try
		{
		  m_grabber = new SequenceGrabber( );
//      System.out.println ("got grabber");
    
      m_videoChannel = new SGVideoChannel(m_grabber);
      //System.out.println ("Got SGVideoChannel");
      
      // get settings
      // yikes! this crashes java 1.4.2 on mac os x!
      // m_videoChannel.settingsDialog();
      m_devName = m_videoChannel.getDataSourceName().toString();
      
      // prepare and start previewing
      // note - second prepare arg should seemingly be false,
      // but if it is, you get erroneous dskFulErr's
//		QTFile movieFile = new QTFile(new java.io.File("NoFile"));
  		m_grabber.setDataOutput(null, quicktime.std.StdQTConstants.seqGrabDontMakeMovie);
  		m_grabber.prepare(true, true);

/*
      m_videoChannel.setUsage(StdQTConstants.seqGrabRecord);
      m_grabber.prepare(false, true);
      m_grabber.startPreview();
*/      
      retval = true;
		}
		catch(QTException e)
		{
//			e.printStackTrace();
			if(e.getMessage().indexOf("-9405") != -1)
				throw new CapturePluginException("No capture devices found!", e);
			else
			  throw new CapturePluginException("Unable to select capture device!", e);
		}
		
		return retval;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.capture.CapturePlugin#getCaptureDeviceName()
	 */
	public String getCaptureDeviceName() 
	{
		return m_devName;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.capture.CapturePlugin#startCapture()
	 */
	public void startCapture() throws CapturePluginException 
	{
		if(m_grabber != null && m_videoChannel != null)
		{
			try
			{
        // prepare and start previewing
        // note - second prepare arg should seemingly be false,
        // but if it is, you get erroneous dskFulErr's
//        m_videoChannel.setUsage(StdQTConstants.seqGrabRecord);
//	      m_grabBounds = new QDRect(640, 480);
//	      m_videoChannel.setBounds(m_grabBounds);
	      
        m_grabBounds = m_videoChannel.getSrcVideoBounds();
        //System.out.println("width = " + m_grabBounds.getWidth() + ", height = " + m_grabBounds.getHeight());        
	      m_videoChannel.setBounds(m_grabBounds);
	      m_image = new BufferedImage(m_grabBounds.getWidth(), m_grabBounds.getHeight(), BufferedImage.TYPE_INT_RGB);
	      if (m_image == null)
	      {
	    	  System.out.println("m_image failed to initialize");
	    	  System.exit(0);
	      }
//	      System.out.println("m_image OK in QTstartCapture");

    		m_gWorld = new QDGraphics(m_grabBounds);
    		if (m_gWorld == null)
    		{
    			System.out.println("m_gWorld is null in QTstartCapture");
    			System.exit(0);
    		}
			//System.out.println("m_gWorld is OK QTstartCapture");
    		m_grabber.setGWorld(m_gWorld, null);
    		m_videoChannel.setUsage(quicktime.std.StdQTConstants.seqGrabRecord |
    			                      quicktime.std.StdQTConstants.seqGrabPreview |
    			                      quicktime.std.StdQTConstants.seqGrabPlayDuringRecord);
    		m_videoChannel.setFrameRate(0);
//    		m_videoChannel.setCompressorType( quicktime.std.StdQTConstants.kComponentVideoCodecType);        
    		
    		m_grabber.startRecord();    		
        
        ActionListener timerCallback =
          new ActionListener( ) {
              public void actionPerformed(ActionEvent e) 
              {
                try 
								{
                  m_grabber.idle( );
                  m_grabber.update(null);
                } 
                catch(QTException qte) 
								{
                  qte.printStackTrace( );
                }
              }
          };
          
        m_capturing = true;
/*        
        m_timer = new Timer(200, timerCallback);
        m_timer.start( );
*/        
      }
			catch(QTException e)
			{
				e.printStackTrace();
				throw new CapturePluginException("Unable to select capture device!", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.capture.CapturePlugin#stopCapture()
	 */
	public void stopCapture() throws CapturePluginException 
	{
		if(m_grabber != null && m_videoChannel != null)
		{
			try
			{
				if(m_timer != null)
				{
					m_timer.stop();
					m_timer = null;
				}
        m_grabber.stop();
        m_capturing = false;
//        m_grabber.idle();
			}
			catch(QTException e)
			{
				throw new CapturePluginException("Unable to select capture device!", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.capture.CapturePlugin#grabPreviewImage()
	 */
	public BufferedImage grabPreviewImage() throws CapturePluginException 
	{
		return grabImage();
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.capture.CapturePlugin#grabImage()
	 */
	public BufferedImage grabImage() throws CapturePluginException 
	{
		if(m_grabber == null)
			return null;
				
    try 
		{
      m_grabber.idle( );
//      m_grabber.update(null);
    } 
    catch(QTException qte) 
		{
      qte.printStackTrace( );
    }    

    if (m_gWorld == null)
    {
    	System.out.println("m_gWorld is null in QTgrabImage()");
    	return (BufferedImage) null;
    }
    PixMap pixMap = m_gWorld.getPixMap();
    RawEncodedImage rei = pixMap.getPixelData();
    // copy bytes to an array
    if(m_intsPerRow == 0)
      m_intsPerRow = (pixMap.getRowBytes() + 3) / 4;
    if(m_pixels == null)
      m_pixels = new int [m_intsPerRow * m_grabBounds.getHeight()];
    rei.copyToArray (0, m_pixels, 0, m_pixels.length);
    if (m_image == null)
    {
    	System.out.println("m_image is null in QTCapturePlugin:grabimage()");
    }
    m_image.setRGB(0, 0, m_grabBounds.getWidth(), m_grabBounds.getHeight(), m_pixels, 0, m_intsPerRow);
    return m_image;    
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.Plugin#getID()
	 */
	public String getID() 
	{
		return ID;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.Plugin#getDesc()
	 */
	public String getDesc() 
	{
		return DESC;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.Plugin#getVendor()
	 */
	public String getVendor() 
	{
		return VENDOR;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.Plugin#getVersion()
	 */
	public String getVersion() 
	{
		return VERSION;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.Plugin#isOk()
	 */
	public boolean isOk() 
	{
		return m_ok;
	}

	/* (non-Javadoc)
	 * @see com.mondobeyondo.stopmojo.plugin.Plugin#dispose()
	 */
	public void dispose() 
	{
		if(m_grabber != null && m_videoChannel != null)
		{
			try
			{
				if(m_timer != null)
				{
					m_timer.stop();
					m_timer = null;
				}				
		    if(m_capturing)
          m_grabber.stop();
        m_grabber.disposeChannel(m_videoChannel);
        m_grabber.disposeQTObject();
        m_grabber = null;
        m_videoChannel = null;
        m_gWorld = null;
        System.out.println("dispose clearing m_gWorld");
        m_pixels = null;
				m_intsPerRow = 0;
			}
			catch(QTException e)
			{
				e.printStackTrace();
			}
		}
	}
}
