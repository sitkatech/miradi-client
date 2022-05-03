/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.forms.FormRow;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.ProjectMetadata;
import org.miradi.questions.CurrencyTypeQuestion;

public class SummaryPlanningFinancialSubPanel extends ObjectDataInputPanel
{
	public SummaryPlanningFinancialSubPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), mainWindowToUse.getProject().getMetadata().getRef());
		
		ObjectDataInputField currency = createRatingChoiceField(ProjectMetadata.TAG_CURRENCY_TYPE, new CurrencyTypeQuestion());
		ObjectDataInputField symbol = createShortStringField(ProjectMetadata.TAG_CURRENCY_SYMBOL);
		
		ObjectDataInputField[] fields = new ObjectDataInputField[] {currency, symbol, };
		addFieldsOnOneLine(EAM.text("Label|Currency"), fields);
		
		addField(createNumericField(ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES, 2));
		addField(createCurrencyField(ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING));
		addField(createPercentageField(ProjectMetadata.TAG_BUDGET_SECURED_PERCENT));
		addField(createMultilineField(ProjectMetadata.TAG_KEY_FUNDING_SOURCES));
		addField(createMultilineField(ProjectMetadata.TAG_FINANCIAL_COMMENTS));
		addField(createStringField(ProjectMetadata.TAG_WORK_UNIT_RATE_DESCRIPTION));
		FormRow workUnitRateDescriptionHint = createReadonlyTextFormRow(EAM.text(" "), EAM.text("This text displays next to the Work Unit Rate. It should explain and provide guidance for that entry."));
		addLabelAndFieldFromForm(workUnitRateDescriptionHint);

		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Financial Settings");
	}
}
