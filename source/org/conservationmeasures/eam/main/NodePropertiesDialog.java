/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

import org.conservationmeasures.eam.annotations.Goal;
import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.annotations.GoalPool;
import org.conservationmeasures.eam.annotations.IndicatorId;
import org.conservationmeasures.eam.annotations.Objective;
import org.conservationmeasures.eam.annotations.ObjectiveIds;
import org.conservationmeasures.eam.annotations.ObjectivePool;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.IndirectFactorIcon;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;
import org.martus.swing.Utilities;

import com.jhlabs.awt.BasicGridLayout;

public class NodePropertiesDialog extends JDialog implements ActionListener
{
	public NodePropertiesDialog(Frame parent, Project projectToUse, String title, DiagramNode node)
			throws HeadlessException
	{
		super(parent, title);
		
		project = projectToUse;

		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());
		
		contents.add(createMainGrid(node), BorderLayout.CENTER);
		contents.add(createButtonBar(), BorderLayout.AFTER_LAST_LINE);

		pack();
		setResizable(true);
		setModal(true);
	}
	
	private Component createMainGrid(DiagramNode node)
	{
		JPanel grid = new JPanel();
		grid.setLayout(new BasicGridLayout(1, 2));
		
		grid.add(new UiLabel(EAM.text("Label|Label")));
		grid.add(createTextField(node.getText()));
		
		if(node.isFactor())
		{
			grid.add(new UiLabel(EAM.text("Label|Type")));
			grid.add(createSwitchFactorTypeDropdown(node.getType()));
		}
		
		grid.add(new UiLabel(EAM.text("Label|Indicator")));
		grid.add(createIndicator(node.getIndicatorId()));
		
		if(node.canHaveObjectives())
		{
			grid.add(new UiLabel(EAM.text("Label|Objective")));
			grid.add(createObjectiveDropdown(project.getObjectivePool(), node.getObjectives()));
		}
		
		if(node.canHaveGoal())
		{
			grid.add(new UiLabel(EAM.text("Label|Goal")));
			grid.add(createTargetGoal(project.getGoalPool(), node.getGoals()));
		}
		
		grid.add(new UiLabel(EAM.text("Label|Comments")));
		grid.add(createComment(node.getComment()));

		return grid;
	}

	private Component createTextField(String initialText)
	{
		textField = new UiTextField(25);
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
		dropdownFactorType.addItem(DiagramNode.TYPE_STRESS);
		
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
		commentField = new UiTextField(50);
		commentField.setText(comment);
		
		JPanel component = new JPanel(new BorderLayout());
		component.add(commentField, BorderLayout.LINE_START);
		return component;
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
		okButton = new UiButton(EAM.text("Button|OK"));
		okButton.addActionListener(this);
		getRootPane().setDefaultButton(okButton);
		cancelButton = new UiButton(EAM.text("Button|Cancel"));
		cancelButton.addActionListener(this);

		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), okButton, cancelButton};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}

	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == okButton)
			result = true;
		dispose();
	}
	
	public boolean getResult()
	{
		return result;
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
	
	Project project;
	boolean result;
	UiTextField textField;
	UiTextField commentField;
	UiComboBox dropdownFactorType;
	UiComboBox dropdownThreatPriority;
	UiComboBox dropdownIndicator;
	UiComboBox dropdownObjective;
	UiComboBox dropdownGoal;
	UiButton okButton;
	UiButton cancelButton;
}
