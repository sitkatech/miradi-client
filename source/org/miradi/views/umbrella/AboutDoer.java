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
package org.miradi.views.umbrella;

import java.util.EventObject;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.VersionConstants;
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
		doIt(null);
	}
	
	public void doIt(EventObject event) throws CommandFailedException
	{
		String text =  buildMainSection();
		
		boolean initialSplash = (event == null);
		if(!initialSplash)
		{
			text += loadHtmlFile("HelpAboutExtra.html");
		}
		
		text += loadHtmlFile("AboutEnd.html");
		
		HelpAboutPanel dialog = new HelpAboutPanel(getMainWindow(), text);
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
