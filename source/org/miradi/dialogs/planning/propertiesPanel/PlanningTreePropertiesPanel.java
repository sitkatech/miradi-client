/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.CardLayout;
import java.awt.Rectangle;

import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.diagram.StrategyPropertiesPanel;
import org.miradi.dialogs.goal.GoalPropertiesPanel;
import org.miradi.dialogs.objective.ObjectivePropertiesPanel;
import org.miradi.dialogs.planning.MeasurementPropertiesPanel;
import org.miradi.dialogs.viability.IndicatorPropertiesPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.ObjectPicker;

public class PlanningTreePropertiesPanel extends ObjectDataInputPanel
{
	public PlanningTreePropertiesPanel(MainWindow mainWindowToUse, ORef orefToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), orefToUse);
		mainWindow = mainWindowToUse;
		objectPicker = objectPickerToUse;
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
		measurementPropertiesPanel.dispose();
	}
	
	private void createPropertiesPanels() throws Exception
	{
		goalPropertiesPanel = new GoalPropertiesPanel(getProject());
		objectivePropertiesPanel = new ObjectivePropertiesPanel(getProject(), getMainWindow().getActions(), objectPicker);
		indicatorPropertiesPanel = new IndicatorPropertiesPanel(getMainWindow());
		strategyPropertiesPanel = new StrategyPropertiesPanel(getMainWindow());
		taskPropertiesInputPanel = new PlanningViewTaskPropertiesPanel(getMainWindow(), objectPicker);
		blankPropertiesPanel = new BlankPropertiesPanel();
		measurementPropertiesPanel = new MeasurementPropertiesPanel(getProject());
		
		add(goalPropertiesPanel);
		add(objectivePropertiesPanel);
		add(indicatorPropertiesPanel);
		add(strategyPropertiesPanel);
		add(taskPropertiesInputPanel);
		add(blankPropertiesPanel);
		add(measurementPropertiesPanel);
	}
	
	private void add(DisposablePanelWithDescription panelToAdd)
	{
		add(panelToAdd, panelToAdd.getPanelDescription());
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		cardLayout.show(this, findPanel(orefsToUse).getPanelDescription());
	
		taskPropertiesInputPanel.setObjectRefs(orefsToUse);
		goalPropertiesPanel.setObjectRefs(orefsToUse);
		objectivePropertiesPanel.setObjectRefs(orefsToUse);
		indicatorPropertiesPanel.setObjectRefs(orefsToUse);
		strategyPropertiesPanel.setObjectRefs(orefsToUse);
		measurementPropertiesPanel.setObjectRefs(orefsToUse);
		
		scrollRectToVisible(new Rectangle(0,0,0,0));
		
		// NOTE: The following are an attempt to fix a reported problem 
		// where the screen was not fully repainted when switching objects
		// This code is duplicated in TargetViabilityTreePropertiesPanel.java
		// and DirectIndicatorPropertiesPanel.java
		validate();
		repaint();
	}
	
	private DisposablePanelWithDescription findPanel(ORef[] orefsToUse)
	{
		if(orefsToUse.length == 0)
			return blankPropertiesPanel;
		
		ORef firstRef = orefsToUse[0];
		int objectType = firstRef.getObjectType();
		if (Goal.getObjectType() == objectType)
			return goalPropertiesPanel;
		
		if (Objective.getObjectType() == objectType)
			return objectivePropertiesPanel;
		
		if (Indicator.getObjectType() == objectType)
			return indicatorPropertiesPanel;
		
		if (Strategy.getObjectType() == objectType)
			return strategyPropertiesPanel;
		
		if (Task.getObjectType() == objectType)
			return taskPropertiesInputPanel;
		
		if (Measurement.getObjectType() == objectType)
			return measurementPropertiesPanel;
		
		return blankPropertiesPanel;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.isSetDataCommand())
			updateTable();
	}
	
	public void updateTable()
	{
		if (taskPropertiesInputPanel == null)
			return;
		
		taskPropertiesInputPanel.dataWasChanged();
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
		
	public static final String PANEL_DESCRIPTION = "Planning Properties Panel";
	
	private MainWindow mainWindow;
	private ObjectPicker objectPicker;
	private CardLayout cardLayout;
	
	private GoalPropertiesPanel goalPropertiesPanel;
	private ObjectivePropertiesPanel objectivePropertiesPanel;
	private IndicatorPropertiesPanel indicatorPropertiesPanel;
	private StrategyPropertiesPanel strategyPropertiesPanel;
	private PlanningViewTaskPropertiesPanel taskPropertiesInputPanel;
	private BlankPropertiesPanel blankPropertiesPanel;
	private MeasurementPropertiesPanel measurementPropertiesPanel;
}
