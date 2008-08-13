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

import org.miradi.dialogs.summary.TeamPoolTable;
import org.miradi.dialogs.summary.TeamPoolTableModel;
import org.miradi.forms.summary.ProjectTabForm;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.rtf.RtfFormExporter;
import org.miradi.rtf.RtfManagementExporter;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.ObjectTableExporter;

public class SummaryViewRtfExporter extends RtfViewExporter
{
	public SummaryViewRtfExporter(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	@Override
	public void ExportView(RtfWriter writer) throws Exception
	{
		RtfFormExporter rtfFormExporter = new RtfFormExporter(getProject(), writer, getProjectMetadataRef());
		rtfFormExporter.exportForm(new ProjectTabForm());
		writer.newParagraph();

		RtfManagementExporter rtfManagementExporter = new RtfManagementExporter(getProject());
		rtfManagementExporter.writeManagement(getTeamPoolTableExporter(), writer);
	}

	private ORef getProjectMetadataRef()
	{
		return getProject().getMetadata().getRef();
	}

	private AbstractTableExporter getTeamPoolTableExporter() throws Exception
	{
		TeamPoolTable teamPoolTable = new TeamPoolTable(getMainWindow(), new TeamPoolTableModel(getProject()));
		return new ObjectTableExporter(teamPoolTable);
	}

}
