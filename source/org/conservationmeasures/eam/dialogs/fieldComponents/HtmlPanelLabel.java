/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import org.martus.swing.HyperlinkHandler;

public class HtmlPanelLabel extends HtmlFormViewer
{

	public HtmlPanelLabel(String htmlSource, HyperlinkHandler hyperLinkHandler)
	{
		super(htmlSource, hyperLinkHandler);
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
