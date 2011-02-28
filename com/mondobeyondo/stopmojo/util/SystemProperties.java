package com.mondobeyondo.stopmojo.util;
import java.awt.*;
import java.util.*;
import javax.swing.*;

//window.setContentPane(new SysPropListGUI());
//window.pack();
//window.setVisible(true);
@SuppressWarnings("serial")
public class SystemProperties extends JPanel {

	    private JTextArea m_propertiesTA;

	    /** Constructor sets layout, adds component(s), sets values*/
	    @SuppressWarnings({ "rawtypes", "unchecked" })
		public SystemProperties() {
	    	
	    	m_propertiesTA = new JTextArea(20, 40);
	    	Font font = new Font("Verdana", Font.BOLD, 22);
	    	m_propertiesTA.setFont(font);
	    	m_propertiesTA.setForeground(Color.BLUE);
	        this.setLayout(new BorderLayout());
	        this.add(new JScrollPane(m_propertiesTA), BorderLayout.CENTER);

	        //... Add property list data to text area.
	        Properties pr = System.getProperties();
	        TreeSet propKeys = new TreeSet(pr.keySet());  // TreeSet sorts keys
	        for (Iterator it = propKeys.iterator(); it.hasNext(); ) {
	            String key = (String)it.next();
	            m_propertiesTA.append("" + key + "=" + pr.get(key) + "\n");
	        }
	    }

}
