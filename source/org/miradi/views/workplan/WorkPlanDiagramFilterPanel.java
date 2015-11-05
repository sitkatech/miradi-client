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
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.TableSettingsSchema;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

public class WorkPlanDiagramFilterPanel extends ObjectDataInputPanel
{

	public WorkPlanDiagramFilterPanel(Project projectToUse, ProjectMetadata projectMetadata, TableSettings tableSettingsToUse)
	{
		super(projectToUse, new ORef[] {projectMetadata.getRef(), tableSettingsToUse.getRef()});

		tableSettings = tableSettingsToUse;

		diagramInclusionChoiceField = (ObjectChoiceField) createChoiceField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION, StaticQuestionManager.getQuestion(DiagramObjectDataInclusionQuestion.class));
		addField(diagramInclusionChoiceField);

		diagramChoiceQuestion = new DiagramChoiceQuestion(projectToUse);
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

		ChoiceItemComboBox combo = (ChoiceItemComboBox) diagramFilterChoiceField.getComponent();

		ActionListener[] actionListeners = combo.getActionListeners();
		for(ActionListener actionListener : actionListeners)
			combo.removeActionListener(actionListener);

		FocusListener[] focusListeners = combo.getFocusListeners();
		for(FocusListener focusListener : focusListeners)
			combo.removeFocusListener(focusListener);

		DefaultComboBoxModel<ChoiceItem> comboBoxModel = new DefaultComboBoxModel<ChoiceItem>(diagramChoices);
		combo.setModel(comboBoxModel);

		String diagramFilter = tableSettings.getData(TableSettings.TAG_WORK_PLAN_DIAGRAM_FILTER);
		ChoiceItem selectedChoiceItem = getSelectedChoiceItem(diagramFilter);
		combo.setSelectedItem(selectedChoiceItem);

		CommandSetObjectData setDiagramFilter = new CommandSetObjectData(tableSettings.getRef(), TableSettings.TAG_WORK_PLAN_DIAGRAM_FILTER, selectedChoiceItem.getCode());
		this.getProject().executeAsSideEffect(setDiagramFilter);

		for(ActionListener actionListener : actionListeners)
			combo.addActionListener(actionListener);

		for(FocusListener focusListener : focusListeners)
			combo.addFocusListener(focusListener);
	}

	private ChoiceItem getSelectedChoiceItem(String diagramFilter)
	{
		if (shouldResetDiagramFilter(diagramFilter))
		{
			return new ChoiceItem("", DiagramChoiceQuestion.getUnspecifiedChoiceText());
		}
		else
		{
			ORef diagramFilterObjectRef = ORef.createFromString(diagramFilter);
			DiagramObject diagramFilterObject = DiagramObject.findDiagramObject(this.getProject(), diagramFilterObjectRef);
			return new ChoiceItem(diagramFilterObjectRef.toString(), diagramFilterObject.getFullName());
		}
	}

	private boolean shouldResetDiagramFilter(String diagramFilter)
	{
		if (diagramFilter.isEmpty())
			return true;

		ORef diagramFilterObjectRef = ORef.createFromString(diagramFilter);

		if (diagramFilterObjectRef.getObjectType() == ConceptualModelDiagramSchema.getObjectType() && !this.getProject().getMetadata().shouldIncludeConceptualModelPage())
			return true;

		if (diagramFilterObjectRef.getObjectType() == ResultsChainDiagramSchema.getObjectType() && !this.getProject().getMetadata().shouldIncludeResultsChain())
			return true;

		return false;
	}

	private final TableSettings tableSettings;
	private final ObjectChoiceField diagramInclusionChoiceField;
	private final DiagramChoiceQuestion diagramChoiceQuestion;
	private final ObjectChoiceField diagramFilterChoiceField;
}
