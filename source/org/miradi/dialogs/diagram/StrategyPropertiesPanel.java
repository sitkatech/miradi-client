/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;


import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;

public class StrategyPropertiesPanel extends ObjectDataInputPanel
{
	public StrategyPropertiesPanel(MainWindow mainWindow)
	{
		super(mainWindow.getProject(), Strategy.getObjectType(), BaseId.INVALID);
		
		addLabel("");
		addSubPanelWithTitledBorder(new StrategyCoreSubpanel(getProject(), mainWindow.getActions(), Strategy.getObjectType()));
		
		ForecastSubPanel budgetSubPanel = new ForecastSubPanel(mainWindow, new ORef(Strategy.getObjectType(), BaseId.INVALID));
		addSubPanel(budgetSubPanel);

		addLabel(EAM.text("Budget"));
		addFieldComponent(budgetSubPanel);
		
		addLabel("");
		addSubPanelWithTitledBorder(new RelatedItemsSubpanel(getProject(), Strategy.getObjectType()));
		addField(createMultilineField(Strategy.TAG_COMMENT));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Strategy Properties");
	}

}
