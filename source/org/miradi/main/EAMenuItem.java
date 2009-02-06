/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
