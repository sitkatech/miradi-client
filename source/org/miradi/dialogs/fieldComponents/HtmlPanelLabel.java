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
package org.miradi.dialogs.fieldComponents;

import org.martus.swing.HyperlinkHandler;
import org.miradi.main.MainWindow;

public class HtmlPanelLabel extends HtmlFormViewer
{

	public HtmlPanelLabel(MainWindow mainWindow, String htmlSource, HyperlinkHandler hyperLinkHandler)
	{
		super(mainWindow, htmlSource, hyperLinkHandler);
	}

	@Override
	public int getFontSize()
	{
		return getMainWindow().getDataPanelFontSize();
	}
	
	//TODO should not use static ref here
	@Override
	public String getFontFamilyCode()
	{
		return getMainWindow().getDataPanelFontFamily();
	}
	
}
