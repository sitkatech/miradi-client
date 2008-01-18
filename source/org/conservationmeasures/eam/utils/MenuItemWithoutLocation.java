/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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