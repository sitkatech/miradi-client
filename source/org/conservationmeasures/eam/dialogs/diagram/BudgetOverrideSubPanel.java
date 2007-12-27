/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
/**
 * 
 */
package org.conservationmeasures.eam.dialogs.diagram;

import java.awt.Component;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogfields.RadioButtonsField;
import org.conservationmeasures.eam.dialogs.base.AbstractObjectDataInputPanel;
import org.conservationmeasures.eam.layout.OneRowGridLayout;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.BudgetCostModeQuestion;
import org.martus.swing.UiLabel;

public class BudgetOverrideSubPanel extends AbstractObjectDataInputPanel
{
	public BudgetOverrideSubPanel(Project projectToUse, ORef initialRef)
	{
		super(projectToUse, initialRef);
		OneRowGridLayout layout = new OneRowGridLayout();
		setLayout(layout);
		setBorder(null);
		
		int type = initialRef.getObjectType();
		BudgetCostModeQuestion question = new BudgetCostModeQuestion(BaseObject.TAG_BUDGET_COST_MODE);
		RadioButtonsField modeField = createRadioButtonsField(type, question);
		ObjectDataInputField rollupField = createReadonlyCurrencyField(BaseObject.PSEUDO_TAG_BUDGET_COST_ROLLUP);
		ObjectDataInputField overrideField = createCurrencyField(type, BaseObject.TAG_BUDGET_COST_OVERRIDE);
		
		addField(modeField);
		addField(rollupField);
		addField(overrideField);
		
		add(modeField.getComponent(question.findIndexByCode(question.OVERRIDE_MODE_CODE)));
		add(new UiLabel(EAM.text("High Level Est.")));
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