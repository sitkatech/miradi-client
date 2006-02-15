/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JDialog;
import javax.swing.JList;

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
import org.conservationmeasures.eam.icons.ThreatPriorityIcon;
import org.conservationmeasures.eam.objects.ThreatRatingValue;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

public class NodePropertiesDialog extends JDialog implements ActionListener
{
	public NodePropertiesDialog(Frame parent, Project project, String title, DiagramNode node)
			throws HeadlessException
	{
		super(parent, title);
		
		UiVBox bigBox = new UiVBox();
		bigBox.add(createTextField(node.getText()));
		if(node.isFactor())
			bigBox.add(createSwitchFactorTypeDropdown(node.getType()));
		bigBox.add(createIndicator(node.getIndicatorId()));
		if(node.canHaveObjectives())
			bigBox.add(createObjectiveDropdown(project.getObjectivePool(), node.getObjectives()));
		if(node.canHavePriority())
			bigBox.add(createThreatLevelDropdown(node.getThreatRating()));
		if(node.canHaveGoal())
			bigBox.add(createTargetGoal(project.getGoalPool(), node.getGoals()));
		bigBox.add(createButtonBar());

		Container contents = getContentPane();
		contents.add(bigBox);
		pack();
		setResizable(true);
		setModal(true);
	}

	private Component createTextField(String initialText)
	{
		UiLabel textLabel = new UiLabel(EAM.text("Label|Label"));
		textField = new UiTextField(initialText);
		textField.requestFocus(true);
		textField.selectAll();

		Box labelBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textLabel, new UiLabel(" "), textField, Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(labelBar, components);
		return labelBar;
	}
	
	private Component createThreatLevelDropdown(ThreatRatingValue currentPriority)
	{
		UiLabel textThreatLevel = new UiLabel(EAM.text("Label|Threat Level"));
		UiComboBox dropDown = createThreatDropDown();

		
		dropdownThreatPriority = dropDown;
		dropdownThreatPriority.setSelectedItem(currentPriority);

		Box threatLevelBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textThreatLevel, new UiLabel(" "), dropDown, Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(threatLevelBar, components);
		return threatLevelBar;
	}

	public static UiComboBox createThreatDropDown()
	{
		UiComboBox dropDown = new UiComboBox();
		dropDown.setRenderer(new ThreatRenderer());
		
		dropDown.addItem(ThreatRatingValue.createVeryHigh());
		dropDown.addItem(ThreatRatingValue.createHigh());
		dropDown.addItem(ThreatRatingValue.createMedium());
		dropDown.addItem(ThreatRatingValue.createLow());
		dropDown.addItem(ThreatRatingValue.createNone());
		return dropDown;
	}
	
	public Component createSwitchFactorTypeDropdown(NodeType currentType)
	{
		UiLabel textObjective = new UiLabel(EAM.text("Label|Type"));
		dropdownFactorType = new UiComboBox();
		dropdownFactorType.setRenderer(new FactorTypeRenderer());
		dropdownFactorType.addItem(DiagramNode.TYPE_INDIRECT_FACTOR);
		dropdownFactorType.addItem(DiagramNode.TYPE_DIRECT_THREAT);
		dropdownFactorType.addItem(DiagramNode.TYPE_STRESS);
		dropdownFactorType.setSelectedItem(currentType);
		
		Box ObjectiveBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textObjective, new UiLabel(" "), dropdownFactorType, Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(ObjectiveBar, components);
		return ObjectiveBar;
	}
	
	public Component createObjectiveDropdown(ObjectivePool allAvailableObjectives, ObjectiveIds currentObjectives)
	{
		UiLabel textObjective = new UiLabel(EAM.text("Label|Objective"));
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
		
		Box ObjectiveBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textObjective, new UiLabel(" "), dropdownObjective, Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(ObjectiveBar, components);
		return ObjectiveBar;
	}
	
	public Component createTargetGoal(GoalPool allAvailableGoals, GoalIds currentGoals)
	{
		UiLabel textGoal = new UiLabel(EAM.text("Label|Goal"));
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
		
		
		Box GoalBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textGoal, new UiLabel(" "), dropdownGoal, Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(GoalBar, components);
		return GoalBar;
	}
	
	public Component createIndicator(IndicatorId indicator)
	{
		UiLabel textIndicator = new UiLabel(EAM.text("Label|Indicator"));
		dropdownIndicator = new UiComboBox();
		dropdownIndicator.addItem(new IndicatorId());
		dropdownIndicator.addItem(new IndicatorId(1));
		dropdownIndicator.addItem(new IndicatorId(2));
		dropdownIndicator.addItem(new IndicatorId(3));

		dropdownIndicator.setSelectedItem(indicator);
		
		Box indicatorBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textIndicator, new UiLabel(" "), dropdownIndicator,Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(indicatorBar, components);
		return indicatorBar;
	}
	
	static class ThreatRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
		{
			Component cell = super.getListCellRendererComponent(list, value, index, isSelected,	cellHasFocus);
			setIcon(new ThreatPriorityIcon((ThreatRatingValue)value));
			return cell;
		}
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
	
	public ThreatRatingValue getPriority()
	{
		return (ThreatRatingValue)dropdownThreatPriority.getSelectedItem();
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
	
	boolean result;
	UiTextField textField;
	UiComboBox dropdownFactorType;
	UiComboBox dropdownThreatPriority;
	UiComboBox dropdownIndicator;
	UiComboBox dropdownObjective;
	UiComboBox dropdownGoal;
	UiButton okButton;
	UiButton cancelButton;
}
