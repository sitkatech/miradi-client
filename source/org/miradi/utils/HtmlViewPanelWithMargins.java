/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.utils;

import javax.swing.BorderFactory;

import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.main.MainWindow;

public class HtmlViewPanelWithMargins extends HtmlViewPanel
{
	public static HtmlViewPanelWithMargins createFromTextString(MainWindow mainWindowToUse, String titleToUse, String htmlText)
	{
		return new HtmlViewPanelWithMargins(mainWindowToUse, titleToUse, htmlText);
	}

	public static HtmlViewPanelWithMargins createFromHtmlFileName(MainWindow mainWindowToUse, String titleToUse, String htmlFileNameToUse) throws Exception
	{
		String html = Translation.getHtmlContent(htmlFileNameToUse);
		return new HtmlViewPanelWithMargins(mainWindowToUse, titleToUse, html);
	}

	private HtmlViewPanelWithMargins(MainWindow mainWindowToUse, String titleToUse, String htmlText)
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
