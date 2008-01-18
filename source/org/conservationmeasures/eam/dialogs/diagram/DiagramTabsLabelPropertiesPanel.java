/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.dialogs.diagram;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
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