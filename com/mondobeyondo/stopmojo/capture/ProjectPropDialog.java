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
package com.mondobeyondo.stopmojo.capture;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.mondobeyondo.stopmojo.util.BasicDialog;
import com.mondobeyondo.stopmojo.util.FieldPanel;
import com.mondobeyondo.stopmojo.util.Project;
import com.mondobeyondo.stopmojo.util.RTypeTextField;
import com.mondobeyondo.stopmojo.util.Util;

/**
 * @author Derry Bryson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")

public class ProjectPropDialog extends BasicDialog 
{
  private JButton
    m_helpBut,
    m_okBut,
    m_cancelBut;

  private JTextField
		m_fpsTextFieldE;
  private JTextField
	m_fpsTextFieldS;
//  private JButton
//	  m_browseBut;
  
  private JComboBox
	  m_formatComboBox;
  
  private Project
	  m_prj;
  
	public ProjectPropDialog(Frame parent, Project prj)
	{
		super(parent, true);
		init(prj);
	}
	
  private void init(Project prj)
  {
  	
  	m_prj = prj;

  	GridBagConstraints
  	  gbc;
  	  
    setTitle("Project Properties");

		JPanel padPanel = new JPanel();
		padPanel.setLayout(new GridBagLayout());
		getContentPane().add(padPanel, BorderLayout.CENTER);
		JScrollPane sp = new JScrollPane(makeFieldPanel());
		sp.setBorder(null);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		padPanel.add(sp, gbc);

		JPanel butPanel = new JPanel();
		butPanel.setLayout(new GridBagLayout());
		m_helpBut = new JButton("Help");
		m_helpBut.setMnemonic(KeyEvent.VK_H);
		m_helpBut.setIcon(Capture.s_helpIcon);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 5);
		gbc.weightx = 0;
		gbc.weighty = 1.0;
		butPanel.add(m_helpBut, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 5, 10, 5);
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		butPanel.add(new JPanel(), gbc);
		m_okBut = new JButton("Ok");
		m_okBut.setMnemonic(KeyEvent.VK_O);
		m_okBut.setIcon(Capture.s_okIcon);
		m_okBut.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				onOk();
			}
		});
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 5, 10, 10);
		gbc.weightx = 0;
		gbc.weighty = 1.0;
		butPanel.add(m_okBut, gbc);
		m_cancelBut = new JButton("Cancel");
		m_cancelBut.setMnemonic(KeyEvent.VK_C);
		m_cancelBut.setIcon(Capture.s_cancelIcon);
		m_cancelBut.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				dispose();
			}
		});
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 5, 10, 10);
		gbc.weightx = 0;
		gbc.weighty = 1.0;
		butPanel.add(m_cancelBut, gbc);

		getContentPane().add(butPanel, java.awt.BorderLayout.SOUTH);
		
		getRootPane().setDefaultButton(m_okBut);

		pack();
		setSize(500, 500);
		restoreSize();
		centerOnParent();
  }
  
  private JPanel makeFieldPanel()
  {
//		JLabel
//			label;
		int
		  i,
		  sel = -1;
		  
		FieldPanel fieldPanel = new FieldPanel();
		
		String[] 
	    formats =
		  {
				"jpg",
				"png",
        "tif",
				"bmp"
		  };
		m_formatComboBox = new JComboBox();
		for(i = 0, sel = 0; i < formats.length; i++)
			if(ImageIO.getImageWritersBySuffix(formats[i]).hasNext())
			{
				m_formatComboBox.addItem(formats[i]);
				if(formats[i].equals(m_prj.getImageFormat()))
					sel = i;
			}
		if(sel == formats.length)
			sel = 0;
		m_formatComboBox.setSelectedIndex(sel);
		fieldPanel.addField("Image File Format:", m_formatComboBox, 100);
    fieldPanel.addField("Frames Per Second:", m_fpsTextFieldE = new RTypeTextField("2I.DD,1 30", Util.formatNumber("#0.00", m_prj.getFps())), 40);
    fieldPanel.addField("Fotogramas per secundo", m_fpsTextFieldS = new RTypeTextField("2I.DD,1 30", Util.formatNumber("#0.00", m_prj.getFps())), 80);
    fieldPanel.done();
    		
		return fieldPanel;
  }
  
  private void onOk()
  {
    if(m_formatComboBox.getSelectedIndex() != -1)
      m_prj.setImageFormat((String)m_formatComboBox.getSelectedItem());
    else
    	m_prj.setImageFormat("");
    m_prj.setFps((float)Util.atof(m_fpsTextFieldE.getText()));
  	
    try
		{
      m_prj.write();
		}
    catch(Exception e)
		{
    	e.printStackTrace();
    	Capture.errorMsg(this, "Unable to write project!", "Error");
		}
    
  	dispose();
  }
}
