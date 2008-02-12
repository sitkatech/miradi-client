/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fieldComponents;

import org.martus.swing.HyperlinkHandler;
import org.miradi.main.MainWindow;

public class HtmlPanelLabel extends HtmlFormViewer
{

	public HtmlPanelLabel(MainWindow mainWindow, String htmlSource, HyperlinkHandler hyperLinkHandler)
	{
		super(mainWindow, htmlSource, hyperLinkHandler);
	}

	public int getFontSize()
	{
		return getMainWindow().getDataPanelFontSize();
	}
	
	//TODO: Richard: should not use static ref here
	public String getFontFamily()
	{
		return getMainWindow().getDataPanelFontFamily();
	}
	
}
