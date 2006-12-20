/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class EAMenuItem extends JMenuItem
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
    
	final static KeyStroke KEY_CTL_P = KeyStroke.getKeyStroke(KeyEvent.VK_P ,KeyEvent.CTRL_MASK,true);
}
