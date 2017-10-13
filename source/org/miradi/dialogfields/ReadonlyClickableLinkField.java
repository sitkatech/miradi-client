/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.HtmlUtilities;

public class ReadonlyClickableLinkField extends ObjectDataInputField
{
	public ReadonlyClickableLinkField(MainWindow mainWindow, ORef refToUse, String tagToUse)
	{
		super(mainWindow.getProject(), refToUse, tagToUse);
		
		htmlFormViewer = new StaticHtmlDataPanelViewer(mainWindow, mainWindow.getHyperlinkHandler());
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
		url = "<body>" +  
		"<a href=\"" + url + "\">" + url +"</a>" +
		"</body>";

		url = HtmlUtilities.wrapInHtmlTags(url);
		return url;
	}
	
	@Override
	protected boolean shouldBeEditable()
	{
		//NOTE: Returning true so that the foreground is not grayed out. This field's component is an html viewer that is not editable
		return true;
	}

	@Override
	public JComponent getComponent()
	{
		return htmlFormViewer;
	}
	
	private StaticHtmlDataPanelViewer htmlFormViewer;
}
