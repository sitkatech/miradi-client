/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.dialogs.treeRelevancy;

import org.miradi.dialogfields.FieldSaver;
import org.miradi.dialogs.base.SingleBooleanColumnEditableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.StrategyActivityRelevancyInterface;
import org.miradi.project.Project;

public class StrategyActivityRelevancyTableModel extends SingleBooleanColumnEditableModel
{
	public StrategyActivityRelevancyTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse, StrategyActivityRelevancyInterface parentObjectToUse)
	{
		super(projectToUse, providerToUse);
		
		parentObject = parentObjectToUse;
	}

	@Override
	public String getColumnName(int column)
	{
		return EAM.text("Is Relevant");
	}	
	
	@Override
	public void setValueAt(Object value, int row, int column)
	{
		// NOTE: Avoid recursive call to stopCellEditing
		FieldSaver.setEditingTable(null);

		if (value == null)
			return;

		try
		{
			ORefList selectedRefs = getCurrentlyCheckedRefs((Boolean) value, row);
			RelevancyOverrideSet relevancySet = parentObject.getCalculatedRelevantStrategyActivityOverrides(selectedRefs);
			setValueUsingCommand(parentObject.getRef(), parentObject.getRelevantStrategyActivitySetTag(), relevancySet.toString());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private ORefList getRelevantStrategyActivityRefs() throws Exception
	{
		return new ORefList(parentObject.getRelevantStrategyAndActivityRefs());
	}

	private ORefList getCurrentlyCheckedRefs(Boolean valueAsBoolean, int row) throws Exception
	{
		ORefSet selectedRefSet = new ORefSet(getCheckedRefsForRow());
		ORef thisRef = getRefForRow(row);
		if (thisRef.isInvalid())
			return selectedRefSet.toRefList();

		boolean nowChecked = valueAsBoolean.booleanValue();
		if(nowChecked)
			selectedRefSet.add(thisRef);
		else
			selectedRefSet.remove(thisRef);

		return selectedRefSet.toRefList();
	}

	private ORefList getCheckedRefsForRow() throws Exception
	{
		return getRelevantStrategyActivityRefs();
	}

	@Override
	protected boolean isRowSelected(int row, int column) throws Exception
	{
		ORefList currentRefList = getCheckedRefsForRow();
		ORef ref = getBaseObjectForRowColumn(row, column).getRef();
		return currentRefList.contains(ref);
	}

	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}

	@Override
	protected boolean shouldSortRows()
	{
		return false;
	}
				
	private static final String UNIQUE_MODEL_IDENTIFIER = "StrategyActivityRelevancyTableModel";
	
	private StrategyActivityRelevancyInterface parentObject;
}
