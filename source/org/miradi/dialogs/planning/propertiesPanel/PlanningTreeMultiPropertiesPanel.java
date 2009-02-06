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
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.CardLayout;
import java.awt.Rectangle;

import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.base.OverlaidObjectDataInputPanel;
import org.miradi.dialogs.diagram.ConceptualModelPropertiesPanel;
import org.miradi.dialogs.diagram.ResultsChainPropertiesPanel;
import org.miradi.dialogs.diagram.StrategyPropertiesPanel;
import org.miradi.dialogs.goal.GoalPropertiesPanel;
import org.miradi.dialogs.objective.ObjectivePropertiesPanel;
import org.miradi.dialogs.planning.MeasurementPropertiesPanel;
import org.miradi.dialogs.viability.IndicatorPropertiesPanel;
import org.miradi.dialogs.viability.NonDiagramKeaModeTargetPropertiesPanel;
import org.miradi.dialogs.viability.NonDiagramSimpleModeTargetPropertiesPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.views.umbrella.ObjectPicker;

public class PlanningTreeMultiPropertiesPanel extends OverlaidObjectDataInputPanel
{
	public PlanningTreeMultiPropertiesPanel(MainWindow mainWindowToUse, ORef orefToUse, ObjectPicker objectPickerToUse) throws Exception
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
		targetSimpleModePropertiesPanel.dispose();
		targetKeaModePropertiesPanel.dispose();
		threatPropertiesPanel.dispose();
		contributingFactorPropertiesPanel.dispose();
		intermediateResultPropertiesPanel.dispose();
		threatReductionResultPropertiesPanel.dispose();
		resultsChainPropertiesPanel.dispose();
		conceptualModelPropertiesPanel.dispose();
		blankPropertiesPanel.dispose();
	}
	
	private void createPropertiesPanels() throws Exception
	{
		goalPropertiesPanel = new GoalPropertiesPanel(getProject(), getMainWindow().getActions(), objectPicker);
		objectivePropertiesPanel = new ObjectivePropertiesPanel(getProject(), getMainWindow().getActions(), objectPicker);
		indicatorPropertiesPanel = new IndicatorPropertiesPanel(getMainWindow());
		strategyPropertiesPanel = new StrategyPropertiesPanel(getMainWindow());
		taskPropertiesInputPanel = new PlanningViewTaskPropertiesPanel(getMainWindow(), objectPicker);
		measurementPropertiesPanel = new MeasurementPropertiesPanel(getProject());
		targetSimpleModePropertiesPanel = new NonDiagramSimpleModeTargetPropertiesPanel(getProject());
		targetKeaModePropertiesPanel = new NonDiagramKeaModeTargetPropertiesPanel(getProject());
		threatPropertiesPanel = new PlanningViewDirectThreatPropertiesPanel(getProject());
		contributingFactorPropertiesPanel = new PlanningViewContributingFactorPropertiesPanel(getProject());
		intermediateResultPropertiesPanel = new PlanningViewIntermediateResultPropertiesPanel(getProject());
		threatReductionResultPropertiesPanel = new PlanningViewThreatReductionResultPropertiesPanel(getProject());
		resultsChainPropertiesPanel = new ResultsChainPropertiesPanel(getProject(), ORef.INVALID);
		conceptualModelPropertiesPanel = new ConceptualModelPropertiesPanel(getProject(), ORef.INVALID);
		
		blankPropertiesPanel = new BlankPropertiesPanel(getProject());
		
		add(goalPropertiesPanel);
		add(objectivePropertiesPanel);
		add(indicatorPropertiesPanel);
		add(strategyPropertiesPanel);
		add(taskPropertiesInputPanel);
		add(measurementPropertiesPanel);
		add(targetSimpleModePropertiesPanel);
		add(targetKeaModePropertiesPanel);
		add(threatPropertiesPanel);
		add(contributingFactorPropertiesPanel);
		add(intermediateResultPropertiesPanel);
		add(threatReductionResultPropertiesPanel);
		add(resultsChainPropertiesPanel);
		add(conceptualModelPropertiesPanel);
		add(blankPropertiesPanel);
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
		currentCard = findPanel(orefsToUse);
		cardLayout.show(this, currentCard.getPanelDescription());
	
		taskPropertiesInputPanel.setObjectRefs(orefsToUse);
		goalPropertiesPanel.setObjectRefs(orefsToUse);
		objectivePropertiesPanel.setObjectRefs(orefsToUse);
		indicatorPropertiesPanel.setObjectRefs(orefsToUse);
		strategyPropertiesPanel.setObjectRefs(orefsToUse);
		measurementPropertiesPanel.setObjectRefs(orefsToUse);
		targetSimpleModePropertiesPanel.setObjectRefs(orefsToUse);
		targetKeaModePropertiesPanel.setObjectRefs(orefsToUse);
		threatPropertiesPanel.setObjectRefs(orefsToUse);
		contributingFactorPropertiesPanel.setObjectRefs(orefsToUse);
		intermediateResultPropertiesPanel.setObjectRefs(orefsToUse);
		threatReductionResultPropertiesPanel.setObjectRefs(orefsToUse);
		resultsChainPropertiesPanel.setObjectRefs(orefsToUse);
		conceptualModelPropertiesPanel.setObjectRefs(orefsToUse);
		
		scrollRectToVisible(new Rectangle(0,0,0,0));
		
		// NOTE: The following are an attempt to fix a reported problem 
		// where the screen was not fully repainted when switching objects
		// This code is duplicated in TargetViabilityTreePropertiesPanel.java
		// and DirectIndicatorPropertiesPanel.java
		validate();
		repaint();
	}
	
	private ObjectDataInputPanel findPanel(ORef[] orefsToUse)
	{
		if(orefsToUse.length == 0)
			return blankPropertiesPanel;
		
		ORef firstRef = orefsToUse[DEEPEST_INDEX];
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
		
		if (Target.getObjectType() == objectType)
			return getTargetPropertiesPanel(firstRef);
		
		if (Cause.getObjectType() == objectType)
			return getCausePropertiesPanel(firstRef);
		
		if (IntermediateResult.getObjectType() == objectType)
			return intermediateResultPropertiesPanel;
		
		if (ThreatReductionResult.getObjectType() == objectType)
			return threatReductionResultPropertiesPanel;
	
		if (ResultsChainDiagram.is(objectType))
			return resultsChainPropertiesPanel;
		
		if (ConceptualModelDiagram.is(objectType))
			return conceptualModelPropertiesPanel;
		
		return blankPropertiesPanel;
	}

	private ObjectDataInputPanel getTargetPropertiesPanel(ORef targetRef)
	{
		Target target = Target.find(getProject(), targetRef);
		if (target.isViabilityModeTNC())
			return targetKeaModePropertiesPanel;
		
		return targetSimpleModePropertiesPanel;
	}
	
	private MinimalFactorPropertiesPanel getCausePropertiesPanel(ORef causeRef)
	{
		Cause cause = Cause.find(getProject(), causeRef);
		if (cause.isDirectThreat())
			return threatPropertiesPanel;
		
		return contributingFactorPropertiesPanel;
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		try
		{
			if (event.isSetDataCommand())
				updateTable();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("An unexpected error has occurred"));
		}		
			
		if (event.isSetDataCommandWithThisTypeAndTag(Target.getObjectType(), Target.TAG_VIABILITY_MODE))
			reloadSelectedRefs();
	}

	public void updateTable() throws Exception
	{
		if (taskPropertiesInputPanel == null)
			return;
		
		taskPropertiesInputPanel.dataWasChanged();
	}
	
	@Override
	public void setFocusOnFirstField()
	{
		currentCard.setFocusOnFirstField();
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
		
	public static final String PANEL_DESCRIPTION = "Planning Properties Panel";
	
	private static final int DEEPEST_INDEX = 0; 
	
	private MainWindow mainWindow;
	private ObjectPicker objectPicker;
	private CardLayout cardLayout;
	private ObjectDataInputPanel currentCard;
	
	private GoalPropertiesPanel goalPropertiesPanel;
	private ObjectivePropertiesPanel objectivePropertiesPanel;
	private IndicatorPropertiesPanel indicatorPropertiesPanel;
	private StrategyPropertiesPanel strategyPropertiesPanel;
	private PlanningViewTaskPropertiesPanel taskPropertiesInputPanel;
	private NonDiagramSimpleModeTargetPropertiesPanel targetSimpleModePropertiesPanel;
	private NonDiagramKeaModeTargetPropertiesPanel targetKeaModePropertiesPanel;
	private PlanningViewIntermediateResultPropertiesPanel intermediateResultPropertiesPanel;
	private PlanningViewThreatReductionResultPropertiesPanel threatReductionResultPropertiesPanel;
	private PlanningViewDirectThreatPropertiesPanel threatPropertiesPanel;
	private PlanningViewContributingFactorPropertiesPanel contributingFactorPropertiesPanel;
	private MeasurementPropertiesPanel measurementPropertiesPanel;
	private ResultsChainPropertiesPanel resultsChainPropertiesPanel;
	private ConceptualModelPropertiesPanel conceptualModelPropertiesPanel;
	private BlankPropertiesPanel blankPropertiesPanel;
}
