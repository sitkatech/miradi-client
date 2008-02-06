/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.SwingUtilities;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.wizard.WizardRightSideHtmlViewer;
import org.martus.swing.HyperlinkHandler;

public class NewsPanel extends WizardRightSideHtmlViewer
{
	public NewsPanel(MainWindow mainWindow, HyperlinkHandler hyperLinkHandler)
	{
		super(mainWindow, hyperLinkHandler);
		setBackground(AppPreferences.SIDEBAR_BACKGROUND);
		new NewsRetriever().start();
	}

	class NewsRetriever extends Thread
	{

		public void run()
		{
			newsHtml = loadRemoteHtml();
			SwingUtilities.invokeLater(new NewsUpdater());
		}
		
		private String loadRemoteHtml()
		{
			try
			{
				URL url = new URL("https://miradi.org/rest/latestnews");
				return readContext((InputStream)url.getContent());
			}
			catch(Exception e)
			{
				return "Miradi news is not available";
			}
		}
		
		private String readContext(InputStream inputStream) throws IOException
		{
			InputStreamReader isr = new InputStreamReader(inputStream);
			String returnLine = "";
			String thisLine;
			BufferedReader br = new BufferedReader(isr);
			while((thisLine = br.readLine()) != null)
			{
				returnLine = returnLine + thisLine;
			}
			return returnLine;
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
	}
	
	private String newsHtml;
}
