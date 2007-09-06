/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.CardLayout;

import org.conservationmeasures.eam.dialogs.GoalPropertiesPanel;
import org.conservationmeasures.eam.dialogs.IndicatorPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.ObjectivePropertiesPanel;
import org.conservationmeasures.eam.dialogs.StrategyPropertiesPanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreePropertiesPanel extends ObjectDataInputPanel
{
	public PlanningTreePropertiesPanel(Project projectToUse, ORef orefToUse) throws Exception
	{
		super(projectToUse, orefToUse);
		project = projectToUse;
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		createPropertiesPanels();
	}
	
	public void dispose()
	{
		super.dispose();
		goalPropertiesPanel.dispose();
		objectivePropertiesPanel.dispose();
		indicatorPropertiesPanel.dispose();
		strategyPropertiesPanel.dispose();
		taskPropertiesInputPanel.dispose();
	}
	
	private void createPropertiesPanels() throws Exception
	{
		goalPropertiesPanel = new GoalPropertiesPanel(project);
		objectivePropertiesPanel = new ObjectivePropertiesPanel(project);
		indicatorPropertiesPanel = new IndicatorPropertiesPanel(project);
		strategyPropertiesPanel = new StrategyPropertiesPanel(project);
		taskPropertiesInputPanel = new PlanningViewTaskPropertiesPanel(project);
		
		add(goalPropertiesPanel, goalPropertiesPanel.getPanelDescription());
		add(objectivePropertiesPanel, objectivePropertiesPanel.getPanelDescription());
		add(indicatorPropertiesPanel, indicatorPropertiesPanel.getPanelDescription());
		add(strategyPropertiesPanel, strategyPropertiesPanel.getPanelDescription());
		add(taskPropertiesInputPanel, taskPropertiesInputPanel.getPanelDescription());
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		cardLayout.show(this, getDescription(orefsToUse));
		taskPropertiesInputPanel.setObjectRefs(orefsToUse);
		goalPropertiesPanel.setObjectRefs(orefsToUse);
		objectivePropertiesPanel.setObjectRefs(orefsToUse);
		indicatorPropertiesPanel.setObjectRefs(orefsToUse);
		strategyPropertiesPanel.setObjectRefs(orefsToUse);
	}
	
	private String getDescription(ORef[] orefsToUse)
	{
		ORef firstRef = orefsToUse[0];
		int objectType = firstRef.getObjectType();
		if (Goal.getObjectType() == objectType)
			return goalPropertiesPanel.getPanelDescription();
		
		if (Objective.getObjectType() == objectType)
			return objectivePropertiesPanel.getPanelDescription();
		
		if (Indicator.getObjectType() == objectType)
			return indicatorPropertiesPanel.getPanelDescription();
		
		if (Strategy.getObjectType() == objectType)
			return strategyPropertiesPanel.getPanelDescription();
		
		if (Task.getObjectType() == objectType)
			return taskPropertiesInputPanel.getPanelDescription();
		
		return "";
	}

	public static final String PANEL_DESCRIPTION = "Planning Properties Panel";
	
	Project project;
	CardLayout cardLayout;
	
	GoalPropertiesPanel goalPropertiesPanel;
	ObjectivePropertiesPanel objectivePropertiesPanel;
	IndicatorPropertiesPanel indicatorPropertiesPanel;
	StrategyPropertiesPanel strategyPropertiesPanel;
	PlanningViewTaskPropertiesPanel taskPropertiesInputPanel;
}
