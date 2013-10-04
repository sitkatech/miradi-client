/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import javax.swing.JComponent;

import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.HtmlUtilities;

public class HtmlLinkField extends ObjectDataInputField
{
	public HtmlLinkField(MainWindow mainWindow, ORef refToUse, String tagToUse)
	{
		super(mainWindow.getProject(), refToUse, tagToUse);
		
		htmlFormViewer = new HtmlFormViewer(mainWindow, HtmlUtilities.wrapInHtmlTags("") , mainWindow.getHyperlinkHandler());
	}
	
	@Override
	public String getText()
	{
		return "";
	}

	@Override
	public void setText(String newValue)
	{
		if (newValue.length() > 0)
			newValue = wrapInHtmlAnchor(newValue);

		htmlFormViewer.setText(newValue);
	}

	private String wrapInHtmlAnchor(String url)
	{
		url = "<body bgcolor=\"" + AppPreferences.getDataPanelBackgroundColorForCss() + "\">" +  
		"<a href=\"" + url + "\">" + url +"</a>" +
		"</body>";

		url = HtmlUtilities.wrapInHtmlTags(url);
		return url;
	}
	
	@Override
	protected boolean shouldBeEditable()
	{
		return true;
	}

	@Override
	public JComponent getComponent()
	{
		return htmlFormViewer;
	}

	private HtmlFormViewer htmlFormViewer;
}
