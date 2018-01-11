/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogfields;

import java.util.Set;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ListSelectionListener;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.IconManager;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.TaggedObjectSetQuestion;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.TaggedObjectSetSchema;
import org.miradi.utils.XmlUtilities2;

public class FactorTagListEditor extends AbstractQuestionEditorComponent implements CommandExecutedListener
{
	public FactorTagListEditor(Project projectToUse, DiagramObject diagramObjectToUse, Factor selectedFactorToUse)
	{
		super(createQuestion(projectToUse), SINGLE_COLUMN);
		
		project = projectToUse;
		diagramObject = diagramObjectToUse;
		selectedFactor = selectedFactorToUse;
	
		updateCheckboxesToMatchCurrentTaggedObjectSets();
		project.addCommandExecutedListener(this);
	}
	
	@Override
	public void dispose()
	{
		getProject().removeCommandExecutedListener(this);
		super.dispose();
	}
	
	@Override
	protected void addAdditionalComponent()
	{
		if (selectedFactor != null)
			add(createFactorLabelPanelWithIcon());
	}
	
	@Override
	public String getText()
	{
		throw new RuntimeException("Unexpected call to getText");
	}

	@Override
	public void setText(String codesToUse)
	{
		throw new RuntimeException("Unexpected call to setText");
	}
	
	@Override
	public void addListSelectionListener(ListSelectionListener listener)
	{
		throw new RuntimeException("Unexpected call to addListSelectionListener");
	}
	
	@Override
	public void removeListSelectionListener(ListSelectionListener listener)
	{
		throw new RuntimeException("Unexpected call to removeListSelectionListener");
	}
	
	private JPanel createFactorLabelPanelWithIcon()
	{
		TwoColumnPanel labelPanelWithIcon = new TwoColumnPanel();
		addEmptySpace(labelPanelWithIcon);
		addEmptySpace(labelPanelWithIcon);
		addEmptySpace(labelPanelWithIcon);
		try
		{
			Icon factorIcon = IconManager.getImage(selectedFactor);
			String label = selectedFactor.toString();
			label = XmlUtilities2.convertXmlTextToHtml(label);
			labelPanelWithIcon.add(new PanelTitleLabel(label, factorIcon));
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
		return labelPanelWithIcon;
	}

	private void addEmptySpace(TwoColumnPanel labelPanelWithIcon)
	{
		labelPanelWithIcon.add(new JLabel(" "));
	}
	
	@Override
	protected void toggleButtonStateChanged(ChoiceItem choiceItem, boolean isSelected) throws Exception
	{
		String refAsCode = choiceItem.getCode();
		ORef taggedObjectSetRef = ORef.createFromString(refAsCode);
		DiagramFactor diagramFactor = getDiagramObject().getDiagramFactor(getFactorToTag().getRef());
		CommandSetObjectData commandToTagUntagDiagramFactor =
				isSelected ? CommandSetObjectData.createAppendORefCommand(diagramFactor, DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS, taggedObjectSetRef) :
						CommandSetObjectData.createRemoveORefCommand(diagramFactor, DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS, taggedObjectSetRef);
		getProject().executeCommand(commandToTagUntagDiagramFactor);
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		if (isTaggedObjectRelatedCommand(event))
			updateCheckboxesToMatchCurrentTaggedObjectSets();
	}
	
	private boolean isTaggedObjectRelatedCommand(CommandExecutedEvent event)
	{
		if (event.isCreateCommandForThisType(TaggedObjectSetSchema.getObjectType()))
			return true;

		if (event.isDeleteCommandForThisType(TaggedObjectSetSchema.getObjectType()))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(TaggedObjectSetSchema.getObjectType(), TaggedObjectSet.TAG_LABEL))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(DiagramFactorSchema.getObjectType(), DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS))
			return true;
		
		return false;
	}

	private void updateCheckboxesToMatchCurrentTaggedObjectSets()
	{
		reloadQuestion();
		rebuildToggleButtonsBoxes();
		Set<ChoiceItem> choices = choiceItemToToggleButtonMap.keySet();
		for(ChoiceItem choiceItem : choices)
		{
			JToggleButton toggleButton = choiceItemToToggleButtonMap.get(choiceItem);
			ORef tagRef = ORef.createFromString(choiceItem.getCode());
			boolean isSelected = isFactorTagged(getFactorToTag(), tagRef);
			toggleButton.setSelected(isSelected);
		}
	}

	private boolean isFactorTagged(Factor factor, ORef tagRef)
	{
		boolean factorIsTagged = false;

		DiagramFactor diagramFactor = getDiagramObject().getDiagramFactor(factor.getRef());
		if (diagramFactor != null)
			factorIsTagged = diagramFactor.getTaggedObjectSetRefs().contains(tagRef);

		return factorIsTagged;
	}

	private void reloadQuestion()
	{
		((TaggedObjectSetQuestion)getQuestion()).reloadQuestion();
	}

	private Project getProject()
	{
		return project;
	}

	private DiagramObject getDiagramObject()
	{
		return diagramObject;
	}

	private Factor getFactorToTag()
	{
		return selectedFactor;
	}
	
	private static TaggedObjectSetQuestion createQuestion(Project projectToUse)
	{
		return new TaggedObjectSetQuestion(projectToUse);
	}
	
	private Project project;
	private DiagramObject diagramObject;
	private Factor selectedFactor;
}
