/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.project.Project;

public class ObjectPoolTableModel extends ObjectTableModel
{
	public ObjectPoolTableModel(Project projectToUse, int listedItemType, String[] columnTagsToUse)
	{
		this(projectToUse, listedItemType, new IdList(), columnTagsToUse);
		rowObjectIds = getLatestIdListFromProject();
	}
	
	public ObjectPoolTableModel(Project projectToUse, int listedItemType, IdList listToUse, String[] columnTagsToUse)
	{
		super(projectToUse, listedItemType, columnTagsToUse);
		rowObjectIds = listToUse;
	}
	
	public IdList getLatestIdListFromProject()
	{
		return project.getPool(getRowObjectType()).getIdList();
	}
}
