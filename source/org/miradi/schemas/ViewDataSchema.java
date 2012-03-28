/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

package org.miradi.schemas;

import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.ActionTreeConfigurationQuestion;
import org.miradi.questions.DiagramModeQuestion;
import org.miradi.questions.InternalQuestionWithoutValues;
import org.miradi.questions.MonitoringTreeConfigurationQuestion;
import org.miradi.questions.PlanningViewSingleLevelQuestion;
import org.miradi.questions.WorkPlanCategoryTypesQuestion;

public class ViewDataSchema extends BaseObjectSchema
{
	public ViewDataSchema(final Project projectToUse)
	{
		super();
		
		project = projectToUse;
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaChoice(ViewData.TAG_CURRENT_MODE, getQuestion(DiagramModeQuestion.class));
		createFieldSchemaReflist(ViewData.TAG_CHAIN_MODE_FACTOR_REFS);
		createFieldSchemaCodeList(ViewData.TAG_DIAGRAM_HIDDEN_TYPES, getQuestion(InternalQuestionWithoutValues.class));
		createFieldSchemaCodeList(ViewData.TAG_BUDGET_ROLLUP_REPORT_TYPES, getQuestion(WorkPlanCategoryTypesQuestion.class));
		createFieldSchemaChoice(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE, new PlanningViewSingleLevelQuestion(getProject()));
		createFieldSchemaRef(ViewData.TAG_TREE_CONFIGURATION_REF);
		createFieldSchemaChoice(ViewData.TAG_ACTION_TREE_CONFIGURATION_CHOICE, getQuestion(ActionTreeConfigurationQuestion.class));
		createFieldSchemaChoice(ViewData.TAG_MONITORING_TREE_CONFIGURATION_CHOICE, getQuestion(MonitoringTreeConfigurationQuestion.class));

		createFieldSchemaCodeField(ViewData.TAG_CURRENT_WIZARD_STEP).setNavigationField();
		createFieldSchemaInteger(ViewData.TAG_CURRENT_TAB).setNavigationField();
		createFieldSchemaRef(ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF).setNavigationField();
		createFieldSchemaRef(ViewData.TAG_CURRENT_RESULTS_CHAIN_REF).setNavigationField();
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
