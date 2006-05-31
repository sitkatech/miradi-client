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

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.conservationmeasures.eam.annotations.Goal;
import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.annotations.GoalPool;
import org.conservationmeasures.eam.annotations.IndicatorId;
import org.conservationmeasures.eam.annotations.Objective;
import org.conservationmeasures.eam.annotations.ObjectiveIds;
import org.conservationmeasures.eam.annotations.ObjectivePool;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ObjectType;
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
import org.martus.swing.Utilities;

public class NodePropertiesDialog extends JDialog implements ActionListener
{
	public NodePropertiesDialog(MainWindow parent, DiagramComponent diagramToUse, String title)
			throws HeadlessException
	{
		super(parent, title);
		
		mainWindow = parent;
		diagram = diagramToUse;

		setResizable(true);
		setModal(false);
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
			contents.add(createButtonBar(), BorderLayout.AFTER_LAST_LINE);
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
			grid.add(statusCheckBox);
			
			grid.add(new UiLabel(EAM.text("Label|IUCN-CMP Classification")));
			grid.add(createInterventionClassificationDropdown());
		}

		grid.add(new UiLabel(EAM.text("Label|Comments")));
		grid.add(createComment(node.getComment()));

		return grid;
	}
	
	private Component createIndicatorsGrid(DiagramNode node)
	{
		DialogGridPanel grid = new DialogGridPanel();
		
		grid.add(new UiLabel(EAM.text("Label|Indicator")));
		grid.add(createIndicator(node.getIndicatorId()));
		
		return grid;
	}

	private Component createObjectivesGrid(DiagramNode node)
	{
		DialogGridPanel grid = new DialogGridPanel();
		
		grid.add(new UiLabel(EAM.text("Label|Objective")));
		grid.add(createObjectiveDropdown(getProject().getObjectivePool(), node.getObjectives()));
		
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
		textField.requestFocus(true);
		textField.selectAll();

		textField.setText(initialText);

		JPanel component = new JPanel(new BorderLayout());
		component.add(textField, BorderLayout.LINE_START);
		return component;
	}
	
	public Component createSwitchFactorTypeDropdown(NodeType currentType)
	{
		dropdownFactorType = new UiComboBox();
		dropdownFactorType.setRenderer(new FactorTypeRenderer());
		dropdownFactorType.addItem(DiagramNode.TYPE_INDIRECT_FACTOR);
		dropdownFactorType.addItem(DiagramNode.TYPE_DIRECT_THREAT);
		
		dropdownFactorType.setSelectedItem(currentType);
		
		JPanel component = new JPanel(new BorderLayout());
		component.add(dropdownFactorType, BorderLayout.LINE_START);
		return component;
	}
	
	public Component createObjectiveDropdown(ObjectivePool allAvailableObjectives, ObjectiveIds currentObjectives)
	{
		dropdownObjective = new UiComboBox();
		int[] objectiveIds = allAvailableObjectives.getIds();
		for(int i = 0; i < objectiveIds.length; ++i)
		{
			dropdownObjective.addItem(allAvailableObjectives.find(objectiveIds[i]));
		}
		
		if(currentObjectives.size() == 0)
		{
			dropdownObjective.setSelectedItem(allAvailableObjectives.find(IdAssigner.INVALID_ID));
		}
		else
		{
			int id = currentObjectives.getId(0);
			Objective objective = allAvailableObjectives.find(id);
			dropdownObjective.setSelectedItem(objective);
		}	

		JPanel component = new JPanel(new BorderLayout());
		component.add(dropdownObjective, BorderLayout.LINE_START);
		return component;
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
		
		JPanel component = new JPanel(new BorderLayout());
		component.add(dropdownGoal, BorderLayout.LINE_START);
		return component;
	}
	
	public Component createIndicator(IndicatorId indicator)
	{
		dropdownIndicator = new UiComboBox();
		dropdownIndicator.addItem(new IndicatorId());
		dropdownIndicator.addItem(new IndicatorId(1));
		dropdownIndicator.addItem(new IndicatorId(2));
		dropdownIndicator.addItem(new IndicatorId(3));

		dropdownIndicator.setSelectedItem(indicator);
		
		JPanel component = new JPanel(new BorderLayout());
		component.add(dropdownIndicator, BorderLayout.LINE_START);
		return component;
	}
	
	public JComponent createComment(String comment)
	{
		commentField = new UiTextArea(4, 25);
		commentField.setWrapStyleWord(true);
		commentField.setLineWrap(true);
		commentField.setText(comment);
		
		JScrollPane component = new JScrollPane(commentField);
		return component;
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

	private Box createButtonBar()
	{
		okButton = new UiButton(EAM.text("Button|Apply"));
		okButton.addActionListener(this);
		getRootPane().setDefaultButton(okButton);
		cancelButton = new UiButton(EAM.text("Button|Revert"));
		cancelButton.addActionListener(this);

		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), okButton, cancelButton};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			if(event.getSource() == okButton)
			{
				saveChanges();
			}
			else
			{
				revertChanges();
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("An unexpected error has occured: " + e.getMessage());
		}
	}
	
	void saveChanges() throws CommandFailedException
	{
		int id = currentNode.getId();
		getProject().executeCommand(new CommandBeginTransaction());
		getProject().executeCommand(new CommandSetNodeName(id, getText()));
		getProject().executeCommand(new CommandSetNodeComment(id, getComment()));
		getProject().executeCommand(new CommandSetIndicator(id, getIndicator()));
		if(currentNode.canHaveObjectives())
			getProject().executeCommand(new CommandSetNodeObjectives(id, getObjectives()));
		if(currentNode.canHaveGoal())
			getProject().executeCommand(new CommandSetTargetGoal(id, getGoals()));
		if(currentNode.isFactor())
			getProject().executeCommand(new CommandSetFactorType(id, getType()));
		if(currentNode.isIntervention())
			getProject().executeCommand(buildStatusCommand());

		getProject().executeCommand(new CommandEndTransaction());
	}
	
	Command buildStatusCommand()
	{
		String newValue = ConceptualModelIntervention.STATUS_REAL;
		if(statusCheckBox.isSelected())
			newValue = ConceptualModelIntervention.STATUS_DRAFT;
		return new CommandSetObjectData(ObjectType.MODEL_NODE, currentNode.getId(), ConceptualModelIntervention.TAG_STATUS, newValue);
	}
	
	void revertChanges()
	{
		setCurrentNode(diagram, currentNode);
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
	
	public IndicatorId getIndicator()
	{
		return (IndicatorId)dropdownIndicator.getSelectedItem();
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
	UiButton okButton;
	UiButton cancelButton;
}
