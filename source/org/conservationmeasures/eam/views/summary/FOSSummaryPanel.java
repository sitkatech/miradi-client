/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.FosProjectData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.FosTrainingTypeQuestion;

public class FOSSummaryPanel extends ObjectDataInputPanel
{
	public FOSSummaryPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(FosProjectData.getObjectType()));
		
		addField(createChoiceField(FosProjectData.getObjectType(), FosProjectData.TAG_TRAINING_TYPE, new FosTrainingTypeQuestion()));
		addField(createStringField(FosProjectData.TAG_TRAINING_DATES));
		addField(createStringField(FosProjectData.TAG_TRAINERS));
		addField(createStringField(FosProjectData.TAG_COACHES));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Label|FOS");
	}
}
