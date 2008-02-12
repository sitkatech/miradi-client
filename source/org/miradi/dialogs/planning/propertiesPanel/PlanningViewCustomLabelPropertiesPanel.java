/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.project.Project;

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
