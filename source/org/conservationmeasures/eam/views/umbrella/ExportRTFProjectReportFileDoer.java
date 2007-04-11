/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.reports.MiradiReport;
import org.conservationmeasures.eam.utils.EAMRTFFileChooser;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class ExportRTFProjectReportFileDoer extends MainWindowDoer
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
		EAMRTFFileChooser eamFileChooser = new EAMRTFFileChooser(mainWindow);
		File chosen = eamFileChooser.displayChooser();
		if (chosen==null) return;
		
		try 
		{
			new MiradiReport(mainWindow.getProject()).getRTFReport(
					EAM.getResourcePath(MiradiReport.class, "MiradisReport.jasper"),
					chosen.getPath());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CommandFailedException(EAM.text("Error Export To Report: Possible Write Protected: ") + e);
		}
	}


}
