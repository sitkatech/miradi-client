/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

public class IndicatorListManagementPanel extends ModelessDialogPanel
{
	public IndicatorListManagementPanel(Project projectToUse, ModelNodeId nodeId, Actions actions) throws Exception
	{
		super(new BorderLayout());
		IndicatorId invalidId = new IndicatorId(BaseId.INVALID.asInt());
		
		listComponent = new IndicatorListTablePanel(projectToUse, actions, nodeId);
		add(listComponent, BorderLayout.CENTER);
		
		propertiesPanel = new IndicatorPropertiesPanel(projectToUse, actions, invalidId);
		listComponent.setPropertiesPanel(propertiesPanel);
		add(propertiesPanel, BorderLayout.AFTER_LAST_LINE);
	}
	
	public void dispose()
	{
		listComponent.dispose();
		listComponent = null;
		
		propertiesPanel.dispose();
		propertiesPanel = null;
		
		super.dispose();
	}

	public EAMObject getObject()
	{
		return listComponent.getSelectedObject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Tab|Indicators");
	}
	
	IndicatorListTablePanel listComponent;
	IndicatorPropertiesPanel propertiesPanel;
}


/////// TODO: Remove this obsolete code from NodePropertiesPanel
//private Component createIndicatorsGrid(DiagramNode node)
//{
//	indicatorsTab = new DialogGridPanel();
//
//	indicatorsTab.add(new UiLabel(EAM.text("Label|Indicator")));
//	indicatorsTab.add(createIndicatorDropdown());
//
//	indicatorsTab.add(new UiLabel(""));
//	EAMAction action = mainWindow.getActions().get(
//			ActionCreateIndicator.class);
//	UiButton buttonCreate = new UiButton(action);
//	JPanel panel = new JPanel(new BorderLayout());
//	panel.add(buttonCreate, BorderLayout.BEFORE_LINE_BEGINS);
//	indicatorsTab.add(panel);
//
//	return indicatorsTab;
//}
//
//public Component createIndicatorDropdown()
//{
//	dropdownIndicator = new UiComboBox();
//	populateIndicators();
//	selectCurrentIndicators();
//	dropdownIndicator.addActionListener(new IndicatorChangeHandler());
//
//	JPanel component = new JPanel(new BorderLayout());
//	component.add(dropdownIndicator, BorderLayout.LINE_START);
//	return component;
//}
//
//private void selectCurrentIndicators()
//{
//	IndicatorPool allAvailableIndicators = getProject().getIndicatorPool();
//	IdList currentIndicators = getCurrentNode().getIndicators();
//	Object nullIndicator = dropdownIndicator.getItemAt(0);
//
//	Object selected = nullIndicator;
//	if(currentIndicators.size() > 0)
//		selected = allAvailableIndicators.find(currentIndicators.get(0));
//	if(selected == null)
//		selected = nullIndicator;
//	dropdownIndicator.setSelectedItem(selected);
//}
//
//private Indicator populateIndicators()
//{
//	ignoreIndicatorChanges = true;
//	dropdownIndicator.removeAllItems();
//	Indicator nullIndicator = new Indicator(new IndicatorId(BaseId.INVALID
//			.asInt()));
//	dropdownIndicator.addItem(nullIndicator);
//
//	IndicatorPool allAvailableIndicators = getProject().getIndicatorPool();
//	BaseId[] availableIds = allAvailableIndicators.getIds();
//	for(int i = 0; i < availableIds.length; ++i)
//	{
//		dropdownIndicator.addItem(allAvailableIndicators
//				.find(availableIds[i]));
//	}
//	ignoreIndicatorChanges = false;
//	return nullIndicator;
//}
//
//class IndicatorChangeHandler implements ActionListener
//{
//	public void actionPerformed(ActionEvent event)
//	{
//		if(ignoreIndicatorChanges)
//			return;
//
//		try
//		{
//			int type = ObjectType.MODEL_NODE;
//			String tag = ConceptualModelNode.TAG_INDICATOR_IDS;
//			String indicators = getIndicators().toString();
//			CommandSetObjectData cmd = new CommandSetObjectData(type,
//					getNodeId(), tag, indicators);
//			getProject().executeCommand(cmd);
//		}
//		catch(CommandFailedException e)
//		{
//			EAM.logException(e);
//			EAM.errorDialog("That action failed due to an unknown error");
//		}
//	}
//
//}
//
//public IdList getIndicators()
//{
//	IdList selected = new IdList();
//	Indicator indicator = (Indicator)dropdownIndicator.getSelectedItem();
//	if(indicator != null && !indicator.getId().isInvalid())
//		selected.add(indicator.getId());
//	return selected;
//}
//
//void refreshIndicatorListIfNecessary(CommandExecutedEvent event)
//{
//	if(dropdownIndicator == null)
//		return;
//	Command rawCommand = event.getCommand();
//	if(rawCommand.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
//	{
//		CommandCreateObject cmd = (CommandCreateObject) rawCommand;
//		if(cmd.getObjectType() == ObjectType.INDICATOR)
//		{
//			populateIndicators();
//		}
//	}
//	if(rawCommand.getCommandName()
//			.equals(CommandSetObjectData.COMMAND_NAME))
//	{
//		CommandSetObjectData cmd = (CommandSetObjectData) rawCommand;
//		if(cmd.getObjectType() == ObjectType.INDICATOR)
//		{
//			Object selected = dropdownIndicator.getSelectedItem();
//			populateIndicators();
//			dropdownIndicator.setSelectedItem(selected);
//		}
//	}
//}
//
//void selectNewlyCreatedIndicatorIfNecessary(CommandExecutedEvent event)
//{
//	if(dropdownIndicator == null)
//		return;
//
//	Command rawCommand = event.getCommand();
//	if(rawCommand.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
//	{
//		CommandCreateObject cmd = (CommandCreateObject) rawCommand;
//		if(cmd.getObjectType() == ObjectType.INDICATOR)
//		{
//			Indicator newIndicator = getProject().getIndicatorPool().find(
//					cmd.getCreatedId());
//			dropdownIndicator.setSelectedItem(newIndicator);
//		}
//	}
//}
//
