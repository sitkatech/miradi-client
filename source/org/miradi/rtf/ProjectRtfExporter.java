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
package org.miradi.rtf;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.questions.ReportTemplateContentQuestion;
import org.miradi.rtf.legend.RtfLegendExporter;
import org.miradi.rtf.viewExporters.DiagramViewRtfExporter;
import org.miradi.rtf.viewExporters.PlanningViewRtfExporter;
import org.miradi.rtf.viewExporters.SummaryViewRtfExporter;
import org.miradi.rtf.viewExporters.ThreatRatingsViewRtfExporter;
import org.miradi.rtf.viewExporters.ViabilityViewRtfExporter;
import org.miradi.utils.CodeList;
import org.miradi.utils.MiradiMultiCalendar;

public class ProjectRtfExporter
{
	public ProjectRtfExporter(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}
	
	public void exportProject(RtfWriter writer, CodeList reportTemplateContent) throws Exception
	{
		writeProjectReporteHeader(writer);
		new SummaryViewRtfExporter(getMainWindow()).exportView(writer, reportTemplateContent);
		new DiagramViewRtfExporter(getMainWindow()).exportView(writer, reportTemplateContent);
		new ViabilityViewRtfExporter(getMainWindow()).exportView(writer, reportTemplateContent);
		new ThreatRatingsViewRtfExporter(getMainWindow()).exportView(writer, reportTemplateContent);
		new PlanningViewRtfExporter(getMainWindow()).exportView(writer, reportTemplateContent);
		exportLegend(writer, reportTemplateContent);
	}
	
	private void writeProjectReporteHeader(RtfWriter writer) throws Exception
	{
		writer.startBlock();
		writer.writeHeading1Style();
		writer.writeEncoded(EAM.text("Project Plan For ") + getProject().getMetadata().getProjectName());
		writer.writeParCommand();
		
		writer.writeHeading1Style();
		writer.writelnEncoded(EAM.text("Version: ") + new MiradiMultiCalendar().toIsoDateString());
		writer.writeParCommand();
		writer.endBlock();
		
		writer.newParagraph();	
	}

	public void exportLegend(RtfWriter writer, CodeList reportTemplateContent) throws Exception
	{
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.LEGEND_TABLE_REPORT_CODE))
			new RtfLegendExporter(getProject()).exportLegend(writer);
	}

	private Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}

	private MainWindow mainWindow;
}
