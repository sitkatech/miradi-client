/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.reports.MiradiReport;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class ExportProjectReportFileDoer extends MainWindowDoer
{
	public boolean isAvailable() 
	{
		Project project = getProject();
		return project.isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		if (!isAvailable())
			return;

		perform(getMainWindow()); 
	}

	static public void perform(MainWindow mainWindow) throws CommandFailedException
	{
		try 
		{
			new MiradiReport(mainWindow.getProject()).getReport(
					EAM.getResourcePath(MiradiReport.class, "MiradisReport.jasper"));
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
			throw new CommandFailedException(EAM.text("Error Export To Report: Possible Write Protected: ") + e);
		}
	}


}
