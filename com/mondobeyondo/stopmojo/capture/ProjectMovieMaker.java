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

import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.ResourceUnavailableEvent;
import javax.media.control.TrackControl;
import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.datasink.EndOfStreamEvent;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;

import com.mondobeyondo.stopmojo.util.ImageDataSource;
import com.mondobeyondo.stopmojo.util.Project;
import com.mondobeyondo.stopmojo.util.ProjectFrameSource;

/**
 * @author Derry Bryson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ProjectMovieMaker implements ControllerListener, DataSinkListener 
{
	Processor
	  m_processor = null;
	
	ImageDataSource
	  m_dataSource = null;
	
	DataSink
	  m_dataSink = null;
	
	Project
	  m_project;
	
	String
	  m_contentDesc,
	  m_encoding,
	  m_filename;
	
	int
	  m_bpp;
	
  public ProjectMovieMaker(Project project, String contentDesc, 
  		                     String encoding, String filename, int bpp) throws Exception
  {
  	m_project = project;
  	m_contentDesc = contentDesc;
  	m_encoding = encoding;
  	m_filename = "file:" + filename;
  	m_bpp = bpp;
  }
  
  public void doit() throws Exception
  {
	  m_dataSource = new ImageDataSource(m_project.getFps(), new ProjectFrameSource(m_project));
  	m_processor = Manager.createProcessor(m_dataSource);
  	m_processor.addControllerListener(this);

  	m_processor.configure();
  	if(!waitForState(m_processor, Processor.Configured)) 
  	{
  		throw new ProjectMovieMakerException("Unable to configure processor!");
  	}

  	m_processor.setContentDescriptor(new ContentDescriptor(m_contentDesc));
  	
  	TrackControl[] tc = m_processor.getTrackControls();
    Format[] formats = tc[0].getSupportedFormats();
    
  	int
		  i;
  	
  	for(i = 0; i < formats.length; i++)
  		if(formats[i] instanceof VideoFormat)
  		{
      	VideoFormat
		      vf = (VideoFormat)formats[i];
   
//      	System.out.println("checking " + vf.getEncoding() + " against " + m_encoding);
      	if(vf.getEncoding().equalsIgnoreCase(m_encoding))
      	  break;
  		}
  		
  	if(i < formats.length)
  		tc[0].setFormat(formats[i]);
		else
  		throw new ProjectMovieMakerException("Invalid encoding!");
  	
   	m_processor.realize();
   	if(!waitForState(m_processor, Processor.Realized)) 
  		throw new ProjectMovieMakerException("Unable to realize processor!");
    	
   	try
		{
      m_dataSink = Manager.createDataSink(m_processor.getDataOutput(), new MediaLocator(m_filename));
      
      if(m_dataSink == null)
    		throw new ProjectMovieMakerException("Unable to create data sink!");
      
      m_dataSink.addDataSinkListener(this);
      m_dataSink.open();
      m_processor.start();
      m_dataSink.start();
        
      waitForFileDone();
      m_dataSink.close();
      m_processor.close();
		}
   	catch(Exception e)
		{
  		throw new ProjectMovieMakerException("Error createing movie!", e);
		}
  }
  
  Object m_waitSync = new Object();
  boolean m_stateTransitionOK = true;

  /**
   * Block until the processor has transitioned to the given state.
   * Return false if the transition failed.
   */
  boolean waitForState(Processor p, int state) 
  {
    synchronized(m_waitSync) 
		{
      try 
			{
	      while(p.getState() < state && m_stateTransitionOK)
	        m_waitSync.wait();
      } catch (Exception e) {}
    }
    return m_stateTransitionOK;
  }

  public void controllerUpdate(ControllerEvent evt) 
  {
    if(evt instanceof ConfigureCompleteEvent ||
       evt instanceof RealizeCompleteEvent ||
       evt instanceof PrefetchCompleteEvent) 
    {
      synchronized(m_waitSync) 
			{
	      m_stateTransitionOK = true;
	      m_waitSync.notifyAll();
      }
    } 
    else if(evt instanceof ResourceUnavailableEvent) 
    {
      synchronized(m_waitSync) 
		  {
	      m_stateTransitionOK = false;
	      m_waitSync.notifyAll();
      }
    } 
    else if(evt instanceof EndOfMediaEvent) 
    {
      evt.getSourceController().stop();
      evt.getSourceController().close();
    }
  }

  Object m_waitFileSync = new Object();
  boolean m_fileDone = false;
  boolean m_fileSuccess = true;

  boolean waitForFileDone() 
  {
    synchronized(m_waitFileSync) 
		{
      try 
			{
	      while(!m_fileDone)
	        m_waitFileSync.wait();
      } 
      catch (Exception e) 
			{
      }
    }
    return m_fileSuccess;
  }

  public void dataSinkUpdate(DataSinkEvent evt) 
  {
    if(evt instanceof EndOfStreamEvent) 
    {
      synchronized(m_waitFileSync) 
			{
	      m_fileDone = true;
	      m_waitFileSync.notifyAll();
      }
    } 
    else if(evt instanceof DataSinkErrorEvent) 
    {
      synchronized(m_waitFileSync) 
			{
	      m_fileDone = true;
	      m_fileSuccess = false;
	      m_waitFileSync.notifyAll();
      }
    }
  }  
}
