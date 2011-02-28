/*
 * @(#)CDSWrapper.java	1.3 01/03/13
 *
 * Copyright (c) 1999-2001 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

package com.mondobeyondo.stopmojo.util;

import javax.media.*;
import javax.media.protocol.*;
import java.io.IOException;
import javax.media.control.FormatControl;

public class CDSWrapper extends PushBufferDataSource
    implements CaptureDevice {

    private PushBufferDataSource inputDS = null;
    private boolean firstConnect = true;
    private boolean firstDisconnect = true;
    
    public CDSWrapper(PushBufferDataSource incoming) {
	this.inputDS = incoming;
    }

    public void connect() throws IOException {
	if (firstConnect) {
	    inputDS.connect();
	    firstConnect = false;
	}
    }

    public void disconnect() {
	// Don't disconnect input first time
	if (firstDisconnect) {
	    firstDisconnect = false;
	} else {
	    close();
	}
    }

    public void close() {
	// If its been connected atleast once, disconnect the input
	if (!firstConnect) {
	    inputDS.disconnect();
	}
    }

    public MediaLocator getLocator() {
	return inputDS.getLocator();
    }
    
    public String getContentType() {
	return inputDS.getContentType();
    }

    public void start() throws IOException {
	inputDS.start();
    }

    public void stop() throws IOException {
	inputDS.stop();
    }

    /****************************************************************
     * PushBufferDataSource
     ****************************************************************/
    
    public PushBufferStream [] getStreams() {
	return inputDS.getStreams();
    }

    /****************************************************************
     * CaptureDevice
     ****************************************************************/
    
    public FormatControl[] getFormatControls() {
	return ((CaptureDevice)inputDS).getFormatControls();
    }

    public CaptureDeviceInfo getCaptureDeviceInfo() {
	return ((CaptureDevice)inputDS).getCaptureDeviceInfo();
    }

    /****************************************************************
     * Controls
     ****************************************************************/

    public Object [] getControls() {
	return inputDS.getControls();
    }

    public Object getControl(String ctype) {
	return inputDS.getControl(ctype);
    }
    
    /****************************************************************
     * Duration
     ****************************************************************/

    public Time getDuration() {
	return inputDS.getDuration();
    }
}

