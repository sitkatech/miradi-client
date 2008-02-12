/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.ProjectMetadata;
import org.miradi.questions.CurrencyTypeQuestion;

public class SummaryPlanningFinancialSubPanel extends ObjectDataInputPanel
{
	public SummaryPlanningFinancialSubPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject(), mainWindowToUse.getProject().getMetadata().getRef());
		
		ObjectDataInputField currency = createRatingChoiceField(ProjectMetadata.TAG_CURRENCY_TYPE, new CurrencyTypeQuestion());
		ObjectDataInputField symbol = createShortStringField(ProjectMetadata.TAG_CURRENCY_SYMBOL);
		
		ObjectDataInputField[] fields = new ObjectDataInputField[] {currency, symbol, };
		addFieldsOnOneLine(EAM.text("Label|Currency"), fields);
		
		addField(createNumericField(ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES, 2));
		addField(createCurrencyField(ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING));
		addField(createPercentageField(ProjectMetadata.TAG_BUDGET_SECURED_PERCENT));
		addField(createStringField(ProjectMetadata.TAG_KEY_FUNDING_SOURCES));
		addField(createMultilineField(ProjectMetadata.TAG_FINANCIAL_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Financial");
	}

}
