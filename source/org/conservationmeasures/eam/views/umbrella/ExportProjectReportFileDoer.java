/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.io.InputStream;
import java.net.URL;

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
// For debugging
//			URL resourcePath = new URL("jar:file:/home/kevins/work/eam/binaries-miradi/dist/miradi.jar!" +
//			//"/version.txt");
//			"/org/conservationmeasures/eam/reports/MiradisReport.jasper");
			URL resourcePath = EAM.getResourceURL(MiradiReport.class, "MiradisReport.jasper");
			Project project = mainWindow.getProject();
			MiradiReport miradiReport = new MiradiReport(project);
			InputStream input = resourcePath.openStream();
			
//			 For debugging
//			ObjectInputStream ois = new ObjectInputStream(input);
//			Object obj = ois.readObject();

			
			miradiReport.getReport(input);
			input.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CommandFailedException(EAM.text("Error Export To Report: Possible Write Protected: ") + e);
		}
	}


}
