/*
 * Created on Nov 14, 2003
 *
 * Copyright (c) 2005 Derone Bryson
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

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * @author Derry Bryson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RTypeDocumentFilter extends DocumentFilter 
{
	private String
	  m_rtype,
  	m_valset;
	  
  @SuppressWarnings("unused")
private double
	  m_min,
	  m_max;
	  
  @SuppressWarnings("unused")
private int
	  m_maxlen,
	  m_ints,
	  m_decs,
	  m_digs;
	  
  private boolean
	  m_upper;
	  
  public RTypeDocumentFilter(String rtype)
  {
		int 
			i;
		
		m_rtype = rtype;
		m_valset = "";
		m_upper = false;
		
		switch(Character.toUpperCase(m_rtype.charAt(0)))
		{
			case 'U':
				m_upper = true;
			  
			case 'A':
				m_maxlen = Util.atoi(m_rtype.substring(1));
				i = Util.Pos(',', m_rtype);
				if(i > 0)
					m_valset = m_rtype.substring(i);
			break;
		  
			case 'S':
			break;

			case 'C':
				m_maxlen = 8;
			break;

			case 'Y':
				m_maxlen = 1;
				m_upper = true;
			break;

			case '0': case '1': case '2': case '3': case '4': case '5':
			case '6': case '7': case '8': case '9': case 'I': case 'D':
			case 'Z': case 'F':
			{
				String
					spec;
				  
				if(Util.Pos(',', m_rtype) != 0)
					spec = m_rtype.substring(0, Util.Pos(',', m_rtype) - 1);
				else
					spec = m_rtype;
				if(m_rtype.charAt(m_rtype.length() - 1) == ',')
				{
					/*No ranges specified, assume maximum in both directions.*/
					m_min = -9999999999.99;
					m_max = 9999999999.99;
				}
				else
				{
					/*Range specified, parse and set.*/
					String range = m_rtype.substring(Util.Pos(',', m_rtype));
					m_min = Util.atof(range);
					range = range.substring(Util.Pos(' ', range));
					m_max = Util.atof(range);
				}
				PatternSize patsize = new PatternSize();
				m_maxlen = Util.Num_Pattern_Size(spec, patsize);
				m_ints = patsize.Ints;
				m_decs = patsize.Decs;
				m_digs = patsize.Digs;
				m_valset = "0123456789";
				if(m_min < 0.0)
				  m_valset += "-";
				if(m_decs > 0)
				  m_valset += ".";
			}
			break;
		} /* end switch */
		
//		System.out.println("m_maxlen = " + m_maxlen);
  }
  
  private String validChars(String string)
  {
  	if(m_valset.equals(""))
  	  return string;
  	  
  	int
  	  i;
  	  
  	StringBuffer
  	  sb = new StringBuffer();
  	  
  	for(i = 0; i < string.length(); i++)
  	{
  		if(m_valset.indexOf(string.charAt(i)) != -1)
  		  sb.append(string.charAt(i));
  		else
  			Toolkit.getDefaultToolkit().beep();
  	}
  	
  	return sb.toString();
  }
  
	public void insertString(DocumentFilter.FilterBypass fb,
													 int offset,
													 String string,
													 AttributeSet attr)
										throws BadLocationException
  {
		if(m_upper)
			string = string.toUpperCase();
  	  
  	StringBuffer
  	  sb = new StringBuffer(fb.getDocument().getText(0, fb.getDocument().getLength()));
  	  										
  	string = validChars(string);
  	
  	if(!string.equals(""))
  	{
      sb.insert(offset, string);  	  
			if(sb.toString().length() <= m_maxlen)
        super.insertString(fb, offset, string, attr);
			else
				Toolkit.getDefaultToolkit().beep();
  	}						 
  }
										
	public void replace(DocumentFilter.FilterBypass fb,
											int offset,
											int length,
											String string,
											AttributeSet attr)
							 throws BadLocationException
  {										
  	if(m_upper)
  	  string = string.toUpperCase();
  	  
		StringBuffer
			sb = new StringBuffer(fb.getDocument().getText(0, fb.getDocument().getLength()));
  	  										 
		string = validChars(string);
  	
		if(!string.equals("") )
		{
  		sb.replace(offset, offset + length, string);
		  if(sb.toString().length() <= m_maxlen)
			  super.replace(fb, offset, length, string, attr);
			else
				Toolkit.getDefaultToolkit().beep();
		}						 
  }
}
