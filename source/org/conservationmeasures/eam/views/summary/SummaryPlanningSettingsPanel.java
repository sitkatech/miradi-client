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
import org.conservationmeasures.eam.questions.FiscalYearStartQuestion;

public class SummaryPlanningSettingsPanel extends ObjectDataInputPanel
{
	public SummaryPlanningSettingsPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject(), mainWindowToUse.getProject().getMetadata().getRef());
		
		ObjectDataInputField currency = createRatingChoiceField(new CurrencyTypeQuestion(ProjectMetadata.TAG_CURRENCY_TYPE));
		ObjectDataInputField symbol = createShortStringField(ProjectMetadata.TAG_CURRENCY_SYMBOL);
		
		ObjectDataInputField[] fields = new ObjectDataInputField[] {currency, symbol, };
		addFieldsOnOneLine(EAM.text("Label|Currency"), fields);
		
		addField(createChoiceField(ProjectMetadata.getObjectType(), new FiscalYearStartQuestion(ProjectMetadata.TAG_FISCAL_YEAR_START)));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Planning Settings");
	}

}
