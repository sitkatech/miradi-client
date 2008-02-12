/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.awt.Component;

import org.miradi.main.AppPreferences;

public class MiradiScrollPane extends FastScrollPane
{
	public MiradiScrollPane(Component view)
	{
		super(view);
		
		setBackground(AppPreferences.getDarkPanelBackgroundColor());
	}	
}
