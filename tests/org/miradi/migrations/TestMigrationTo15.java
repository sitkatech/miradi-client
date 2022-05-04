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

package org.miradi.migrations;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.migrations.forward.MigrationTo15;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.*;
import org.miradi.project.ProjectForTesting;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.utils.CommandVector;

public class TestMigrationTo15 extends AbstractTestMigration
{
	public TestMigrationTo15(String name)
	{
		super(name);
	}

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        DiagramFactor strategyWithObjectiveDiagramFactor = getProject().createAndAddFactorToDiagram(StrategySchema.getObjectType());
        strategy = (Strategy) strategyWithObjectiveDiagramFactor.getWrappedFactor();
        activity = getProject().createTask(strategy);
        objective = createObjective(strategy);

        DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(TargetSchema.getObjectType());
        targetWithGoal = (Target) targetDiagramFactor.getWrappedFactor();
        goal = getProject().createGoal(targetWithGoal);
        getProject().createDiagramLinkAndAddToDiagram(strategyWithObjectiveDiagramFactor, targetDiagramFactor);
    }

	public void testDefaultRelevancyForActivitiesStrategyRelevantActivityRelevant() throws Exception
	{
        verifyDefaultRelevancyForActivities(true, true);
    }
    
	public void testDefaultRelevancyForActivitiesStrategyRelevantActivityIrrelevant() throws Exception
	{
        verifyDefaultRelevancyForActivities(true, false);
    }
    
	public void testDefaultRelevancyForActivitiesStrategyIrrelevantActivityRelevant() throws Exception
	{
        verifyDefaultRelevancyForActivities(false, true);
    }
    
	public void testDefaultRelevancyForActivitiesStrategyIrrelevantActivityIrrelevant() throws Exception
	{
        verifyDefaultRelevancyForActivities(false, false);
    }
    
    private void verifyDefaultRelevancyForActivities(final boolean strategyRelevant, final boolean activityRelevant) throws Exception
    {
        ORefList expectedRelevantStrategyRefs = strategyRelevant ? new ORefList(strategy.getRef()) : new ORefList();
        ORefList expectedRelevantActivityRefs = activityRelevant ? new ORefList(activity.getRef()) : new ORefList();

        setRelevancy(strategy, goal, strategyRelevant);
        setRelevancy(strategy, objective, strategyRelevant);
        setRelevancy(activity, goal, activityRelevant);
        setRelevancy(activity, objective, activityRelevant);

        assertEquals(expectedRelevantStrategyRefs, goal.getRelevantStrategyRefs());
        assertEquals(expectedRelevantStrategyRefs, objective.getRelevantStrategyRefs());
        assertEquals(expectedRelevantActivityRefs, goal.getRelevantActivityRefs());
        assertEquals(expectedRelevantActivityRefs, objective.getRelevantActivityRefs());

        String goalRelevancyOverrideBeforeForceReset = goal.getStrategyActivityRelevancyOverrideSet().toString();
        String objectiveRelevancyOverrideBeforeForceReset = objective.getStrategyActivityRelevancyOverrideSet().toString();

        // force reset of relevancy json string based on old rules
        forceRelevancyOverride(objective, "");
        if (!strategyRelevant)
            forceRelevancyOverride(strategy, objective, false);

        if (activityRelevant)
            forceRelevancyOverride(activity, objective, true);

        forceRelevancyOverride(goal, "");
        if (!strategyRelevant)
            forceRelevancyOverride(strategy, goal, false);

        if (activityRelevant)
            forceRelevancyOverride(activity, goal, true);

        String objectiveRelevancyOverrideAfterForceReset = objective.getStrategyActivityRelevancyOverrideSet().toString();
        assertNotEquals(objectiveRelevancyOverrideBeforeForceReset, objectiveRelevancyOverrideAfterForceReset);

        ProjectForTesting migratedProject = verifyFullCircleMigrations(createVersionRange());
        Goal migratedGoal = Goal.find(migratedProject, goal.getRef());
        Objective migratedObjective = Objective.find(migratedProject, objective.getRef());

        String goalRelevancyOverrideAfterMigration = migratedGoal.getStrategyActivityRelevancyOverrideSet().toString();
        assertEquals(goalRelevancyOverrideBeforeForceReset, goalRelevancyOverrideAfterMigration);
        assertEquals(expectedRelevantStrategyRefs, migratedGoal.getRelevantStrategyRefs());
        assertEquals(expectedRelevantActivityRefs, migratedGoal.getRelevantActivityRefs());

        String objectiveRelevancyOverrideAfterMigration = migratedObjective.getStrategyActivityRelevancyOverrideSet().toString();
        assertEquals(objectiveRelevancyOverrideBeforeForceReset, objectiveRelevancyOverrideAfterMigration);
        assertEquals(expectedRelevantStrategyRefs, migratedObjective.getRelevantStrategyRefs());
        assertEquals(expectedRelevantActivityRefs, migratedObjective.getRelevantActivityRefs());
    }

    private void setRelevancy(Factor strategyOrActivity, Desire objectiveOrGoal, boolean isRelevant) throws Exception {
        CommandVector commandsToEnsureStrategyOrActivityRelevancy =
                isRelevant ?
                objectiveOrGoal.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyOrActivity.getRef()) :
                objectiveOrGoal.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyOrActivity.getRef());
        getProject().executeCommands(commandsToEnsureStrategyOrActivityRelevancy);
    }

    private void forceRelevancyOverride(Factor strategyOrActivityRef, Desire objectiveOrGoalToUse, boolean isRelevant) throws Exception
    {
        RelevancyOverrideSet overrideSet = objectiveOrGoalToUse.getStrategyActivityRelevancyOverrideSet();
        overrideSet.add(new RelevancyOverride(strategyOrActivityRef.getRef(), isRelevant));
        forceRelevancyOverride(objectiveOrGoalToUse, overrideSet.toString());
    }

    private void forceRelevancyOverride(Desire objectiveOrGoalToUse, String relevancyJsonString) throws CommandFailedException
    {
        CommandSetObjectData forceRelevancyOverrideOnObjectiveOrGoal = new CommandSetObjectData(objectiveOrGoalToUse.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancyJsonString);
        getProject().executeCommand(forceRelevancyOverrideOnObjectiveOrGoal);
    }

    private VersionRange createVersionRange() throws Exception
    {
        return new VersionRange(MigrationTo15.VERSION_FROM, MigrationTo15.VERSION_TO);
    }

	@Override
	protected int getFromVersion()
	{
		return MigrationTo15.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo15.VERSION_TO;
	}

    protected Desire createObjective(Factor owner) throws Exception
    {
        return getProject().createObjective(owner);
    }

    private Target targetWithGoal;
    private Strategy strategy;
    private Desire objective;
    private Desire goal;
    private Task activity;
}
