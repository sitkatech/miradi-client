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

package org.miradi.views.planning;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.ObjectChoiceField;
import org.miradi.dialogfields.ObjectDataField;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.CodeListEditorPanel;
import org.miradi.dialogs.base.MiradiDialog;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBox;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.ObjectTreeTableConfigurationSchema;
import org.miradi.schemas.ViewDataSchema;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

public class PlanningCustomizePanel extends ObjectDataInputPanel
{
	public PlanningCustomizePanel(Project projectToUse, MiradiDialog parentDialogToUse, ORef planningConfigurationRef) throws Exception
	{
		super(projectToUse, planningConfigurationRef);
		
		parentDialog = parentDialogToUse;
		objectTreeTableConfiguration = ObjectTreeTableConfiguration.find(projectToUse, planningConfigurationRef);

		addField(createStringField(ObjectTreeTableConfiguration.TAG_LABEL));
		
		ObjectDataInputField dataInclusion = createChoiceField(ObjectTreeTableConfigurationSchema.getObjectType(), ObjectTreeTableConfiguration.TAG_DIAGRAM_DATA_INCLUSION, StaticQuestionManager.getQuestion(DiagramObjectDataInclusionQuestion.class));
		addField(dataInclusion);

		PlanningTreeRowColumnProvider rowColumnProvider = ObjectTreeTableConfiguration.find(getProject(), planningConfigurationRef);
		diagramChoiceQuestion = new DiagramChoiceQuestion(getProject(), rowColumnProvider);
		diagramFilterChoiceField = (ObjectChoiceField) createChoiceField(ObjectTreeTableConfigurationSchema.getObjectType(), ObjectTreeTableConfiguration.TAG_DIAGRAM_FILTER, diagramChoiceQuestion);
		diagramFilterChoiceField.getComponent().setPreferredSize(dataInclusion.getComponent().getPreferredSize());
		addField(diagramFilterChoiceField);

		ObjectDataInputField objectiveStrategyNodeOrder = createChoiceField(ObjectTreeTableConfigurationSchema.getObjectType(), ObjectTreeTableConfiguration.TAG_STRATEGY_OBJECTIVE_ORDER, new StrategyObjectiveTreeOrderQuestion());
		addField(objectiveStrategyNodeOrder);
		
		ObjectDataField targetPositionField = createChoiceField(ObjectTreeTableConfigurationSchema.getObjectType(), ObjectTreeTableConfiguration.TAG_TARGET_NODE_POSITION, StaticQuestionManager.getQuestion(PlanningTreeTargetPositionQuestion.class));
		addField(targetPositionField);
		
		ObjectDataInputPanel rowEditor = new CodeListEditorPanel(getProject(), planningConfigurationRef, ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION, new CustomPlanningRowsQuestion(getProject()), 1);
		addSubPanelWithoutTitledBorder(rowEditor);
		
		ObjectDataInputPanel columnEditor = new CodeListEditorPanel(getProject(), planningConfigurationRef, ObjectTreeTableConfiguration.TAG_COL_CONFIGURATION, StaticQuestionManager.getQuestion(CustomPlanningColumnsQuestion.class), 1);
		addSubPanelWithoutTitledBorder(columnEditor);
		
		updateFieldsFromProject();
	}

	public void updateDiagramFilterChoices() throws CommandFailedException
	{
		diagramChoiceQuestion.reloadQuestion(this.getProject());
		ChoiceItem[] diagramChoices = diagramChoiceQuestion.getChoices();

		ChoiceItemComboBox combo = (ChoiceItemComboBox) diagramFilterChoiceField.getComponent();

		ActionListener[] actionListeners = combo.getActionListeners();
		for(ActionListener actionListener : actionListeners)
			combo.removeActionListener(actionListener);

		FocusListener[] focusListeners = combo.getFocusListeners();
		for(FocusListener focusListener : focusListeners)
			combo.removeFocusListener(focusListener);

		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(diagramChoices);
		combo.setModel(comboBoxModel);

		String diagramFilter = objectTreeTableConfiguration.getData(ObjectTreeTableConfiguration.TAG_DIAGRAM_FILTER);
		ChoiceItem selectedChoiceItem = diagramChoiceQuestion.getSelectedChoiceItem(this.getProject(), diagramFilter);
		combo.setSelectedItem(selectedChoiceItem);

		CommandSetObjectData setDiagramFilter = new CommandSetObjectData(objectTreeTableConfiguration.getRef(), ObjectTreeTableConfiguration.TAG_DIAGRAM_FILTER, selectedChoiceItem.getCode());
		this.getProject().executeAsSideEffect(setDiagramFilter);

		for(ActionListener actionListener : actionListeners)
			combo.addActionListener(actionListener);

		for(FocusListener focusListener : focusListeners)
			combo.addFocusListener(focusListener);
	}

	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		if (event.isSetDataCommandWithThisTypeAndTag(ViewDataSchema.getObjectType(), ViewData.TAG_TREE_CONFIGURATION_REF))
		{
			parentDialog.dispose();
		}

		if (event.isSetDataCommandWithThisTypeAndTag(ObjectTreeTableConfigurationSchema.getObjectType(), ObjectTreeTableConfiguration.TAG_DIAGRAM_DATA_INCLUSION))
		{
			try
			{
				updateDiagramFilterChoices();
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
			}
		}
	}
	
	@Override
	public String getPanelDescription()
	{
		return "PlanningCustomizePanel";
	}
	
	private MiradiDialog parentDialog;
	private ObjectTreeTableConfiguration objectTreeTableConfiguration;
	private DiagramChoiceQuestion diagramChoiceQuestion;
	private ObjectChoiceField diagramFilterChoiceField;
}
