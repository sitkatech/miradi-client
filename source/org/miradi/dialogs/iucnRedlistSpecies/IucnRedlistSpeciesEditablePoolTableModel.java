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

package org.miradi.dialogs.iucnRedlistSpecies;

import org.miradi.dialogs.base.EditableObjectRefsTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.IucnRedlistSpecies;
import org.miradi.project.Project;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;

public class IucnRedlistSpeciesEditablePoolTableModel extends EditableObjectRefsTableModel
{
	public IucnRedlistSpeciesEditablePoolTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	@Override
	public String getUniqueTableModelIdentifier()
	{
		return "IucnRedlistSpeciesEditablePoolTableModel";
	}

	@Override
	protected ORefList extractOutEditableRefs(ORef[] hierarchyToSelectedRef)
	{	
		return getProject().getPool(IucnRedlistSpecies.getObjectType()).getRefList();
	}

	@Override
	protected int getObjectType()
	{
		return IucnRedlistSpecies.getObjectType();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		BaseObject iucnRedlistSpecies = getIucnRedlistSpeciesForRow(rowIndex, columnIndex);
		if (isLabelColumn(columnIndex))
			return new TaglessChoiceItem(iucnRedlistSpecies.getData(IucnRedlistSpecies.TAG_LABEL));
		
		return new EmptyChoiceItem();
	}

	@Override
	public void setValueAt(Object value, int row, int column)
	{
		if (value == null)
			return;
		
		ORef ref = getBaseObjectForRowColumn(row, column).getRef();
		setDataValue(ref, column, value.toString());
	}
	
	private void setDataValue(ORef ref, int column, String value)
	{
		setValueUsingCommand(ref, getColumnTag(column), value);
	}
	
	public boolean isLabelColumn(int modelColumn)
	{
		return isColumnForTag(modelColumn, IucnRedlistSpecies.TAG_LABEL);
	}
	
	private BaseObject getIucnRedlistSpeciesForRow(int rowIndex, int columnIndex)
	{
		return getBaseObjectForRowColumn(rowIndex, columnIndex);
	}

	@Override
	protected String[] getColumnTags()
	{
		return new String[]{
				IucnRedlistSpecies.TAG_LABEL, 
			};
	}
}
