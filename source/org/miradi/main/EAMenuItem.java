/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main;

import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.miradi.utils.LocationHolder;

public class EAMenuItem extends JMenuItem implements LocationHolder
{
    public EAMenuItem(Action action, int mnemonic, KeyStroke accelerator)
    {
        this(action, mnemonic);
        setAccelerator(accelerator);
    }
    
    public EAMenuItem(Action action, KeyStroke accelerator)
    {
        this(action);
        setAccelerator(accelerator);
    }
    
    public EAMenuItem(Action action, int mnemonic)
    {
        this(action);
        setMnemonic(mnemonic);
    }
    
    public EAMenuItem(Action action)
    {
        super(action);
    }
    
	public boolean hasLocation()
	{
		return false;
	}
    
	final static KeyStroke KEY_CTL_P = KeyStroke.getKeyStroke(KeyEvent.VK_P ,KeyEvent.CTRL_MASK,true);
}
