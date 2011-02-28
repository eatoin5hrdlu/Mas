package com.mondobeyondo.stopmojo.capture;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;
import java.lang.Math;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;


@SuppressWarnings("serial")
public class filmStrip extends JPanel
{	
	private static int SCREENWIDTH = 1024;
	private static int SCREENHEIGHT = 768;
	private static String idir;
	private Vector<Pwb> shots; 
	private String fnum;
	private Pwb topsproket;
	private Pwb botsproket;
	private Vector<JPanel> spacer;
	private int numspaces;
	private JPanel filmspace;
	private int nframes;
	public SliderDemo m_speedLabel;
	
	public filmStrip(String dir, int size, int position)
	{
		idir = dir;
		nframes = size;
		if (CaptureFrame.m_prj != null)
		{
			SCREENWIDTH = CaptureFrame.SCREENWIDTH;
			SCREENHEIGHT = CaptureFrame.SCREENHEIGHT;
		//	System.out.println("filmStrip thinks width="+SCREENWIDTH+" h="+SCREENHEIGHT);
		}
		// System.out.println("Width(" + SCREENWIDTH  + ")  Height(" + SCREENHEIGHT +")");
	    int spaceWidth = SCREENWIDTH/16 + 10;
	    int stripWidth = SCREENWIDTH-(2*spaceWidth + 40 + size/3);
	    if (size == 24) stripWidth -= 20;
	    if (size == 6)  stripWidth -= 2;
	    int labelHeight = SCREENWIDTH/12;
	    int filmHeight  = SCREENHEIGHT/12;

	    //setPreferredSize(new Dimension(SCREENWIDTH, 90));
	    setOpaque(false);

	    numspaces = 7;
	    spacer = new Vector<JPanel>(numspaces);
	    int s;
	    for (s = 0; s < numspaces; s++)
	    {
	    	
	    	spacer.add(new JPanel()
	      	{
	      	    @Override
	      	    protected void paintComponent(Graphics g) {
	      	    	super.paintComponent(g);
//	    	    	  Graphics2D g2d = (Graphics2D)g;
//	    	    	  g2d.setFont(new Font("Dialog",Font.PLAIN,12));
//	      	    	  g2d.setColor(Color.black);
//	      	    	  g2d.drawString("sp", 10, 10); 
	      	    }
	      	} 
	    	
	    	);
	    	spacer.elementAt(s).setPreferredSize(new Dimension(spaceWidth, spaceWidth/10));
	    	spacer.elementAt(s).setOpaque(false); 	
	    }
	    
	    topsproket = new Pwb("images/filmsproket.png");
	    topsproket.setPreferredSize(new Dimension(stripWidth, 30));
	    botsproket = new Pwb("images/filmbotsproket.png");
	    botsproket.setPreferredSize(new Dimension(stripWidth, 20));

	    shots = new Vector<Pwb>();
	    setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		add(spacer.elementAt(0));
		add(topsproket);
		add(spacer.elementAt(1));
		add(spacer.elementAt(2));
		int fwidth = stripWidth/nframes;
		int k;
	   for(k=0; k<nframes; k++)
	    {
	    	shots.add(new Pwb(ifile(k,position)));
	    	Pwb tmp = shots.elementAt(k);
	    	if (tmp == null)
	    	{
	    		System.out.println("Failed to create snapshot "+ k);
	    	}
	    	else
	    	{
    			tmp.setPreferredSize(new Dimension(fwidth, filmHeight));
	    		if ( k < (nframes-1) )
	    		{
	    			tmp.setBorder(BorderFactory.createLineBorder (Color.black, 4));
	    		}
	    		else
	    		{
	    			Border raisedbevel=null, loweredbevel=null, compound=null;
	    			raisedbevel = BorderFactory.createRaisedBevelBorder();
	    			loweredbevel = BorderFactory.createLoweredBevelBorder();
	    			compound = BorderFactory.createCompoundBorder(raisedbevel,loweredbevel);
	    			
	    			tmp.setBorder(compound);
	    		}
	    		add(tmp);
	    	}
	    }
	
	   add(spacer.elementAt(3));
	   add(spacer.elementAt(4));
	   add(botsproket); 
	   add(spacer.elementAt(5));
	   // System.out.println("Trying to show "+size+"fps");
	  //Pwb speedLabel = new Pwb("images/"+size+"fps.png");

	  add(spacer.elementAt(6));	
	  m_speedLabel = new SliderDemo(size, position);
	  m_speedLabel.setPreferredSize(new Dimension(stripWidth+20, labelHeight));

	  add(m_speedLabel);
	  m_speedLabel.setFPS(nframes);
	}
	
	  public void position(int frameNumber)
	  {
		    m_speedLabel.setNumFrames(frameNumber);

		   for(int k=0; k<nframes; k++)
		    {
		    	shots.elementAt(k).newImage(ifile(k,frameNumber));
		    }
		   repaint();
	  }
	  
	  
	  public String ifile(int position, int curFrame)
	  {
		  int leadin = curFrame - (nframes - position);
		  if (leadin < 1)
		  {
			  return String.format("images/Test%06d.png", Math.abs(leadin));
		  }
		  else
		  {
			  return String.format(idir+"Frame%06d.jpg", leadin);
		  }
	  }
	  
	  public void setFPS(int fps)
	  {
		  m_speedLabel.setFPS(fps);
	  }
	  

}
