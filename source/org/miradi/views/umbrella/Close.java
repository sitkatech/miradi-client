/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.MainWindowDoer;

public class Close extends MainWindowDoer
{
	public boolean isAvailable()
	{
		return getMainWindow().getProject().isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		try
		{
			getMainWindow().closeProject();
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

}
