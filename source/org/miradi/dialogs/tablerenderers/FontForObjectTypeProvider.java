/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.tablerenderers;

import java.awt.Font;

import org.miradi.main.EAM;

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
