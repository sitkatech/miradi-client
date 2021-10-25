/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
package org.miradi.views.planning.doers;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.activity.MovableActivityPoolTablePanel;
import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.dialogs.diagram.MoveActivitySelectionDialog;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.*;
import org.miradi.project.FactorCommandHelper;
import org.miradi.project.FactorDeleteHelper;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.CommandVector;

import java.awt.*;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Vector;


public class TreeNodeMoveActivityDoer extends AbstractTreeNodeTaskDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;

		BaseObject selected = getSingleSelectedObject();
		if (selected != null)
			if (!Strategy.is(selected))
				return false;

		return activitiesCanBeMoved();
	}

	@Override
	protected void doIt() throws Exception
	{
		if (!isAvailable())
			return;

		moveSelectedActivity();
	}

	protected boolean childWouldBeVisible() throws Exception
	{
		return childWouldBeVisible(TaskSchema.ACTIVITY_NAME) || childWouldBeVisible(TaskSchema.MONITORING_ACTIVITY_NAME);

	}

	private void moveSelectedActivity() throws CommandFailedException
	{
		ORef parentOfSelectedRef = getParentRefOfSelectedObject();
		if (parentOfSelectedRef.isInvalid())
			return;

		ObjectPoolTablePanel movableObjectPoolTablePanel = new MovableActivityPoolTablePanel(getMainWindow(), parentOfSelectedRef);

		getProject().executeBeginTransaction();

		try
		{
			MoveActivitySelectionDialog listDialog = new MoveActivitySelectionDialog(getMainWindow(), getShareDialogTitle(), movableObjectPoolTablePanel);
			listDialog.setVisible(true);

			BaseObject activityToMove = listDialog.getSelectedObject();
			if (activityToMove == null)
				return;

			BaseObject originalStrategy = activityToMove.getOwner();
			BaseObject newStrategy = getProject().findObject(parentOfSelectedRef);

			// commands to move activity from old to new strategy
			CommandVector commandsToMoveActivity  = new CommandVector();

			CommandSetObjectData appendActivityToNewStrategyCommand = CommandSetObjectData.createAppendIdCommand(newStrategy, getParentTaskIdsTag(), activityToMove.getId());
			commandsToMoveActivity.add(appendActivityToNewStrategyCommand);
			CommandSetObjectData removeActivityFromCurrentStrategyCommand = CommandSetObjectData.createRemoveIdCommand(originalStrategy, getParentTaskIdsTag(), activityToMove.getId());
			commandsToMoveActivity.add(removeActivityFromCurrentStrategyCommand);

			getProject().executeCommands(commandsToMoveActivity);

			// commands to fix activity relevancy on old and new strategy's goals / objectives / indicators
			CommandVector commandsToFixRelevancy  = new CommandVector();

			ORefList originalStrategyOwnedObjectRefs = originalStrategy.getOwnedObjectRefs();
			for (ORef ownedObjectRef : originalStrategyOwnedObjectRefs)
			{
				BaseObject ownedObject = BaseObject.find(getProject(), ownedObjectRef);
				if (ownedObject instanceof StrategyActivityRelevancyInterface)
				{
					StrategyActivityRelevancyInterface activityRelevancyInterface = (StrategyActivityRelevancyInterface) ownedObject;
					CommandVector commandToRemoveRelevancy = activityRelevancyInterface.createCommandsToEnsureStrategyOrActivityIsIrrelevant(activityToMove.getRef());
					commandsToFixRelevancy.addAll(commandToRemoveRelevancy);
				}
			}

			ORefList newStrategyOwnedObjectRefs = newStrategy.getOwnedObjectRefs();
			for (ORef ownedObjectRef : newStrategyOwnedObjectRefs)
			{
				BaseObject ownedObject = BaseObject.find(getProject(), ownedObjectRef);
				if (ownedObject instanceof StrategyActivityRelevancyInterface)
				{
					StrategyActivityRelevancyInterface activityRelevancyInterface = (StrategyActivityRelevancyInterface) ownedObject;
					CommandVector commandToAddRelevancy = activityRelevancyInterface.createCommandsToEnsureStrategyOrActivityIsRelevant(activityToMove.getRef());
					commandsToFixRelevancy.addAll(commandToAddRelevancy);
				}
			}

			// reposition activity if displayed in rc diagrams
			repositionActivityInResultsChainDiagrams(activityToMove, originalStrategy, newStrategy);

			getProject().executeCommands(commandsToFixRelevancy);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeEndTransaction();
			movableObjectPoolTablePanel.dispose();
		}
	}

	private void repositionActivityInResultsChainDiagrams(BaseObject activityToMove, BaseObject originalStrategy, BaseObject newStrategy) throws Exception
	{
		ORefList activityDiagramFactorRefs = activityToMove.findObjectsThatReferToUs(DiagramFactorSchema.getObjectType());
		if (activityDiagramFactorRefs.size() > 0)
        {
            ORefList originalStrategyResultsChainRefs = ((Strategy) originalStrategy).getResultsChains();
            ORefList newStrategyResultsChainRefs = ((Strategy) newStrategy).getResultsChains();

			// remove activity refs from rcs where only old strategy is displayed
			removeActivityFromDiagrams(originalStrategyResultsChainRefs, newStrategyResultsChainRefs, activityDiagramFactorRefs);

			// reposition activity or add it to rcs where new strategy is displayed
			addActivityToDiagrams(activityToMove, newStrategyResultsChainRefs, newStrategy, activityDiagramFactorRefs);
        }
	}

	private DiagramFactor getStrategyDiagramFactor(ResultsChainDiagram resultsChainDiagram, BaseObject strategy)
	{
		HashSet<DiagramFactor> strategyDiagramFactors = resultsChainDiagram.getFactorsFromDiagram(StrategySchema.getObjectType());
		for (DiagramFactor strategyDiagramFactor : strategyDiagramFactors)
		{
			if (strategyDiagramFactor.getWrappedFactor().getRef().equals(strategy.getRef()))
				return strategyDiagramFactor;
		}

		return null;
	}

	private void removeActivityFromDiagrams(ORefList originalStrategyResultsChainRefs, ORefList newStrategyResultsChainRefs, ORefList activityDiagramFactorRefs) throws Exception
	{
		for (ORef resultsChainRef : originalStrategyResultsChainRefs)
		{
			if (!newStrategyResultsChainRefs.contains(resultsChainRef))
			{
				ResultsChainDiagram resultsChainDiagram = ResultsChainDiagram.find(getProject(), resultsChainRef);
				FactorDeleteHelper helper = FactorDeleteHelper.createFactorDeleteHelperForNonSelectedFactors(resultsChainDiagram);

				for (ORef activityDiagramFactorRef : activityDiagramFactorRefs)
				{
					DiagramFactorId activityDiagramFactorId = (DiagramFactorId) activityDiagramFactorRef.getObjectId();
					if (resultsChainDiagram.containsDiagramFactor(activityDiagramFactorId))
						removeActivityFromDiagram(helper, resultsChainDiagram, activityDiagramFactorRef);
				}
			}
		}

	}

	private void removeActivityFromDiagram(FactorDeleteHelper helper, ResultsChainDiagram resultsChainDiagram, ORef activityDiagramFactorRef) throws Exception
	{
		CommandVector commandsToHide = new CommandVector();
		DiagramFactor diagramFactorToDelete = DiagramFactor.find(getProject(), activityDiagramFactorRef);

		commandsToHide.add(helper.buildCommandToRemoveNodeFromDiagram(resultsChainDiagram, diagramFactorToDelete.getDiagramFactorId()));
		commandsToHide.addAll(helper.buildCommandsToDeleteDiagramFactor(diagramFactorToDelete));
		getProject().executeCommands(commandsToHide);
	}

	private void addActivityToDiagrams(BaseObject activityToMove, ORefList newStrategyResultsChainRefs, BaseObject newStrategy, ORefList activityDiagramFactorRefs) throws Exception
	{
		for (ORef resultsChainRef : newStrategyResultsChainRefs)
		{
			ResultsChainDiagram resultsChainDiagram = ResultsChainDiagram.find(getProject(), resultsChainRef);

			DiagramFactor strategyDiagramFactor = getStrategyDiagramFactor(resultsChainDiagram, newStrategy);
			if (strategyDiagramFactor != null)
			{
				FactorCommandHelper commandHelper = new FactorCommandHelper(getProject(), resultsChainDiagram);

				for (ORef activityDiagramFactorRef : activityDiagramFactorRefs)
				{
					DiagramFactorId activityDiagramFactorId = (DiagramFactorId) activityDiagramFactorRef.getObjectId();
					if (resultsChainDiagram.containsDiagramFactor(activityDiagramFactorId))
					{
						// reposition activity
						setActivityDiagramLocation(commandHelper, newStrategy, strategyDiagramFactor, activityToMove.getRef(), activityDiagramFactorId);
					}
					else
					{
						// add activity to diagram
						DiagramFactorId newActivityDiagramFactorId = (DiagramFactorId) commandHelper.createDiagramFactor(resultsChainDiagram, activityToMove.getRef()).getCreatedId();
						setActivityDiagramLocation(commandHelper, newStrategy, strategyDiagramFactor, activityToMove.getRef(), newActivityDiagramFactorId);
						commandHelper.setDiagramFactorSize(newActivityDiagramFactorId, DiagramFactor.DEFAULT_ACTIVITY_SIZE);
					}
				}
			}
		}
	}

	private void setActivityDiagramLocation(FactorCommandHelper helper, BaseObject newStrategy, DiagramFactor strategyDiagramFactor, ORef activityRef, DiagramFactorId activityDiagramFactorId) throws Exception
	{
		ORefList activityRefs = ((Strategy) newStrategy).getActivityRefs();
		int offset = activityRefs.find(activityRef);
		Point location = new Point(strategyDiagramFactor.getLocation());
		location.x += (offset * getProject().getGridSize());
		location.y += strategyDiagramFactor.getSize().height;
		helper.setDiagramFactorLocation(activityDiagramFactorId, location);
	}

	private ORef getParentRefOfSelectedObject()
	{
		BaseObject foundObject = getSingleSelected(getParentType());
		if (foundObject == null)
			return ORef.INVALID;

		return foundObject.getRef();
	}

	@Override
	protected boolean canBeParentOfTask(BaseObject selectedObject) throws Exception
	{
		if(super.canBeParentOfTask(selectedObject))
			return true;

		if (Task.is(selectedObject))
			return hasAdjacentParentInSelectionHierarchy((Task) selectedObject);

		return false;
	}

	private boolean activitiesCanBeMoved()
	{
		ORef parentRef = getParentRefOfSelectedObject();
		if (parentRef.getObjectType() != getParentType())
			return false;

		Vector<BaseObject> activities = getTasksForParent(parentRef);
		Vector<Task> tasksNotAlreadyInParent = getProject().getTaskPool().getTasks(getTaskTypeName());
		tasksNotAlreadyInParent.removeAll(activities);

		return tasksNotAlreadyInParent.size() > 0;
	}

	private Vector<BaseObject> getTasksForParent(ORef parentRef)
	{
		try
		{
			BaseObject parent = BaseObject.find(getProject(), parentRef);
			String taskIdsAsString = parent.getData(getParentTaskIdsTag());
			IdList taskIds = new IdList(TaskSchema.getObjectType(), taskIdsAsString);
			ORefList taskRefs = new ORefList(TaskSchema.getObjectType(), taskIds);

			return getProject().getObjectManager().findObjectsAsVector(taskRefs);
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			return new Vector<BaseObject>();
		}
	}

	private String getTaskTypeName()
	{
		return TaskSchema.ACTIVITY_NAME;
	}

	private String getParentTaskIdsTag()
	{
		return Strategy.TAG_ACTIVITY_IDS;
	}

	private String getShareDialogTitle()
	{
		return EAM.text("Move Activity to this Strategy");
	}

	@Override
	protected int getParentType()
	{
		return StrategySchema.getObjectType();
	}
}

