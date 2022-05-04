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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.project.Project;
import org.miradi.questions.AccountingCodeQuestionWithUnspecifiedChoice;
import org.miradi.questions.BudgetCategoryOneQuestionWithUnspecifiedChoice;
import org.miradi.questions.BudgetCategoryTwoQuestionWithUnspecifiedChoice;
import org.miradi.questions.FundingSourceQuestionWithUnspecifiedChoice;
import org.miradi.schemas.ExpenseAssignmentSchema;

public class ExpenseAssignmentSubPanel extends AbstractAssignmentSubPanel
{
	public ExpenseAssignmentSubPanel(Project projectToUse, int objectType) throws Exception
	{
		super(projectToUse, objectType);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Expense");
	}

	protected void rebuild() throws Exception
	{
		removeAll();
		getFields().clear();

		ObjectDataInputField expenseNameField = createExpandableField(ExpenseAssignmentSchema.getObjectType(), ExpenseAssignment.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Expense"), IconManager.getExpenseIcon(), new ObjectDataInputField[]{expenseNameField});

		addAccountingClassificationFields(ExpenseAssignmentSchema.getObjectType());

		addTaxonomyFields(ExpenseAssignmentSchema.getObjectType());

		ObjectDataInputField accountingCodeDropdownField = createChoiceField(ExpenseAssignmentSchema.getObjectType(), ExpenseAssignment.TAG_ACCOUNTING_CODE_REF, new AccountingCodeQuestionWithUnspecifiedChoice(getProject()));
		addField(accountingCodeDropdownField);

		ObjectDataInputField fundingSourceDropdownField = createChoiceField(ExpenseAssignmentSchema.getObjectType(), ExpenseAssignment.TAG_FUNDING_SOURCE_REF, new FundingSourceQuestionWithUnspecifiedChoice(getProject()));
		addField(fundingSourceDropdownField);

		ObjectDataInputField budgetCategoryOneDropdownField = createChoiceField(ExpenseAssignmentSchema.getObjectType(), ExpenseAssignment.TAG_CATEGORY_ONE_REF, new BudgetCategoryOneQuestionWithUnspecifiedChoice(getProject()));
		addField(budgetCategoryOneDropdownField);

		ObjectDataInputField budgetCategoryTwoDropdownField = createChoiceField(ExpenseAssignmentSchema.getObjectType(), ExpenseAssignment.TAG_CATEGORY_TWO_REF, new BudgetCategoryTwoQuestionWithUnspecifiedChoice(getProject()));
		addField(budgetCategoryTwoDropdownField);

		updateFieldsFromProject();

		doLayout();

		validate();
		repaint();
	}
}
