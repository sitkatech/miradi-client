/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import org.miradi.diagram.ChainWalker;
import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.*;
import org.miradi.utils.CodeList;

public class NormalTreeRebuilder extends AbstractTreeRebuilder
{
	public NormalTreeRebuilder(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse)
	{
		super(projectToUse, rowColumnProviderToUse);
	}
	
	@Override
	public ORefList getChildRefs(ORef grandparentRef, ORef parentRef, DiagramObject diagram) throws Exception
	{
		final ORefList noChildren = new ORefList();
		if(ProjectMetadata.is(parentRef))
			return getChildrenOfProjectNode();

		if(DiagramObject.isDiagramObject(parentRef))
			return getChildrenOfDiagramNode(parentRef);

		if(AbstractTarget.isAbstractTarget(parentRef))
			return getChildrenOfAbstractTarget(parentRef, diagram);

		if(BiophysicalFactor.is(parentRef))
			return getChildrenOfBasicFactor(parentRef);

		if(BiophysicalResult.is(parentRef))
			return getChildrenOfBasicFactor(parentRef);

		if(Cause.is(parentRef))
			return getChildrenOfBasicFactor(parentRef);

		if(Strategy.is(parentRef))
			return getChildrenOfStrategy(grandparentRef, parentRef, diagram);

		if(ThreatReductionResult.is(parentRef))
			return getChildrenOfBasicFactor(parentRef);

		if(IntermediateResult.is(parentRef))
			return getChildrenOfBasicFactor(parentRef);

		if(Desire.isDesire(parentRef))
            return getChildrenOfDesire(parentRef, diagram);

		if(Indicator.is(parentRef))
			return getChildrenOfIndicator(parentRef, diagram);

		if(Method.is(parentRef))
			return noChildren;

		if(Task.is(parentRef))
			return getChildrenOfTask(parentRef);

		if(Output.is(parentRef))
			return noChildren;

		if(Measurement.is(parentRef))
			return noChildren;

		if(ResourceAssignment.is(parentRef))
			return noChildren;

		if(ExpenseAssignment.is(parentRef))
			return noChildren;

		if(SubTarget.is(parentRef))
			return noChildren;

		if (FutureStatus.is(parentRef))
			return noChildren;

		if (AnalyticalQuestion.is(parentRef))
			return getChildrenOfAnalyticalQuestion(parentRef);

		if (SubAssumption.is(parentRef))
			return noChildren;

		if(parentRef.isInvalid())
		{
			EAM.logDebug("NormalTreeRebuilder.getChildRefs called for INVALID, type=" + parentRef.getObjectType());
			return noChildren;
		}

		EAM.logDebug("Don't know how to get children of " + parentRef);
		return new ORefList();
	}

	private ORefList getChildrenOfProjectNode() throws Exception
	{
		ORefList childRefs = new ORefList();

		PlanningTreeRowColumnProvider rowColumnProvider = getRowColumnProvider();
		String diagramFilter = rowColumnProvider.getDiagramFilter();

		if (!diagramFilter.isEmpty())
		{
			ORef diagramFilterObjectRef = ORef.createFromString(diagramFilter);
			childRefs.add(diagramFilterObjectRef);
		}
		else
		{
			if(getRowColumnProvider().shouldIncludeConceptualModelPage())
			{
				ORefList conceptualModelRefs = getProject().getConceptualModelDiagramPool().getORefList();
				childRefs.addAll(conceptualModelRefs);
			}
			if(getRowColumnProvider().shouldIncludeResultsChain())
			{
				ORefList resultsChainRefs = getProject().getResultsChainDiagramPool().getORefList();
				childRefs.addAll(resultsChainRefs);
			}
		}

		if(shouldTargetsBeAtSameLevelAsDiagrams())
		{
			childRefs.addAll(getProject().getTargetPool().getRefList());
			if(getProject().getMetadata().isHumanWelfareTargetMode())
				childRefs.addAll(getProject().getHumanWelfareTargetPool().getRefList());
		}

		return childRefs;
	}

	private ORefList getChildrenOfDiagramNode(ORef diagramRef) throws Exception
	{
		ORefList childRefs = new ORefList();
		DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramRef); 
		ORefList diagramFactorRefs = diagramObject.getAllDiagramFactorRefs();
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor)getProject().findObject(diagramFactorRefs.get(i));
			Factor factor = diagramFactor.getWrappedFactor();
			if(shouldIncludeFactorWithinDiagram(factor))
			{
				childRefs.add(factor.getRef());
			}
		}
		
		childRefs.addAll(diagramObject.getAllObjectiveRefs());
		childRefs.addAll(diagramObject.getAllGoalRefs());
		
		return childRefs;
	}
	
	private ORefList getChildrenOfAbstractTarget(ORef targetRef, DiagramObject diagram) throws Exception
	{
		ORefList childRefs = new ORefList();
		AbstractTarget target = AbstractTarget.findTarget(getProject(), targetRef);
		childRefs.addAll(target.getSubTargetRefs());
		childRefs.addAll(target.getOwnedObjectRefs().getFilteredBy(GoalSchema.getObjectType()));
		childRefs.addAll(getIndicatorsForTarget(target));
		childRefs.addAll(getDirectlyLinkedNonDraftStrategies(target, diagram));
		
		return childRefs;
	}

	protected ORefList getIndicatorsForTarget(AbstractTarget target)
	{
		return new ORefList(IndicatorSchema.getObjectType(), target.getDirectOrIndirectIndicators());
	}

	private ORefList getDirectlyLinkedNonDraftStrategies(AbstractTarget target, DiagramObject diagram)
	{
		if(diagram == null)
			return new ORefList();
		
		ORefList strategyRefs = new ORefList();
		ChainWalker chain = diagram.getDiagramChainWalker();
		DiagramFactor targetDiagramFactor = diagram.getDiagramFactor(target.getRef());
		FactorSet factors = chain.buildDirectlyLinkedUpstreamChainAndGetFactors(targetDiagramFactor);
		for(Factor factor : factors)
		{
			if(factor.isStrategy() && !factor.isStatusDraft())
				strategyRefs.add(factor.getRef());
		}
		
		return strategyRefs;
	}
	
	private ORefList getChildrenOfBasicFactor(ORef parentRef)
	{
		ORefList childRefs = new ORefList();
		Factor factor = Factor.findFactor(getProject(), parentRef);
		childRefs.addAll(factor.getObjectiveRefs());
		childRefs.addAll(factor.getDirectOrIndirectIndicatorRefs());
		return childRefs;
	}

	private ORefList getChildrenOfStrategy(ORef grandparentRef, ORef parentRef, DiagramObject diagram) throws Exception
	{
		ORefList childRefs = new ORefList();

        CodeList visibleRowTypes = getRowColumnProvider().getRowCodesToShow();
        boolean includeStrategies = visibleRowTypes.contains(StrategySchema.OBJECT_NAME);

        if (!includeStrategies && Desire.isDesire(grandparentRef))
            return childRefs;

		Strategy strategy = Strategy.find(getProject(), parentRef);
		childRefs.addAll(getOutputs(strategy));
		childRefs.addAll(getActivities(strategy));

		if (doStrategiesContainObjectives())
			childRefs.addAll(getRelevantObjectivesAndGoalsOnDiagram(diagram, parentRef));

		childRefs.addAll(getRelevantIndicatorsForStrategy(strategy));
		return childRefs;
	}

	protected ORefList getRelevantIndicatorsForStrategy(Strategy strategy)
	{
		try
		{
			return strategy.getRelevantIndicatorRefList();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new ORefList();
		}
	}

	private ORefList getActivities(Strategy strategy) throws Exception
	{
		ORefList activityRefsToReturn = strategy.getActivityRefs();

		ORefList monitoringActivityRefs = strategy.getMonitoringActivityRefs();
		ORefList activityRefs = ORefList.subtract(strategy.getActivityRefs(), monitoringActivityRefs);

		if (!getRowColumnProvider().shouldIncludeActivities())
			activityRefsToReturn = ORefList.subtract(activityRefsToReturn, activityRefs);

		if (!getRowColumnProvider().shouldIncludeMonitoringActivities())
			activityRefsToReturn = ORefList.subtract(activityRefsToReturn, monitoringActivityRefs);

		return activityRefsToReturn;
	}

	private ORefList getOutputs(BaseObject factor) throws Exception
	{
		ORefList outputRefsToReturn = factor.getOutputRefs();

		return outputRefsToReturn;
	}

	private ORefList getChildrenOfDesire(ORef parentRef, DiagramObject diagram) throws Exception
	{
		ORefList childRefs = new ORefList();
		Desire desire = Desire.findDesire(getProject(), parentRef);

        ORefList relevantStrategyAndActivityRefs = getRelevantStrategyAndActivityRefsInDiagram(diagram, desire);

        if (doObjectivesContainStrategies())
        {
            childRefs.addAll(relevantStrategyAndActivityRefs);
        }
        else
        {
            childRefs.addAll(relevantStrategyAndActivityRefs.getFilteredBy(TaskSchema.getObjectType()));
        }

		childRefs.addAll(getRelevantIndicatorsInDiagram(diagram, desire));
		return childRefs;
	}

	protected ORefList getRelevantIndicatorsInDiagram(DiagramObject diagram, Desire desire) throws Exception
	{
		if (willThisTypeEndUpInTheTree(desire.getTypeName()))
			return keepObjectsThatAreInDiagram(diagram, desire.getRelevantIndicatorRefList());

		return new ORefList();
	}

	private ORefList getRelevantStrategiesInDiagram(DiagramObject diagram, Desire desire) throws Exception
    {
        return keepObjectsThatAreInDiagram(diagram, desire.getRelevantStrategyRefs());
    }

    private ORefList getRelevantStrategyAndActivityRefsInDiagram(DiagramObject diagram, Desire desire) throws Exception
	{
        ORefList strategyAndActivityRefs = new ORefList();
		strategyAndActivityRefs.addAll(getRelevantStrategiesInDiagram(diagram, desire));
		if (willThisTypeEndUpInTheTree(desire.getTypeName()))
		{
			strategyAndActivityRefs.addAll(desire.getRelevantActivityRefs());
		}
		return strategyAndActivityRefs;
	}

	protected boolean willThisTypeEndUpInTheTree(String rowTypeCode) throws Exception
	{
		CodeList visibleRowTypes = getRowColumnProvider().getRowCodesToShow();
		return visibleRowTypes.contains(rowTypeCode);
	}

	protected ORefList getChildrenOfIndicator(ORef parentRef, DiagramObject diagram) throws Exception
	{
		ORefList childRefs = new ORefList();
		Indicator indicator = Indicator.find(getProject(), parentRef);
		childRefs.addAll(indicator.getMethodRefs());
		childRefs.addAll(indicator.getFutureStatusRefs());
		if(getRowColumnProvider().getRowCodesToShow().contains(MeasurementSchema.OBJECT_NAME))
		{
			childRefs.addAll(getSortedByDateMeasurementRefs(indicator));
		}
		
		return childRefs;
	}

	private ORefList getChildrenOfTask(ORef parentTaskRef) throws Exception
	{
		ORefList childRefs = new ORefList();

		Task parentTask = Task.find(getProject(), parentTaskRef);

		childRefs.addAll(parentTask.getOutputRefs());

		String rowTypeCode = getRowColumnProvider().getRowTypeCodeForTask(parentTask);

		if(willThisTypeEndUpInTheTree(rowTypeCode))
			childRefs.addAll(parentTask.getChildTaskRefs());

		ORefList childTaskRefs = parentTask.getChildTaskRefs();
		for (int index = 0; index < childTaskRefs.size(); ++index)
		{
			Task task = Task.find(getProject(), childTaskRefs.get(index));
			childRefs.addAll(task.getOutputRefs());
		}

		return childRefs;
	}

	private ORefList getChildrenOfAnalyticalQuestion(ORef analyticalQuestionRef) throws Exception
	{
		ORefList childRefs = new ORefList();
		AnalyticalQuestion analyticalQuestion = AnalyticalQuestion.find(getProject(), analyticalQuestionRef);
		childRefs.addAll(analyticalQuestion.getSubAssumptionRefs());

		return childRefs;
	}

	@Override
	protected boolean isVisible(CodeList objectTypesToShow, AbstractPlanningTreeNode child) throws Exception
	{
		return getRowColumnProvider().shouldBeVisible(child);
	}

	private boolean doStrategiesContainObjectives() throws Exception
	{
		return !doObjectivesContainStrategies();
	}

	private ORefList getRelevantObjectivesAndGoalsOnDiagram(DiagramObject diagram, ORef strategyRef) throws Exception
	{
		ORefList relevant = new ORefList();
		relevant.addAll(findRelevantObjectives(getProject(), strategyRef));
		relevant.addAll(findRelevantGoals(getProject(), strategyRef));
		
		return keepObjectsThatAreInDiagram(diagram, relevant);
	}
	
	private ORefList keepObjectsThatAreInDiagram(DiagramObject diagram, final ORefList baseObjectRefs)
	{
		if (diagram == null)
			return baseObjectRefs;
		
		ORefList itemRefsInDiagram = new ORefList();
		for(ORef candidateRef : baseObjectRefs)
		{
			BaseObject baseObject = BaseObject.find(getProject(), candidateRef);
			Factor factorOwner = baseObject.getDirectOrIndirectOwningFactor();
			if (diagram.containsWrappedFactorRef(factorOwner.getRef()))
				itemRefsInDiagram.add(candidateRef);
		}
		
		return itemRefsInDiagram;
	}
	
	private boolean doObjectivesContainStrategies() throws Exception
	{
		return getRowColumnProvider().doObjectivesContainStrategies();
	}
	
	private boolean shouldIncludeFactorWithinDiagram(Factor factor) throws Exception
	{
		if (AbstractTarget.isAbstractTarget(factor) && !shouldTargetsBeAtSameLevelAsDiagrams())
			return true;

		if (factor.isStrategy() && !factor.isStatusDraft())
			return true;
		
		if (factor.isDirectThreat())
			return true;
		
		if (factor.isContributingFactor())
			return true;
		
		if (factor.isBiophysicalFactor())
			return true;

		if (factor.isBiophysicalResult())
			return true;

		if (factor.isThreatReductionResult())
			return true;
		
		if (factor.isIntermediateResult())
			return true;
		
		if (factor.isAnalyticalQuestion())
			return true;

		if (factor.isSubAssumption())
			return true;

		return false;
	}
}
