/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class NewProject extends MainWindowDoer
{
	public void doIt() throws CommandFailedException
	{
		while(true)
		{
			CreateProjectDialog dlg = new CreateProjectDialog(getMainWindow());
			if(!dlg.showCreateDialog(EAM.text("Button|Create")))
				return;
	
			File chosen = dlg.getSelectedFile();

			getMainWindow().createOrOpenProject(chosen);
				return;
		}
				
	}

	public boolean isAvailable()
	{
		if (getProject().isOpen())
			return false;
		
		return true;
	}

}
