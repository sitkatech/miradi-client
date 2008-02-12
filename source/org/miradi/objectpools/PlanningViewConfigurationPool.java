/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.project.ObjectManager;

public class PlanningViewConfigurationPool extends EAMNormalObjectPool
{
	public PlanningViewConfigurationPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.PLANNING_VIEW_CONFIGURATION);
	}
	
	public PlanningViewConfiguration find(BaseId id)
	{
		return (PlanningViewConfiguration) findObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new PlanningViewConfiguration(objectManager, actualId);
	}
	
	public PlanningViewConfiguration[] getAllConfigurations()
	{
		BaseId[] allConfigurationIds = getIds();
		PlanningViewConfiguration[] allPlanningViewConfigurations = new PlanningViewConfiguration[allConfigurationIds.length];
		for (int i = 0; i < allPlanningViewConfigurations.length; ++i)
		{
			allPlanningViewConfigurations[i] = find(allConfigurationIds[i]);
		}
			
		return allPlanningViewConfigurations;
	}
}
