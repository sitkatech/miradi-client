/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.MiradiTabDelimitedFileChooser;
import org.conservationmeasures.eam.views.ViewDoer;

public class ExportTableDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		Project project = getMainWindow().getProject();
		if(!project.isOpen())
			return false;
		
		return getView().isExportableTableAvailable();
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		MiradiTabDelimitedFileChooser eamFileChooser = new MiradiTabDelimitedFileChooser(getMainWindow());
		File chosen = eamFileChooser.displayChooser();
		if (chosen==null) 
			return;
		

	}
}
