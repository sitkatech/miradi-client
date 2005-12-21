/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import javax.swing.Action;
import javax.swing.JMenuItem;


public class MenuItemWithoutLocation extends JMenuItem implements LocationHolder
{
	public MenuItemWithoutLocation(Action action)
	{
		super(action);
	}
	
	public boolean hasLocation()
	{
		return false;
	}
}