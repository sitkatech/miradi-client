/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
/**
 * 
 */
package org.miradi.dialogs.diagram;

import java.awt.Component;

import org.miradi.dialogfields.RadioButtonsField;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.layout.TwoColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.questions.BudgetCostModeQuestion;

public class ForecastSubPanel extends AbstractObjectDataInputPanel
{
	public ForecastSubPanel(MainWindow mainWindow, ORef initialRef)
	{
		super(mainWindow.getProject(), initialRef);
		
		setLayout(new TwoColumnGridLayout());
				
		BudgetCostModeQuestion question = new BudgetCostModeQuestion();
		RadioButtonsField modeField = createRadioButtonsField(initialRef.getObjectType(), BaseObject.TAG_BUDGET_COST_MODE, question);
		addField(modeField);		
		add(modeField.getComponent(question.findIndexByCode(question.ROLLUP_MODE_CODE)));
		add(modeField.getComponent(question.findIndexByCode(question.OVERRIDE_MODE_CODE)));

		ForecastRollupSubPanel forecastRollupSubPanel = new ForecastRollupSubPanel(getProject(), initialRef);
		addSubPanel(forecastRollupSubPanel);
		add(forecastRollupSubPanel);
		
		ForecastEstimateSubPanel forecastEstimateSubPanel = new ForecastEstimateSubPanel(mainWindow, mainWindow.getActions(), initialRef);
		addSubPanel(forecastEstimateSubPanel);
		add(forecastEstimateSubPanel);
		
	}
	
	public void addFieldComponent(Component component)
	{
		add(component);
	}

	public String getPanelDescription()
	{
		return EAM.text("Cost/When/Who");
	}
}