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
package org.miradi.views.workplan;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.ObjectChoiceField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBox;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.schemas.TableSettingsSchema;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

public class WorkPlanDiagramFilterPanel extends ObjectDataInputPanel
{
	public WorkPlanDiagramFilterPanel(Project projectToUse, ProjectMetadata projectMetadata, TableSettings tableSettingsToUse, PlanningTreeRowColumnProvider rowColumnProvider)
	{
		super(projectToUse, new ORef[] {projectMetadata.getRef(), tableSettingsToUse.getRef()});

		tableSettings = tableSettingsToUse;

		diagramInclusionChoiceQuestion = StaticQuestionManager.getQuestion(DiagramObjectDataInclusionQuestion.class);
		diagramInclusionChoiceField = (ObjectChoiceField) createChoiceField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION, diagramInclusionChoiceQuestion);
		addField(diagramInclusionChoiceField);

		diagramChoiceQuestion = new DiagramChoiceQuestion(projectToUse, rowColumnProvider);
		diagramFilterChoiceField = (ObjectChoiceField) createChoiceField(TableSettingsSchema.getObjectType(), TableSettings.TAG_WORK_PLAN_DIAGRAM_FILTER, diagramChoiceQuestion);
		diagramFilterChoiceField.getComponent().setPreferredSize(diagramInclusionChoiceField.getComponent().getPreferredSize());
		addField(diagramFilterChoiceField);

		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return null;
	}

	public void updateDiagramFilterChoices() throws CommandFailedException
	{
		diagramChoiceQuestion.reloadQuestion(this.getProject());
		ChoiceItem[] diagramChoices = diagramChoiceQuestion.getChoices();

		ChoiceItemComboBox diagramInclusionCombo = (ChoiceItemComboBox) diagramInclusionChoiceField.getComponent();
		ChoiceItemComboBox diagramFilterCombo = (ChoiceItemComboBox) diagramFilterChoiceField.getComponent();

		ActionListener[] actionListeners = diagramFilterCombo.getActionListeners();
		for(ActionListener actionListener : actionListeners)
			diagramFilterCombo.removeActionListener(actionListener);

		FocusListener[] focusListeners = diagramFilterCombo.getFocusListeners();
		for(FocusListener focusListener : focusListeners)
			diagramFilterCombo.removeFocusListener(focusListener);

		ProjectMetadata metadata = this.getProject().getMetadata();
		String diagramInclusion = metadata.getData(ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION);
		ChoiceItem selectedInclusionChoiceItem = diagramInclusionChoiceQuestion.findChoiceByCode(diagramInclusion);
		diagramInclusionCombo.setSelectedItem(selectedInclusionChoiceItem);

		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(diagramChoices);
		diagramFilterCombo.setModel(comboBoxModel);

		String diagramFilter = tableSettings.getData(TableSettings.TAG_WORK_PLAN_DIAGRAM_FILTER);
		ChoiceItem selectedFilterChoiceItem = diagramChoiceQuestion.getSelectedChoiceItem(this.getProject(), diagramFilter);
		diagramFilterCombo.setSelectedItem(selectedFilterChoiceItem);

		CommandSetObjectData setDiagramFilter = new CommandSetObjectData(tableSettings.getRef(), TableSettings.TAG_WORK_PLAN_DIAGRAM_FILTER, selectedFilterChoiceItem.getCode());
		this.getProject().executeAsSideEffect(setDiagramFilter);

		for(ActionListener actionListener : actionListeners)
			diagramFilterCombo.addActionListener(actionListener);

		for(FocusListener focusListener : focusListeners)
			diagramFilterCombo.addFocusListener(focusListener);
	}

	private final TableSettings tableSettings;
	private final ChoiceQuestion diagramInclusionChoiceQuestion;
	private final ObjectChoiceField diagramInclusionChoiceField;
	private final DiagramChoiceQuestion diagramChoiceQuestion;
	private final ObjectChoiceField diagramFilterChoiceField;
}
