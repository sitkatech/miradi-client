package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectInfo;
import org.conservationmeasures.eam.questions.FontFamiliyQuestion;
import org.conservationmeasures.eam.questions.FontSizeQuestion;

public class DiagramProjectPreferencesPanel extends ObjectDataInputPanel
{
	public DiagramProjectPreferencesPanel(Project projectToUse, ProjectInfo projectInfo)
	{
		super(projectToUse, ProjectMetadata.getObjectType(), projectInfo.getMetadataId());
		addField(createChoiceField(ProjectMetadata.getObjectType(), new FontSizeQuestion(ProjectMetadata.TAG_DIAGRAM_FONT_SIZE)));
		addField(createChoiceField(ProjectMetadata.getObjectType(), new FontFamiliyQuestion(ProjectMetadata.TAG_DIAGRAM_FONT_FAMILY)));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return null;
	}
}
