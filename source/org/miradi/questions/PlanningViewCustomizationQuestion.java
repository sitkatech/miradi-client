/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import java.util.Vector;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objectpools.PlanningViewConfigurationPool;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.project.Project;

public class PlanningViewCustomizationQuestion extends StaticChoiceQuestion
{
	public PlanningViewCustomizationQuestion(Project projectToUse)
	{
		super(getCustomizationChoices(projectToUse));
	}

	private static ChoiceItem[] getCustomizationChoices(Project project)
	{
		PlanningViewConfigurationPool configurationPool = (PlanningViewConfigurationPool) project.getPool(PlanningViewConfiguration.getObjectType());
		ORefList allConfigurationRefs = configurationPool.getORefList();

		Vector allCustomizations = new Vector();
		for (int i = 0; i < allConfigurationRefs.size(); ++i)
		{
			ChoiceItem choiceItem = createChoiceItem(project, allConfigurationRefs.get(i));
			allCustomizations.add(choiceItem);
		}

		return (ChoiceItem[]) allCustomizations.toArray(new ChoiceItem[0]);	
	}

	private static ChoiceItem createChoiceItem(Project project, ORef configurationRef)
	{
		return new ObjectChoiceItem(project, configurationRef);
	}
	
}
