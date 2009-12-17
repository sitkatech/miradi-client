/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.diagram;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.miradi.actions.ActionInsertDraftStrategy;
import org.miradi.actions.ActionInsertFactorLink;
import org.miradi.actions.ActionInsertGroupBox;
import org.miradi.actions.ActionInsertHumanWelfareTarget;
import org.miradi.actions.ActionInsertScopeBox;
import org.miradi.actions.ActionInsertStrategy;
import org.miradi.actions.ActionInsertTarget;
import org.miradi.actions.ActionInsertTextBox;
import org.miradi.actions.Actions;
import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.dialogs.base.ObjectRefListEditorPanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.taggedObjectSet.TaggedObjectSetManagementPanel;
import org.miradi.dialogs.taggedObjectSet.TaggedObjectSetPoolTable;
import org.miradi.dialogs.taggedObjectSet.TaggedObjectSetPoolTableModel;
import org.miradi.icons.GoalIcon;
import org.miradi.icons.IndicatorIcon;
import org.miradi.icons.ObjectiveIcon;
import org.miradi.icons.TaggedObjectSetIcon;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Goal;
import org.miradi.objects.GroupBox;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.ScopeBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.DiagramLegendQuestion;
import org.miradi.utils.CodeList;
import org.miradi.views.umbrella.LegendPanel;
import org.miradi.views.umbrella.doers.AbstractPopUpEditDoer;

abstract public class DiagramLegendPanel extends LegendPanel implements CommandExecutedListener
{
	public DiagramLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject());
		
		mainWindow = mainWindowToUse;
		getProject().addCommandExecutedListener(this);
		createLegendCheckBoxes();
		addAllComponents();
		updateLegendPanel(getLegendSettings(DiagramObject.TAG_HIDDEN_TYPES));
		
		editListPanel = new ObjectRefListEditorPanel(getProject(), getDiagramType(), DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, TaggedObjectSet.getObjectType());
	}
	
	@Override
	public void dispose()
	{
		getProject().removeCommandExecutedListener(this);

		if(editListPanel != null)
			editListPanel.dispose();
		editListPanel = null;
		
		super.dispose();
	}
	
	private void addAllComponents()
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
		editListPanel.setObjectRef(diagramObject.getRef());
		editListPanel.setBackground(AppPreferences.getControlPanelBackgroundColor());
		add(editListPanel);
		
		PanelButton manageButton = new PanelButton(EAM.text("Manage Tags (BETA)..."), new TaggedObjectSetIcon());
		manageButton.addActionListener(new ManageTaggedObjectSetButtonHandler());
		add(manageButton);
	}
	
	private void createLegendCheckBoxes()
	{
		createCheckBox(ScopeBox.OBJECT_NAME);
		createCheckBox(Target.OBJECT_NAME);
		createCheckBox(HumanWelfareTarget.OBJECT_NAME);
		
		createCheckBox(Cause.OBJECT_NAME_THREAT);
		createCheckBox(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR);
		createCheckBox(ThreatReductionResult.OBJECT_NAME);
		createCheckBox(IntermediateResult.OBJECT_NAME);
		createCheckBox(Strategy.OBJECT_NAME);
		createCheckBox(Strategy.OBJECT_NAME_DRAFT);
		createCheckBox(TextBox.OBJECT_NAME);
		createCheckBox(ScopeBox.OBJECT_NAME);
		createCheckBox(GroupBox.OBJECT_NAME);
		
		createCheckBox(FactorLink.OBJECT_NAME);
		createCheckBox(Stress.OBJECT_NAME);
		createCheckBox(Task.ACTIVITY_NAME);
		
		createCheckBox(Goal.OBJECT_NAME);
		createCheckBox(Objective.OBJECT_NAME);
		createCheckBox(Indicator.OBJECT_NAME);

	}
	
	private JPanel createLegendButtonPanel(Actions actions)
	{
		TwoColumnPanel jpanel = new TwoColumnPanel();
		jpanel.disableFill();
		jpanel.setBackground(AppPreferences.getControlPanelBackgroundColor());
		
		addButtonLineWithCheckBox(jpanel, ScopeBox.getObjectType(), ScopeBox.OBJECT_NAME, actions.get(ActionInsertScopeBox.class));
		
		addButtonLineWithCheckBox(jpanel, Target.getObjectType(), Target.OBJECT_NAME, actions.get(ActionInsertTarget.class));
		if (getProject().getMetadata().isHumanWelfareTargetMode())
			addButtonLineWithCheckBox(jpanel, HumanWelfareTarget.getObjectType(), HumanWelfareTarget.OBJECT_NAME, actions.get(ActionInsertHumanWelfareTarget.class));
		
		createCustomLegendPanelSection(actions, jpanel);
		
		addButtonLineWithCheckBox(jpanel, Strategy.getObjectType(),Strategy.OBJECT_NAME, actions.get(ActionInsertStrategy.class));
		if (mainWindow.getDiagramView().isStategyBrainstormMode())
			addButtonLineWithCheckBox(jpanel, Strategy.getObjectType(), Strategy.OBJECT_NAME_DRAFT, actions.get(ActionInsertDraftStrategy.class));

		addButtonLineWithCheckBox(jpanel, FactorLink.getObjectType(), FactorLink.OBJECT_NAME, actions.get(ActionInsertFactorLink.class));
		
		addIconLineWithCheckBox(jpanel, Goal.getObjectType(), Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(jpanel, Objective.getObjectType(), Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(jpanel, Indicator.getObjectType(), Indicator.OBJECT_NAME, new IndicatorIcon());
		
		addStressLine(jpanel);
		addActivityLine(jpanel);
		
		addButtonLineWithCheckBox(jpanel, TextBox.getObjectType(), TextBox.OBJECT_NAME, actions.get(ActionInsertTextBox.class));
		addButtonLineWithCheckBox(jpanel, GroupBox.getObjectType(), GroupBox.OBJECT_NAME, actions.get(ActionInsertGroupBox.class));
		
		return jpanel;
	}

	protected void addActivityLine(TwoColumnPanel jpanel)
	{
	}

	protected void addStressLine(TwoColumnPanel jpanel)
	{
	}

	private void updateCheckBoxes()
	{
		if (isInvalidLayerManager(getLayerManager()))
			return;
		
		Object[] keys = checkBoxes.keySet().toArray();
		for (int index = 0; index < keys.length; ++index)
		{
			updateCheckBox(getLayerManager(), ((JCheckBox)checkBoxes.get(keys[index])).getClientProperty(LAYER).toString());
		}
	}
	
	public void actionPerformed(ActionEvent event)
	{
		updateVisiblity();
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
	
	public void resetCheckBoxes()
	{
		removeAll();
		addAllComponents();
		updateLegendPanel(getLegendSettings(DiagramObject.TAG_HIDDEN_TYPES));
		validate();
	}
	
	private void updateCheckBox(LayerManager manager, String property)
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

		updateVisiblity();
	}

	private void updateVisiblity()
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
		if (shouldResetCheckBoxes(event))
			resetCheckBoxes();
	}

	private boolean shouldResetCheckBoxes(CommandExecutedEvent event)
	{
		if (isUpdateTaggedObjectSetsCommand(event))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_HUMAN_WELFARE_TARGET_MODE))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ConceptualModelDiagram.getObjectType(), ConceptualModelDiagram.TAG_HIDDEN_TYPES))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ResultsChainDiagram.getObjectType(), ResultsChainDiagram.TAG_HIDDEN_TYPES))
			return true;
		
		return false;
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
		
		return event.isSetDataCommandWithThisType(TaggedObjectSet.getObjectType());
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
				TaggedObjectSetPoolTable poolTable = new TaggedObjectSetPoolTable(getMainWindow(), new TaggedObjectSetPoolTableModel(getProject()));
				ObjectManagementPanel panel = new TaggedObjectSetManagementPanel(getMainWindow(), poolTable);
				AbstractPopUpEditDoer.showManagementDialog(mainWindow, panel, EAM.text("Manage Tagged Object Sets"));
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
	}
	
	abstract protected void createCustomLegendPanelSection(Actions actions, JPanel jpanel);
	
	abstract protected int getDiagramType();

	private MainWindow mainWindow;
	private ObjectRefListEditorPanel editListPanel;
}