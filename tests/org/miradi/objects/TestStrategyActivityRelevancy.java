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

package org.miradi.objects;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.CommandVector;

public class TestStrategyActivityRelevancy extends ObjectTestCase
{

    public TestStrategyActivityRelevancy(String name, ProjectForTesting projectToUse)
    {
        super(name);
        project = projectToUse;
    }

    public void verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(Factor strategyOrActivityRef, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        CommandVector commandsToMakeRelevant = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyOrActivityRef.getRef());
        assertEquals("Should contain exactly one command to make irrelevant factor with no override, relevant with override?", 1, commandsToMakeRelevant.size());
        CommandSetObjectData makeRlevantCommand = (CommandSetObjectData) commandsToMakeRelevant.get(0);
        RelevancyOverrideSet overrides = new RelevancyOverrideSet();
        overrides.add(new RelevancyOverride(strategyOrActivityRef.getRef(), true));
        CommandSetObjectData expectedMakeRelevantCommand = new CommandSetObjectData(parentObjectToUse.getRef(), parentObjectToUse.getRelevantStrategyActivitySetTag(), overrides.toString());
        assertEquals("Incorrect method to make irrelevant factor with no override, relevant with override?", makeRlevantCommand, expectedMakeRelevantCommand);
    }

    public void verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeIrrelevant(Factor strategyOrActivityRef, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        CommandVector commands = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyOrActivityRef.getRef());
        assertTrue("Should not make already irrelevant factor irrelevant again?", commands.isEmpty());
    }

    public void verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(Factor strategyOrActivityRef, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        CommandVector makeRelevantCommands = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyOrActivityRef.getRef());
        getProject().executeCommands(makeRelevantCommands);

        assertTrue("Irrelevant factor should be relevant?", parentObjectToUse.getRelevantStrategyAndActivityRefs().contains(strategyOrActivityRef.getRef()));

        CommandVector makeIrrelevantCommands = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyOrActivityRef.getRef());
        assertEquals("Should contain one command to make relevant factor irrelevant?", 1, makeIrrelevantCommands.size());
        CommandSetObjectData makeIrrelevantCommand = (CommandSetObjectData) makeIrrelevantCommands.get(0);
        RelevancyOverrideSet overrides = new RelevancyOverrideSet();
        CommandSetObjectData expectedMakeIrrelevantCommand = new CommandSetObjectData(parentObjectToUse.getRef(), parentObjectToUse.getRelevantStrategyActivitySetTag(), overrides.toString());
        assertEquals("incorrect command to ensure already relevant factor?", expectedMakeIrrelevantCommand, makeIrrelevantCommand);
    }

    public void verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(Factor strategyOrActivityRef, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        getProject().executeCommands(parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyOrActivityRef.getRef()));
        CommandVector commands = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyOrActivityRef.getRef());
        assertTrue("Should not make already relevant factor relevant again?", commands.isEmpty());
    }

    public void verifyStrategyDefaultRelevantNoOverridesMakeIrrelevant(Strategy strategyWithObjectiveToUse, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        CommandVector createCommandsToEnsureStrategyIsRelevant = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjectiveToUse.getRef());
        assertEquals("Should contain one command to make default strategy irrelevant?", 1, createCommandsToEnsureStrategyIsRelevant.size());
        CommandSetObjectData expectedMakeIrrelevantCommand = (CommandSetObjectData) createCommandsToEnsureStrategyIsRelevant.get(0);
        CommandSetObjectData makeIrrelevantCommand = createExpectedRelevancyOverrideCommand(strategyWithObjectiveToUse, parentObjectToUse, false);
        assertEquals("incorrect command to ensure already relevant strategy?", expectedMakeIrrelevantCommand, makeIrrelevantCommand);
    }

    public void verifyStrategyDefaultRelevantNoOverrideMakeRelevant(Strategy strategyWithObjectiveToUse, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        CommandVector commands = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyWithObjectiveToUse.getRef());
        assertTrue("Should not make already relevant strategy relevant again", commands.isEmpty());
    }

    public void verifyStrategyDefaultRelevantOverrideIrrelevantMakeRelevant(Strategy strategyWithObjectiveToUse, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        CommandVector commandsToMakeDefaultStrategyIrrelevant = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjectiveToUse.getRef());
        getProject().executeCommands(commandsToMakeDefaultStrategyIrrelevant);

        CommandVector commandsToMakeIrrelevantStrategyRelevant = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyWithObjectiveToUse.getRef());
        assertEquals("Should contain one command to make irrelevant strategy relevant by default?", 1, commandsToMakeIrrelevantStrategyRelevant.size());
        CommandSetObjectData makeRelevantCommand = (CommandSetObjectData) commandsToMakeIrrelevantStrategyRelevant.get(0);
        RelevancyOverrideSet overrides = new RelevancyOverrideSet();
        CommandSetObjectData expectedMakeRelevantCommand = new CommandSetObjectData(parentObjectToUse.getRef(), parentObjectToUse.getRelevantStrategyActivitySetTag(), overrides.toString());
        assertEquals("incorrect command to ensure already relevant strategy?", expectedMakeRelevantCommand, makeRelevantCommand);
    }

    public void verifyStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant(Strategy strategyWithObjectiveToUse, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        getProject().executeCommands(parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjectiveToUse.getRef()));

        assertFalse("default relevant strategy should be irrelevant override?", parentObjectToUse.getRelevantStrategyRefs().contains(strategyWithObjectiveToUse.getRef()));

        CommandVector commands = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjectiveToUse.getRef());
        assertTrue("Should not make already irrelevant strategy irrelevant again", commands.isEmpty());
    }

    public void verifyStrategyDefaultRelevantOverrideRelevantMakeIrrelevant(Strategy strategyWithObjectiveToUse, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        forceRelevancyForDefaultRelevantStrategyOrActivity(strategyWithObjectiveToUse, parentObjectToUse);
        CommandVector commandsToEnsureDefaultRelevantIsIrrelevant = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjectiveToUse.getRef());
        assertEquals("Should contain one command to remove incorrect relevant override for default relevant", 1, commandsToEnsureDefaultRelevantIsIrrelevant.size());

        CommandSetObjectData setCommand = (CommandSetObjectData) commandsToEnsureDefaultRelevantIsIrrelevant.get(0);
        CommandSetObjectData expectedCommand = createExpectedRelevancyOverrideCommand(strategyWithObjectiveToUse, parentObjectToUse, false);
        assertEquals("Command should only make strategy default relevant, irrelevant?", expectedCommand, setCommand);
    }

    public void verifyActivityDefaultRelevantNoOverridesMakeIrrelevant(Task activityToUse, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        CommandVector createCommandsToEnsureActivityIsRelevant = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(activityToUse.getRef());
        assertEquals("Should contain one command to make default activity irrelevant?", 1, createCommandsToEnsureActivityIsRelevant.size());
        CommandSetObjectData expectedMakeIrrelevantCommand = (CommandSetObjectData) createCommandsToEnsureActivityIsRelevant.get(0);
        CommandSetObjectData makeIrrelevantCommand = createExpectedRelevancyOverrideCommand(activityToUse, parentObjectToUse, false);
        assertEquals("incorrect command to ensure already relevant activity?", expectedMakeIrrelevantCommand, makeIrrelevantCommand);
    }

    public void verifyActivityDefaultRelevantNoOverrideMakeRelevant(Task activityToUse, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        CommandVector commands = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(activityToUse.getRef());
        assertTrue("Should not make already relevant activity relevant again", commands.isEmpty());
    }

    public void verifyActivityDefaultRelevantOverrideIrrelevantMakeRelevant(Task activityToUse, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        CommandVector commandsToMakeDefaultActivityIrrelevant = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(activityToUse.getRef());
        getProject().executeCommands(commandsToMakeDefaultActivityIrrelevant);

        CommandVector commandsToMakeIrrelevantActivityRelevant = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(activityToUse.getRef());
        assertEquals("Should contain one command to make irrelevant activity relevant by default?", 1, commandsToMakeIrrelevantActivityRelevant.size());
        CommandSetObjectData makeRelevantCommand = (CommandSetObjectData) commandsToMakeIrrelevantActivityRelevant.get(0);
        RelevancyOverrideSet overrides = new RelevancyOverrideSet();
        CommandSetObjectData expectedMakeRelevantCommand = new CommandSetObjectData(parentObjectToUse.getRef(), parentObjectToUse.getRelevantStrategyActivitySetTag(), overrides.toString());
        assertEquals("incorrect command to ensure already relevant activity?", expectedMakeRelevantCommand, makeRelevantCommand);
    }

    public void verifyActivityDefaultRelevantOverrideIrrelevantMakeIrrelevant(Task activityToUse, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        getProject().executeCommands(parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(activityToUse.getRef()));

        assertFalse("default relevant activity should be irrelevant override?", parentObjectToUse.getRelevantActivityRefs().contains(activityToUse.getRef()));

        CommandVector commands = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(activityToUse.getRef());
        assertTrue("Should not make already irrelevant activity irrelevant again", commands.isEmpty());
    }

    public void verifyActivityDefaultRelevantOverrideRelevantMakeIrrelevant(Task activityToUse, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        forceRelevancyForDefaultRelevantStrategyOrActivity(activityToUse, parentObjectToUse);
        CommandVector commandsToEnsureDefaultRelevantIsIrrelevant = parentObjectToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(activityToUse.getRef());
        assertEquals("Should contain one command to remove incorrect relevant override for default relevant", 1, commandsToEnsureDefaultRelevantIsIrrelevant.size());

        CommandSetObjectData setCommand = (CommandSetObjectData) commandsToEnsureDefaultRelevantIsIrrelevant.get(0);
        CommandSetObjectData expectedCommand = createExpectedRelevancyOverrideCommand(activityToUse, parentObjectToUse, false);
        assertEquals("Command should only make activity default relevant, irrelevant?", expectedCommand, setCommand);
    }

    private void forceRelevancyForDefaultRelevantStrategyOrActivity(Factor strategyOrActivityRef, StrategyActivityRelevancyInterface parentObjectToUse) throws Exception
    {
        forceOverride(strategyOrActivityRef, parentObjectToUse, true);
    }

    private void forceOverride(Factor strategyOrActivityRef, StrategyActivityRelevancyInterface parentObjectToUse, boolean isRelevant) throws Exception
    {
        RelevancyOverrideSet overrideSet = parentObjectToUse.getStrategyActivityRelevancyOverrideSet();
        overrideSet.add(new RelevancyOverride(strategyOrActivityRef.getRef(), isRelevant));
        CommandSetObjectData forceRelevancyOverrideOnDefaultRelevantFactor = new CommandSetObjectData(parentObjectToUse.getRef(), parentObjectToUse.getRelevantStrategyActivitySetTag(), overrideSet.toString());
        getProject().executeCommand(forceRelevancyOverrideOnDefaultRelevantFactor);
    }

    private CommandSetObjectData createExpectedRelevancyOverrideCommand(Factor strategyOrActivityRef, StrategyActivityRelevancyInterface parentObjectToUse, boolean isRelevant)
    {
        RelevancyOverrideSet expectedRelevancyOverride = new RelevancyOverrideSet();
        expectedRelevancyOverride.add(new RelevancyOverride(strategyOrActivityRef.getRef(), isRelevant));
        CommandSetObjectData expectedCommand = new CommandSetObjectData(parentObjectToUse.getRef(), parentObjectToUse.getRelevantStrategyActivitySetTag(), expectedRelevancyOverride.toString());
        return expectedCommand;
    }

    public ProjectForTesting getProject()
    {
        return project;
    }

    private ProjectForTesting project;
}
