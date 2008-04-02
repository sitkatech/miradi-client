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
package org.miradi.dialogs.planning.upperPanel;

import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.TrendQuestion;

import com.java.sun.jtreetable.TreeTableModelAdapter;

public class PlanningViewMeasurementTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewMeasurementTableModel(Project projectToUse, TreeTableModelAdapter adapterToUse) throws Exception
	{
		super(projectToUse, adapterToUse);
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}
	
	@Override
	public String getColumnTag(int column)
	{
		return columnTags[column];
	}
	
	public String getColumnName(int column)
	{
		return EAM.fieldLabel(Measurement.getObjectType(), getColumnTag(column));
	}
	
	public Object getValueAt(int row, int column)
	{
		BaseObject objectForRow = getBaseObjectForRowColumn(row, column);
		if (objectForRow.getType() != Measurement.getObjectType())
			return "";
		
		String columnTag = getColumnTag(column);
		String data = objectForRow.getData(columnTag);
		
		ChoiceQuestion question = getColumnQuestion(column);
		if(question != null)
			return question.findChoiceByCode(data);
		
		return data;
	}
	
	public boolean isChoiceItemColumn(int column)
	{
		return (getColumnQuestion(column) != null);
	}
	
	public ChoiceQuestion getColumnQuestion(int column)
	{
		String columnTag = getColumnTag(column);
		if(columnTag.equals(Measurement.TAG_TREND))
			return new TrendQuestion();
		
		if(columnTag.equals(Measurement.TAG_STATUS_CONFIDENCE))
			return new StatusConfidenceQuestion();
		
		return null;
	}
	
	public final static String[] columnTags = {
		Measurement.TAG_DATE, 
		Measurement.TAG_SUMMARY, 
		Measurement.TAG_TREND, 
		Measurement.TAG_STATUS_CONFIDENCE
		};
}
