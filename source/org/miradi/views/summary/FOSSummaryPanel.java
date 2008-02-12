/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.FosProjectData;
import org.miradi.project.Project;
import org.miradi.questions.FosTrainingTypeQuestion;

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
