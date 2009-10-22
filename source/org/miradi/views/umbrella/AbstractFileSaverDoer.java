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
import java.util.zip.ZipException;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.views.ViewDoer;

abstract public class AbstractFileSaverDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		return (getProject().isOpen());
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try 
		{
			if (!doesUserPreConfirm())
				return;
			
			EAMFileSaveChooser eamFileChooser = createFileChooser();
			File chosen = eamFileChooser.displayChooser();
			if (chosen==null) 
				return;

			doWork(chosen);
			EAM.notifyDialog(EAM.text("Export complete"));
		}
		catch (ZipException e)
		{
			throw new CommandFailedException(e);
		}
		catch (IOException e)
		{
			EAM.logException(e);
			EAM.errorDialog(getIOExceptionErrorMessage());
			loopBack();
		}
		catch (Exception e) 
		{
			throw new CommandFailedException(e);
		} 
	}

	protected String getIOExceptionErrorMessage()
	{
		return EAM.text("Error Occurred please try saving under different name.");
	}
	
	protected boolean doesUserPreConfirm() throws Exception
	{
		return true;
	}

	protected void loopBack() throws CommandFailedException
	{
		doIt();
	}
	
	abstract protected EAMFileSaveChooser createFileChooser();
	
	abstract protected void doWork(File chosen) throws Exception;
}
