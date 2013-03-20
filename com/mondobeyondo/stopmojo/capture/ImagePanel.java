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



import java.awt.AlphaComposite;
import java.awt.Color;
//import java.awt.Font;
//import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

/**
 * @author derry
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")

public class ImagePanel extends JPanel 
{

	
  public Image
	  m_images[];
  
  private AlphaComposite
	  m_alphas[];
  
  private Color
	  m_gridColor;
  
  private int
	  m_numGridX,
		m_numGridY;
  
  private boolean
	  m_gridOn;
 // private static String m_label = "IMAGE";
 // private static int db = 0;
  public ImagePanel(int numImages, String lbl)
  {
	//m_label = lbl;
  	m_images = new Image[numImages];
  	m_alphas = new AlphaComposite[numImages];
  	for(int i = 0; i < numImages; i++)
  	{
  		m_images[i] = null;
  		m_alphas[i] = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)1.0);
  	}
  	m_gridColor = Color.WHITE;
  	m_numGridX = 10;
  	m_numGridY = 10;
  	m_gridOn = true;
  	setOpaque(true);
  }
  
  public ImagePanel()
  {
  	m_images = new Image[1];
  	m_alphas = new AlphaComposite[1];
  	m_images[0] = null;
  	m_alphas[0] = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)1.0);
  	m_gridColor = Color.WHITE;
  	m_numGridX = 10;
  	m_numGridY = 10;
  	m_gridOn = false;
  	setOpaque(true);
  }
  public void setImage(int index, Image image, float alpha)
  {
  	m_images[index] = image;
  	m_alphas[index] = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
  	repaint();
  }
  
  public void setImage(int index, Image image)
  {
  	m_images[index] = image;
  	repaint();
  }
  
  public void setImage(Image image)
  {
  	m_images[0] = image;
  	repaint();
  }
  
  public void setAlpha(int index, float alpha)
  {
  	m_alphas[index] = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
  	repaint();
  }
  
  public void setGridColor(Color c)
  {
  	m_gridColor = c;
  	repaint();
  }
  
  public Color getGridColor()
  {
  	return m_gridColor;
  }
  
  public void setGridNumX(int numX)
  {
  	m_numGridX = numX;
  	repaint();
  }
  
  public int getGridNumX()
  {
  	return m_numGridX;
  }
  
  public void setGridNumY(int numY)
  {
  	m_numGridY = numY;
  	repaint();
  }
  
  public int getGridNumY()
  {
  	return m_numGridY;
  }
  
  public void showGrid(boolean on)
  {
  	m_gridOn = on;
  	repaint();
  }
  
  public boolean isShowingGrid()
  {
  	return m_gridOn;
  }
  
  protected void paintComponent(Graphics g) 
  {
	  super.paintComponent(g);
	 // System.out.println("ImagePanel:paintComponent() "+getX()+", "+getY());
	  Graphics2D
		  g2d = (Graphics2D)g.create();
  	
  	int
		 w = getSize().width,
		 h = getSize().height;
  	     //System.out.println("ipc"+w+", "+h);
  	
  	double
		  windowRatio = (double)w / (double)h;
	double
			x = 0,
			y = 0,
			scale = 0;
      AffineTransform
		    at = new AffineTransform(),
		    iat = new AffineTransform();

     // if (m_images.length==2) {
    //	  w = 799;
     // }
    	
  	for(int i = 0; i < m_images.length; i++)
  	{
  		Image img = m_images[i];

  		if(img != null)
  		{
  			double
  				iw = img.getWidth(null),
  				ih = img.getHeight(null),
  				imageRatio = iw / ih;

  			if (scale == 0)
  			{
  			//	if(windowRatio < imageRatio){
  			//		scale = (double)w / iw;
  			//	}
  			//	else   {
  			//		scale = (double)h / ih;
  			//	}
				scale = (double)w / iw;
  	  	//	  System.out.println("Scale =" + scale);

  		  iat.scale(scale, scale);
  		//  System.out.println("X "+x+"  Y " + y);
  		  if(scale * iw < w)
  		  {
  			  x = (w - (scale * iw)) / 2.0;
  			 // System.out.println("Adjust x "+x);
  		  }
  		  if(scale * ih < h)
  		  {
  			  y = (h - (scale * ih)) / 2.0;
  			 // System.out.println("Adjust y "+y);
  		  }
  		  at.translate(x, y);

  		  //System.out.println("G2D: "+ at.toString());
  		  g2d.setTransform(at);
  			}
  		  g2d.setComposite(m_alphas[i]);
  		  // System.out.println("GDraw: "+ iat.toString());
  		  g2d.drawImage(img, iat, null);
  		} // check for null image
		} // For Each Image

		g2d.dispose();
  }
}
