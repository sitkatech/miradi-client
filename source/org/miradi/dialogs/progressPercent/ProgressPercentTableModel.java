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
import org.miradi.objects.ProgressPercent;
import org.miradi.project.Project;
import org.miradi.questions.EmptyChoiceItem;

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
		return new ORefList();
	}

	@Override
	protected int getObjectType()
	{
		return ProgressPercent.getObjectType();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{		
		return new EmptyChoiceItem();
	}

	@Override
	public void setValueAt(Object value, int row, int column)
	{
	}
	
	public boolean isDateColumn(int modelColumn)
	{
		return isColumnForTag(modelColumn, ProgressPercent.TAG_DATE);
	}
	
	public boolean isPercentCompleteNotesColumn(int modelColumn)
	{
		return isColumnForTag(modelColumn, ProgressPercent.TAG_PERCENT_COMPLETE_NOTES);
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
