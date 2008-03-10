/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class ForecastRollupSubPanel extends ObjectDataInputPanel
{
	public ForecastRollupSubPanel(Project projectToUse, ORef initialRef)
	{
		super(projectToUse, initialRef);
		
		addField(createReadonlyCurrencyField(BaseObject.PSEUDO_TAG_BUDGET_COST_ROLLUP));
		addField(createReadonlyTextField(Task.getObjectType(), BaseObject.PSEUDO_TAG_WHEN_ROLLUP));
		addField(createReadonlyTextField(Task.getObjectType(), BaseObject.PSEUDO_TAG_WHO_ROLLUP));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Rollup Panel");
	}
}
