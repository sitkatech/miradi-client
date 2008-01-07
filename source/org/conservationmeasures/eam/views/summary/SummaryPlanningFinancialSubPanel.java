/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.questions.CurrencyTypeQuestion;

public class SummaryPlanningFinancialSubPanel extends ObjectDataInputPanel
{
	public SummaryPlanningFinancialSubPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject(), mainWindowToUse.getProject().getMetadata().getRef());
		
		ObjectDataInputField currency = createRatingChoiceField(new CurrencyTypeQuestion(ProjectMetadata.TAG_CURRENCY_TYPE));
		ObjectDataInputField symbol = createShortStringField(ProjectMetadata.TAG_CURRENCY_SYMBOL);
		
		ObjectDataInputField[] fields = new ObjectDataInputField[] {currency, symbol, };
		addFieldsOnOneLine(EAM.text("Label|Currency"), fields);
		
		addField(createNumericField(ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES, 2));
		addField(createNumericField(ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING));
		addField(createNumericField(ProjectMetadata.TAG_BUDGET_SECURED_PERCENT));
		addField(createStringField(ProjectMetadata.TAG_KEY_FUNDING_SOURCES));
		addField(createMultilineField(ProjectMetadata.TAG_FINANCIAL_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Financial");
	}

}
