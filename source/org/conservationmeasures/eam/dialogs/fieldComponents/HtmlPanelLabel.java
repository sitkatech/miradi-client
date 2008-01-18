/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.HyperlinkHandler;

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
