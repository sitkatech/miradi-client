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

package org.miradi.dialogs.progressPercent;

import org.miradi.dialogs.base.EditableObjectRefsTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.objects.ProgressPercent;
import org.miradi.project.Project;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.views.diagram.doers.AbstractCreateProgressDoer;

public class ProgressPercentTableModel extends EditableObjectRefsTableModel
{
	public ProgressPercentTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	@Override
	public String getUniqueTableModelIdentifier()
	{
		return "ProgressPercentTableModel";
	}

	@Override
	protected ORefList extractOutEditableRefs(ORef[] hierarchyToSelectedRef)
	{
		BaseObject parent = AbstractCreateProgressDoer.extractProgressParentCandidate(getProject(), new ORefList(hierarchyToSelectedRef), getObjectType());
		if (parent == null)
			return new ORefList();
		
		if (Desire.isDesire(parent.getType()))
				return ((Desire)parent).getProgressPercentRefs();
		
		return new ORefList();
	}

	@Override
	protected int getObjectType()
	{
		return ProgressPercent.getObjectType();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		ProgressPercent progressPercent = getProgressPercentForRow(rowIndex, columnIndex);
		if (isDateColumn(columnIndex))
			return new TaglessChoiceItem(progressPercent.getData(ProgressPercent.TAG_DATE));
		
		if (isPercentCompleteColumn(columnIndex))
			return new TaglessChoiceItem(progressPercent.getData(ProgressPercent.TAG_PERCENT_COMPLETE));
		
		if (isPercentCompleteNotesColumn(columnIndex))
			return new TaglessChoiceItem(progressPercent.getData(ProgressPercent.TAG_PERCENT_COMPLETE_NOTES)); 
			
		return new EmptyChoiceItem();
	}

	@Override
	public void setValueAt(Object value, int row, int column)
	{
		if (value == null)
			return;
		
		ORef ref = getBaseObjectForRowColumn(row, column).getRef();
		setProgressPercentValue(ref, column, value.toString());
	}
	
	private void setProgressPercentValue(ORef ref, int column, String value)
	{
		setValueUsingCommand(ref, getColumnTag(column), value);
	}
	
	public boolean isDateColumn(int modelColumn)
	{
		return isColumnForTag(modelColumn, ProgressPercent.TAG_DATE);
	}
	
	public boolean isPercentCompleteColumn(int modelColumn)
	{
		return isColumnForTag(modelColumn, ProgressPercent.TAG_PERCENT_COMPLETE);
	}
	
	public boolean isPercentCompleteNotesColumn(int modelColumn)
	{
		return isColumnForTag(modelColumn, ProgressPercent.TAG_PERCENT_COMPLETE_NOTES);
	}
	
	private ProgressPercent getProgressPercentForRow(int rowIndex, int columnIndex)
	{
		return (ProgressPercent) getBaseObjectForRowColumn(rowIndex, columnIndex);
	}

	@Override
	protected String[] getColumnTags()
	{
		return new String[]{
			ProgressPercent.TAG_DATE, 
			ProgressPercent.TAG_PERCENT_COMPLETE, 
			ProgressPercent.TAG_PERCENT_COMPLETE_NOTES, 
			};
	}
}
