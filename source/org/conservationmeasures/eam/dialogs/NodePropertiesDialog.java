/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.annotations.Goal;
import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.annotations.GoalPool;
import org.conservationmeasures.eam.annotations.IndicatorPool;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetFactorType;
import org.conservationmeasures.eam.commands.CommandSetIndicator;
import org.conservationmeasures.eam.commands.CommandSetNodeComment;
import org.conservationmeasures.eam.commands.CommandSetNodeName;
import org.conservationmeasures.eam.commands.CommandSetNodeObjectives;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.commands.CommandSetTargetGoal;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.IndirectFactorIcon;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ObjectiveIds;
import org.conservationmeasures.eam.objects.ObjectivePool;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DialogGridPanel;
import org.conservationmeasures.eam.utils.UiTextFieldWithLengthLimit;
import org.conservationmeasures.eam.views.strategicplan.StrategicPlanPanel;
import org.martus.swing.UiButton;
import org.martus.swing.UiCheckBox;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextArea;
import org.martus.swing.UiTextField;

public class NodePropertiesDialog extends JDialog implements CommandExecutedListener
{
	public NodePropertiesDialog(MainWindow parent, DiagramComponent diagramToUse, String title)
			throws HeadlessException
	{
		super(parent, title);
		
		mainWindow = parent;
		diagram = diagramToUse;
		
		getProject().addCommandExecutedListener(this);

		setResizable(true);
		setModal(false);
	}
	
	
	
	public void dispose()
	{
		getProject().removeCommandExecutedListener(this);
		super.dispose();
	}



	public void setCurrentNode(DiagramComponent diagram, DiagramNode node)
	{
		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());
		contents.removeAll();
		try
		{
			currentNode = node;
			contents.add(createLabelBar(currentNode), BorderLayout.BEFORE_FIRST_LINE);
			contents.add(createTabbedPane(currentNode), BorderLayout.CENTER);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error reading activity information");
		}
		pack();
	}
	
	public DiagramNode getCurrentNode()
	{
		return currentNode;
	}
	
	public int getNodeId()
	{
		return getCurrentNode().getId();
	}
	
	private Component createLabelBar(DiagramNode node)
	{
		createTextField(node.getLabel(), MAX_LABEL_LENGTH);
				
		DialogGridPanel grid = new DialogGridPanel();
		grid.add(new UiLabel(EAM.text("Label|Label")));
		grid.add(textField);

		if(node.isFactor())
		{
			grid.add(new UiLabel(EAM.text("Label|Type")));
			grid.add(createSwitchFactorTypeDropdown(node.getNodeType()));
		}
		
		grid.add(new UiLabel());
		return grid;
	}
	
	private Component createTabbedPane(DiagramNode node) throws Exception
	{
		JTabbedPane pane = new JTabbedPane();
		pane.add(createMainGrid(node), EAM.text("Tab|Details"));
		pane.add(createIndicatorsGrid(node), EAM.text("Tab|Indicators"));
		if(node.canHaveObjectives())
			pane.add(createObjectivesGrid(node), EAM.text("Tab|Objectives"));
		if(node.canHaveGoal())
			pane.add(createGoalsGrid(node), EAM.text("Tab|Goals"));
		if(node.isIntervention())
			pane.add(createTasksGrid(node), EAM.text("Tab|Actions"));
		return pane;
	}
	
	private Component createMainGrid(DiagramNode node)
	{
		DialogGridPanel grid = new DialogGridPanel();
		statusCheckBox = new UiCheckBox(EAM.text("Label|Draft"));
		
		if(node.isDirectThreat())
		{
			grid.add(new UiLabel(EAM.text("Label|IUCN-CMP Classification")));
			grid.add(createThreatClassificationDropdown());
		}
		
		if(node.isIntervention())
		{
			grid.add(new UiLabel(EAM.text("Label|Status")));
			statusCheckBox.setSelected(node.isStatusDraft());
			statusCheckBox.addItemListener(new StatusChangeHandler());
			grid.add(statusCheckBox);
			
			grid.add(new UiLabel(EAM.text("Label|IUCN-CMP Classification")));
			grid.add(createInterventionClassificationDropdown());
		}

		grid.add(new UiLabel(EAM.text("Label|Comments")));
		grid.add(createComment(node.getComment()));

		return grid;
	}
	
	class StatusChangeHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			try
			{
				getProject().executeCommand(buildStatusCommand());
				mainWindow.getDiagramComponent().updateVisibilityOfSingleNode(mainWindow, getCurrentNode());
			}
			catch (CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}
		
	}
	
	private Component createIndicatorsGrid(DiagramNode node)
	{
		DialogGridPanel grid = new DialogGridPanel();
		
		grid.add(new UiLabel(EAM.text("Label|Indicator")));
		grid.add(createIndicatorDropdown(getProject().getIndicatorPool(), node.getIndicatorId()));
		
		return grid;
	}

	private Component createObjectivesGrid(DiagramNode node)
	{
		DialogGridPanel grid = new DialogGridPanel();
		
		grid.add(new UiLabel(EAM.text("Label|Objective")));
		grid.add(createObjectiveDropdown());
		
		grid.add(new UiLabel(""));
		EAMAction action = mainWindow.getActions().get(ActionCreateObjective.class);
		UiButton buttonCreate = new UiButton(action);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(buttonCreate, BorderLayout.BEFORE_LINE_BEGINS);
		grid.add(panel);
		
		return grid;
	}

	private Component createGoalsGrid(DiagramNode node)
	{
		DialogGridPanel grid = new DialogGridPanel();
			
		grid.add(new UiLabel(EAM.text("Label|Goal")));
		grid.add(createTargetGoal(getProject().getGoalPool(), node.getGoals()));
		
		return grid;
	}

	private Component createTasksGrid(DiagramNode node) throws Exception
	{
		ConceptualModelIntervention intervention = (ConceptualModelIntervention)node.getUnderlyingObject();
		return StrategicPlanPanel.createForStrategy(mainWindow, intervention);
	}

	private Component createTextField(String initialText, int maxLength)
	{
		textField = new UiTextFieldWithLengthLimit(maxLength);
		textField.addFocusListener(new LabelFocusHandler());
		textField.requestFocus(true);

		textField.setText(initialText);
		textField.selectAll();
		
		JPanel component = new JPanel(new BorderLayout());
		component.add(textField, BorderLayout.LINE_START);
		return component;
	}
	
	class LabelFocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent event)
		{
		}

		public void focusLost(FocusEvent event)
		{
			String newText = getText();
			if(newText.equals(getCurrentNode().getLabel()))
				return;
			try
			{
				getProject().executeCommand(new CommandSetNodeName(getNodeId(), newText));
			}
			catch (CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}
	}
	
	public Component createSwitchFactorTypeDropdown(NodeType currentType)
	{
		dropdownFactorType = new UiComboBox();
		dropdownFactorType.setRenderer(new FactorTypeRenderer());
		dropdownFactorType.addItem(DiagramNode.TYPE_INDIRECT_FACTOR);
		dropdownFactorType.addItem(DiagramNode.TYPE_DIRECT_THREAT);
		dropdownFactorType.addActionListener(new FactorTypeChangeHandler());
		
		dropdownFactorType.setSelectedItem(currentType);
		
		JPanel component = new JPanel(new BorderLayout());
		component.add(dropdownFactorType, BorderLayout.LINE_START);
		return component;
	}
	
	class FactorTypeChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				getProject().executeCommand(new CommandSetFactorType(getNodeId(), getType()));
			}
			catch (CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}
		
	}
	
	public Component createObjectiveDropdown()
	{
		dropdownObjective = new UiComboBox();
		populateObjectives();	
		selectCurrentObjectives();
		dropdownObjective.addActionListener(new ObjectiveChangeHandler());

		JPanel component = new JPanel(new BorderLayout());
		component.add(dropdownObjective, BorderLayout.LINE_START);
		return component;
	}

	class ObjectiveChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				getProject().executeCommand(new CommandSetNodeObjectives(getNodeId(), getObjectives()));
			}
			catch (CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}
		
	}
	
	private void selectCurrentObjectives()
	{
		ObjectivePool allAvailableObjectives = getProject().getObjectivePool();
		ObjectiveIds currentObjectives = currentNode.getObjectives();
		Object nullObjective = dropdownObjective.getItemAt(0);
		Object selected = nullObjective;
		if(currentObjectives.size() > 0)
			selected = allAvailableObjectives.find(currentObjectives.getId(0));
		if(selected == null)
			selected = nullObjective;
		dropdownObjective.setSelectedItem(selected);
	}

	private void populateObjectives()
	{
		dropdownObjective.removeAllItems();
		Objective nullObjective = new Objective(IdAssigner.INVALID_ID);
		dropdownObjective.addItem(nullObjective);

		ObjectivePool allAvailableObjectives = getProject().getObjectivePool();
		int[] objectiveIds = allAvailableObjectives.getIds();
		for(int i = 0; i < objectiveIds.length; ++i)
		{
			dropdownObjective.addItem(allAvailableObjectives.find(objectiveIds[i]));
		}
	}
	
	public Component createTargetGoal(GoalPool allAvailableGoals, GoalIds currentGoals)
	{
		dropdownGoal = new UiComboBox();
		int[] goalIds = allAvailableGoals.getIds();
		for(int i = 0; i < goalIds.length; ++i)
		{
			dropdownGoal.addItem(allAvailableGoals.find(goalIds[i]));
		}
		
		if(currentGoals.size() == 0)
		{
			dropdownGoal.setSelectedItem(allAvailableGoals.find(IdAssigner.INVALID_ID));
		}
		else
		{
			int id = currentGoals.getId(0);
			Goal goal = allAvailableGoals.find(id);
			dropdownGoal.setSelectedItem(goal);
		}
		dropdownGoal.addActionListener(new GoalChangeHandler());
		
		JPanel component = new JPanel(new BorderLayout());
		component.add(dropdownGoal, BorderLayout.LINE_START);
		return component;
	}
	
	class GoalChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				getProject().executeCommand(new CommandSetTargetGoal(getNodeId(), getGoals()));
			}
			catch (CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}
		
	}
	
	public Component createIndicatorDropdown(IndicatorPool allAvailableIndicators, int indicatorId)
	{
		dropdownIndicator = new UiComboBox();
		Indicator nullIndicator = new Indicator(IdAssigner.INVALID_ID);
		dropdownIndicator.addItem(nullIndicator);
		
		int[] availableIds = allAvailableIndicators.getIds();
		for(int i = 0; i < availableIds.length; ++i)
		{
			dropdownIndicator.addItem(allAvailableIndicators.find(availableIds[i]));
		}
		
		Indicator selected = allAvailableIndicators.find(indicatorId);
		if(selected == null)
			selected = nullIndicator;
		dropdownIndicator.setSelectedItem(selected);
		dropdownIndicator.addActionListener(new IndicatorChangeHandler());

		JPanel component = new JPanel(new BorderLayout());
		component.add(dropdownIndicator, BorderLayout.LINE_START);
		return component;
	}
	
	class IndicatorChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				getProject().executeCommand(new CommandSetIndicator(getNodeId(), getIndicator().getId()));
			}
			catch (CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}
		
	}
	
	public JComponent createComment(String comment)
	{
		commentField = new UiTextArea(4, 25);
		commentField.setWrapStyleWord(true);
		commentField.setLineWrap(true);
		commentField.setText(comment);
		commentField.addFocusListener(new CommentFocusHandler());
		
		JScrollPane component = new JScrollPane(commentField);
		return component;
	}
	
	class CommentFocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent event)
		{
		}

		public void focusLost(FocusEvent event)
		{
			String newComment = getComment();
			if(newComment.equals(getCurrentNode().getComment()))
				return;
			try
			{
				getProject().executeCommand(new CommandSetNodeComment(getNodeId(), newComment));
			}
			catch (CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}
		
	}
	
	
	JComponent createThreatClassificationDropdown()
	{
		String[] choices = {
				"--Select a classification--",
				"1. Infrastructure Development",
				" 1.1 Housing & urban areas ",
				" 1.2 Commercial & industrial development", 
				" 1.3 Tourism & recreation ",
				"2. Agriculture & Aquaculture",
				" 2.1 Annual crops ",
				" 2.2 Perennial non-timber crops", 
				" Etc.",
				
		};
		
		return new UiComboBox(choices);
	}
	
	JComponent createInterventionClassificationDropdown()
	{
		String[] choices = {
				"--Select a classification--",
				"1. Land/Water Protection", 
				" 1.1 Publically owned site/area protection", 
				" 1.2 Privatel;y & community owned site/area protection", 
				" 1.3 Resource & habitat protection",
				"2.Land/Water Management ",
				" 2.1 Site/area management ",
				" 2.2 Invasive/problematic species control", 
				" Etc.",
				
		};
		
		return new UiComboBox(choices);
	}
	
	class FactorTypeRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
		{
			Component cell = super.getListCellRendererComponent(list, value, index, isSelected,	cellHasFocus);
			if(((NodeType)value).isDirectThreat())
				setIcon(new DirectThreatIcon());
			if(((NodeType)value).isIndirectFactor())
				setIcon(new IndirectFactorIcon());
			if(((NodeType)value).isStress())
				setIcon(new StressIcon());
			return cell;
		}
	}

	
	Command buildStatusCommand()
	{
		String newValue = ConceptualModelIntervention.STATUS_REAL;
		if(statusCheckBox.isSelected())
			newValue = ConceptualModelIntervention.STATUS_DRAFT;
		return new CommandSetObjectData(ObjectType.MODEL_NODE, currentNode.getId(), ConceptualModelIntervention.TAG_STATUS, newValue);
	}
	
	Project getProject()
	{
		return getDiagram().getProject();
	}
	
	DiagramComponent getDiagram()
	{
		return diagram;
	}

	public String getText()
	{
		return textField.getText();
	}
	
	public String getComment()
	{
		return commentField.getText();
	}
	
	public Indicator getIndicator()
	{
		return (Indicator)dropdownIndicator.getSelectedItem();
	}
	
	public NodeType getType()
	{
		return (NodeType)dropdownFactorType.getSelectedItem();
	}


	public ObjectiveIds getObjectives()
	{
		Objective oneObjective = (Objective)dropdownObjective.getSelectedItem();
		ObjectiveIds objectives = new ObjectiveIds();
		objectives.setObjectives(oneObjective);
		return objectives;
	}

	public GoalIds getGoals()
	{
		Goal oneGoal = (Goal)dropdownGoal.getSelectedItem();
		GoalIds goals = new GoalIds();
		goals.addId(oneGoal.getId());
		return goals;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		refreshObjectiveListIfNecessary(event);
		selectNewlyCreatedObjectiveIfNecessary(event);
	}
	
	public void commandUndone(CommandExecutedEvent event)
	{
		refreshObjectiveListIfNecessary(event);
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}

	void refreshObjectiveListIfNecessary(CommandExecutedEvent event)
	{
		if(dropdownObjective == null)
			return;
		Command rawCommand = event.getCommand();
		if(rawCommand.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
		{
			CommandCreateObject cmd = (CommandCreateObject)rawCommand;
			if(cmd.getObjectType() == ObjectType.OBJECTIVE)
			{
				populateObjectives();
			}
		}
		if(rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
		{
			CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
			if(cmd.getObjectType() == ObjectType.OBJECTIVE)
			{
				Object selected = dropdownObjective.getSelectedItem();
				populateObjectives();
				dropdownObjective.setSelectedItem(selected);
			}
		}

	}

	void selectNewlyCreatedObjectiveIfNecessary(CommandExecutedEvent event)
	{
		if(dropdownObjective == null)
			return;
		
		Command rawCommand = event.getCommand();
		if(rawCommand.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
		{
			CommandCreateObject cmd = (CommandCreateObject)rawCommand;
			if(cmd.getObjectType() == ObjectType.OBJECTIVE)
			{
				Objective newObjective = getProject().getObjectivePool().find(cmd.getCreatedId());
				dropdownObjective.setSelectedItem(newObjective);
			}
		}
	}

	
	
	
	static final int MAX_LABEL_LENGTH = 40;
	
	MainWindow mainWindow;
	DiagramComponent diagram;
	DiagramNode currentNode;
	UiTextField textField;
	UiTextArea commentField;
	UiComboBox dropdownFactorType;
	UiComboBox dropdownThreatPriority;
	UiComboBox dropdownIndicator;
	UiComboBox dropdownObjective;
	UiComboBox dropdownGoal;
	UiCheckBox statusCheckBox;
}
