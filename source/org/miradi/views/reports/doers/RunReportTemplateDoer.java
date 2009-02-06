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
package org.miradi.views.reports.doers;

import java.io.File;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ReportTemplate;
import org.miradi.rtf.ProjectRtfExporter;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.CodeList;
import org.miradi.utils.RtfFileChooser;
import org.miradi.views.ObjectsDoer;

public class RunReportTemplateDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		BaseObject selectedReportTemplate = getSingleSelected(ReportTemplate.getObjectType());
		return (selectedReportTemplate != null);
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		try
		{
			BaseObject selectedReportTemplate = getSingleSelected(ReportTemplate.getObjectType());
			CodeList reportTemplateContent = selectedReportTemplate.getCodeList(ReportTemplate.TAG_INCLUDE_SECTION_CODES);
			runReport(getMainWindow(), reportTemplateContent);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	public static void runReport(MainWindow mainWindow, CodeList reportTemplateContent)
	{
		try
		{
			RtfFileChooser rtfFileChooser = new RtfFileChooser(mainWindow);
			File destination = rtfFileChooser.displayChooser();
			if (destination == null) 
				return;

			writeRtf(mainWindow, destination, reportTemplateContent);
			EAM.notifyDialog(EAM.text("Selected Report Template Was Exported as RTF."));

		}
		catch (Exception e)
		{
			EAM.logException(e);
			String message = getValidExceptionMessage(e);
			EAM.errorDialog(EAM.text("Error occurred while trying to export selected report template as RTF.\n") + message);
		}
	}

	private static String getValidExceptionMessage(Exception e)
	{
		String message = e.getMessage();
		if (message == null)
			return "";
		
		return message;
	}
	
	private static void writeRtf(MainWindow mainWindow, File destination, CodeList reportTemplateContent) throws Exception
	{
		RtfWriter rtfWriter = new RtfWriter(destination);
		try
		{
			rtfWriter.startRtf();
			rtfWriter.landscapeMode();
			
			new ProjectRtfExporter(mainWindow).exportProject(rtfWriter, reportTemplateContent);
			
			rtfWriter.endRtf();
		}
		finally
		{
			rtfWriter.close();
		}
	}
}
