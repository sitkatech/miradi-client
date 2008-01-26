/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.Miradi;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.reports.MiradiReport;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.conservationmeasures.eam.views.umbrella.doers.ExportProjectXmlDoer;

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
			URL resourcePath = Miradi.class.getResource("/reports/RareReport.jasper");
			Project project = mainWindow.getProject();
			MiradiReport miradiReport = new MiradiReport(project);
			InputStream input = resourcePath.openStream();
			
			File xmlFile = File.createTempFile("MiradiXML", null);
			ExportProjectXmlDoer.exportProjectToXml(project, xmlFile);
			
			miradiReport.getReport(input, xmlFile);
			input.close();
			xmlFile.delete();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CommandFailedException(EAM.text("Error Export To Report: Possible Write Protected: ") + e);
		}
	}


}
