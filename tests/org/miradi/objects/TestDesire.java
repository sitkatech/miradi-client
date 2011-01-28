/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.objects;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.utils.CommandVector;

public class TestDesire extends ObjectTestCase
{
	public TestDesire(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		DiagramFactor strategyDiagramFactor = getProject().createAndAddFactorToDiagram(Strategy.getObjectType());
		strategy = (Strategy) strategyDiagramFactor.getWrappedFactor();
		
		DiagramFactor strategyWithObjectiveDiagramFactor = getProject().createAndAddFactorToDiagram(Strategy.getObjectType());
		strategyWithObjective = (Strategy) strategyWithObjectiveDiagramFactor.getWrappedFactor();
		activity = getProject().createTask(strategyWithObjective);
		objective = createObjective(strategyWithObjective);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyWithObjectiveDiagramFactor, strategyDiagramFactor);

		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		targetWithGoal = (Target) targetDiagramFactor.getWrappedFactor();
		goal = getProject().createGoal(targetWithGoal);
		getProject().createDiagramLinkAndAddToDiagram(strategyWithObjectiveDiagramFactor, targetDiagramFactor);
	}
	
	public void testStrategyDefaultRelevantNoOverridesMakeIrrelevantToGoal() throws Exception
	{
		verifyStrategyDefaultRelevantNoOverridesMakeIrrelevant(goal);
	}

	public void testStrategyDefaultRelevantNoOverridesMakeIrrelevant() throws Exception
	{
		verifyStrategyDefaultRelevantNoOverridesMakeIrrelevant(objective);
	}
	
	public void testStrategyDefaultRelevantNoOverrideMakeRelevant() throws Exception
	{
		CommandVector commands = objective.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyWithObjective.getRef());
		assertTrue("Should not make already relevant strategy relevant again", commands.isEmpty());
	}
	
	public void testStrategyDefaultRelevantNoOverrideMakeRelevantToGoal() throws Exception
	{
		CommandVector commands = goal.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyWithObjective.getRef());
		assertTrue("Should not make already relevant strategy relevant again", commands.isEmpty());
	}
	
	public void testStrategyDefaultRelevantOverrideIrrelevantMakeRelevant() throws Exception
	{
		CommandVector commandsToMakeDefaultStrategyIrrelevant = objective.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjective.getRef());
		getProject().executeCommandsAsTransaction(commandsToMakeDefaultStrategyIrrelevant);
		
		CommandVector commandsToMakeIrrelevantStrategyRelevant = objective.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyWithObjective.getRef());
		assertEquals("Should contain one command to make irrelevant strategy relevant by default?", 1, commandsToMakeIrrelevantStrategyRelevant.size());
		CommandSetObjectData makeRelevantCommand = (CommandSetObjectData) commandsToMakeIrrelevantStrategyRelevant.get(0);
		RelevancyOverrideSet overrides = new RelevancyOverrideSet();
		CommandSetObjectData expectedMakeRelevantCommand = new CommandSetObjectData(objective.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrides.toString());
		assertEquals("incorrect command to ensure already relevant strategy?", expectedMakeRelevantCommand, makeRelevantCommand);
	}
	
	public void testStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant() throws Exception
	{
		getProject().executeCommandsAsTransaction(objective.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjective.getRef()));
		
		assertFalse("default relevant stratgey should be irrelevant override?", objective.getRelevantStrategyRefs().contains(strategyWithObjective.getRef()));
		
		CommandVector commands = objective.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjective.getRef());
		assertTrue("Should not make already irrelevant strategy irrelevant again", commands.isEmpty());
	}
	
	public void testStrategyDefaultRelevantOverrideRelevantMakeIrrelevant() throws Exception
	{
		forceRelevancyForDefaultRelevantStrategy(strategyWithObjective, objective);
		CommandVector commandsToEnsureDefaultRelevantIsIrrelevant = objective.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjective.getRef());
		assertEquals("Should contain one command to remove incorrect relevant override for default relevant", 1, commandsToEnsureDefaultRelevantIsIrrelevant.size());
		
		CommandSetObjectData setCommand = (CommandSetObjectData) commandsToEnsureDefaultRelevantIsIrrelevant.get(0);
		CommandSetObjectData expectedCommand = createExpectedRelevancyOverrideCommand(strategyWithObjective, objective, false);
		assertEquals("Command should only make strategy default relevant, irrelevant?", expectedCommand, setCommand);
	}

	public void testStrategyDefaultIrrelevantNoOverrideMakeRelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(strategy, objective);
	}
	
	public void testActivityDefaultIrrelevantNoOverrideMakeRelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(activity, objective);
	}
	
	public void testStrategyDefaultIrrelevantNoOverrideMakeIrrelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeIrrelevant(strategy, objective);
	}
	
	public void testStrategyDefaultIrrelevantRelevantOverrideMakeIrrelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(strategy,	objective);
	}
	
	public void testActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(activity,	objective);
	}
	
	public void testActivityDefaultIrrelevantOverrideRelevantMakeRelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(activity, objective);
	}

	public void testStrategyDefaultIrrelevantOverrideRelevantMakeRelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(strategy, objective);
	}
	
	public void testDefaultIrrelevantOverrideIrrelevantMakeRelevant() throws Exception
	{
		forceIrrelevancyForDefaultIrrelevantStrategyOrActivity(activity, objective);

		CommandVector commandsToEnsureDefaultRelevantIsIrrelevant = objective.createCommandsToEnsureStrategyOrActivityIsRelevant(activity.getRef());
		assertEquals("Should contain one command to remove incorrect irrelevant override for default irrelevant", 1, commandsToEnsureDefaultRelevantIsIrrelevant.size());
		
		CommandSetObjectData setCommand = (CommandSetObjectData) commandsToEnsureDefaultRelevantIsIrrelevant.get(0);
		CommandSetObjectData expectedCommand = createExpectedRelevancyOverrideCommand(activity, objective, true);
		assertEquals("Command should only make activity default irrelevant, relevant?", expectedCommand, setCommand);
	}
	
	private void verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(Factor strategyOrActivityRef, Desire desireToUse) throws Exception
	{
		CommandVector commandsToMakeRelevant = desireToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyOrActivityRef.getRef());
		assertEquals("Should contain exactly one command to make irrelevant factor with no override, relevant with override?", 1, commandsToMakeRelevant.size());
		CommandSetObjectData makeRlevantCommand = (CommandSetObjectData) commandsToMakeRelevant.get(0);
		RelevancyOverrideSet overrides = new RelevancyOverrideSet();
		overrides.add(new RelevancyOverride(strategyOrActivityRef.getRef(), true));
		CommandSetObjectData expectedMakeRelevantCommand = new CommandSetObjectData(desireToUse.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrides.toString());
		assertEquals("Incorrect method to make irrelevant factor with no override, relevant with override?", makeRlevantCommand, expectedMakeRelevantCommand);
	}

	private void verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeIrrelevant(Factor strategyOrActivityRef, Desire desireToUse) throws Exception
	{
		CommandVector commands = desireToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyOrActivityRef.getRef());
		assertTrue("Should not make already irrelevant factor irrelevant again?", commands.isEmpty());
	}
	
	private void verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(Factor strategyOrActivityRef, Desire desireToUse) throws Exception
	{
		CommandVector makeRelevantCommands = desireToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyOrActivityRef.getRef());
		getProject().executeCommandsAsTransaction(makeRelevantCommands);
		
		assertTrue("Irrelevant factor should be relevant?", desireToUse.getRelevantStrategyAndActivityRefs().contains(strategyOrActivityRef.getRef()));
		
		CommandVector makeIrrelevantCommands = desireToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyOrActivityRef.getRef());
		assertEquals("Should contain one command to make relevant factor irrelevant?", 1, makeIrrelevantCommands.size());
		CommandSetObjectData makeIrrelevantCommand = (CommandSetObjectData) makeIrrelevantCommands.get(0);
		RelevancyOverrideSet overrides = new RelevancyOverrideSet();
		CommandSetObjectData expectedMakeIrrelevantCommand = new CommandSetObjectData(desireToUse.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrides.toString());
		assertEquals("incorrect command to ensure already relevant factor?", expectedMakeIrrelevantCommand, makeIrrelevantCommand);
	}
	
	private void verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(Factor strategyOrActivityRef, Desire desireToUse) throws Exception
	{
		getProject().executeCommandsAsTransaction(desireToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyOrActivityRef.getRef()));
		CommandVector commands = desireToUse.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyOrActivityRef.getRef());
		assertTrue("Should not make already relevant factor relevant again?", commands.isEmpty());
	}
	
	private void verifyStrategyDefaultRelevantNoOverridesMakeIrrelevant(Desire desireToUse) throws Exception
	{
		CommandVector createCommandsToEnsureStrategyIsRelevant = desireToUse.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjective.getRef());
		assertEquals("Should contain one command to make default strategy irrelevant?", 1, createCommandsToEnsureStrategyIsRelevant.size());
		CommandSetObjectData expectedMakeIrrelevantCommand = (CommandSetObjectData) createCommandsToEnsureStrategyIsRelevant.get(0);
		CommandSetObjectData makeIrrelevantCommand = createExpectedRelevancyOverrideCommand(strategyWithObjective, desireToUse, false);
		assertEquals("incorrect command to ensure already relevant strategy?", expectedMakeIrrelevantCommand, makeIrrelevantCommand);
	}
	
	private void forceRelevancyForDefaultRelevantStrategy(Factor strategyOrActivityRef, Desire desireToUse) throws Exception
	{
		forceOverride(strategyOrActivityRef, desireToUse, true);
	}
	
	private void forceIrrelevancyForDefaultIrrelevantStrategyOrActivity(Factor StrategyOrActivity, Desire desireToUse) throws Exception
	{
		forceOverride(StrategyOrActivity, desireToUse, false);
	}
	
	private void forceOverride(Factor strategyOrActivityRef, Desire desireToUse, boolean isRelevant) throws Exception
	{
		RelevancyOverrideSet overrideSet = desireToUse.getStrategyActivityRelevancyOverrideSet();		
		overrideSet.add(new RelevancyOverride(strategyOrActivityRef.getRef(), isRelevant));
		CommandSetObjectData forceRelevancyOverrideOnDefaultRelevantFactor = new CommandSetObjectData(desireToUse.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrideSet.toString());
		getProject().executeCommand(forceRelevancyOverrideOnDefaultRelevantFactor);
	}
	
	private CommandSetObjectData createExpectedRelevancyOverrideCommand(Factor strategyOrActivityRef, Desire desireToUse, boolean isRelevant)
	{
		RelevancyOverrideSet exepctedRelevancyOverride = new RelevancyOverrideSet();
		exepctedRelevancyOverride.add(new RelevancyOverride(strategyOrActivityRef.getRef(), isRelevant));
		CommandSetObjectData expectedCommand = new CommandSetObjectData(desireToUse.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, exepctedRelevancyOverride.toString());
		return expectedCommand;
	}
	
	protected Desire createObjective(Factor owner) throws Exception
	{
		return getProject().createObjective(owner);
	}

	private Target targetWithGoal;
	private Strategy strategy;
	private Strategy strategyWithObjective;
	private Desire objective;
	private Desire goal;
	private Task activity;
}
