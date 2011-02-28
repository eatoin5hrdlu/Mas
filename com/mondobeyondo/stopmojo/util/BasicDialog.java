/*
 * Created on Oct 23, 2003
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

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 * @author Derry Bryson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")

public class BasicDialog extends JDialog 
{
  /**
   * @throws java.awt.HeadlessException
   */
  public BasicDialog() throws HeadlessException 
  {
    super();
    init();
  }

  /**
   * @param arg0
   * @throws java.awt.HeadlessException
   */
  public BasicDialog(Frame arg0) throws HeadlessException 
  {
    super(arg0);
		init();
  }

  /**
   * @param arg0
   * @param arg1
   * @throws java.awt.HeadlessException
   */
  public BasicDialog(Frame arg0, boolean arg1) throws HeadlessException 
  {
    super(arg0, arg1);
		init();
  }

  /**
   * @param arg0
   * @param arg1
   * @throws java.awt.HeadlessException
   */
  public BasicDialog(Frame arg0, String arg1) throws HeadlessException 
  {
    super(arg0, arg1);
		init();
  }

  /**
   * @param arg0
   * @param arg1
   * @param arg2
   * @throws java.awt.HeadlessException
   */
  public BasicDialog(Frame arg0, String arg1, boolean arg2) throws HeadlessException 
  {
    super(arg0, arg1, arg2);
		init();
  }

  /**
   * @param arg0
   * @param arg1
   * @param arg2
   * @param arg3
   */
  public BasicDialog(
    Frame arg0,
    String arg1,
    boolean arg2,
    GraphicsConfiguration arg3) 
  {
    super(arg0, arg1, arg2, arg3);
		init();
  }

  /**
   * @param arg0
   * @throws java.awt.HeadlessException
   */
  public BasicDialog(Dialog arg0) throws HeadlessException 
  {
    super(arg0);
		init();
  }

  /**
   * @param arg0
   * @param arg1
   * @throws java.awt.HeadlessException
   */
  public BasicDialog(Dialog arg0, boolean arg1) throws HeadlessException 
  {
    super(arg0, arg1);
		init();
  }

  /**
   * @param arg0
   * @param arg1
   * @throws java.awt.HeadlessException
   */
  public BasicDialog(Dialog arg0, String arg1) throws HeadlessException 
  {
    super(arg0, arg1);
		init();
  }

  /**
   * @param arg0
   * @param arg1
   * @param arg2
   * @throws java.awt.HeadlessException
   */
  public BasicDialog(Dialog arg0, String arg1, boolean arg2)
    throws HeadlessException 
  {
    super(arg0, arg1, arg2);
		init();
  }

  /**
   * @param arg0
   * @param arg1
   * @param arg2
   * @param arg3
   * @throws java.awt.HeadlessException
   */
  public BasicDialog(
    Dialog arg0,
    String arg1,
    boolean arg2,
    GraphicsConfiguration arg3)
    throws HeadlessException 
  {
    super(arg0, arg1, arg2, arg3);
		init();
  }

  private void init()
  {
  	//
  	// setup to close on ESCAPE
  	//
		getRootPane().registerKeyboardAction(new ActionListener()
		{
			public void actionPerformed(ActionEvent actionEvent) 
			{
				dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosed(java.awt.event.WindowEvent evt)
			{
				saveSizeAndPosition();
			}
		});
  }
  
	private static final String
		PREF_X = "X",
		PREF_Y = "Y",
		PREF_WIDTH = "Width",
		PREF_HEIGHT = "Height",
		PREF_EXTENDEDSTATE = "ExtendedState";
	  
	public void restoreSizeAndPosition() 
	{
		restoreSize();
		restorePosition();
	}

	public void restorePosition() 
	{
		Preferences
			pref = Preferences.userNodeForPackage(this.getClass());

		int
			x = getX(),
			y = getY();
			
		String
			className = this.getClass().getName();
  	  
		x = pref.getInt(className + "_" + PREF_X, x);
		y = pref.getInt(className + "_" + PREF_Y, y);
		
		setLocation(x, y);
	}

	public void restoreSize() 
	{
		Preferences
			pref = Preferences.userNodeForPackage(this.getClass());

		int
			w = getWidth(),
			h = getHeight();
			
		String
			className = this.getClass().getName();
  	  
		w = pref.getInt(className + "_" + PREF_WIDTH, w);
		h = pref.getInt(className + "_" + PREF_HEIGHT, h);
		
		setSize(w, h);
	}

	public void saveSizeAndPosition() 
	{
		savePosition();
		saveSize();
	}
	
	public void savePosition() 
	{
		Preferences
			pref = Preferences.userNodeForPackage(this.getClass());

		String
			className = this.getClass().getName();
  	  
		pref.putInt(className + "_" + PREF_X, getX());
		pref.putInt(className + "_" + PREF_Y, getY());
	}
	
	public void saveSize() 
	{
		Preferences
			pref = Preferences.userNodeForPackage(this.getClass());

		String
			className = this.getClass().getName();
  	  
		pref.putInt(className + "_" + PREF_WIDTH, getWidth());
		pref.putInt(className + "_" + PREF_HEIGHT, getHeight());
	}
	
	public void center()
	{
		Dimension
		  screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		  
		int
		  x,
		  y;
		  
		x = (screenSize.width - getWidth()) / 2;
		if(x < 0)
		  x = 0;
		y = (screenSize.height - getHeight()) / 2;
		if(y < 0)
			y = 0;
			
	  setLocation(x, y);
	}
	
	public void centerOnParent()
	{
		int
		  x,
		  y,
		  w = getWidth(),
		  h = getHeight(),
		  px = getParent().getX(),
		  py = getParent().getY(),
		  pw = getParent().getWidth(),
		  ph = getParent().getHeight();
		
		x = (px + (pw / 2)) - (w / 2);
		y = (py + (ph / 2)) - (h / 2);
		
		if(x < 0)
		  x = 0;
		if(y < 0)
		  y = 0;
		  
		setLocation(x, y);  
	}
}
