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

package org.miradi.dialogs.base;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public abstract class EditableObjectRefsTableModel extends EditableObjectTableModel
{
	public EditableObjectRefsTableModel(Project projectToUse)
	{
		super(projectToUse);
		
		clearRefs();
	}
	
	@Override
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		clearRefs();
		refs = extractOutEditableRefs(hierarchyToSelectedRef);
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return BaseObject.find(getProject(), refs.get(row));
	}

	public int getRowCount()
	{
		return refs.size();
	}
	
	private void clearRefs()
	{
		refs = new ORefList();
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return true;
	}

	public String getColumnTag(int column)
	{
		return getColumnTags()[column];
	}

	public int getColumnCount()
	{
		return getColumnTags().length;
	}
	
	abstract protected String[] getColumnTags();
	
	abstract protected ORefList extractOutEditableRefs(ORef[] hierarchyToSelectedRef);
	
	protected ORefList refs;
}
