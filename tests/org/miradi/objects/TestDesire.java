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
	
	public void testStrategyDefaultRelevantNoOverridesMakeIrrelevant() throws Exception
	{
		verifyStrategyDefaultRelevantNoOverridesMakeIrrelevant(goal);
		verifyStrategyDefaultRelevantNoOverridesMakeIrrelevant(objective);
	}

	public void testStrategyDefaultRelevantNoOverrideMakeRelevant() throws Exception
	{
		verifyStrategyDefaultRelevantNoOverrideMakeRelevant(objective);
		verifyStrategyDefaultRelevantNoOverrideMakeRelevant(goal);
	}
	
	public void testStrategyDefaultRelevantOverrideIrrelevantMakeRelevant() throws Exception
	{
		verifyStrategyDefaultRelevantOverrideIrrelevantMakeRelevant(objective);
		verifyStrategyDefaultRelevantOverrideIrrelevantMakeRelevant(goal);
	}
	
	public void testStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant() throws Exception
	{
		verifyStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant(objective);
		verifyStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant(goal);
	}
	
	public void testStrategyDefaultRelevantOverrideRelevantMakeIrrelevant() throws Exception
	{
		verifyStrategyDefaultRelevantOverrideRelevantMakeIrrelevant(objective);
		verifyStrategyDefaultRelevantOverrideRelevantMakeIrrelevant(goal);
	}

	public void testStrategyDefaultIrrelevantNoOverrideMakeRelevantToObjective() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(strategy, objective);
		verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(strategy, goal);
	}
	
	public void testActivityDefaultIrrelevantNoOverrideMakeRelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(activity, objective);
		verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(activity, goal);
	}
	
	public void testStrategyDefaultIrrelevantNoOverrideMakeIrrelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeIrrelevant(strategy, objective);
		verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeIrrelevant(strategy, goal);
	}
	
	public void testStrategyDefaultIrrelevantRelevantOverrideMakeIrrelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(strategy, objective);
		verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(strategy, goal);
	}
	
	public void testActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(activity, objective);
		verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(activity, goal);
	}
	
	public void testActivityDefaultIrrelevantOverrideRelevantMakeRelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(activity, objective);
		verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(activity, goal);
	}

	public void testStrategyDefaultIrrelevantOverrideRelevantMakeRelevant() throws Exception
	{
		verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(strategy, objective);
		verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(strategy, goal);
	}
	
	public void testDefaultIrrelevantOverrideIrrelevantMakeRelevant() throws Exception
	{
		verifyDefaultIrrelevantOverrideIrrelevantMakeRelevant(objective);
		verifyDefaultIrrelevantOverrideIrrelevantMakeRelevant(goal);
	}

	private void verifyDefaultIrrelevantOverrideIrrelevantMakeRelevant(Desire desire) throws Exception
	{
		forceIrrelevancyForDefaultIrrelevantStrategyOrActivity(activity, desire);

		CommandVector commandsToEnsureDefaultRelevantIsIrrelevant = desire.createCommandsToEnsureStrategyOrActivityIsRelevant(activity.getRef());
		assertEquals("Should contain one command to remove incorrect irrelevant override for default irrelevant", 1, commandsToEnsureDefaultRelevantIsIrrelevant.size());
		
		CommandSetObjectData setCommand = (CommandSetObjectData) commandsToEnsureDefaultRelevantIsIrrelevant.get(0);
		CommandSetObjectData expectedCommand = createExpectedRelevancyOverrideCommand(activity, desire, true);
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
	
	private void verifyStrategyDefaultRelevantNoOverrideMakeRelevant(Desire desire) throws Exception
	{
		CommandVector commands = desire.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyWithObjective.getRef());
		assertTrue("Should not make already relevant strategy relevant again", commands.isEmpty());
	}
	
	private void verifyStrategyDefaultRelevantOverrideIrrelevantMakeRelevant(Desire desire) throws Exception
	{
		CommandVector commandsToMakeDefaultStrategyIrrelevant = desire.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjective.getRef());
		getProject().executeCommandsAsTransaction(commandsToMakeDefaultStrategyIrrelevant);
		
		CommandVector commandsToMakeIrrelevantStrategyRelevant = desire.createCommandsToEnsureStrategyOrActivityIsRelevant(strategyWithObjective.getRef());
		assertEquals("Should contain one command to make irrelevant strategy relevant by default?", 1, commandsToMakeIrrelevantStrategyRelevant.size());
		CommandSetObjectData makeRelevantCommand = (CommandSetObjectData) commandsToMakeIrrelevantStrategyRelevant.get(0);
		RelevancyOverrideSet overrides = new RelevancyOverrideSet();
		CommandSetObjectData expectedMakeRelevantCommand = new CommandSetObjectData(desire.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrides.toString());
		assertEquals("incorrect command to ensure already relevant strategy?", expectedMakeRelevantCommand, makeRelevantCommand);
	}
	
	private void verifyStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant(Desire desire) throws Exception
	{
		getProject().executeCommandsAsTransaction(desire.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjective.getRef()));
		
		assertFalse("default relevant stratgey should be irrelevant override?", desire.getRelevantStrategyRefs().contains(strategyWithObjective.getRef()));
		
		CommandVector commands = desire.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjective.getRef());
		assertTrue("Should not make already irrelevant strategy irrelevant again", commands.isEmpty());
	}

	private void verifyStrategyDefaultRelevantOverrideRelevantMakeIrrelevant(Desire desire) throws Exception
	{
		forceRelevancyForDefaultRelevantStrategy(strategyWithObjective, desire);
		CommandVector commandsToEnsureDefaultRelevantIsIrrelevant = desire.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategyWithObjective.getRef());
		assertEquals("Should contain one command to remove incorrect relevant override for default relevant", 1, commandsToEnsureDefaultRelevantIsIrrelevant.size());
		
		CommandSetObjectData setCommand = (CommandSetObjectData) commandsToEnsureDefaultRelevantIsIrrelevant.get(0);
		CommandSetObjectData expectedCommand = createExpectedRelevancyOverrideCommand(strategyWithObjective, desire, false);
		assertEquals("Command should only make strategy default relevant, irrelevant?", expectedCommand, setCommand);
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
