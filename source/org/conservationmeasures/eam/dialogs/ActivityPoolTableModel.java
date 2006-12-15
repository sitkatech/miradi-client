/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class ActivityPoolTableModel extends ObjectPoolTableModel
{
	public ActivityPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.TASK, COLUMN_TAGS);
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		Task.TAG_LABEL,
		Task.PSEUDO_TAG_FACTOR_LABEL,
	};
	
	public IdList getLatestIdListFromProject()
	{
		IdList filteredStrategy = new IdList();
		
		IdList indicator = super.getLatestIdListFromProject();
		for (int i=0; i<indicator.size(); ++i)
		{
			BaseId baseId = indicator.get(i);
			Factor factor = (Factor) project.findObject(ObjectType.FACTOR, baseId);
			if (factor==null)
			{
				EAM.logError("ObjectType.FACTOR not found:" + baseId);
				continue;
			}
			if (factor.isStrategy())
				filteredStrategy.add(baseId);
		}
		return filteredStrategy;
	}


}
