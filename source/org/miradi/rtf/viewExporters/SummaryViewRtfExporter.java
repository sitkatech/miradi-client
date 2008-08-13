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
package org.miradi.rtf.viewExporters;

import org.miradi.dialogs.organization.OrganizationPoolTableModel;
import org.miradi.dialogs.summary.TeamPoolTableModel;
import org.miradi.forms.summary.ProjectTabForm;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.rtf.RtfFormExporter;
import org.miradi.rtf.RtfWriter;

public class SummaryViewRtfExporter extends RtfViewExporter
{
	public SummaryViewRtfExporter(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	@Override
	public void ExportView(RtfWriter writer) throws Exception
	{
		exportProjectTab(writer);
		exportTeamTab(writer);
		exportOrganizationTab(writer);
		exportScopeTab(writer);
		exportLocationTab(writer);
		exportPlanningTab(writer);
		exportTncTab(writer);
		exportWwfTab(writer);
		exportWcs(writer);
		exportRareTab(writer);
		exportFos(writer);
	}

	private void exportProjectTab(RtfWriter writer) throws Exception
	{
		RtfFormExporter rtfFormExporter = new RtfFormExporter(getProject(), writer, getProjectMetadataRef());
		rtfFormExporter.exportForm(new ProjectTabForm());
		writer.newParagraph();
	}

	private void exportTeamTab(RtfWriter writer) throws Exception
	{
		exportObjectTableModel(writer, new TeamPoolTableModel(getProject()));
	}
	
	private void exportOrganizationTab(RtfWriter writer) throws Exception
	{
		exportObjectTableModel(writer, new OrganizationPoolTableModel(getProject()));
	}	

	private void exportScopeTab(RtfWriter writer) throws Exception
	{
	}
	
	private void exportLocationTab(RtfWriter writer) throws Exception
	{
	}
	
	private void exportPlanningTab(RtfWriter writer) throws Exception
	{
	}
	
	private void exportTncTab(RtfWriter writer) throws Exception
	{
	}

	private void exportWwfTab(RtfWriter writer) throws Exception
	{
	}

	private void exportWcs(RtfWriter writer) throws Exception
	{
	}
	
	private void exportRareTab(RtfWriter writer) throws Exception
	{
	}

	private void exportFos(RtfWriter writer) throws Exception
	{
	}

	private ORef getProjectMetadataRef()
	{
		return getProject().getMetadata().getRef();
	}
}
