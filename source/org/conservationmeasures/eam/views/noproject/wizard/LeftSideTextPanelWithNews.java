/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.wizard.WizardHtmlViewer;
import org.martus.swing.HyperlinkHandler;

public class LeftSideTextPanelWithNews extends LeftSideTextPanel
{

	public LeftSideTextPanelWithNews(MainWindow mainWindow, String htmlToUse, HyperlinkHandler wizardToUse)
	{
		super(mainWindow, htmlToUse, wizardToUse);
		
		newsViewer = new WizardHtmlViewer(mainWindow, wizardToUse);
		newsViewer.setText(loadRemoteHtml());
		
		titleBorder = BorderFactory.createTitledBorder(EAM.text("Latest Miradi News"));
		titleBorder.setTitleFont(mainWindow.getUserDataPanelFont());
		newsViewer.setBorder(titleBorder);

		add(newsViewer, BorderLayout.AFTER_LAST_LINE);
	}
	
	public void refresh() throws Exception
	{
		super.refresh();
		titleBorder.setTitleFont(newsViewer.getMainWindow().getUserDataPanelFont());
		newsViewer.setText(newsViewer.getText());
	}
	
	public String loadRemoteHtml()
	{
		try
		{
			URL url = new URL("https://miradi.org/robots.txt");
			return readContext((InputStream)url.getContent());
		}
		catch(Exception e)
		{
			return "news unavaiable";
		}
	}
	
	public String readContext(InputStream inputStream) throws IOException
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
	
	TitledBorder titleBorder;
	WizardHtmlViewer newsViewer;
}
	
	

