/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.wizard.noproject;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.SwingUtilities;

import org.martus.swing.HyperlinkHandler;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.main.VersionConstants;
import org.miradi.utils.RemoteHtmlRetriever;
import org.miradi.utils.Translation;
import org.miradi.wizard.WizardRightSideHtmlViewer;

public class NewsPanel extends WizardRightSideHtmlViewer
{
	public NewsPanel(MainWindow mainWindow, HyperlinkHandler hyperLinkHandler, MainNewsPanel mainNewsPanelToUse) throws Exception
	{
		super(mainWindow, hyperLinkHandler);
		
		mainNewsPanel = mainNewsPanelToUse;
		setBackground(AppPreferences.getSideBarBackgroundColor());
		try
		{
			new NewsRetriever(constructUrl()).start();
		}
		catch(MalformedURLException e)
		{
			EAM.logException(e);
		}
	}
	
	private static URL constructUrl() throws Exception
	{
		final String VERSION_HEADER = "version=" + VersionConstants.VERSION_STRING;
		final String BUILD_HEADER = "build=" + VersionConstants.TIMESTAMP_STRING;
		final String NEWS_ADDRESS = "https://miradi.org/rest/latestnews";
		
		return new URL((NEWS_ADDRESS + "?" + VERSION_HEADER + "&"+  BUILD_HEADER));
	}
	
	class NewsRetriever extends RemoteHtmlRetriever
	{
		public NewsRetriever(URL url)
		{
			super(url);
		}

		public void run()
		{
			super.run();
			newsHtml = getResults();
			if(newsHtml == null)
				newsHtml = getNoNewsText();
			SwingUtilities.invokeLater(new NewsUpdater());
		}
		
		private String getNoNewsText() 
		{
			try
			{
				return Translation.getHtmlContent("NoNews.html");
			}
			catch(Exception e)
			{
				EAM.logException(e);
				return "Miradi news is not available";

			}
		}
		
	}
	
	private class NewsUpdater implements Runnable
	{
		public void run()
		{
			updateText();
		}
		
	}
	
	
	private void updateText()
	{
		setText("<div class='navigation'>" + newsHtml);
		mainNewsPanel.updateNewNewsText(newsHtml);
	}
	
	private String newsHtml;
	private MainNewsPanel mainNewsPanel;
}
