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
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.TreeSet;

import javax.media.CachingControlEvent;
import javax.media.Codec;
import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.DurationUpdateEvent;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.MediaTimeSetEvent;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RateChangeEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.SizeChangeEvent;
import javax.media.StartEvent;
import javax.media.StopTimeChangeEvent;
import javax.media.Time;
import javax.media.TransitionEvent;
import javax.media.UnsupportedPlugInException;
import javax.media.control.TrackControl;
import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.datasink.EndOfStreamEvent;
import javax.media.format.FormatChangeEvent;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.FileTypeDescriptor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

import com.mondobeyondo.stopmojo.util.BasicDialog;
import com.mondobeyondo.stopmojo.util.ExtensionFileFilter;
import com.mondobeyondo.stopmojo.util.FieldPanel;
import com.mondobeyondo.stopmojo.util.ImageDataSource;
import com.mondobeyondo.stopmojo.util.ProgressCodec;
import com.mondobeyondo.stopmojo.util.Project;
import com.mondobeyondo.stopmojo.util.ProjectFrameSource;
import com.mondobeyondo.stopmojo.util.SimpleListModelData;
import com.mondobeyondo.stopmojo.util.Util;

/**
 * @author Derry Bryson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")

public class ExportDialog extends BasicDialog implements ControllerListener, ActionListener, DataSinkListener 
{
	Processor
	  m_processor = null;
	
	ImageDataSource
	  m_dataSource = null;
	
	DataSink
	  m_dataSink = null;
	
	ContentDescriptor
	  m_contentDesc[];
	
	Format
	  m_formats[];
	
	JComboBox
	  m_fileTypeComboBox,
		m_encodingComboBox,
		m_bppComboBox;
	
	Project
	  m_project;
	
	JButton
	  m_helpBut,
		m_exportBut,
		m_cancelBut;
	
	boolean
	  m_exporting = false;
	
	String
	  m_filename;
	
  public ExportDialog(JFrame frame, Project project) throws Exception
  {
  	super(frame, true);
  	m_project = project;
	  setTitle("Export Movie");

	  m_dataSource = new ImageDataSource(m_project.getFps(), new ProjectFrameSource(m_project));
  	m_processor = Manager.createProcessor(m_dataSource);
  	m_processor.addControllerListener(this);

  	GridBagConstraints
		  gbc;
  	
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
		m_exportBut = new JButton("Export");
		m_exportBut.setMnemonic(KeyEvent.VK_E);
		m_exportBut.setIcon(Capture.s_okIcon);
		m_exportBut.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				onExport();
			}
		});
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 5, 10, 10);
		gbc.weightx = 0;
		gbc.weighty = 1.0;
		butPanel.add(m_exportBut, gbc);
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
		
		getRootPane().setDefaultButton(m_exportBut);

		pack();
		centerOnParent();
		m_processor.configure();
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
	
  private JPanel makeFieldPanel()
  {	  
		FieldPanel fieldPanel = new FieldPanel();

		m_fileTypeComboBox = new JComboBox();
		m_fileTypeComboBox.setEditable(false);
		m_fileTypeComboBox.addActionListener(this);
		fieldPanel.addField("File Type:", m_fileTypeComboBox, 50);
		
		m_encodingComboBox = new JComboBox();
		m_encodingComboBox.setEditable(false);
		m_encodingComboBox.addActionListener(this);
		fieldPanel.addField("Encoding:", m_encodingComboBox, 50);
		
		m_bppComboBox = new JComboBox();
		m_bppComboBox.setEditable(false);
		m_bppComboBox.addActionListener(this);
		fieldPanel.addField("Bits Per Pixel:", m_bppComboBox, 50);

		fieldPanel.done();
    		
		return fieldPanel;
  }
  
  private void cleanup()
  {  	
  	SwingUtilities.invokeLater(new Runnable() {
      public void run() {
      	if(m_dataSink != null)
      	{
//      		m_dataSink.removeDataSinkListener(this);
      		m_dataSink.close();
      	}
      	
      	if(m_processor != null)
      	{
//      		m_processor.removeControllerListener(this);
      		m_processor.close();
      	}
      	
//      	hide();
      	dispose();
      }
    });
  }
  
  private String getCurEncoding()
  {
  	VideoFormat
  	  vf = (VideoFormat)((SimpleListModelData)m_encodingComboBox.getSelectedItem()).getData();
  	
  	return vf.getEncoding();
  }
  
  private int getCurBpp()
  {
  	if(m_bppComboBox.getSelectedItem() != null)
  		return Util.atoi(((SimpleListModelData)m_bppComboBox.getSelectedItem()).getEntryText());
  	
  	return Format.NOT_SPECIFIED;
  }
  
  private VideoFormat getFormat()
  {
  	if(m_bppComboBox.getSelectedItem() != null)
    	return (VideoFormat)((SimpleListModelData)m_bppComboBox.getSelectedItem()).getData();
  	return (VideoFormat)((SimpleListModelData)m_encodingComboBox.getSelectedItem()).getData();
  }
  
  private void onExport()
  {
  	VideoFormat
		  vf = getFormat();
  	
  	if(vf == null)
  	{
  		JOptionPane.showMessageDialog(this, "Invalid format selected!");
  		return;
  	}
  	
//  	System.out.println("getFormat() = " + getFormat());
  	m_processor.getTrackControls()[0].setFormat(getFormat());
  	try 
		{
	    Codec codec[] = new Codec[1];
	    codec[0] = new ProgressCodec(new ProgressMonitor(this, "Exporting Frames", "", 1, m_project.getNumFrames()));
	    
	  	m_processor.getTrackControls()[0].setCodecChain(codec);
  	} 
  	catch (UnsupportedPlugInException e) 
		{
	    System.err.println("The process does not support effects.");
	  }

  	
  	JFileChooser
	    fc = new JFileChooser(Capture.getPref().get(Capture.PREF_LASTEXPORTFOLDER, ""));
	
  	ContentDescriptor
		  cd = (ContentDescriptor)((SimpleListModelData)m_fileTypeComboBox.getSelectedItem()).getData();
  	
    if(cd.getContentType().equals(FileTypeDescriptor.MSVIDEO))
  	  fc.addChoosableFileFilter(new ExtensionFileFilter("avi", "AVI Movie File (*.avi)"));  	
  	else if(cd.getContentType().equals(FileTypeDescriptor.QUICKTIME))
  	  fc.addChoosableFileFilter(new ExtensionFileFilter("mov", "Quicktime Movie File (*.mov)"));  	
	
	  int rc = fc.showSaveDialog(this);
	  if(rc == JFileChooser.APPROVE_OPTION)
	  {
	  	Capture.getPref().put(Capture.PREF_LASTEXPORTFOLDER, fc.getSelectedFile().getAbsolutePath());
	  	
	  	String
			  ext = Util.getFileExtension(fc.getSelectedFile());
	  	
		  m_filename = "file:" + fc.getSelectedFile().getAbsolutePath();

		  if(ext.equals("") && cd.getContentType().equals(FileTypeDescriptor.MSVIDEO))
	    	m_filename += ".avi";
	    else if(ext.equals("") && cd.getContentType().equals(FileTypeDescriptor.QUICKTIME))
	    	m_filename += ".mov";
	  	
    	setCursor(new Cursor(Cursor.WAIT_CURSOR));
    	setEnabled(false);
    	
    	m_exporting = true;
    	m_processor.realize();
//    	hide();
	  }
  }
  
  private void fillFileTypeComboBox()
  {
  	int
		  count = 0,
		  sel = 0;
  	
		DefaultComboBoxModel
  		cm = new DefaultComboBoxModel();
						  
  	m_contentDesc = m_processor.getSupportedContentDescriptors();
  	for(int i = 0; i < m_contentDesc.length; i++)
  	{
  		if(m_contentDesc[i].getContentType().equals(FileTypeDescriptor.MSVIDEO))
  		{
  			count++;
  	    cm.addElement(new SimpleListModelData("AVI", m_contentDesc[i]));
  		}
  		else if(m_contentDesc[i].getContentType().equals(FileTypeDescriptor.QUICKTIME))
  		{
  			sel = count;
  			count++;
  	    cm.addElement(new SimpleListModelData("QUICKTIME", m_contentDesc[i]));
  		}
  	}
  	m_fileTypeComboBox.setModel(cm);
  	m_fileTypeComboBox.setSelectedIndex(sel);
  }
  
  private void fillEncodingComboBox()
  {
  	int 
		  i = m_fileTypeComboBox.getSelectedIndex();
  	
		TreeSet<?>
		  encodings = new TreeSet<Object>();
		
  	if(i != -1)
  	{
//System.out.println("get formats");  		
      m_processor.setContentDescriptor((ContentDescriptor)((SimpleListModelData)m_fileTypeComboBox.getSelectedItem()).getData());
  		TrackControl tc[] = m_processor.getTrackControls();
  	  m_formats = tc[0].getSupportedFormats();
  	  
  	  TreeSet<SimpleListModelData>
 	      set = new TreeSet<SimpleListModelData>();
	
	    DefaultComboBoxModel
 		    cm = new DefaultComboBoxModel();
	
	    for(i = 0; i < m_formats.length; i++)
	    {
	  	  if(m_formats[i] instanceof VideoFormat)
	  	  {
	  		  VideoFormat
				    vf = (VideoFormat)m_formats[i];
		  	
//System.out.println("saw vf = " + vf.toString());	  		
  			  set.add(new SimpleListModelData(vf.getEncoding().toUpperCase(), vf));
  	  	}
	    }
		  
	    Iterator<SimpleListModelData> it = set.iterator();
 	    while(it.hasNext())
 	  	  cm.addElement(it.next());
  	  
  	  m_encodingComboBox.setModel(cm);
  	  m_encodingComboBox.setSelectedIndex(0);
  	}
  }
  
  private void fillBppComboBox()
  {
//System.out.println("filling bpp combo box");	  	
	  TreeSet<SimpleListModelData>
 	    set = new TreeSet<SimpleListModelData>();
	
	  DefaultComboBoxModel
 		  cm = new DefaultComboBoxModel();
	
	  for(int i = 0; i < m_formats.length; i++)
	  {
	  	if(m_formats[i] instanceof RGBFormat)
	  	{
	  		RGBFormat
				  vf = (RGBFormat)m_formats[i];
		  	
//System.out.println("saw vf = " + vf.toString());	  		
	  		if(vf.getEncoding().equals(getCurEncoding()))
	  			if(vf.getBitsPerPixel() != -1)
	  			  set.add(new SimpleListModelData("" + vf.getBitsPerPixel(), vf));
	  			else 
	  			  set.add(new SimpleListModelData("<Not Specified>", vf));
	  	}
	  }
		  
	  Iterator<SimpleListModelData> it = set.iterator();
 	  while(it.hasNext())
 	  	cm.addElement(it.next());
  	  
 	  m_bppComboBox.setModel(cm);
	  if(cm.getSize() > 0)
	  {
   	  m_bppComboBox.setSelectedIndex(0);
   	  m_bppComboBox.setEnabled(true);
	  }
	  else
	  	m_bppComboBox.setEnabled(false);
  }
  
  public synchronized void controllerUpdate(ControllerEvent event) 
  {
//System.out.println("controllerUpdate: " + event.toString());  	
    if(event instanceof ConfigureCompleteEvent) 
    {
      processConfigureComplete((ConfigureCompleteEvent)event);
    }
    else if(event instanceof RealizeCompleteEvent) 
    {
      processRealizeComplete((RealizeCompleteEvent)event);
    }
    else if(event instanceof PrefetchCompleteEvent) 
    {
      processPrefetchComplete((PrefetchCompleteEvent)event);
    }
    else if(event instanceof ControllerErrorEvent) 
    {
      processControllerError((ControllerErrorEvent)event);
    }
    else if(event instanceof ControllerClosedEvent ) 
    {
      processControllerClosed ( (ControllerClosedEvent) event );
    }
    else if(event instanceof DurationUpdateEvent) 
    {
      Time t = ((DurationUpdateEvent)event).getDuration();
    }
    else if(event instanceof CachingControlEvent) 
    {
//      processCachingControl((CachingControlEvent)event);
    }
    else if(event instanceof StartEvent) 
    {
    }
    else if(event instanceof MediaTimeSetEvent) 
    {
    }
    else if(event instanceof TransitionEvent)     
    {
      if (event instanceof EndOfMediaEvent) 
      {
//      	System.out.println("endofmedia");
  	    event.getSourceController().stop();
  	    event.getSourceController().close();
//  	    cleanup();
  	  }  
    }
    else if(event instanceof RateChangeEvent) 
    {
    }
    else if(event instanceof StopTimeChangeEvent) 
    {
    }
    else if(event instanceof FormatChangeEvent) 
    {
//      processFormatChange((FormatChangeEvent) event);
    }
    else if(event instanceof SizeChangeEvent) 
    {
    }
    else if(event.getClass().getName().endsWith("ReplaceURLEvent")) 
    {
//      processReplaceURL ( event );
    }
  }

  protected void processConfigureComplete(ConfigureCompleteEvent event) 
  {
//System.out.println("processRealizeComplete");
//  	m_processor.prefetch();
  	fillFileTypeComboBox();
  }

  protected void processRealizeComplete(RealizeCompleteEvent event) 
  {
//System.out.println("processRealizeComplete");
//  	m_processor.prefetch();
    try
	  {
      m_dataSink = Manager.createDataSink(m_processor.getDataOutput(), new MediaLocator(m_filename));
      
      if(m_dataSink == null)
      {
  	    JOptionPane.showMessageDialog(this, "Unable to create data sink!", "Error", JOptionPane.ERROR_MESSAGE);
  	    cleanup();
      }
      
      m_dataSink.addDataSinkListener(this);
      m_dataSink.open();
      m_processor.start();
      m_dataSink.start();
	  }
    catch(Exception e)
	  {
  	  e.printStackTrace();
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    cleanup();
	  }
  }

  protected void processPrefetchComplete ( PrefetchCompleteEvent event ) 
  {
//System.out.println("processPrefetchComplete");
  	m_processor.start();
  }

  protected void processFormatChange ( FormatChangeEvent event ) 
  {
  }

  protected void processControllerError ( ControllerErrorEvent event ) 
  {
  	JOptionPane.showMessageDialog(this, event.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    cleanup();
  }

  protected void processControllerClosed ( ControllerClosedEvent event ) 
  {
  }

  protected void processCachingControl ( CachingControlEvent event ) 
  {
  }

  protected void processReplaceURL ( ControllerEvent event ) 
  {
  }
  
  public void actionPerformed(ActionEvent e) 
  {
  	if(e.getSource() == m_fileTypeComboBox)
	  {
		  fillEncodingComboBox();
	  }
  	else if(e.getSource() == m_encodingComboBox)
  	{
  		fillBppComboBox();
  	}
  }

  public void dataSinkUpdate(DataSinkEvent e) 
  {
  	if(e instanceof DataSinkErrorEvent)
  	{
   		JOptionPane.showMessageDialog(this, "Error writing file!", "Error", JOptionPane.ERROR_MESSAGE);
	    cleanup();
  	}
  	else if(e instanceof EndOfStreamEvent)
  	{
//  		System.out.println("datasink: endofstream");
  		m_dataSink.close();
  		cleanup();
  	}
  }
  

}