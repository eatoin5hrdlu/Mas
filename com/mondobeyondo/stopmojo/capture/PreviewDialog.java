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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mondobeyondo.stopmojo.util.BasicDialog;
import com.mondobeyondo.stopmojo.util.Project;

/**
 * @author Derry Bryson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")

public class PreviewDialog extends BasicDialog implements ChangeListener 
{
	private Project
	  m_prj;
	
	private Timer
	  m_timer;
	
	private int
	  m_curFrame,
		m_numFrames,
		m_delay;
	@SuppressWarnings("unused")
	private boolean
	  m_playing;
	
	private ImagePanel
	  m_imagePanel;
	
	private JSlider
	  m_posSlider;
	
	private JButton
		m_stopButton,
		m_beginButton;
  
	private JToggleButton
	  m_playButton;
	
  public PreviewDialog(JFrame frame, Project prj, String title)
  {
  	super(frame, true);
  	m_prj = prj;
  	m_curFrame = 0;
  	m_numFrames = m_prj.getNumFrames();
  	m_playing = true;
  	
	  setTitle(title);
	  
		//setIconImage(EmailApp.s_iconImage);
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent evt)
			{
				onClose(evt);
			}
		});
		
		setBackground(Color.BLACK);
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setBorder(new EmptyBorder(5, 5, 5, 5));
		p.add(m_imagePanel = new ImagePanel(1,"Last Frame"), BorderLayout.CENTER);
		m_imagePanel.showGrid(false);
		m_imagePanel.setBackground(Color.BLACK);
		getContentPane().add(p, BorderLayout.CENTER);
		getContentPane().add(makeControlPanel(), BorderLayout.SOUTH);
		
		setSize(500, 500);
		restoreSizeAndPosition();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		m_delay = (int)(1000.0 / m_prj.getFps() + 0.5);
//		setTimer(m_delay);
		setImage(m_curFrame);
	}
  
  private JPanel makeControlPanel()
  {
  	JPanel
		  panel,
		  sliderPanel,
			butPanel;
  	
  	panel = new JPanel();
  	panel.setLayout(new BorderLayout());
  	
  	sliderPanel = new JPanel();
  	sliderPanel.setLayout(new BorderLayout());
//		sliderPanel.setBackground(Color.BLACK);
  	sliderPanel.add(m_posSlider = new JSlider(JSlider.HORIZONTAL, 0, m_numFrames - 1, 0), BorderLayout.CENTER);
  	m_posSlider.addChangeListener(this);
//  	m_posSlider.setMajorTickSpacing(10);
//  	m_posSlider.setPaintTicks(true);
  	m_posSlider.setPaintTrack(true);
  	
  	butPanel = new JPanel();
  	butPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//		butPanel.setBackground(Color.BLACK);
  	m_playButton = new JToggleButton(Capture.s_playIcon, false);
//  	m_playButton.setPressedIcon(Capture.s_pauseIcon);
		m_playButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
//System.out.println("play pressed, selected = " + m_playButton.isSelected());				
				if(!m_playButton.isSelected())
				{
					m_playButton.setIcon(Capture.s_playIcon);
					stopTimer();
				}
				else
				{
					m_playButton.setIcon(Capture.s_pauseIcon);
					setTimer(m_delay);
				}
			}
		});
  	butPanel.add(m_playButton);
  	butPanel.add(m_beginButton = new JButton(Capture.s_beginIcon));
		m_beginButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_curFrame = 0;
				m_posSlider.setValue(0);
				setImage(m_curFrame);
			}
		});
  	butPanel.add(m_stopButton = new JButton(Capture.s_stopIcon));
		m_stopButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				stopTimer();
				m_playButton.setSelected(false);
				m_playButton.setIcon(Capture.s_playIcon);
			}
		});
		
		panel.add(sliderPanel, BorderLayout.NORTH);
		panel.add(butPanel, BorderLayout.SOUTH);
		
		return panel;
  }
  
  @SuppressWarnings("unused")
private JPanel makeLeftPanel()
  {
  	JPanel
		  panel, butPanel;
  	
  	panel = new JPanel();
  	panel.setLayout(new BorderLayout());
  	
    	
  	butPanel = new JPanel();
  	butPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//		butPanel.setBackground(Color.BLACK);
  	m_playButton = new JToggleButton(Capture.s_playIcon, false);
//  	m_playButton.setPressedIcon(Capture.s_pauseIcon);
		m_playButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
//System.out.println("play pressed, selected = " + m_playButton.isSelected());				
				if(!m_playButton.isSelected())
				{
					m_playButton.setIcon(Capture.s_playIcon);
					stopTimer();
				}
				else
				{
					m_playButton.setIcon(Capture.s_pauseIcon);
					setTimer(m_delay);
				}
			}
		});
  	butPanel.add(m_playButton);
  	butPanel.add(m_beginButton = new JButton(Capture.s_beginIcon));
		m_beginButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_curFrame = 0;
				m_posSlider.setValue(0);
				setImage(m_curFrame);
			}
		});
  	butPanel.add(m_stopButton = new JButton(Capture.s_stopIcon));
		m_stopButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				stopTimer();
				m_playButton.setSelected(false);
				m_playButton.setIcon(Capture.s_playIcon);
			}
		});
		
		panel.add(butPanel, BorderLayout.SOUTH);
		
		return panel;
  }
  
	private void onClose(java.awt.event.WindowEvent evt)
	{
		doFrameClose();
	}
	
	private void doFrameClose()
	{
		saveSizeAndPosition();
		
		dispose();
	}
	
	public void setTimer(int delay)
	{
		if(m_timer != null)
		{
		  m_timer.stop();
		  m_timer = null;
		}

    if(delay > 0)
    {
//System.out.println("setting timout to " + delay + " minutes");    	
      m_timer = new Timer(delay, new ActionListener() 
      {
		  	public void actionPerformed(ActionEvent evt) 
			  {
		  		doNextFrame();
		  		if(m_timer != null)
			      m_timer.restart();
			  }
		  });
      m_timer.setRepeats(false);
		  m_timer.start();
    }
	}
	
	private void stopTimer()
	{
		if(m_timer != null)
		{
			m_timer.stop();
			m_timer = null;
		}
	}
	
	private void setImage(int frameNum)
	{
		try
		{
		  m_imagePanel.setImage(0, m_prj.getFrame(frameNum + 1));
		}
		catch(Exception e)
		{
			System.out.println("unable to read image " + frameNum);
			e.printStackTrace();
		}
	}

	private void doNextFrame()
	{
		if(m_curFrame < m_numFrames - 1)
		{
			m_posSlider.setValue(m_curFrame);
			m_curFrame++;
			setImage(m_curFrame);
		}
		else
		{
			m_curFrame = 0;
			m_posSlider.setValue(0);
			setImage(m_curFrame);
			stopTimer();
			m_playButton.setSelected(false);
			m_playButton.setIcon(Capture.s_playIcon);
		}
	}

	public void stateChanged(ChangeEvent e) 
	{
		m_curFrame = m_posSlider.getValue();
		setImage(m_curFrame);
	}
}
