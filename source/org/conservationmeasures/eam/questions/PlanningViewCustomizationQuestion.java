/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objectpools.PlanningViewConfigurationPool;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.project.Project;

public class PlanningViewCustomizationQuestion extends ChoiceQuestion
{
	public PlanningViewCustomizationQuestion(Project projectToUse)
	{
		super(getEmptyTag(), EAM.text("Customization Style"), getCustomizationChoices(projectToUse));
	}

	private static String getEmptyTag()
	{
		return "";
	}
	
	private static ChoiceItem[] getCustomizationChoices(Project project)
	{
		ChoiceItem invalidChoice = createDefaultInvalidConfigurationObject(project);
		PlanningViewConfigurationPool configurationPool = (PlanningViewConfigurationPool) project.getPool(PlanningViewConfiguration.getObjectType());
		PlanningViewConfiguration[] allConfigurations = configurationPool.getAllConfigurations();

		Vector allChoiceItemsWithFirstInvalid = new Vector();
		allChoiceItemsWithFirstInvalid.add(invalidChoice);
		for (int i = 0; i < allConfigurations.length; ++i)
		{
			PlanningViewConfiguration configuration = allConfigurations[i];
			ChoiceItem choiceItem = createChoiceItem(configuration);
			allChoiceItemsWithFirstInvalid.add(choiceItem);
		}

		return (ChoiceItem[]) allChoiceItemsWithFirstInvalid.toArray(new ChoiceItem[0]);	
	}

	private static ChoiceItem createChoiceItem(PlanningViewConfiguration configuration)
	{
		return new ChoiceItem(configuration.getRef().toString(), configuration.getLabel());
	}
	
	public static ChoiceItem createDefaultInvalidConfigurationObject(Project projectToUse)
	{	
		return new ChoiceItem(ORef.INVALID.toString(), EAM.text("--Customize--"));
	}
}
