/*
 * Created on Feb 4, 2005
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

//import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
//import java.awt.GradientPaint;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.io.File;
import java.io.FileInputStream;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.prefs.Preferences;

//import javax.mail.MessagingException;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;
import javax.media.Manager;
//import javax.media.format.VideoFormat;
import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JSplitPane;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

//import com.mondobeyondo.stopmojo.util.SystemProperties;
//import com.sun.media.JMFSecurity;
//import com.sun.media.JMFSecurityManager;

/**
 * @author peter
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * 
 * 1) User must press "Save" button twice (add Dialog)
 *    "Saving" a project will create a video file and
 *    delete the snapshots.
 * 
 * 
 */
public class Capture
{
  public static final String
	  s_appName = "NCMLS Animation Station",
	  s_appVersion = "1.0",
	  s_propFile = "capture.properties",
	  PREF_LASTPRJFOLDER = "LastPrjFolder",
	  PREF_LASTEXPORTFOLDER = "LastExportFolder";
  public static int monitor;
  
  // RESPONSIVENESS OF BUTTONS CAN BE TUNED HERE
  private static final int MAIN_LOOP_DELAY = 30;
  
  private static final int MLS_B_PLAY    = 0x1FF & (~0x01);
  private static final int MLS_B_DELETE  = 0x1FF & (~0x02);
  private static final int MLS_B_CAPTURE = 0x1FF & (~0x04);
  private static final int MLS_B_FASTER  = 0x1FF & (~0x08);
  private static final int MLS_B_SLOWER  = 0x1FF & (~0x10);
//  private static final int MLS_B_CAM2    = 0x1FF & (~0x20);
  private static final int MLS_B_CAM1    = 0x1FF & (~0x40);
  private static final int MLS_B_NEW     = 0x1FF & (~0x80);
  private static final int MLS_B_MOVIE   = 0x1FF & (~0x40); // using old 'CAM1' MOVIE was 0x1FF & (~0x100);
	private static int ccam  = 0;
	
  public static final boolean m_slider = false; // Frame rate Slider or Label
  
	public static Image
	  s_stopmojoImage;
  
	public static ImageIcon
	  s_helpIcon,
		s_aboutIcon,
		s_okIcon,
		s_cancelIcon,
		s_prefIcon,
		s_exitIcon,
		s_newIcon,
		s_openIcon,
		s_saveIcon,
  	    s_bigNewIcon,
	    s_bigOpenIcon,
	    s_bigSaveIcon,
		s_bigGridIcon,
		s_playIcon,
		s_stopIcon,
		s_pauseIcon,
		s_rewindIcon,
		s_fastForwardIcon,
		s_beginIcon,
		s_endIcon,
		s_closeIcon,
		s_bigCloseIcon;
	
 //   private static JMFSecurity jmfSecurity = null;
    
 //   private static boolean securityPrivilege = false;
    
	private static Properties
	  s_prop;
	
	private static String
	  s_captureDevName = "";
	
	private static int
	  s_captureFormatIndex = -1;
	
	private static Preferences
	  s_pref;
	private static CaptureFrame f;
	
	public static String getCaptureDevName()
	{
		return s_captureDevName;
	}
	
	public static void setCaptureDevName(String name)
	{
		s_captureDevName = name;
	}
	
	public static int getCaptureFormatIndex()
	{
		return s_captureFormatIndex;
	}
	
	public static void setCaptureFormatIndex(int format)
	{
		s_captureFormatIndex = format;
	}
	
	public static Preferences getPref()
	{
		return s_pref;
	}
/*
	private static int getCurrentCam()
	{
		byte data[] = new byte[24];
	    File file = new File("C:\\TEMP\\defaultProject\\dp\\dc.txt");
	    FileInputStream fis = null;
	    try {
	      fis = new FileInputStream(file);
		  fis.read(data);
	    } catch (Exception ie){ return 0; }

	    if (data[0] == '0')
	    { 
	    	System.out.println("Appears to be Camera 1");
	    	return MLS_B_CAM1;
	    }
	    else 
	    {
	    	System.out.println("Appears to be Camera 2");
	    	return MLS_B_CAM2;
	    }
	}
	*/
	@SuppressWarnings("unused")
	public static void main(String[] args) 
	{
		s_pref = Preferences.userNodeForPackage(Capture.class);
		s_prop = new Properties();
		reportDevices();

	    ccam = MLS_B_CAM1;
		try
		{
			FileInputStream in = new FileInputStream(s_propFile);
			s_prop.load(in);
			in.close();		
		}
		catch (Exception e)
		{
			System.out.println("Unable to read properties from " + s_propFile);
		}
			
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) 
		{ 
		}
  
		s_stopmojoImage = new ImageIcon(Capture.class.getResource("/images/mls.gif")).getImage();
		s_helpIcon = new ImageIcon(Capture.class.getResource("/images/Help16.gif"));
		s_aboutIcon = new ImageIcon(Capture.class.getResource("/images/About16.gif"));
		s_prefIcon = new ImageIcon(Capture.class.getResource("/images/Options16.gif"));
		s_okIcon = new ImageIcon(Capture.class.getResource("/images/Check16.gif"));
		s_cancelIcon = new ImageIcon(Capture.class.getResource("/images/Cancel16.gif"));
		s_exitIcon = new ImageIcon(Capture.class.getResource("/images/Exit16.gif"));
		s_newIcon = new ImageIcon(Capture.class.getResource("/images/New16.gif"));
		s_openIcon = new ImageIcon(Capture.class.getResource("/images/OpenDoc16.gif"));
		s_saveIcon = new ImageIcon(Capture.class.getResource("/images/Save16.gif"));
		s_bigNewIcon = new ImageIcon(Capture.class.getResource("/images/New20.gif"));
		s_bigOpenIcon = new ImageIcon(Capture.class.getResource("/images/OpenDoc20.gif"));
		s_bigSaveIcon = new ImageIcon(Capture.class.getResource("/images/Save20.gif"));
		s_bigGridIcon = new ImageIcon(Capture.class.getResource("/images/Sheet20.gif"));
		s_playIcon = new ImageIcon(Capture.class.getResource("/images/VCRPlay.gif"));
		s_stopIcon = new ImageIcon(Capture.class.getResource("/images/VCRStop.gif"));
		s_pauseIcon = new ImageIcon(Capture.class.getResource("/images/VCRPause.gif"));
		s_rewindIcon = new ImageIcon(Capture.class.getResource("/images/VCRRewind.gif"));
		s_fastForwardIcon = new ImageIcon(Capture.class.getResource("/images/VCRFastForward.gif"));
		s_beginIcon = new ImageIcon(Capture.class.getResource("/images/VCRBegin.gif"));
		s_endIcon = new ImageIcon(Capture.class.getResource("/images/VCREnd.gif"));
		s_closeIcon = new ImageIcon(Capture.class.getResource("/images/DeleteDocument16.gif"));
		s_bigCloseIcon = new ImageIcon(Capture.class.getResource("/images/DeleteDocument20.gif"));
		
		Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, new Boolean(true));
		String  prjFileName = null;
		
		if(args.length > 0)	prjFileName = args[0];
		MLSButtons mlsbuttons = new MLSButtons();
		
		/* CREATE A WINDOW WITH ALL THE SYSTEM PROPERTIES */
		/*
		JFrame props = new JFrame();
		props.add(new SystemProperties());
		props.pack();
		props.setVisible(true);
		*/
		
		f = new CaptureFrame("C:/TEMP/defaultProject/dp/dp.smp", true);// MLSButtons.labjackInstalled());

		Toolkit tk = Toolkit.getDefaultToolkit();  
		int xSize = ((int) tk.getScreenSize().getWidth());  
		int ySize = ((int) tk.getScreenSize().getHeight());  
		f.setSize(xSize,ySize);  
		f.setVisible(true);
		//	f.reportSizes();


	//	f.summarizeCaptureDevices();
	/*	f.onProjectProp();
		
		try {
			    jmfSecurity = JMFSecurityManager.getJMFSecurity();
			    securityPrivilege = true;
		}
		catch (SecurityException e)
		{
		}
*/
		int waiter = 0;
		if (mlsbuttons != null && MLSButtons.labjackInstalled())
		{

		System.out.println("Starting Loop...");
		int b = 0;
		int last = b;
		monitor = 1;
		while(true)
		{
			f.requestFocusInWindow();
			waiter++;
			// if (waiter %5== 0) System.out.println(waiter);
			if (waiter == 50)  // Time delayed operation for testing
			{
				System.out.println("Waiter woke up: put 'after start' activities here");
				// Put the activity here
			}
			try 				{ b = mlsbuttons.getButtons();  }
			catch(Exception be) { be.printStackTrace();			}
			monitor++;
//			if ((monitor % 300)==0) {
//				  Date date = new Date();
//				  DateFormat dateFormat = new SimpleDateFormat("MMddyyHH_mm_ss");
//					System.out.println(dateFormat.format(date)+" ("+last+","+b+")");
//			}

			if ( b != last )
			{
//				System.out.println(" ("+last+","+b+")");

			//	System.out.println("buttons = " + b);
				switch(b)
				{
				case MLS_B_CAPTURE:
					System.out.println("CAPTURE");
					CaptureFrame.otherButton = 1;
					f.takeAPicture();
					break;
				case MLS_B_DELETE:
					System.out.println("DELETE");
					CaptureFrame.otherButton = 1;
					f.deletePicture();
					break;
				case MLS_B_PLAY:
					CaptureFrame.otherButton = 1;
					System.out.println("PLAY");
				    f.playPreview();
					break;
				case MLS_B_SLOWER:
					System.out.println("SLOWER");
					CaptureFrame.otherButton = 1;
					f.slower(); 
					break;
				case MLS_B_FASTER:
					System.out.println("FASTER");
					CaptureFrame.otherButton = 1;
					f.faster();
					break;
/*				case MLS_B_CAM1:
				    if (CaptureFrame.movieButton == 1 || CaptureFrame.startButton == 1)
					{
					    f.resetMessage();
					}
				    else
					{
					    if (CaptureFrame.m_oneCamera) //f. not static enough
						{
						    System.out.println("There is only one Camera");
						}
					    else
						{
						  System.out.println("Camera 1");
						  if (ccam == MLS_B_CAM1) 
						      {
							  System.out.println("No need to switch camera1");
						      }
						  else 
						      {
							  System.out.println("Switching to camera1");
							  f.switchCamera(0);
						      }
						}
					}
					break;
				case MLS_B_CAM2:
				    if (CaptureFrame.movieButton == 1 || CaptureFrame.startButton == 1)
					{
					    f.resetMessage();
					}
				    else
					{
					    if (CaptureFrame.m_oneCamera)
						{
						    System.out.println("There is only one Camera");
						}
					    else
						{
						    System.out.println("Camera 2");
						    if (ccam == MLS_B_CAM2) 
							{
							    System.out.println("No need to switch camera2");
							}
						    else 
							{
							    System.out.println("Switching to camera2");
							    f.switchCamera(1);
							}
						}
					}
					break;
	*/				
				case MLS_B_MOVIE:
				    if (CaptureFrame.startButton == 1)
					{
				    	CaptureFrame.otherButton = 0;
					    f.resetMessage();
					}
				    else
					{
					    System.out.println("MLS_B_MOVIE");
					    f.movie();
					}
					break;
					
				case MLS_B_NEW:
				    if (CaptureFrame.movieButton == 1)
					{
				    	CaptureFrame.otherButton = 1;
					    f.resetMessage();
					}
				    else
					{
					    System.out.println("MLS_B_NEW");
					    f.nextMovie();
					}
					break;
					/*
				case ( MLS_B_DELETE & MLS_B_PLAY ):
					f.doFrameClose();
					break;
					*/
				}
			}
			last = b;
			
			try {  Thread.sleep(MAIN_LOOP_DELAY);  }
			catch(Exception bf) { bf.printStackTrace(); }
		}
		}
		else // NO BUTTON BOX. DISPLAY MENUS
		{
			CaptureFrame.mlsVersion = false;
			CaptureFrame.mlsButtonBox = false;
			System.out.print("No button box, keyboard only");
			/*
			int speed = 12;
			while(true && speed > 3)
			{
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				f.slower();
				speed /= 2;
				f.setFPS(speed);
			}		

			try {Thread.sleep(10000);}
			catch (InterruptedException e) {e.printStackTrace();}
			f.switchCamera();
			try {Thread.sleep(10000);}
			catch (InterruptedException e) {e.printStackTrace();}
			f.switchCamera();
			try {Thread.sleep(10000);}
			catch (InterruptedException e) {e.printStackTrace();}
			f.switchCamera();
			try {Thread.sleep(10000);}
			catch (InterruptedException e) {e.printStackTrace();}
			f.switchCamera();
			*/
			
			//System.out.println("Now Clear Images from Project");
			//try {
			//	f.eraseFiles();
			//} catch (Exception e) {
			//	e.printStackTrace();
			//}
		}
		
		//System.out.println("End of Main?? buttons = " + b);
	}

	public static Properties getProp()
	{
		return s_prop;
	}
	
	public static void onAbout(Frame parent)
	{
		JOptionPane.showMessageDialog(parent, 
			Capture.s_appName + "\n" + "Version " + Capture.s_appVersion + 
			"\n\nCopyright (c) 2005 Derone Bryson. " +
			"\n\nCopyright (c) 2011 N.C. Museum of Life + Science.  All Rights Reserved." , 
			"About " + Capture.s_appName, JOptionPane.INFORMATION_MESSAGE); 
	}
	
    public static void exit(int exitVal)
    {
		f.dispose();
		f = null;
		System.exit(exitVal);
    }
  
	public static void handleError(Component parent, String message, String title, Exception e, boolean fatal)
	{ 
		if(message == null)
		  message = "";
		  
//		message += "\nPlease try the operation again and notify your system\n" +
//						"administrator if the problem persists.\n";
		         
		if(fatal)
			message += "\nThe program will exit.\n";
  	   
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(parent, message, s_appName + ": " + title, JOptionPane.ERROR_MESSAGE); 
												
		if(fatal)
		{
			e.printStackTrace();
			f.doFrameClose();
		}
	}
	
	public static void errorMsg(Component parent, String message, String title)
	{ 
		JOptionPane.showMessageDialog(parent, message, s_appName + ": " + title, JOptionPane.ERROR_MESSAGE); 
	}
	
	public static void databaseError(Component parent, Exception e, boolean fatal)
	{
		String
		  mess = e.getMessage();
		  
		if(mess != null && mess.indexOf('\n') != -1)
		  mess = mess.substring(0, mess.indexOf('\n'));
		
		if(mess != null && mess.length() > 1000)
		  mess = mess.substring(0, 1000);
		  
   	handleError(parent, mess, "Database Error", e, fatal); 
	}
	
	public static void generalError(Component parent, Exception e, boolean fatal)
	{
		String
			mess = e.getMessage();
		  
		if(mess != null && mess.indexOf('\n') != -1)
			mess = mess.substring(0, mess.indexOf('\n'));
		
		if(mess != null && mess.length() > 1000)
			mess = mess.substring(0, 1000);
		  
		handleError(parent, mess, "General Error", e, fatal); 
	}
	

	public static void generalError(Component parent, String title, Exception e, boolean fatal)
	{
		String
			mess = e.getMessage();
		  
		if(mess != null && mess.indexOf('\n') != -1)
			mess = mess.substring(0, mess.indexOf('\n'));
		
		if(mess != null && mess.length() > 1000)
			mess = mess.substring(0, 1000);
		  
		handleError(parent, mess, title, e, fatal); 
	}
	
	public static void reportDevices()
	{
		Vector<?> devs = CaptureDeviceManager.getDeviceList(null);
		System.out.println("");
		for(int i = 0; i < devs.size(); i++)
		{
		CaptureDeviceInfo
		  c = (CaptureDeviceInfo)devs.elementAt(i);
		System.out.println("Available Device "+i+" "+ c.getName());
			
			Format[]
		    formats = c.getFormats();
			System.out.println("Format length is "+ formats.length);
			for(int j = 0; j < formats.length; j++)
			{
				System.out.println("FORMAT " + j + formats[j].toString());
			}
		
		}
		System.out.println("");
	}
}
