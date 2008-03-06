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

import org.martus.swing.UiLabel;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogfields.RadioButtonsField;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.layout.OneRowGridLayout;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.BudgetCostModeQuestion;

public class ForcastOverrideSubPanel extends AbstractObjectDataInputPanel
{
	public ForcastOverrideSubPanel(Project projectToUse, ORef initialRef)
	{
		super(projectToUse, initialRef);
		OneRowGridLayout layout = new OneRowGridLayout();
		layout.setGaps(3);
		setLayout(layout);
		setBorder(null);
		
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		
		int type = initialRef.getObjectType();
		BudgetCostModeQuestion question = new BudgetCostModeQuestion();
		RadioButtonsField modeField = createRadioButtonsField(type, BaseObject.TAG_BUDGET_COST_MODE, question);
		ObjectDataInputField rollupField = createReadonlyCurrencyField(BaseObject.PSEUDO_TAG_BUDGET_COST_ROLLUP);
		ObjectDataInputField overrideField = createCurrencyField(type, BaseObject.TAG_BUDGET_COST_OVERRIDE);
		
		addField(modeField);
		addField(rollupField);
		addField(overrideField);
		
		add(modeField.getComponent(question.findIndexByCode(question.OVERRIDE_MODE_CODE)));
		add(new UiLabel(EAM.text("High Level Estimate")));
		add(overrideField.getComponent());
		addSpacer();
		add(modeField.getComponent(question.findIndexByCode(question.ROLLUP_MODE_CODE)));
		add(new UiLabel(EAM.text("Rollup")));
		add(rollupField.getComponent());
	}

	public void addSpacer()
	{
		add(new UiLabel("   "));
	}
	
	public void addFieldComponent(Component component)
	{
		add(component);
	}

	public String getPanelDescription()
	{
		return "Budget SubPanel";
	}
}