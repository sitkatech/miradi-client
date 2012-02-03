/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.CodeList;


public class IndicatorViabilityTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public IndicatorViabilityTableModel(Project projectToUse, RowColumnBaseObjectProvider adapterToUse, RowColumnProvider rowColumnProviderToUse) throws Exception
	{
		super(projectToUse, adapterToUse);
		
		rowColumnProvider = rowColumnProviderToUse;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	
	@Override
	public boolean isChoiceItemColumn(int column)
	{
		if(isStatusConfidenceColumn(column))
			return true;
		
		return super.isChoiceItemColumn(column);
	}

	@Override
	public Class getCellQuestion(int row, int modelColumn)
	{
		if (isStatusConfidenceColumn(modelColumn))
			return StatusConfidenceQuestion.class;
		
		return super.getCellQuestion(row, modelColumn);
	}
	
	private boolean isStatusConfidenceColumn(int column)
	{
		return getColumnTag(column).equals(Measurement.TAG_STATUS_CONFIDENCE);
	}

	public int getColumnCount()
	{
		return getColumnTags().size();
	}
	
	public String getColumnTag(int column)
	{
		return getColumnTags().get(column);
	}
	
	@Override
	public String getColumnName(int column)
	{
		return getColumnTag(column);
	}
	
	public Object getValueAt(int row, int column)
	{
		return getChoiceItemAt(row, column);
	}

	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		String tag = getColumnTag(column);
		BaseObject baseObject = getBaseObjectForRow(row);
		if (Measurement.is(baseObject))
		{
			if (isChoiceItemColumn(column))
				return baseObject.getChoiceItemData(getColumnTag(column));

			return new TaglessChoiceItem(baseObject.getData(tag));
		}

		return new EmptyChoiceItem();
	}
	
	@Override
	public void setValueAt(Object value, int row, int column)
	{
		BaseObject baseObject = getBaseObjectForRow(row);
		if (Measurement.is(baseObject))
		{
			if (isChoiceItemColumn(column))
				setChoiceValueUsingCommand(baseObject, getColumnTag(column), (ChoiceItem) value);
			else
				setValueUsingCommand(baseObject.getRef(), getColumnTag(column), value.toString());
		}
	}

	@Override
	public Color getCellBackgroundColor(int column)
	{
		return Color.WHITE;
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
	
	private CodeList getColumnTags()
	{
		try
		{
		return rowColumnProvider.getColumnCodesToShow();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new CodeList();
		}
	}
				
	private RowColumnProvider rowColumnProvider;
	private static final String UNIQUE_MODEL_IDENTIFIER = "TargetViabilityTableModel";
}
