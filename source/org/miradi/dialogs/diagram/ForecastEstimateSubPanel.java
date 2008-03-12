/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import org.miradi.actions.ActionEditOverrideWhoValues;
import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.ObjectsActionButton;

public class ForecastEstimateSubPanel extends ObjectDataInputPanel
{
	public ForecastEstimateSubPanel(Project projectToUse, Actions actions, ORef initialRef)
	{
		super(projectToUse, initialRef);
		
		addField(createCurrencyField(initialRef.getObjectType(), BaseObject.TAG_BUDGET_COST_OVERRIDE));

		addField(createDateRangeChooserField(initialRef.getObjectType(), BaseObject.TAG_WHEN_OVERRIDE));

		PanelTitleLabel estimatedResourcesLabel = new PanelTitleLabel(EAM.text("Who Override"));
		ObjectDataInputField readOnlyEstimatedResourcesList = createReadOnlyObjectList(initialRef.getObjectType(), BaseObject.TAG_WHO_OVERRIDE_REFS);
		ObjectsActionButton editEstimatedResourcesButton = createObjectsActionButton(actions.getObjectsAction(ActionEditOverrideWhoValues.class), getPicker());
		addFieldWithEditButton(estimatedResourcesLabel, readOnlyEstimatedResourcesList, editEstimatedResourcesButton);
	}

	public String getPanelDescription()
	{
		return EAM.text("Estimate Panel");
	}
}
