/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;

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