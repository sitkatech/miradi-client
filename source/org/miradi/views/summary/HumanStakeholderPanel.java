/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;

public class HumanStakeholderPanel extends ObjectDataInputPanel
{
	public HumanStakeholderPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);

		ObjectDataInputField humanPopulationField = createNumericField(ProjectMetadata.TAG_HUMAN_POPULATION);
		ObjectDataInputField humanPopulationNotesField = createMultilineField(ProjectMetadata.TAG_HUMAN_POPULATION_NOTES);
		addFieldsOnOneLine(EAM.text("Label|Human Stakeholder Pop Size"), new ObjectDataInputField[]{humanPopulationField, humanPopulationNotesField});
		
		addField(createMultilineField(ProjectMetadata.TAG_SOCIAL_CONTEXT));
		
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Human Stakeholders");
	}

}
