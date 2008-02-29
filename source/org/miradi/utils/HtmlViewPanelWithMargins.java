/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import javax.swing.BorderFactory;

import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.main.MainWindow;

public class HtmlViewPanelWithMargins extends HtmlViewPanel
{
	public HtmlViewPanelWithMargins(MainWindow mainWindowToUse, String titleToUse, Class classToUse, String htmlFileNameToUse)
	{
		super(mainWindowToUse, titleToUse, classToUse, htmlFileNameToUse, new DummyHandler());
	}
	
	public HtmlViewPanelWithMargins(MainWindow mainWindowToUse, String titleToUse, String htmlText)
	{
		super(mainWindowToUse, titleToUse, htmlText, new DummyHandler());
	}
	
	
	@Override
	protected void configureBodyComponent(HtmlFormViewer bodyComponent)
	{
		super.configureBodyComponent(bodyComponent);
		bodyComponent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}
}
