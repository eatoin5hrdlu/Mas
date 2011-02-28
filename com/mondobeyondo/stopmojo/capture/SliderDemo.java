package com.mondobeyondo.stopmojo.capture;

import java.awt.*;
import java.awt.event.*;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicGraphicsUtils;

public class SliderDemo extends JPanel
                        implements ActionListener,
                                   WindowListener,
                                   ChangeListener {
    //Set up animation parameters.
    static final int FPS_MIN = 0;
    static final int FPS_MAX = 24;
    static final int FPS_INIT = 12;    //initial frames per second
    public static JSlider framesPerSecond;
    public JLabel numFrames;
    public int fps;
    
    public SliderDemo(int size, int position) {
        setLayout(null); // new BoxLayout(this, BoxLayout.PAGE_AXIS));

        int tfrs = position - 1;
        if (tfrs < 0) { tfrs = 0; }
        numFrames = new JLabel(tfrs+" Total");
		numFrames.setFont( new Font("Trebuchet MS",Font.BOLD, 26));
        numFrames.setBounds(1200,10,1300,40);
		add(numFrames);
		
	    JLabel sliderLabel = new JLabel("            " +
	    	        size +
	    	        "   Frames    Per    Second                                                 Frames", JLabel.CENTER);
	    			sliderLabel.setFont( new Font("Trebuchet MS",Font.BOLD, 26));
	    	        sliderLabel.setBounds(300,30,1100,60);
        add(sliderLabel);
        sliderLabel.setVisible(true);
     //   Icon icon = new ImageIcon("images/mlspointer.gif");
      //  UIDefaults defaults = UIManager.getDefaults();
       // defaults.put("Slider.horizontalThumbIcon", icon);
  
        framesPerSecond = new JSlider(JSlider.HORIZONTAL,
                                              FPS_MIN, FPS_MAX, FPS_INIT);
        setVisible(false);
        framesPerSecond.setPaintTrack(false);

        framesPerSecond.addChangeListener(this);

        framesPerSecond.setMajorTickSpacing(3);
        framesPerSecond.setMinorTickSpacing(1);
        framesPerSecond.setPaintTicks(true);
        framesPerSecond.setPaintLabels(true);
        framesPerSecond.setBorder(
                BorderFactory.createEmptyBorder(0,0,10,0));
        Font font = new Font("Trebuchet MS", Font.ITALIC, 22);
        framesPerSecond.setFont(font);


        Dictionary d = framesPerSecond.getLabelTable();

        for (Enumeration e = d.keys() ; e.hasMoreElements() ; )
        {
        	Object key = e.nextElement();
        	String k = key.toString();
        	if (       k.equals("0")
        			|| k.equals("9")
        			|| k.equals("15")
        			|| k.equals("18")
        			|| k.equals("21")
        		)
        	{
        		d.remove(key);
        	}
        }


        if (Capture.m_slider) 
        	{
        	add(framesPerSecond);
            framesPerSecond.setVisible(true);
        	}

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        fps = size;
        setOpaque(false);
        setVisible(true);
    }

    /** Add a listener for window events. */
    void addWindowListener(Window w) {
        w.addWindowListener(this);
    }

    //React to window events.
    public void windowIconified(WindowEvent e) {
    }
    public void windowDeiconified(WindowEvent e) {
    }
    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

    /** Listen to the slider. */
    public void stateChanged(ChangeEvent e) {
//    	System.out.println("stateChanged: "+ e.toString());
//        JSlider source = (JSlider)e.getSource();
//      if (!source.getValueIsAdjusting())
//        {
//        	
//        }
    }
    public void setFPS(int fs)
    {
    	framesPerSecond.setValue(fs);
    }
    public void setNumFrames(int fs)
    {
    	int n = fs-1;
    	numFrames.setText(n + " Total");
    }
    //Called when the Timer fires.
    public void actionPerformed(ActionEvent e) {
    	
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SliderDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SliderDemo animator = new SliderDemo(12,0);
        frame.add(animator, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
/*
    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        try {Thread.sleep(3000);}
        catch (InterruptedException e){e.printStackTrace();}
        framesPerSecond.setValue(3);

        try {Thread.sleep(3000);}
        catch (InterruptedException e){e.printStackTrace();}
        framesPerSecond.setValue(6);

        try {Thread.sleep(3000);}
        catch (InterruptedException e){e.printStackTrace();}
        framesPerSecond.setValue(24);

        try {Thread.sleep(3000);}
        catch (InterruptedException e){e.printStackTrace();}
        framesPerSecond.setValue(12);  
    }
    */
}

