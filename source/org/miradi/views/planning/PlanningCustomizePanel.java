/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.planning;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.CodeListEditorPanel;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.project.Project;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.questions.CustomPlanningRowsQuestion;
import org.miradi.questions.DiagramObjectDataInclusionQuestion;
import org.miradi.questions.PlanningTreeTargetPositionQuestion;
import org.miradi.questions.StrategyObjectiveTreeOrderQuestion;

public class PlanningCustomizePanel extends ObjectDataInputPanel
{
	public PlanningCustomizePanel(Project projectToUse, ORef planningConfigurationRef) throws Exception
	{
		super(projectToUse, planningConfigurationRef);
		
		ObjectDataInputField dataInclusion = createChoiceField(ObjectTreeTableConfiguration.getObjectType(), ObjectTreeTableConfiguration.TAG_DIAGRAM_DATA_INCLUSION, getProject().getQuestion(DiagramObjectDataInclusionQuestion.class));
		addField(dataInclusion);
		
		ObjectDataInputField objectiveStrategyNodeOrder = createChoiceField(ObjectTreeTableConfiguration.getObjectType(), ObjectTreeTableConfiguration.TAG_STRATEGY_OBJECTIVE_ORDER, new StrategyObjectiveTreeOrderQuestion());
		addField(objectiveStrategyNodeOrder);
		
		addRadioButtonField(ObjectTreeTableConfiguration.getObjectType(), ObjectTreeTableConfiguration.TAG_TARGET_NODE_POSITION, getProject().getQuestion(PlanningTreeTargetPositionQuestion.class));
		
		ObjectDataInputPanel rowEditor = new CodeListEditorPanel(getProject(), planningConfigurationRef, ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION, new CustomPlanningRowsQuestion(getProject()), 1);
		addSubPanelWithoutTitledBorder(rowEditor);
		
		ObjectDataInputPanel columnEditor = new CodeListEditorPanel(getProject(), planningConfigurationRef, ObjectTreeTableConfiguration.TAG_COL_CONFIGURATION, getProject().getQuestion(CustomPlanningColumnsQuestion.class), 1);
		addSubPanelWithoutTitledBorder(columnEditor);
		
		updateFieldsFromProject();
	}
	
	@Override
	public String getPanelDescription()
	{
		return "PlanningCustomizePanel";
	}
}
