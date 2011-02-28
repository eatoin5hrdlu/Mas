/*
 * Created on Oct 22, 2003
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

import java.awt.Frame;
import java.util.prefs.Preferences;


/**
 * @author Derry Bryson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FramePosSizeHandler
{
	private static final String
	  PREF_X = "X",
	  PREF_Y = "Y",
	  PREF_WIDTH = "Width",
	  PREF_HEIGHT = "Height",
	  PREF_EXTENDEDSTATE = "ExtendedState";
	  
  public static void restoreSizeAndPosition(Frame frame) 
  {
  	Preferences
  	  pref = Preferences.userNodeForPackage(frame.getClass());
  	  
  	int
  	  x = frame.getX(),
  	  y = frame.getY(),
  	  w = frame.getWidth(),
  	  h = frame.getHeight(),
  	  es = frame.getExtendedState();
  	  
  	String
  	  className = frame.getClass().getName();
  	  
		x = pref.getInt(className + "_" + PREF_X, x);
		y = pref.getInt(className + "_" + PREF_Y, y);
		w = pref.getInt(className + "_" + PREF_WIDTH, w);
		h = pref.getInt(className + "_" + PREF_HEIGHT, h);
		es = pref.getInt(className + "_" + PREF_EXTENDEDSTATE, es);
		
		frame.setLocation(x, y);
  	frame.setSize(w, h);
  	if(es != Frame.ICONIFIED)
  		frame.setExtendedState(es);
  }

  public static void saveSizeAndPosition(Frame frame) 
  {
		Preferences
			pref = Preferences.userNodeForPackage(frame.getClass());

		String
			className = frame.getClass().getName();
  	  
    if(frame.getExtendedState() == Frame.NORMAL)
    {  	  
      pref.putInt(className + "_" + PREF_X, frame.getX());
	  	pref.putInt(className + "_" + PREF_Y, frame.getY());
		  pref.putInt(className + "_" + PREF_WIDTH, frame.getWidth());
		  pref.putInt(className + "_" + PREF_HEIGHT, frame.getHeight());
    }
		pref.putInt(PREF_EXTENDEDSTATE, frame.getExtendedState());
  }
}
