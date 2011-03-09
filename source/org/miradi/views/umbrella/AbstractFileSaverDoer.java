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
import org.miradi.utils.ImageTooLargeException;
import org.miradi.views.ViewDoer;

abstract public class AbstractFileSaverDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		return (getProject().isOpen());
	}
	
	@Override
	public void doIt() throws Exception
	{
		if (!isAvailable())
			return;
		
		try 
		{
			//FIXME medium - this needs to be cleaned up.  Push down into sub classes.  
			if (!doesConfirmBetaExport() ||  !doesUserConfirm())
				return;
			
			EAMFileSaveChooser eamFileChooser = createFileChooser();
			File chosen = eamFileChooser.displayChooser();
			if (chosen==null) 
				return;

			boolean workWasCompleted = doWork(chosen);
			if (workWasCompleted)
				EAM.notifyDialog(EAM.text("Export complete"));
		}
		catch(ImageTooLargeException e)
		{
			String errorMessage = EAM.text("The image is too large to be exported.\n" +
					"Please use the <Zoom Out> feature to make it smaller, and try again.");
			EAM.errorDialog(errorMessage);
		}
		catch (ZipException e)
		{
			throw new CommandFailedException(e);
		}
		catch (IOException e)
		{
			EAM.logException(e);
			EAM.errorDialog(getIOExceptionErrorMessage());
			tryAgain();
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
	
	protected boolean doesConfirmBetaExport() throws Exception
	{
		return true;
	}
	
	protected boolean doesUserConfirm() throws Exception
	{
		return true;
	}

	protected void tryAgain() throws Exception
	{
		safeDoIt();
	}
	
	abstract protected EAMFileSaveChooser createFileChooser();
	
	abstract protected boolean doWork(File destinationFile) throws Exception;
}
