/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class MethodPoolTableModel extends ObjectPoolTableModel
{
	public MethodPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.TASK, COLUMN_TAGS);
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		Task.TAG_LABEL,
		Task.PSEUDO_TAG_FACTOR_LABEL,
	};
	
	public IdList getLatestIdListFromProject()
	{
		IdList filteredStrategies = new IdList();
		
		IdList strategy = super.getLatestIdListFromProject();
		for (int i=0; i<strategy.size(); ++i)
		{
			BaseId baseId = strategy.get(i);
			Task task = (Task) project.findObject(ObjectType.TASK, baseId);
			if ((task.getParentRef().getObjectType() == ObjectType.INDICATOR))
				filteredStrategies.add(baseId);
		}
		return filteredStrategies;
	}


}
