/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;

public class StrategyPoolTableModel extends ObjectPoolTableModel
{
	public StrategyPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.FACTOR, COLUMN_TAGS);	
	}

	
	public IdList getLatestIdListFromProject()
	{
		IdList filteredResources = new IdList();
		
		IdList resources = super.getLatestIdListFromProject();
		for (int i=0; i<resources.size(); ++i)
		{
			BaseId baseId = resources.get(i);
			Factor factor = (Factor) project.findObject(ObjectType.FACTOR, baseId);
			if (!factor.isStrategy())
				continue;
			if (!((Strategy)factor).isStatusDraft())
				filteredResources.add(baseId);
		}
		return filteredResources;
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		Strategy.TAG_LABEL,
	};

}
