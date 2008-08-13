/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.rtf;

import org.miradi.main.MainWindow;
import org.miradi.rtf.viewExporters.DiagramViewRtfExporter;
import org.miradi.rtf.viewExporters.PlanningViewRtfExporter;
import org.miradi.rtf.viewExporters.SummaryViewRtfExporter;
import org.miradi.rtf.viewExporters.ViabilityViewRtfExporter;
import org.miradi.rtf.viewExporters.ThreatRatingsViewRtfExporter;
import org.miradi.utils.CodeList;

public class ProjectRtfExporter
{
	public ProjectRtfExporter(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}
	
	public void exportProject(RtfWriter writer, CodeList reportTemplateContent) throws Exception
	{
		new SummaryViewRtfExporter(getMainWindow()).ExportView(writer, reportTemplateContent);
		new DiagramViewRtfExporter(getMainWindow()).ExportView(writer, reportTemplateContent);
		new ViabilityViewRtfExporter(getMainWindow()).ExportView(writer, reportTemplateContent);
		new ThreatRatingsViewRtfExporter(getMainWindow()).ExportView(writer, reportTemplateContent);
		new PlanningViewRtfExporter(getMainWindow()).ExportView(writer, reportTemplateContent);
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}

	private MainWindow mainWindow;
}
