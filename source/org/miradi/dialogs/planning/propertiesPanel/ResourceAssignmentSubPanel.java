/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.ResourceAssignmentSchema;

public class ResourceAssignmentSubPanel extends AbstractAssignmentSubPanel
{
	public ResourceAssignmentSubPanel(Project projectToUse, int objectType) throws Exception
	{
		super(projectToUse, objectType);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Assignment");
	}

	protected void rebuild() throws Exception
	{
		removeAll();
		getFields().clear();

		ObjectDataInputField resourceDropdownField = createChoiceField(ResourceAssignmentSchema.getObjectType(), ResourceAssignment.TAG_RESOURCE_ID, new ProjectResourceIdQuestionWithUnspecifiedChoice(getProject()));
		addFieldsOnOneLine(EAM.text("Work Assignment"), IconManager.getAssignmentIcon(), new ObjectDataInputField[]{resourceDropdownField});

		ObjectDataInputField readonlyResourceDailyRateField = createReadonlyShortTextField(ResourceAssignmentSchema.getObjectType(), ResourceAssignment.PSEUDO_TAG_PROJECT_RESOURCE_COST_PER_UNIT);
		addField(readonlyResourceDailyRateField);

		addAccountingClassificationFields(ResourceAssignmentSchema.getObjectType());

		addTaxonomyFields(ResourceAssignmentSchema.getObjectType());

		ObjectDataInputField accountingCodeDropdownField = createChoiceField(ResourceAssignmentSchema.getObjectType(), ResourceAssignment.TAG_ACCOUNTING_CODE_ID, new AccountingCodeIdQuestionWithUnspecifiedChoice(getProject()));
		addField(accountingCodeDropdownField);

		ObjectDataInputField fundingSourceDropdownField = createChoiceField(ResourceAssignmentSchema.getObjectType(), ResourceAssignment.TAG_FUNDING_SOURCE_ID, new FundingSourceIdQuestionWithUnspecifiedChoice(getProject()));
		addField(fundingSourceDropdownField);

		ObjectDataInputField budgetCategoryOneDropdownField = createChoiceField(ResourceAssignmentSchema.getObjectType(), ResourceAssignment.TAG_CATEGORY_ONE_REF, new BudgetCategoryOneQuestionWithUnspecifiedChoice(getProject()));
		addField(budgetCategoryOneDropdownField);

		ObjectDataInputField budgetCategoryTwoDropdownField = createChoiceField(ResourceAssignmentSchema.getObjectType(), ResourceAssignment.TAG_CATEGORY_TWO_REF, new BudgetCategoryTwoQuestionWithUnspecifiedChoice(getProject()));
		addField(budgetCategoryTwoDropdownField);

		updateFieldsFromProject();

		doLayout();

		validate();
		repaint();
	}
}
