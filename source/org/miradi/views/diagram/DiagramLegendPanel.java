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
import org.miradi.actions.ActionInsertScopeBox;
import org.miradi.actions.ActionInsertStrategy;
import org.miradi.actions.ActionInsertTarget;
import org.miradi.actions.ActionInsertTextBox;
import org.miradi.actions.Actions;
import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.DiagramGroupBoxCell;
import org.miradi.diagram.cells.DiagramScopeBoxCell;
import org.miradi.diagram.cells.DiagramStrategyCell;
import org.miradi.diagram.cells.DiagramTargetCell;
import org.miradi.diagram.cells.DiagramTextBoxCell;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.dialogs.base.ObjectRefListEditorPanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.taggedObjectSet.TaggedObjectSetManagementPanel;
import org.miradi.dialogs.taggedObjectSet.TaggedObjectSetPoolTable;
import org.miradi.dialogs.taggedObjectSet.TaggedObjectSetPoolTableModel;
import org.miradi.icons.FactorLinkIcon;
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
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Goal;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Objective;
import org.miradi.objects.ScopeBox;
import org.miradi.objects.Strategy;
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
	abstract protected void createCustomLegendPanelSection(Actions actions, JPanel jpanel);
	
	public DiagramLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject());
		mainWindow = mainWindowToUse;
	
		getProject().addCommandExecutedListener(this);
		createLegendCheckBoxes();
		addAllComponents();
		updateLegendPanel(getLegendSettings(DiagramObject.TAG_HIDDEN_TYPES));
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
		// TODO: Really, we should only construct eLP once in the constructor,
		// but that would require the subclasses to tell us what type of DO
		// we are managing so we could pass that to the construtor. 
		// Then, we would call eLP.setObjectRef() here
		if(editListPanel != null)
			editListPanel.dispose();
		editListPanel = new ObjectRefListEditorPanel(getProject(), diagramObject.getRef(), DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, TaggedObjectSet.getObjectType());
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
		createCheckBox(FactorLink.OBJECT_NAME_TARGETLINK);
		createCheckBox(FactorLink.OBJECT_NAME_STRESS);
		createCheckBox(Task.ACTIVITY_NAME);
		
		createCheckBox(Goal.OBJECT_NAME);
		createCheckBox(Objective.OBJECT_NAME);
		createCheckBox(Indicator.OBJECT_NAME);

	}
	
	protected JPanel createLegendButtonPanel(Actions actions)
	{
		TwoColumnPanel jpanel = new TwoColumnPanel();
		jpanel.disableFill();
		jpanel.setBackground(AppPreferences.getControlPanelBackgroundColor());
		
		addButtonLineWithCheckBox(jpanel, ScopeBox.getObjectType(), ScopeBox.OBJECT_NAME, actions.get(ActionInsertScopeBox.class));
		
		addButtonLineWithCheckBox(jpanel, Target.getObjectType(), Target.OBJECT_NAME, actions.get(ActionInsertTarget.class));
		createCustomLegendPanelSection(actions, jpanel);
		
		addButtonLineWithCheckBox(jpanel, Strategy.getObjectType(),Strategy.OBJECT_NAME, actions.get(ActionInsertStrategy.class));
		if (mainWindow.getDiagramView().isStategyBrainstormMode())
		{
			addButtonLineWithCheckBox(jpanel, Strategy.getObjectType(), Strategy.OBJECT_NAME_DRAFT, actions.get(ActionInsertDraftStrategy.class));
		}

		addButtonLineWithCheckBox(jpanel, FactorLink.getObjectType(), FactorLink.OBJECT_NAME, actions.get(ActionInsertFactorLink.class));
		addTargetLinkLine(jpanel, FactorLink.getObjectType(), FactorLink.OBJECT_NAME_TARGETLINK);
		
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

	protected void addTargetLinkLine(JPanel jpanel, int objectType, String objectName)
	{
		targetLinkCheckBox = findCheckBox(objectName);
		jpanel.add(targetLinkCheckBox);
		jpanel.add(new PanelTitleLabel(EAM.fieldLabel(objectType, objectName), new FactorLinkIcon()));
	}
	
	private void updateCheckBoxes()
	{
		if (isInvalidLayerManager(getLayerManager()))
			return;
		
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i=0; i<keys.length; ++i)
		{
			updateCheckBox(getLayerManager(), ((JCheckBox)checkBoxes.get(keys[i])).getClientProperty(LAYER).toString());
		}
	}
	
	public void actionPerformed(ActionEvent event)
	{
		JCheckBox checkBox = (JCheckBox)event.getSource();
		String property = (String) checkBox.getClientProperty(LAYER);
		LayerManager manager = getLayerManager();
		setLegendVisibilityOfFactorCheckBoxes(manager, property);
		updateVisiblity();
		saveSettingsToProject(DiagramObject.TAG_HIDDEN_TYPES);
	}
	
	protected void saveSettingsToProject(String tag)
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

	protected CodeList getLegendSettings(String tag)
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
	
	protected void setLegendVisibilityOfFactorCheckBoxes(LayerManager manager, String property)
	{
		JCheckBox checkBox = findCheckBox(property);
		
		if (property.equals(Strategy.OBJECT_NAME))
			manager.setVisibility(DiagramStrategyCell.class, checkBox.isSelected());
		else if (property.equals(Target.OBJECT_NAME))
			manager.setVisibility(DiagramTargetCell.class, checkBox.isSelected());
		else if (property.equals(FactorLink.OBJECT_NAME))
		{
			manager.setFactorLinksVisible(checkBox.isSelected());
			targetLinkCheckBox.setEnabled(checkBox.isSelected());
		}
		else if (property.equals(FactorLink.OBJECT_NAME_TARGETLINK))
			manager.setTargetLinksVisible(checkBox.isSelected());
		else if (property.equals(Goal.OBJECT_NAME))
			manager.setGoalsVisible(checkBox.isSelected());
		else if (property.equals(Objective.OBJECT_NAME))
			manager.setObjectivesVisible(checkBox.isSelected());
		else if (property.equals(Indicator.OBJECT_NAME))
			manager.setIndicatorsVisible(checkBox.isSelected());
		else if (property.equals(ScopeBox.OBJECT_NAME))
			manager.setScopeBoxVisible(checkBox.isSelected()); 
		else if (property.equals(TextBox.OBJECT_NAME))
			manager.setVisibility(DiagramTextBoxCell.class, checkBox.isSelected());
		else if (property.equals(GroupBox.OBJECT_NAME))
			manager.setVisibility(DiagramGroupBoxCell.class, checkBox.isSelected());
		else if (property.equals(FactorLink.OBJECT_NAME_STRESS))
			manager.setStressesVisible(checkBox.isSelected());
		else if (property.equals(ScopeBox.OBJECT_NAME))
			manager.setVisibility(DiagramScopeBoxCell.class, checkBox.isSelected());
	}
	
	public void resetCheckBoxes()
	{
		removeAll();
		addAllComponents();
		updateLegendPanel(getLegendSettings(DiagramObject.TAG_HIDDEN_TYPES));
		validate();
	}
	
	public void updateCheckBox(LayerManager manager, String property)
	{
		JCheckBox checkBox = findCheckBox(property);

		if (property.equals(ScopeBox.OBJECT_NAME))
			checkBox.setSelected(manager.isScopeBoxVisible());
	
		else if (property.equals(Target.OBJECT_NAME))
			checkBox.setSelected(manager.isTypeVisible(DiagramTargetCell.class));
		
		else if (property.equals(Strategy.OBJECT_NAME))
			checkBox.setSelected(manager.isTypeVisible(DiagramStrategyCell.class));
		
		else if (property.equals(FactorLink.OBJECT_NAME))
			checkBox.setSelected(manager.areFactorLinksVisible());
		
		else if (property.equals(FactorLink.OBJECT_NAME_TARGETLINK))
			checkBox.setSelected(manager.areTargetLinksVisible());
		
		else if (property.equals(Goal.OBJECT_NAME))
			checkBox.setSelected(manager.areGoalsVisible());
		
		else if (property.equals(Objective.OBJECT_NAME))
			checkBox.setSelected(manager.areObjectivesVisible());
		
		else if (property.equals(Indicator.OBJECT_NAME))
			checkBox.setSelected(manager.areIndicatorsVisible());
		
		else if (property.equals(TextBox.OBJECT_NAME))
			checkBox.setSelected(manager.areTextBoxesVisible());
		
		else if (property.equals(ScopeBox.OBJECT_NAME))
			checkBox.setSelected(manager.areTextBoxesVisible());
		
		else if (property.equals(GroupBox.OBJECT_NAME))
			checkBox.setSelected(manager.areGroupBoxesVisible());
		
		else if (property.equals(FactorLink.OBJECT_NAME_STRESS))
			checkBox.setSelected(manager.areStressesVisible());
	}
	
	public CodeList getLegendSettings()
	{
		CodeList hiddenTypes = new CodeList();
		ChoiceItem[] choices = new DiagramLegendQuestion().getChoices();
		for (int i=0; i<choices.length; ++i)
		{
			if (!isSelected(choices[i].getCode()))
				hiddenTypes.add(choices[i].getCode());
		}
		return hiddenTypes;
	}
	
	public void updateLegendPanel(CodeList hiddenTypes)
	{
		if (isInvalidLayerManager(getLayerManager()))
			return;
		
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i=0; i<keys.length; ++i)
		{
			findCheckBox(keys[i]).setSelected(true);
			setLegendVisibilityOfFactorCheckBoxes(getLayerManager(), keys[i].toString());
		}
		
		for (int i=0; i<hiddenTypes.size(); ++i)
		{
			findCheckBox(hiddenTypes.get(i)).setSelected(false);
			setLegendVisibilityOfFactorCheckBoxes(getLayerManager(), hiddenTypes.get(i));
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
		if (isUpdateTaggedObjectSetsCommand(event))
			resetCheckBoxes();
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
	
	protected boolean isInvalidLayerManager(LayerManager manager)
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
	
	class ManageTaggedObjectSetButtonHandler implements ActionListener
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

	private MainWindow mainWindow;
	private JCheckBox targetLinkCheckBox;
	private ObjectRefListEditorPanel editListPanel;
}

