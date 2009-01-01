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

import java.awt.Color;

import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.TrendQuestion;
import org.miradi.utils.CodeList;

public class PlanningViewMeasurementTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewMeasurementTableModel(Project projectToUse, RowColumnBaseObjectProvider adapterToUse) throws Exception
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
		Measurement measurement = getMeasurementOrLatestIndicatorMeasurement(row, column);
		if (measurement == null)
			return "";
		
		String columnTag = getColumnTag(column);
		String data = measurement.getData(columnTag);
		
		ChoiceQuestion question = getColumnQuestion(column);
		if(question != null)
			return question.findChoiceByCode(data);
		
		return data;
	}

	private Measurement getMeasurementOrLatestIndicatorMeasurement(int row, int column)
	{
		BaseObject rawObjectForRow = getBaseObjectForRowColumn(row, column);
		if (Measurement.is(rawObjectForRow))
			return (Measurement) rawObjectForRow; 
		
		if (!Indicator.is(rawObjectForRow))
			return null;
		
		if (areMeasurementRowsVisible())
			return null;
		
		return getLatestIndicatorMeasurement(rawObjectForRow);
	}

	private Measurement getLatestIndicatorMeasurement(BaseObject rawObjectForRow)
	{
		ORef latestMeasurementRef = ((Indicator) rawObjectForRow).getLatestMeasurementRef();
		if (latestMeasurementRef.isInvalid())
				return null;
		
		return Measurement.find(getProject(), latestMeasurementRef);
	}

	private boolean areMeasurementRowsVisible()
	{
		try
		{
			return getVisibleRowCodes().contains(Measurement.OBJECT_NAME);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	private CodeList getVisibleRowCodes() throws Exception
	{
		CodeList visibleRows = PlanningTreeTableModel.getVisibleRowCodes(getProject());
		return visibleRows;
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
	
	public Color getCellBackgroundColor(int column)
	{
		return AppPreferences.MEASUREMENT_COLOR_BACKGROUND;
	}

	public final static String[] columnTags = {
		Measurement.TAG_DATE, 
		Measurement.TAG_SUMMARY, 
		Measurement.TAG_TREND, 
		Measurement.TAG_STATUS_CONFIDENCE
		};

}
