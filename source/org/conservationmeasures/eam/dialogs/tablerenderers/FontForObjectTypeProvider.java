/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.tablerenderers;

import java.awt.Font;

import org.conservationmeasures.eam.main.EAM;

abstract public class FontForObjectTypeProvider
{
	public FontForObjectTypeProvider()
	{
		plainFont = EAM.getMainWindow().getUserDataPanelFont();
	}
	
	public Font getFont(int objectType)
	{
		return getPlainFont();
	}

	public Font getPlainFont()
	{
		return plainFont;
	}
	
	private Font plainFont;
}
