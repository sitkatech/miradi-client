package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectInfo;
import org.conservationmeasures.eam.questions.FontSizeQuestion;
import org.conservationmeasures.eam.questions.FontStyleQuestion;
import org.conservationmeasures.eam.questions.FontWeightQuestion;

public class DiagramPreferencesPanel extends ObjectDataInputPanel
{
	public DiagramPreferencesPanel(Project projectToUse, ProjectInfo pInfo)
	{
		super(projectToUse, ProjectMetadata.getObjectType(), pInfo.getMetadataId());
		addField(createChoiceField(ProjectMetadata.getObjectType(), new FontSizeQuestion(ProjectMetadata.TAG_DIAGRAM_FONT_SIZE)));
		addField(createChoiceField(ProjectMetadata.getObjectType(), new FontStyleQuestion(ProjectMetadata.TAG_DIAGRAM_FONT_STYLE)));
		addField(createChoiceField(ProjectMetadata.getObjectType(), new FontWeightQuestion(ProjectMetadata.TAG_DIAGRAM_FONT_WEIGHT)));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return null;
	}
}
