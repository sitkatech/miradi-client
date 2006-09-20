/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.conservationmeasures.eam.actions.jump.ActionJumpActivitiesAndActionPlan;
import org.conservationmeasures.eam.actions.jump.ActionJumpAdaptAndMonitorPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeData;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeInterventions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeProjectCapacity;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeResourcesFeasibilityAndRisk;
import org.conservationmeasures.eam.actions.jump.ActionJumpArticulateCoreAssumptions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessStakeholders;
import org.conservationmeasures.eam.actions.jump.ActionJumpCloseTheLoop;
import org.conservationmeasures.eam.actions.jump.ActionJumpCommunicateResults;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreate;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreateModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineAudiences;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDescribeTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDesignateLeader;
import org.conservationmeasures.eam.actions.jump.ActionJumpDetermineNeeds;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopActivities;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopBudgets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopCharter;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopTargetGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpDocument;
import org.conservationmeasures.eam.actions.jump.ActionJumpEstablishVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpGroundTruthRevise;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyIndirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpImplementPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanDataStorage;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpRefinePlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpResultsChains;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectAppropriateMethods;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectTeam;
import org.conservationmeasures.eam.actions.jump.ActionJumpShare;
import org.conservationmeasures.eam.actions.jump.ActionJumpShorttermPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpTeamRoles;
import org.conservationmeasures.eam.main.MainWindow;

public class Actions
{
	public Actions(MainWindow mainWindow)
	{
		actions = new HashMap();
		
		registerAction(new ActionAbout(mainWindow));
		registerAction(new ActionClose(mainWindow));
		registerAction(new ActionContextualHelp(mainWindow));
		registerAction(new ActionCopy(mainWindow));
		registerAction(new ActionCut(mainWindow));
		registerAction(new ActionDelete(mainWindow));
		registerAction(new ActionExit(mainWindow));
		registerAction(new ActionPrint(mainWindow));
		registerAction(new ActionInsertConnection(mainWindow));
		registerAction(new ActionInsertTarget(mainWindow));
		registerAction(new ActionInsertDraftIntervention(mainWindow));
		registerAction(new ActionInsertIntervention(mainWindow));
		registerAction(new ActionInsertDirectThreat(mainWindow));
		registerAction(new ActionInsertIndirectFactor(mainWindow));
		registerAction(new ActionNewProject(mainWindow));
		registerAction(new ActionProperties(mainWindow));
		registerAction(new ActionSaveImage(mainWindow));
		registerAction(new ActionPaste(mainWindow));
		registerAction(new ActionPasteWithoutLinks(mainWindow));
		registerAction(new ActionRedo(mainWindow));
		registerAction(new ActionSelectAll(mainWindow));
		registerAction(new ActionSelectChain(mainWindow));
		registerAction(new ActionUndo(mainWindow));
		registerAction(new ActionViewDiagram(mainWindow));
		registerAction(new ActionViewInterview(mainWindow));
		registerAction(new ActionViewThreatMatrix(mainWindow));
		registerAction(new ActionViewBudget(mainWindow));
		registerAction(new ActionViewTask(mainWindow));
		registerAction(new ActionViewMap(mainWindow));
		registerAction(new ActionViewStrategicPlan(mainWindow));
		registerAction(new ActionViewImages(mainWindow));
		registerAction(new ActionViewCalendar(mainWindow));
		registerAction(new ActionViewMonitoring(mainWindow));
		registerAction(new ActionConfigureLayers(mainWindow));
		registerAction(new ActionStrategyBrainstormMode(mainWindow));
		registerAction(new ActionNormalDiagramMode(mainWindow));
		registerAction(new ActionZoomIn(mainWindow));
		registerAction(new ActionZoomOut(mainWindow));
		registerAction(new ActionNudgeNodeUp(mainWindow));
		registerAction(new ActionNudgeNodeDown(mainWindow));
		registerAction(new ActionNudgeNodeLeft(mainWindow));
		registerAction(new ActionNudgeNodeRight(mainWindow));
		registerAction(new ActionInsertActivity(mainWindow));
		registerAction(new ActionModifyActivity(mainWindow));
		registerAction(new ActionDeleteActivity(mainWindow));
		registerAction(new ActionCreateResource(mainWindow));
		registerAction(new ActionModifyResource(mainWindow));
		registerAction(new ActionDeleteResource(mainWindow));
		registerAction(new ActionCreateIndicator(mainWindow));
		registerAction(new ActionModifyIndicator(mainWindow));
		registerAction(new ActionDeleteIndicator(mainWindow));
		registerAction(new ActionCreateObjective(mainWindow));
		registerAction(new ActionModifyObjective(mainWindow));
		registerAction(new ActionDeleteObjective(mainWindow));
		registerAction(new ActionPreferences(mainWindow));
		registerAction(new ActionTreeNodeUp(mainWindow));
		registerAction(new ActionTreeNodeDown(mainWindow));
		registerAction(new ActionJumpDesignateLeader(mainWindow));
		registerAction(new ActionJumpDevelopCharter(mainWindow));
		registerAction(new ActionJumpSelectTeam(mainWindow));
		registerAction(new ActionJumpDefineScope(mainWindow));
		registerAction(new ActionJumpEstablishVision(mainWindow));
		registerAction(new ActionJumpIdentifyTargets(mainWindow));
		registerAction(new ActionJumpDescribeTargets(mainWindow));
		registerAction(new ActionJumpIdentifyDirectThreats(mainWindow));
		registerAction(new ActionJumpRankDirectThreats(mainWindow));
		registerAction(new ActionJumpIdentifyIndirectThreats(mainWindow));
		registerAction(new ActionJumpAssessStakeholders(mainWindow));
		registerAction(new ActionJumpAnalyzeProjectCapacity(mainWindow));
		registerAction(new ActionJumpArticulateCoreAssumptions(mainWindow));
		registerAction(new ActionJumpCreateModel(mainWindow));
		registerAction(new ActionJumpGroundTruthRevise(mainWindow));
		registerAction(new ActionJumpDevelopTargetGoals(mainWindow));
		registerAction(new ActionJumpIdentifyStrategies(mainWindow));
		registerAction(new ActionJumpDevelopObjectives(mainWindow));
		registerAction(new ActionJumpRankDraftStrategies(mainWindow));
		registerAction(new ActionJumpResultsChains(mainWindow));
		registerAction(new ActionJumpActivitiesAndActionPlan(mainWindow));
		registerAction(new ActionJumpAnalyzeResourcesFeasibilityAndRisk(mainWindow));
		registerAction(new ActionJumpDetermineNeeds(mainWindow));
		registerAction(new ActionJumpDefineAudiences(mainWindow));
		registerAction(new ActionJumpDefineIndicators(mainWindow));
		registerAction(new ActionJumpSelectAppropriateMethods(mainWindow));
		registerAction(new ActionJumpPlanDataStorage(mainWindow));
		registerAction(new ActionJumpShorttermPlans(mainWindow));
		registerAction(new ActionJumpDevelopActivities(mainWindow));
		registerAction(new ActionJumpDefineTasks(mainWindow));
		registerAction(new ActionJumpDevelopBudgets(mainWindow));
		registerAction(new ActionJumpTeamRoles(mainWindow));
		registerAction(new ActionJumpImplementPlans(mainWindow));
		registerAction(new ActionJumpRefinePlans(mainWindow));
		registerAction(new ActionJumpAnalyzeData(mainWindow));
		registerAction(new ActionJumpAnalyzeInterventions(mainWindow));
		registerAction(new ActionJumpCommunicateResults(mainWindow));
		registerAction(new ActionJumpAdaptAndMonitorPlans(mainWindow));
		registerAction(new ActionJumpDocument(mainWindow));
		registerAction(new ActionJumpShare(mainWindow));
		registerAction(new ActionJumpCreate(mainWindow));
		registerAction(new ActionJumpCloseTheLoop(mainWindow));
	}
	
	public EAMAction get(Class c)
	{
		Object action = actions.get(c);
		if(action == null)
			throw new RuntimeException("Unknown action: " + c);
		
		return (EAMAction)action;
	}

	public void updateActionStates()
	{
		Collection actualActions = actions.values();
		Iterator iter = actualActions.iterator();
		while(iter.hasNext())
		{
			EAMAction action = (EAMAction)iter.next();
			action.setEnabled(action.shouldBeEnabled());
		}
	}
	
	void registerAction(EAMAction action)
	{
		actions.put(action.getClass(), action);
	}

	Map actions;
}
