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

import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.Project;

public class ObjectPoolTableModel extends ObjectTableModel
{
	public ObjectPoolTableModel(Project projectToUse, int listedItemType, String[] columnTagsToUse)
	{
		this(projectToUse, listedItemType, new IdList(listedItemType), columnTagsToUse);
	}
	
	public ObjectPoolTableModel(Project projectToUse, int listedItemType, IdList listToUse, String[] columnTagsToUse)
	{
		super(projectToUse, listedItemType, columnTagsToUse);
	}
	
	public ORefList getLatestRefListFromProject()
	{
		return new ORefList(getRowObjectType(), getLatestIdListFromProject());
	}
	
	private IdList getLatestIdListFromProject()
	{
		IdList idList = project.getPool(getRowObjectType()).getIdList();
		idList.sort();
		return idList;
	}
}
