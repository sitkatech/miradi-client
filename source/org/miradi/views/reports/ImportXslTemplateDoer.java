/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.reports;

import java.io.File;

import org.martus.util.UnicodeReader;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.XslTemplate;
import org.miradi.utils.GenericFileOpenChooser;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.XmlUtilities2;
import org.miradi.views.ObjectsDoer;

public class ImportXslTemplateDoer extends ObjectsDoer
{
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
		
		final GenericFileOpenChooser fileChooser = new GenericFileOpenChooser(getMainWindow());
		final File userChosenFile = fileChooser.displayChooser();
		if (userChosenFile == null)
			return;
		
		String xslFileContent = UnicodeReader.getFileContents(userChosenFile);
		xslFileContent = XmlUtilities2.getXmlEncoded(xslFileContent);
		xslFileContent = HtmlUtilities.removeNonHtmlNewLines(xslFileContent);

		getProject().executeBeginTransaction();
		try
		{
			CommandCreateObject createCommand = new CommandCreateObject(XslTemplate.getObjectType());
			getProject().executeCommand(createCommand);
			
			CommandSetObjectData setXslField = new CommandSetObjectData(createCommand.getObjectRef(), XslTemplate.TAG_XSL_TEMPLATE, xslFileContent);
			getProject().executeCommand(setXslField);
			
			CommandSetObjectData setXslLabel = new CommandSetObjectData(createCommand.getObjectRef(), XslTemplate.TAG_LABEL, userChosenFile.getName());
			getProject().executeCommand(setXslLabel);
			
			if(getPicker() != null)
				getPicker().ensureOneCopyOfObjectSelectedAndVisible(createCommand.getObjectRef());
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}
}
