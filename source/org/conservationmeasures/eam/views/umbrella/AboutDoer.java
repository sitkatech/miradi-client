/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.util.EventObject;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.VersionConstants;
import org.conservationmeasures.eam.utils.HtmlViewPanel;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class AboutDoer extends MainWindowDoer 
{
	public AboutDoer()
	{
	}
	
	public boolean isAvailable()
	{
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		doIt(null);
	}
	
	public void doIt(EventObject event) throws CommandFailedException
	{
		String title = EAM.text("Title|About Miradi");
		String text =  buildMainSection();
		
		boolean initialSplash = (event == null);
		if(!initialSplash)
		{
			text += loadHtmlFile("HelpAboutExtra.html");
		}
		
		HtmlViewPanel dialog = new HtmlViewPanel(getMainWindow(), title, text, 900);
		if(initialSplash)
			dialog.setCloseButtonText(EAM.text("Continue"));
		dialog.showAsOkDialog();
	}

	
	private String buildMainSection()
	{
		String text = loadHtmlFile("AboutPart1.html");
		text +=  VersionConstants.VERSION_STRING;
		text += loadHtmlFile("AboutPart2.html");
		return text;
	}
	
	
	private String loadHtmlFile(String htmlFile)
	{
		try
		{
			return EAM.loadResourceFile(getClass(), htmlFile);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

}
