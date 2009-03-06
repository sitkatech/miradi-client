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
package org.miradi.views.umbrella;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.VersionConstants;
import org.miradi.utils.Translation;
import org.miradi.views.MainWindowDoer;

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
		String text =  buildMainSection();
		text += loadHtmlFile("AboutExtra.html");
		text += buildEndSection();
		
		HelpAboutPanel dialog = new HelpAboutPanel(getMainWindow(), text);
		dialog.showAsOkDialog();
	}

	public static String buildEndSection()
	{
		return loadHtmlFile("AboutEnd.html");
	}
	
	public static String buildMainSection()
	{
		String text = loadHtmlFile("AboutPart1.html");
		text += "<p>";
		{
			String template = EAM.text("<strong>Version: %s</strong>");
			try
			{
				String translationVersion = VersionConstants.getVersionAndTimestamp();
				text += " " + EAM.substitute(template, translationVersion);
			}
			catch(Exception e)
			{
				EAM.logError("Unable to determine Miradi version number");
			}
		}
		if(!Translation.isDefaultLocalization())
		{
			String textToDisplay = EAM.text("(Translation: %code %date)");
			textToDisplay = EAM.substitute(textToDisplay, "%code", Translation.getCurrentLanguageCode());
			textToDisplay = EAM.substitute(textToDisplay, "%date", EAM.text(Translation.TRANSLATION_VERSION_KEY));
			text += " " + textToDisplay;
		}
		text += "</p>";
		text += loadHtmlFile("AboutPart2.html");
		return text;
	}
	
	
	private static String loadHtmlFile(String htmlFile)
	{
		try
		{
			htmlFile = "help/" + htmlFile;
			return Translation.getHtmlContent(htmlFile);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

}
