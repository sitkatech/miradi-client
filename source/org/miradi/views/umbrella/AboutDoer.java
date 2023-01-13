/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.views.umbrella;

import org.miradi.main.EAM;
import org.miradi.main.VersionConstants;
import org.miradi.utils.Translation;
import org.miradi.views.MainWindowDoer;

public class AboutDoer extends MainWindowDoer 
{
	public AboutDoer()
	{
	}
	
	@Override
	public boolean isAvailable()
	{
		return true;
	}
	
	@Override
	protected void doIt() throws Exception
	{
        if (!isAvailable())
            return;

 		String text = loadHtmlFile("AboutMiradi.html");
		text = replaceMiradiVersion(text);
 		HelpAboutPanel dialog = new HelpAboutPanel(getMainWindow(), text);
 		dialog.showAsOkDialog();
	}

	private static String loadHtmlFile(String htmlFile)
	{
		try
		{
			return Translation.getHtmlContent(htmlFile);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	private static String replaceMiradiVersion(String text)
	{
		String textToReturn = text;
		String template = "%s";

		try
		{
			String translationVersion = VersionConstants.getVersionAndTimestamp();
			String platform = System.getProperty("sun.arch.data.model");
			String version = translationVersion + " (" + platform + "-bit)";
			textToReturn = EAM.substitute(text, template, version);
		}
		catch (Exception e)
		{
			EAM.logError("Unable to determine Miradi version number");
		}

		return textToReturn;
	}
}
