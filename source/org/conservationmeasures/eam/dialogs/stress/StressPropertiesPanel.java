/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.stress;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StressScopeChoiceQuestion;
import org.conservationmeasures.eam.questions.StressSeverityChoiceQuestion;
import org.conservationmeasures.eam.questions.StressRatingChoiceQuestion;

public class StressPropertiesPanel extends ObjectDataInputPanel
{
	public StressPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ObjectType.STRESS, BaseId.INVALID);
	
		addField(createStringField(Stress.TAG_LABEL));
		addField(createStringField(Stress.TAG_SHORT_LABEL, 10));
		addField(createRatingChoiceField(Stress.getObjectType(), new StressScopeChoiceQuestion(Stress.TAG_SCOPE)));
		addField(createRatingChoiceField(Stress.getObjectType(), new StressSeverityChoiceQuestion(Stress.TAG_SEVERITY)));
		addField(createReadOnlyChoiceField(Stress.getObjectType(), new StressRatingChoiceQuestion(Stress.PSEUDO_STRESS_RATING)));
	
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Stress Properties");
	}
}
