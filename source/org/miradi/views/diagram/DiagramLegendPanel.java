/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
package org.miradi.views.diagram;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.miradi.actions.*;
import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.ObjectCheckBoxField;
import org.miradi.dialogs.base.AbstractDialogWithClose;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.dialogs.base.ObjectRefListEditorPanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.taggedObjectSet.TaggedObjectSetManagementPanel;
import org.miradi.dialogs.taggedObjectSet.TaggedObjectSetPoolTable;
import org.miradi.dialogs.taggedObjectSet.TaggedObjectSetPoolTableModel;
import org.miradi.icons.GoalIcon;
import org.miradi.icons.IconManager;
import org.miradi.icons.ObjectiveIcon;
import org.miradi.icons.TaggedObjectSetIcon;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.DiagramLegendQuestion;
import org.miradi.schemas.*;
import org.miradi.utils.CodeList;
import org.miradi.views.umbrella.LegendPanel;
import org.miradi.views.umbrella.doers.AbstractPopUpEditDoer;

abstract public class DiagramLegendPanel extends LegendPanel implements CommandExecutedListener
{
	public DiagramLegendPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse.getProject());
		
		mainWindow = mainWindowToUse;
		editListPanel = new ObjectRefListEditorPanel(getProject(), ORef.createInvalidWithType(getDiagramType()), DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, TaggedObjectSetSchema.getObjectType());
		createLegendCheckBoxes();
		addAllComponents();
		updateLegendPanel(getLegendSettings(DiagramObject.TAG_HIDDEN_TYPES));	
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();

		editListPanel.becomeActive();
		getProject().addCommandExecutedListener(this);
	}
	
	@Override
	public void becomeInactive()
	{
		getProject().removeCommandExecutedListener(this);
		editListPanel.becomeInactive();
		
		super.becomeInactive();	
	}
	
	@Override
	public void dispose()
	{
		if(editListPanel != null)
			editListPanel.dispose();
		editListPanel = null;
		
		super.dispose();
	}
	
	private void addAllComponents() throws Exception
	{
		setBorder(new EmptyBorder(5,5,5,5));
		
		add(createLegendButtonPanel(mainWindow.getActions()));
		
		DiagramObject diagramObject = getCurrentDiagramObject();
		if (diagramObject != null)
			addTaggedObjectSetPanel(diagramObject);
		
		updateCheckBoxes();
		setMinimumSize(new Dimension(0,0));
	}

	private void addTaggedObjectSetPanel(DiagramObject diagramObject)
	{
		TwoColumnPanel manageTagsPanel = new TwoColumnPanel();
		manageTagsPanel.disableFill();
		manageTagsPanel.setBackground(AppPreferences.getControlPanelBackgroundColor());

		ObjectCheckBoxField enableTaggingCheckBox = new ObjectCheckBoxField(getProject(), diagramObject.getRef(), DiagramObject.TAG_IS_TAGGING_ENABLED, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE);
		enableTaggingCheckBox.getComponent().setBackground(AppPreferences.getControlPanelBackgroundColor());
		enableTaggingCheckBox.updateFromObject();
		manageTagsPanel.add(enableTaggingCheckBox.getComponent());

		PanelButton manageTagsButton = new PanelButton(EAM.text("Manage Tags"), new TaggedObjectSetIcon());
		manageTagsButton.addActionListener(new ManageTaggedObjectSetButtonHandler());
		manageTagsPanel.add(manageTagsButton);

		add(manageTagsPanel);

		editListPanel.setObjectRef(diagramObject.getRef());
		editListPanel.setBackground(AppPreferences.getControlPanelBackgroundColor());
		boolean isTaggingEnabled = diagramObject.isTaggingEnabled();
		editListPanel.setEditable(isTaggingEnabled);
		add(editListPanel);
	}
	
	private void createLegendCheckBoxes()
	{
		createCheckBox(ScopeBoxSchema.OBJECT_NAME);
		createCheckBox(TargetSchema.OBJECT_NAME);
		createCheckBox(HumanWelfareTargetSchema.OBJECT_NAME);
		createCheckBox(BiophysicalFactorSchema.OBJECT_NAME);
		createCheckBox(BiophysicalResultSchema.OBJECT_NAME);
		createCheckBox(Cause.OBJECT_NAME_THREAT);
		createCheckBox(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR);
		createCheckBox(ThreatReductionResultSchema.OBJECT_NAME);
		createCheckBox(IntermediateResultSchema.OBJECT_NAME);
		createCheckBox(StrategySchema.OBJECT_NAME);
		createCheckBox(Strategy.OBJECT_NAME_DRAFT);
		createCheckBox(TextBoxSchema.OBJECT_NAME);
		createCheckBox(ScopeBoxSchema.OBJECT_NAME);
		createCheckBox(GroupBoxSchema.OBJECT_NAME);
		
		createCheckBox(FactorLinkSchema.OBJECT_NAME);
		createCheckBox(StressSchema.OBJECT_NAME);
		createCheckBox(TaskSchema.ACTIVITY_NAME);
		
		createCheckBox(GoalSchema.OBJECT_NAME);
		createCheckBox(ObjectiveSchema.OBJECT_NAME);
		createCheckBox(IndicatorSchema.OBJECT_NAME);
	}
	
	private JPanel createLegendButtonPanel(Actions actions)
	{
		TwoColumnPanel jpanel = new TwoColumnPanel();
		jpanel.disableFill();
		jpanel.setBackground(AppPreferences.getControlPanelBackgroundColor());
		
		addButtonLineWithCheckBox(jpanel, ScopeBoxSchema.getObjectType(), ScopeBoxSchema.OBJECT_NAME, actions.get(ActionInsertScopeBox.class));
		
		addButtonLineWithCheckBox(jpanel, TargetSchema.getObjectType(), TargetSchema.OBJECT_NAME, actions.get(ActionInsertTarget.class));
		if (getProject().getMetadata().isHumanWelfareTargetMode())
			addButtonLineWithCheckBox(jpanel, HumanWelfareTargetSchema.getObjectType(), HumanWelfareTargetSchema.OBJECT_NAME, actions.get(ActionInsertHumanWelfareTarget.class));

		createCustomLegendPanelSection(actions, jpanel);
		
		addButtonLineWithCheckBox(jpanel, StrategySchema.getObjectType(),StrategySchema.OBJECT_NAME, actions.get(ActionInsertStrategy.class));
		if (mainWindow.getDiagramView().isStrategyBrainstormMode())
			addButtonLineWithCheckBox(jpanel, StrategySchema.getObjectType(), Strategy.OBJECT_NAME_DRAFT, actions.get(ActionInsertDraftStrategy.class));

		addButtonLineWithCheckBox(jpanel, FactorLinkSchema.getObjectType(), FactorLinkSchema.OBJECT_NAME, actions.get(ActionInsertLink.class));
		
		addIconLineWithCheckBox(jpanel, GoalSchema.getObjectType(), GoalSchema.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(jpanel, ObjectiveSchema.getObjectType(), ObjectiveSchema.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(jpanel, IndicatorSchema.getObjectType(), IndicatorSchema.OBJECT_NAME, IconManager.getIndicatorIcon());
		
		addStressLine(jpanel);
		addActivityLine(jpanel);
		
		addButtonLineWithCheckBox(jpanel, TextBoxSchema.getObjectType(), TextBoxSchema.OBJECT_NAME, actions.get(ActionInsertTextBox.class));
		addButtonLineWithCheckBox(jpanel, GroupBoxSchema.getObjectType(), GroupBoxSchema.OBJECT_NAME, actions.get(ActionInsertGroupBox.class));
		
		return jpanel;
	}

	protected void addActivityLine(TwoColumnPanel jpanel)
	{
	}

	protected void addStressLine(TwoColumnPanel jpanel)
	{
	}

	private void updateCheckBoxes() throws Exception
	{
		if (isInvalidLayerManager(getLayerManager()))
			return;
		
		Object[] keys = checkBoxes.keySet().toArray();
		for (int index = 0; index < keys.length; ++index)
		{
			updateCheckBox(getLayerManager(), checkBoxes.get(keys[index]).getClientProperty(LAYER).toString());
		}
	}
	
	public void actionPerformed(ActionEvent event)
	{
		updateVisibility();
		saveSettingsToProject(DiagramObject.TAG_HIDDEN_TYPES);
		getMainWindow().updateActionStates();
	}
	
	private void saveSettingsToProject(String tag)
	{
		try
		{
			CommandSetObjectData setLegendSettingsCommand = new CommandSetObjectData(getCurrentDiagramObject().getRef(), tag, getLegendSettings().toString());
			getProject().executeCommand(setLegendSettingsCommand);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unable to update diagram legend settings:" + e.getMessage());
		}
	}

	private CodeList getLegendSettings(String tag)
	{
		try
		{
			if (getCurrentDiagramObject() == null)
				return new CodeList();
			
			return new CodeList(getCurrentDiagramObject().getData(tag));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unable to read project settings:" + e.getMessage());
			return new CodeList();
		}
	}
	
	public void resetCheckBoxes() throws Exception
	{
		removeAll();
		addAllComponents();
		updateLegendPanel(getLegendSettings(DiagramObject.TAG_HIDDEN_TYPES));
		validate();
	}
	
	private void updateCheckBox(LayerManager manager, String property) throws Exception
	{
		JCheckBox checkBox = findCheckBox(property);
		checkBox.setSelected(manager.isTypeVisible(property));
	}
	
	private CodeList getLegendSettings()
	{
		CodeList hiddenTypes = new CodeList();
		ChoiceItem[] choices = new DiagramLegendQuestion().getChoices();
		for (int index = 0; index < choices.length; ++index)
		{
			if (!isSelected(choices[index].getCode()))
				hiddenTypes.add(choices[index].getCode());
		}
		return hiddenTypes;
	}
	
	public void updateLegendPanel(CodeList hiddenTypes)
	{
		if (isInvalidLayerManager(getLayerManager()))
			return;
		
		Object[] keys = checkBoxes.keySet().toArray();
		for (int index = 0; index < keys.length; ++index)
		{
			findCheckBox(keys[index]).setSelected(true);
		}
		
		for (int index = 0; index < hiddenTypes.size(); ++index)
		{
			String hiddenType = hiddenTypes.get(index);
			JCheckBox checkBoxToSetSelectionOn = findCheckBox(hiddenType);
			if (checkBoxToSetSelectionOn == null)
			{
				EAM.logVerbose("No check box was found for:" + hiddenType);
				continue;
			}
			
			checkBoxToSetSelectionOn.setSelected(false);
		}

		updateVisibility();
	}

	private void updateVisibility()
	{
		mainWindow.preventActionUpdates();
		try
		{
			mainWindow.getDiagramView().updateVisibilityOfFactorsAndClearSelectionModel();
			mainWindow.updateStatusBar();
		}
		finally
		{
			mainWindow.allowActionUpdates();
			mainWindow.updateActionsAndStatusBar();
		}
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (isToggleDiagramTaggingCommand(event))
			{
				DiagramObject diagramObject = getCurrentDiagramObject();
				if (diagramObject != null)
				{
					boolean isTaggingEnabled = diagramObject.isTaggingEnabled();
					editListPanel.setEditable(isTaggingEnabled);
				}
			}

			if (shouldResetCheckBoxes(event))
				resetCheckBoxes();

			if (isUpdateTaggedObjectSetsCommand(event))
			{
				DiagramObject diagramObject = getCurrentDiagramObject();
				if (diagramObject != null)
					editListPanel.setObjectRef(diagramObject.getRef());
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private boolean shouldResetCheckBoxes(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_HUMAN_WELFARE_TARGET_MODE))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_BIOPHYSICAL_FACTOR_MODE))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(ConceptualModelDiagramSchema.getObjectType(), ConceptualModelDiagram.TAG_HIDDEN_TYPES))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ResultsChainDiagramSchema.getObjectType(), ResultsChainDiagram.TAG_HIDDEN_TYPES))
			return true;
		
		return false;
	}

	private boolean isToggleDiagramTaggingCommand(CommandExecutedEvent event)
	{
		return 	event.isSetDataCommandWithThisTypeAndTag(ConceptualModelDiagramSchema.getObjectType(), DiagramObject.TAG_IS_TAGGING_ENABLED) ||
				event.isSetDataCommandWithThisTypeAndTag(ResultsChainDiagramSchema.getObjectType(), DiagramObject.TAG_IS_TAGGING_ENABLED);
	}

	private boolean isUpdateTaggedObjectSetsCommand(CommandExecutedEvent event)
	{
		Command command = event.getCommand();
		if (event.isCreateObjectCommand())
		{
			CommandCreateObject createCommand = (CommandCreateObject) command;
			return TaggedObjectSet.is(createCommand.getObjectType());
		}
		
		if (event.isDeleteObjectCommand())
		{
			CommandDeleteObject deleteCommand = (CommandDeleteObject) command;
			return TaggedObjectSet.is(deleteCommand.getObjectType());
		}
		
		return event.isSetDataCommandWithThisType(TaggedObjectSetSchema.getObjectType());
	}
	
	private boolean isInvalidLayerManager(LayerManager manager)
	{
		return manager == null;
	}
		
	private LayerManager getLayerManager()
	{
		if (getMainWindow().getCurrentDiagramComponent() == null)
			return null;
		
		return getMainWindow().getCurrentDiagramComponent().getLayerManager();
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private DiagramObject getCurrentDiagramObject()
	{
		return getMainWindow().getDiagramView().getCurrentDiagramObject();
	}

	private class ManageTaggedObjectSetButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			showTaggedObjectSetManageDialog();
		}

		private void showTaggedObjectSetManageDialog()
		{
			try
			{
				closeActiveTaggedObjectsDialog();

				TaggedObjectSetPoolTable poolTable = new TaggedObjectSetPoolTable(getMainWindow(), new TaggedObjectSetPoolTableModel(getProject()));
				ObjectManagementPanel panel = new TaggedObjectSetManagementPanel(getMainWindow(), poolTable);
				activeTaggedObjectsDialog = AbstractPopUpEditDoer.createAndShowManagementDialogWithInstructionsButton(mainWindow, panel, EAM.text("Manage Tags"));
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}

		private void closeActiveTaggedObjectsDialog()
		{
			if(activeTaggedObjectsDialog != null && activeTaggedObjectsDialog.isDisplayable())
			{
				activeTaggedObjectsDialog.setVisible(false);
				activeTaggedObjectsDialog.dispose();
			}
			activeTaggedObjectsDialog = null;
		}


		private AbstractDialogWithClose activeTaggedObjectsDialog;
	}
	
	abstract protected void createCustomLegendPanelSection(Actions actions, JPanel jpanel);
	
	abstract protected int getDiagramType();

	private MainWindow mainWindow;
	private ObjectRefListEditorPanel editListPanel;
}