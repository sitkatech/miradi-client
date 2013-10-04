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
import javax.swing.text.html.StyleSheet;

import org.martus.swing.HyperlinkHandler;
import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.HtmlUtilities;

public class ReadonlyClickableLinkField extends ObjectDataInputField
{
	public ReadonlyClickableLinkField(MainWindow mainWindow, ORef refToUse, String tagToUse)
	{
		super(mainWindow.getProject(), refToUse, tagToUse);
		
		//NOTE: passing anything other than "" so that cursor changes to index finger when mouse is over clickable link
		htmlFormViewer = new DataPanelHtmlFormViewer(mainWindow, HtmlUtilities.wrapInHtmlTags("") , mainWindow.getHyperlinkHandler());
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
		//NOTE: Returning true so that the foreground is grayed out. This field's component is an html viewer that is not editable
		return true;
	}

	@Override
	public JComponent getComponent()
	{
		return htmlFormViewer;
	}
	
	private class DataPanelHtmlFormViewer extends HtmlFormViewer
	{
		public DataPanelHtmlFormViewer(MainWindow mainWindow, String wrapInHtmlTags, HyperlinkHandler hyperlinkHandler)
		{
			super(mainWindow, wrapInHtmlTags, hyperlinkHandler);
		}

		@Override
		public void customizeStyleSheet(StyleSheet style)
		{
			super.customizeStyleSheet(style);
			
			style.addRule("body {background-color: " + AppPreferences.getDataPanelBackgroundColorForCss() + ";}");
		}	
	}

	private DataPanelHtmlFormViewer htmlFormViewer;
}
