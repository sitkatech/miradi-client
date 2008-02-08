/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Component;

import org.conservationmeasures.eam.main.AppPreferences;
import org.martus.swing.UiScrollPane;

public class MiradiScrollPane extends UiScrollPane
{
	public MiradiScrollPane(Component view)
	{
		super(view);
		
		setBackground(AppPreferences.DARK_PANEL_BACKGROUND);
	}	
}
