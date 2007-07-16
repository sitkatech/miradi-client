package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.Project;

public class DiagramTabsLabelPropertiesPanel extends ObjectDataInputPanel
{
	public DiagramTabsLabelPropertiesPanel(Project projectToUse, ORef diagramObjectRefToUse)
	{
		super(projectToUse, diagramObjectRefToUse);
		ref = diagramObjectRefToUse;
		addField(createStringField(DiagramObject.TAG_LABEL));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		if (ref.getObjectType() == ObjectType.RESULTS_CHAIN_DIAGRAM)
			return EAM.text("Results Chain Properties");
		
		return EAM.text("Conceptual Model Page Properties");
	}
	
	private ORef ref;
}