/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.forms.FormRow;
import org.miradi.layout.TwoColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.TncProjectData;
import org.miradi.project.Project;
import org.miradi.schemas.TncProjectDataSchema;

public class TncBusinessPlanningSummarySubPanel extends ObjectDataInputPanel
{
	public TncBusinessPlanningSummarySubPanel(Project projectToUse, ORef[] refs) throws Exception
	{
		super(projectToUse, refs);
		
		TwoColumnGridLayout layout = new TwoColumnGridLayout();
		int REASONABLE_GAP_BETWEEN_COLUMNS = 10;
		int REASONABLE_GAP_BETWEEN_ROWS = 30;
		layout.setGaps(REASONABLE_GAP_BETWEEN_COLUMNS, REASONABLE_GAP_BETWEEN_ROWS);
		setLayout(layout);

		String summaryRowContent = EAM.text("Summary View - Project Description, Scope, and Vision fields");
		String outcomesRowContent = EAM.text("Diagram View - Use Targets and Target Goals for describing ultimate desired outcomes for both biodiversity and human well-being targets.  " +
											"Use Objectives field for capturing other types of outcomes.");
		String situationRowContent = EAM.text("Diagram View - Conceptual Model diagram showing key drivers and opportunities and their " +
												"relationships to ultimate outcomes. Text field for alternative or supplemental narrative " +
												"description of conceptual model (available via right-click menu on all diagrams - " +
												"Diagram Properties)");
		String strategySelectionRowContent = EAM.text("Conceptual Model diagrams (including Brainstorm mode to capture all draft strategies " +
												"considered that were not selected). Strategy Ranking fields of: Potential Impact and Feasibility");
		String strategyLogicRowContent = EAM.text("Results Chain diagrams for entering intermediate results linking strategies to targets. Text field " +
												"for alternative or supplemental narrative description of results chain diagrams (available via right-click " +
												"menu on all diagrams - Diagram Properties).");
		String measuresRowContent = EAM.text("Diagram View - Indicators explicitly linked to outcomes captured as either goals or objectives");
		String workPlanRowContent = EAM.text("Work Plan View - Flexible management of work planning content (including Strategies, Activities, and Tasks with who and when information) " +
											"and budget information (including work effort, expenses, and funding sources).");
		
		FormRow contextRow = createReadonlyTextFormRow(EAM.text("Context"), summaryRowContent);
		addLabelAndFieldFromForm(contextRow);
		addField(createMultilineField(TncProjectDataSchema.getObjectType(), TncProjectData.TAG_MAKING_THE_CASE));
		FormRow outcomesRow = createReadonlyTextFormRow(EAM.text("Outcomes"), outcomesRowContent);
		addLabelAndFieldFromForm(outcomesRow);
		FormRow situationRow = createReadonlyTextFormRow(EAM.text("Situation Analysis"), situationRowContent);
		addLabelAndFieldFromForm(situationRow);
		FormRow strategySelectionRow = createReadonlyTextFormRow(EAM.text("Theory of Change<br/>- Strategy Selection"), strategySelectionRowContent);
		addLabelAndFieldFromForm(strategySelectionRow);
		FormRow strategyLogicRow = createReadonlyTextFormRow(EAM.text("Theory of Change<br/>- Strategy Logic"), strategyLogicRowContent);
		addLabelAndFieldFromForm(strategyLogicRow);
		addField(createMultilineField(TncProjectDataSchema.getObjectType(), TncProjectData.TAG_RISKS));
		FormRow measuresRow = createReadonlyTextFormRow(EAM.text("Measures"), measuresRowContent);
		addLabelAndFieldFromForm(measuresRow);
		FormRow workPlanRow = createReadonlyTextFormRow(EAM.text("Work Planning"), workPlanRowContent);
		addLabelAndFieldFromForm(workPlanRow);
		addField(createMultilineField(TncProjectDataSchema.getObjectType(), TncProjectData.TAG_FUNDRAISING_PLAN));
		addField(createMultilineField(TncProjectDataSchema.getObjectType(), TncProjectData.TAG_CAPACITY_AND_FUNDING));
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public static final String PANEL_DESCRIPTION = EAM.text("Label|Business Planning");
}
