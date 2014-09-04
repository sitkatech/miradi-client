/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FutureStatus;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.schemas.FutureStatusSchema;

public class PlanningViewFutureStatusTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewFutureStatusTableModel(Project projectToUse, RowColumnBaseObjectProvider adapterToUse) throws Exception
	{
		super(projectToUse, adapterToUse);
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}
	
	public String getColumnTag(int modelColumn)
	{
		return columnTags[modelColumn];
	}
	
	@Override
	public String getColumnName(int column)
	{
		return EAM.fieldLabel(FutureStatusSchema.getObjectType(), getColumnTag(column));
	}
	
	public Object getValueAt(int row, int column)
	{
		return getChoiceItemAt(row, column);
	}
	
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		BaseObject objectForRow = getBaseObjectForRowColumn(row, column);
		final String tag = getColumnTag(column);
		if (FutureStatus.is(objectForRow))
			return new TaglessChoiceItem(objectForRow.getData(tag));
		
		if (Indicator.is(objectForRow))
			return getLatestFutureStatusData((Indicator) objectForRow, tag);
		
		return new EmptyChoiceItem();
	}
	
	private ChoiceItem getLatestFutureStatusData(Indicator indicator, String tag)
	{
		ORef latestFutureStatusRef = indicator.getLatestFutureStatusRef();
		if (latestFutureStatusRef.isInvalid())
			return new EmptyChoiceItem();
		
		return new TaglessChoiceItem(getProject().getObjectData(latestFutureStatusRef, tag));
	}

	@Override
	public Color getCellBackgroundColor(int column)
	{
		return FactorRenderer.ANNOTATIONS_COLOR;
	}
	
	public void updateColumnsToShow() throws Exception
	{
		fireTableStructureChanged();
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}
				
	private static final String UNIQUE_MODEL_IDENTIFIER = "PlanningViewFutureStatusTableModel";
	
	public final static String[] columnTags = {FutureStatusSchema.TAG_FUTURE_STATUS_DATE, FutureStatusSchema.TAG_FUTURE_STATUS_SUMMARY};
}
