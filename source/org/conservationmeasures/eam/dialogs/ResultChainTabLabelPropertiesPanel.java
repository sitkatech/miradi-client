package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.Project;

public class ResultChainTabLabelPropertiesPanel extends ObjectDataInputPanel
{
	public ResultChainTabLabelPropertiesPanel(Project projectToUse, BaseId objectIdToUse)
	{
		super(projectToUse, ObjectType.RESULTS_CHAIN_DIAGRAM, objectIdToUse);
		addField(createStringField(DiagramObject.TAG_LABEL));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Results Chain Properties");
	}
}