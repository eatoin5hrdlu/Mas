/*
 * Created on Apr 30, 2005
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
package com.mondobeyondo.stopmojo.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

/**
 * @author derry
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PluginManager 
{
	private Vector<Plugin>
	  m_plugins;
	
  public PluginManager()
  {
  }
  
  public int LoadPlugins(String path) throws Exception
	{
  	m_plugins = new Vector<Plugin>();
  	
  	File
		  dir = new File(path);
  	
  	String[]
			files = dir.list(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return name.endsWith(".jar");
      }});
  	
  	for(int i = 0; i < files.length; i++)
  	{
  		File
			  pluginFile = new File(path, files[i]);
  		
  	  try
			{
//  	  System.out.println("classpath=file:///" + pluginFile.getAbsolutePath());
      	URLClassLoader loader = new URLClassLoader(new URL[] {new URL("file:///" + pluginFile.getAbsolutePath())}, this.getClass().getClassLoader());
//  System.out.println("loading class " + files[i].substring(0, files[i].indexOf(".jar")));    	
//        Class plugin = loader.loadClass(files[i].substring(0, files[i].indexOf(".jar")));
    	  @SuppressWarnings("unchecked")
		Class<Object> plugin = (Class<Object>) Class.forName(files[i].substring(0, files[i].indexOf(".jar")), true, loader);
  	  	m_plugins.add((Plugin) plugin.newInstance());
			}
  	  catch(UnsatisfiedLinkError er)
			{
  		  	System.out.println("Unsatisfied link error loading "+ pluginFile.getAbsolutePath());
			}
  	  catch (Exception e)
			{
		  	System.out.println("Exception (other than Unsatisfied link error) "+ pluginFile.getAbsolutePath());
		  	e.printStackTrace();  	  	
			}
  	}
  	
  	return m_plugins.size();
	}
  
  public Vector<Plugin> getPlugins()
  {
  	return m_plugins;
  }
  
  public Vector<Plugin> getPlugins(Class<?> c)
  {
    Vector<Plugin>
		  v = new Vector<Plugin>();
    
    for(int i = 0; i < m_plugins.size(); i++)
    {
    	if(c.isAssignableFrom(m_plugins.elementAt(i).getClass()))
    		v.add((Plugin)m_plugins.elementAt(i));
    }
    	
    return v;
  }
  
  public void disposeAll()
  {
  	if(m_plugins != null)
  		return;
  	
  	for(int i = 0; i < m_plugins.size(); i++)
  	{
  		Plugin 
			  p = (Plugin)m_plugins.elementAt(i);
  		
  		p.dispose();
  	}
  }
}
