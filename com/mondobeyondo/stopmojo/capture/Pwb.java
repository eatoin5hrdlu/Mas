package com.mondobeyondo.stopmojo.capture;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.swing.JPanel;

public class Pwb extends JPanel {
	private static final long serialVersionUID = -8718242328529771056L;
	private Image image;
	private File  fd;
	  public Pwb(String file)
	  {
		  
	    try
	    {
	      fd = new File(file);
	      image = javax.imageio.ImageIO.read(fd);  
	      if (image == null)
	      {
	    	  System.out.println("Failed to load image [" + file +"]");
	      }
	    }
	    catch (Exception e) { 
	    	System.out.println("ImageIO.read threw an exception for ["+file+"]");
	    	e.printStackTrace();
	    }
	  }

	  @Override
	  protected void paintComponent(Graphics g)
	  {
		  super.paintComponent(g);
		  g.drawImage(image, 0,0,this.getWidth(),this.getHeight(),this);
	  }
 
	  public void newImage(String file)
	  {
		    fd = null;
		    Image tmp = image;

		    try
		    {
		    	fd = new File(file);
		    	image = javax.imageio.ImageIO.read(fd);  
		    	
		      if (image == null)
		      {
		    	  System.out.println("Failed to load NEW image [" + file +"]");
		      }
		    }
		    catch (Exception e) { 
		    	System.out.println("NEW ImageIO.read threw an exception for " + file);
		    	e.printStackTrace();
		    } 
	  }
}
