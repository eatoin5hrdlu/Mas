/*
 * Created on Oct 24, 2003
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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Derry Bryson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")

public class FieldPanel extends JPanel 
{
  private int
    m_fieldCount;
    
  public FieldPanel()
  {
  	super();
  	setLayout(new GridBagLayout());
  	m_fieldCount = 0;
  }
 
	public void done()
	{
		done(true);
	}
	
  public void done(boolean addFill)
  { 
  	if(addFill)
  	{
  		GridBagConstraints gbc = new GridBagConstraints();
	  	gbc.gridx = 0;
		  gbc.gridy = m_fieldCount;
		  gbc.gridwidth = 4;
		  gbc.fill = GridBagConstraints.BOTH;
		  gbc.anchor = GridBagConstraints.WEST;
		  gbc.insets = new Insets(10, 10, 10, 10);
		  gbc.weightx = 1.0;
		  gbc.weighty = 10.0;
		  add(new JPanel(), gbc);
  	}
  }
  
	public void addField(String label, Component field, int ipadx)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = m_fieldCount;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 5, 0, 5);
		gbc.weightx = .1;
		gbc.weighty = .1;
		add(new JLabel(label, JLabel.RIGHT), gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = m_fieldCount;
		gbc.gridwidth = 3;
		gbc.ipadx = ipadx;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 0, 0, 5);
		gbc.weightx = 1.0;
		gbc.weighty = .1;
		add(field, gbc);
		
		m_fieldCount++;
	}
	
	public void addField(int x, Component field, int ipadx)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = m_fieldCount;
		gbc.gridwidth = 4;
		gbc.ipadx = ipadx;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 0, 0, 5);
		gbc.weightx = 1.0;
		gbc.weighty = .1;
		add(field, gbc);
		
		m_fieldCount++;
	}
	
	public void addField(Component field, int ipadx)
	{
		addField(1, field, ipadx);
	}
	
	public void addField(String label, Component field, int ipadx, Component field2, int ipadx2)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = m_fieldCount;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 5, 0, 5);
		gbc.weightx = .1;
		gbc.weighty = .1;
		add(new JLabel(label, JLabel.RIGHT), gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = m_fieldCount;
		gbc.ipadx = ipadx;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.weightx = .1;
		gbc.weighty = .1;
		add(field, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = m_fieldCount;
		gbc.ipadx = ipadx2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 0, 0, 5);
		gbc.weightx = .1;
		gbc.weighty = .1;
		add(field2, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 0, 0, 5);
		gbc.weightx = 10;
		gbc.weighty = .1;
		add(new JPanel(), gbc);
		
		m_fieldCount++;
	}
	
	public void addFieldC(String label, Component field, int ipadx, Component field2, int ipadx2)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = m_fieldCount;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 5, 0, 5);
		gbc.weightx = .1;
		gbc.weighty = .1;
		add(new JLabel(label, JLabel.RIGHT), gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = m_fieldCount;
		gbc.ipadx = ipadx;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.weightx = .1;
		gbc.weighty = .1;
		add(field, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = m_fieldCount;
		gbc.ipadx = ipadx2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 0, 0, 5);
		gbc.weightx = .1;
		gbc.weighty = .1;
		add(field2, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 0, 0, 5);
		gbc.weightx = 10;
		gbc.weighty = .1;
		add(new JPanel(), gbc);
		
		m_fieldCount++;
	}
}
