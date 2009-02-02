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
package org.miradi.project;

import java.io.IOException;

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceItem;
import org.miradi.rtf.viewExporters.PlanningViewRtfExporter;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.CodeList;
import org.miradi.utils.MultiTableCombinedAsOneExporter;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.RowManager;

public class PlanningTreeXmlExporter
{
	public PlanningTreeXmlExporter(Project projectToUse) throws Exception
	{
		project = projectToUse;
	}
	
	//NOTE: this code was copied from PlanningTreeTablePanel, and
	//that we believe it will go away when we change the report to use an
	//exported tree of refs instead of a table of cell values.              
	private void createTables(CodeList rowsToShow, CodeList columnsToShow) throws Exception
	{
		multiModelExporter = PlanningViewRtfExporter.createTables(getProject(), rowsToShow, columnsToShow); 
	}
	
	public void toXmlPlanningTreeTables(UnicodeWriter out) throws Exception
	{
		toXml(out, RowManager.getStrategicPlanRows(), ColumnManager.getStrategicPlanColumns(), "StrategicPlanTree");
		toXml(out, RowManager.getMonitoringPlanRows(), ColumnManager.getMonitoringPlanColumns(), "MonitoringPlanTree");
		toXml(out, RowManager.getWorkPlanRows(), ColumnManager.getWorkPlanColumns(), "WorkPlanTree");
	}
	
	private void toXml(UnicodeWriter out, CodeList rowsToShow, CodeList columnsToShow, String treeName) throws Exception
	{
		createTables(rowsToShow, columnsToShow);		
		int columnCount = multiModelExporter.getColumnCount();
		int rowCount = multiModelExporter.getRowCount();

		out.writeln("<" + treeName + ">");
		for (int row = 0; row < rowCount; ++row)
		{
			BaseObject objectForRow = multiModelExporter.getBaseObjectForRow(row);
			String objectTypeName = getSafeTypeName(objectForRow);
			out.writeln("<Row ObjectTypeName='" + objectTypeName + "'>");
			for (int column = 0; column < columnCount; ++column)
			{
				
				String elementName = getElementName(column);
				out.write("<" + elementName + ">");
				String padding = pad(multiModelExporter.getDepth(row, column), column);
				String safeValue = getSafeValue(multiModelExporter, row, column, objectTypeName);
				out.write(padding + safeValue);
				out.writeln("</" + elementName + ">");
			}

			out.writeln("</Row>");
		}

		out.writeln("</" + treeName + ">");
	}

	private String getSafeTypeName(BaseObject objectForRow)
	{
		if (objectForRow == null)
			return "";
		
		return objectForRow.getTypeName();
	}

	private String getElementName(int column)
	{
		return multiModelExporter.getHeaderFor(column).replaceAll(" ", "");
	}
	
	private String getSafeValue(AbstractTableExporter table, int row, int column, String objectTypeName)
	{
		ChoiceItem choiceItem = table.getChoiceItemAt(row, column);
		String label = choiceItem.getLabel();
		if (label == null)
			return "";
		
		label = appendObjectTypeName(column, objectTypeName, label);
		
		return XmlUtilities.getXmlEncoded(label.toString());
	}

	private String appendObjectTypeName(int column, String objectTypeName, String label)
	{
		final int ITEM_COLUMN_INDEX = 0;
		if (column == ITEM_COLUMN_INDEX && label.toString().length() > 0)
		{
			label = objectTypeName + ": " + label;
		}
			
		return label;
	}
	
	private boolean isTreeColumn(int column)
	{
		return (column == 0);
	}
		
	private String pad(int padCount, int column) throws IOException
	{
		if (!isTreeColumn(column))
			return ""; 

		final String FIVE_SPACES = "     ";
		String padding = "";
		for (int i = 0; i < padCount; ++i)
		{
			padding += FIVE_SPACES;
		}
		
		return padding;
	}

	private Project getProject()
	{
		return project;
	}
	
	private Project project;
	private MultiTableCombinedAsOneExporter multiModelExporter;
}
