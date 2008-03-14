/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;

public class ForecastEstimateSubPanel extends ObjectDataInputPanel
{
	public ForecastEstimateSubPanel(MainWindow mainWindow, Actions actions, ORef initialRef)
	{
		super(mainWindow.getProject(), initialRef);
		
		addField(createCurrencyField(initialRef.getObjectType(), BaseObject.TAG_BUDGET_COST_OVERRIDE));
		addField(createDateRangeChooserField(initialRef.getObjectType(), BaseObject.TAG_WHEN_OVERRIDE));
		addField(createEditableObjectListField(mainWindow, initialRef.getObjectType(),  BaseObject.TAG_WHO_OVERRIDE_REFS));
	}

	public String getPanelDescription()
	{
		return EAM.text("Estimate Panel");
	}
}
