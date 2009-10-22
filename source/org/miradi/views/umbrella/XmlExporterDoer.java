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

import java.io.File;
import java.io.IOException;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.utils.ConstantButtonNames;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.views.MainWindowDoer;

abstract public class XmlExporterDoer extends MainWindowDoer
{
	public boolean isAvailable()
	{
		return (getProject().isOpen());
	}
	
	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		String title = EAM.text("Export Project (BETA)");
		String[] body = new String[] {
			EAM.text("This feature is not yet fully supported. " +
			"It exports all the project data, but the XML schema is still in flux. " +
			"Future versions of Miradi will export the data in different formats."),
		};

		String[] buttons = new String[] {
			EAM.text("Export"),
			ConstantButtonNames.CANCEL,
		};
		if(!EAM.confirmDialog(title, body, buttons))
			return;
		
		doWork();
	}

	private void doWork() throws CommandFailedException
	{
		EAMFileSaveChooser eamFileChooser = createFileChooser();
		File chosen = eamFileChooser.displayChooser();
		if (chosen==null) 
			return;

		try
		{
			export(chosen);
			EAM.notifyDialog(EAM.text("Export complete"));
		}
		catch(IOException e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unable to write XML. Perhaps the disk was full, or you " +
					"don't have permission to write to it, or you are using invalid characters in the file name."));
			
			loopBack();
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	private void loopBack() throws CommandFailedException
	{
		doWork();
	}
	
	abstract protected EAMFileSaveChooser createFileChooser();
	
	abstract protected void export(File chosen) throws Exception;
}
