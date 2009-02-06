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
package org.miradi.dialogs.taggedObjectSet;

import org.miradi.dialogs.base.SingleBooleanColumnEditableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.Project;

public class TaggedObjectSetEditableTableModel extends SingleBooleanColumnEditableModel
{
	public TaggedObjectSetEditableTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse, TaggedObjectSet taggedObjectSetToUse)
	{
		super(projectToUse, providerToUse);
		
		taggedObjectSet = taggedObjectSetToUse;
	}

	@Override
	public boolean isCellEditable(int row, int column)
	{
		return isValidFactorRow(row, column);
	}

	private boolean isValidFactorRow(int row, int column)
	{
		BaseObject objectForRow = getBaseObjectForRowColumn(row, column);
		return Factor.isFactor(objectForRow.getRef());
	}
	
	public String getColumnName(int column)
	{
		return SINGLE_COLUMN_NAME;
	}	
	
	public void setValueAt(Object value, int row, int column)
	{
		if (value == null)
			return;

		try
		{
			ORefList selectedRefs = getCurrentlyCheckedRefs((Boolean) value, row);	
			setValueUsingCommand(taggedObjectSet.getRef(), TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, selectedRefs.toString());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	protected ORefList getCheckedRefsAccordingToTheDatabase() throws Exception
	{
		return new ORefList(taggedObjectSet.getTaggedObjectRefs());
	}
	
	private TaggedObjectSet taggedObjectSet;
	public static final String SINGLE_COLUMN_NAME = EAM.text("Is Tagged");
}
