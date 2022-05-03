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
package org.miradi.dialogs.taggedObjectSet;

import org.miradi.actions.ActionEditTaggedObjectSet;
import org.miradi.diagram.DiagramComponent;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogfields.TaggedObjectSetFactorListField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.ConceptualModelIcon;
import org.miradi.icons.ResultsChainIcon;
import org.miradi.icons.TaggedObjectSetIcon;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.schemas.TaggedObjectSetSchema;
import org.miradi.utils.ObjectsActionButton;
import org.miradi.views.diagram.DiagramView;
import org.miradi.views.diagram.DiagramViewEvent;
import org.miradi.views.diagram.DiagramViewListener;
import org.miradi.views.umbrella.ObjectPicker;

import javax.swing.*;


public class TaggedObjectSetPropertiesPanel extends ObjectDataInputPanel implements DiagramViewListener
{
	public TaggedObjectSetPropertiesPanel(MainWindow mainWindowToUse, DiagramObject diagramObjectToUse, ObjectPicker picker) throws Exception
	{
		super(mainWindowToUse.getProject(), TaggedObjectSetSchema.getObjectType());

		mainWindow = mainWindowToUse;
		diagramObject = diagramObjectToUse;
		objectPicker = picker;

		DiagramView diagramView = getMainWindow().getDiagramView();
		if (diagramView != null)
			diagramView.addDiagramViewListener(this);

		rebuild();
	}

	@Override
	public void dispose()
	{
		DiagramView diagramView = getMainWindow().getDiagramView();
		if (diagramView != null)
			diagramView.removeDiagramViewListener(this);
	}

	@Override
	public void tabWasSelected(DiagramViewEvent event)
	{
		rebuildForCurrentDiagramObject();
	}

	@Override
	public void diagramWasSelected(DiagramViewEvent event)
	{
		rebuildForCurrentDiagramObject();
	}

	private void rebuild() throws Exception
	{
		removeAll();
		getFields().clear();

		ObjectDataInputField shortLabelField = createStringField(TaggedObjectSet.TAG_SHORT_LABEL, 10);
		ObjectDataInputField labelField = createExpandableField(TaggedObjectSet.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Tag"), new TaggedObjectSetIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

		OneRowPanel fieldPanel = new OneRowPanel();
		fieldPanel.setGaps(3);
		fieldPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		fieldPanel.add(new PanelTitleLabel(diagramObject.getFullName()));
		fieldPanel.add(new JLabel(" "));

		Icon diagramIcon = diagramObject.isConceptualModelDiagram() ? new ConceptualModelIcon() : new ResultsChainIcon();
		fieldPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		addLabelWithIcon(EAM.text("Diagram"), diagramIcon);
		add(fieldPanel);

		ObjectDataInputField taggedDiagramFactorListField = createTaggedDiagramFactorListField(diagramObject, TaggedObjectSetSchema.getObjectType(), TaggedObjectSet.PSEUDO_TAG_REFERRING_DIAGRAM_FACTOR_REFS);
		ObjectsActionButton editButton = createObjectsActionButton(mainWindow.getActions().getObjectsAction(ActionEditTaggedObjectSet.class), objectPicker);
		addFieldWithEditButton(EAM.text("Tagged Items"), taggedDiagramFactorListField, editButton);

		addField(createMultilineField(TaggedObjectSet.TAG_COMMENTS));
		updateFieldsFromProject();

		doLayout();

		validate();
		repaint();
	}

	private void rebuildForCurrentDiagramObject()
	{
		try
		{
			DiagramComponent diagramComponent = getMainWindow().getCurrentDiagramComponent();
			if (diagramComponent != null)
			{
				diagramObject = diagramComponent.getDiagramObject();
				rebuild();
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Tag Properties Panel");
	}

	private ObjectDataInputField createTaggedDiagramFactorListField(DiagramObject diagramObject, int objectType, String tag)
	{
		return new TaggedObjectSetFactorListField(getMainWindow(), diagramObject, getRefForType(objectType), tag, createUniqueIdentifierForTable(objectType, tag));
	}

	private MainWindow mainWindow;
	private DiagramObject diagramObject;
	private ObjectPicker objectPicker;
}
