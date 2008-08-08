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

import org.miradi.forms.FieldPanelSpec;
import org.miradi.forms.PropertiesPanelSpec;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.AbstractTableExporter;

public class RtfManagementExporter
{
	public RtfManagementExporter(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void writeManagement(AbstractTableExporter tableExporter, RtfWriter writer) throws Exception
	{
		writer.writeRtfTable(tableExporter);
		writer.newParagraph();
		writePropertiesForEachObject(writer, tableExporter);
	}
	
	public void writePropertiesForEachObject(RtfWriter writer, AbstractTableExporter tableExporter) throws Exception
	{
		for (int row = 0; row < tableExporter.getRowCount(); ++row)
		{
			BaseObject baseObjectForRow = tableExporter.getBaseObjectForRow(row);
			if (baseObjectForRow == null)
			{
				EAM.logDebug("Found null baseObject for row:" + row);
				continue;
			}
			PropertiesPanelSpec form = ObjectToFormMap.getForm(baseObjectForRow);
			writePropertiesPanel(writer, baseObjectForRow, form);
			writer.newParagraph();
		}
	}

	private void writePropertiesPanel(RtfWriter writer, BaseObject baseObjectForRow, PropertiesPanelSpec form) throws Exception
	{
		RtfFormExporter rtfFormExporter = new RtfFormExporter(getProject(), writer, baseObjectForRow.getRef());
		for (int subPanelIndex = 0; subPanelIndex < form.getPanelCount(); ++subPanelIndex)
		{
			FieldPanelSpec fieldPanelSpec = form.getPanel(subPanelIndex);
			rtfFormExporter.exportForm(fieldPanelSpec);
		}
	}

	public Project getProject()
	{
		return project;
	}

	private Project project;
}
