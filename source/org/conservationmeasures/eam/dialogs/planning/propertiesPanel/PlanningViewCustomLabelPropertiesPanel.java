/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.project.Project;

public class PlanningViewCustomLabelPropertiesPanel extends ObjectDataInputPanel
{
	public PlanningViewCustomLabelPropertiesPanel(Project projectToUse, ORef planningViewConfigurationRefToUse)
	{
		super(projectToUse, planningViewConfigurationRefToUse);
		addField(createStringField(PlanningViewConfiguration.TAG_LABEL));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Planning View Configeration Label Properties");
	}
}
