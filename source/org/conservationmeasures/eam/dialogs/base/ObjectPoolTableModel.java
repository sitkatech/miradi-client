/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.project.Project;

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
