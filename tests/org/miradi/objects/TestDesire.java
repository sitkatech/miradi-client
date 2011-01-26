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
	
	public void testStrategyDefaultRelevantNoOverridesMakeIrrelevant() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategy);
		CommandVector createCommandsToEnsureStrategyIsRelevant = desire.createCommandsToEnsureFactorIsIrrelevant(strategy.getRef());
		assertEquals("Should contain one command to make default strategy irrelevant?", 1, createCommandsToEnsureStrategyIsRelevant.size());
		CommandSetObjectData expectedMakeIrrelevantCommand = (CommandSetObjectData) createCommandsToEnsureStrategyIsRelevant.get(0);
		CommandSetObjectData makeIrrelevantCommand = createExpectedRelevancyOverrideCommand(strategy, desire, false);
		assertEquals("incorrect command to ensure already relevant strategy?", expectedMakeIrrelevantCommand, makeIrrelevantCommand);
	}
	
	public void testStrategyDefaultRelevantNoOverrideMakeRelevant() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategy);
		CommandVector commands = desire.createCommandsToEnsureFactorIsRelevant(strategy.getRef());
		assertTrue("Should not make already relevant strategy relevant again", commands.isEmpty());
	}
	
	public void testStrategyDefaultRelevantOverrideIrrelevantMakeRelevant() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategy);
		CommandVector commandsToMakeDefaultStrategyIrrelevant = desire.createCommandsToEnsureFactorIsIrrelevant(strategy.getRef());
		getProject().executeCommandsAsTransaction(commandsToMakeDefaultStrategyIrrelevant);
		
		CommandVector commandsToMakeIrrelevantStrategyRelevant = desire.createCommandsToEnsureFactorIsRelevant(strategy.getRef());
		assertEquals("Should contain one command to make irrelevant strategy relevant by default?", 1, commandsToMakeIrrelevantStrategyRelevant.size());
		CommandSetObjectData makeRelevantCommand = (CommandSetObjectData) commandsToMakeIrrelevantStrategyRelevant.get(0);
		RelevancyOverrideSet overrides = new RelevancyOverrideSet();
		CommandSetObjectData expectedMakeRelevantCommand = new CommandSetObjectData(desire.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrides.toString());
		assertEquals("incorrect command to ensure already relevant strategy?", expectedMakeRelevantCommand, makeRelevantCommand);
	}
	
	public void testStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategy);
		getProject().executeCommandsAsTransaction(desire.createCommandsToEnsureFactorIsIrrelevant(strategy.getRef()));
		
		assertFalse("default relevant stratgey should be irrelevant override?", desire.getRelevantStrategyRefs().contains(strategy.getRef()));
		
		CommandVector commands = desire.createCommandsToEnsureFactorIsIrrelevant(strategy.getRef());
		assertTrue("Should not make already irrelevant strategy irrelevant again", commands.isEmpty());
	}
	
	public void testStrategyDefaultRelevantOverrideRelevantMakeIrrelevant() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategy);
		
		forceRelevancyForDefaultRelevantStrategy(strategy, desire);
		CommandVector commandsToEnsureDefaultRelevantIsIrrelevant = desire.createCommandsToEnsureFactorIsIrrelevant(strategy.getRef());
		assertEquals("Should contain one command to remove incorrect relevant override for default relevant", 1, commandsToEnsureDefaultRelevantIsIrrelevant.size());
		
		CommandSetObjectData setCommand = (CommandSetObjectData) commandsToEnsureDefaultRelevantIsIrrelevant.get(0);
		CommandSetObjectData expectedCommand = createExpectedRelevancyOverrideCommand(strategy, desire, false);
		assertEquals("Command should only make strategy default relevant, irrelevant?", expectedCommand, setCommand);
	}

	public void testStrategyDefaultIrrelevantNoOverrideMakeRelevant() throws Exception
	{
		Strategy strategyWithObjective = getProject().createStrategy();
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategyWithObjective);
		getProject().createFactorLink(strategyWithObjective.getRef(), strategy.getRef());
		
		verifyFactorDefaultIrrelevantNoOverrideMakeRelevant(strategy, desire);
	}
	
	public void testActivityDefaultIrrelevantNoOverrideMakeRelevant() throws Exception
	{
		Strategy strategyWithObjective = getProject().createStrategy();
		Task activity = getProject().createTask(strategyWithObjective);
		Desire desire = createDesire(strategyWithObjective);
		
		verifyFactorDefaultIrrelevantNoOverrideMakeRelevant(activity, desire);
	}
	
	public void testStrategyDefaultIrrelevantNoOverrideMakeIrrelevant() throws Exception
	{
		Strategy strategyWithObjective = getProject().createStrategy();
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategyWithObjective);
		getProject().createFactorLink(strategyWithObjective.getRef(), strategy.getRef());
		
		verifyFactorDefaultIrrelevantNoOverrideMakeIrrelevant(strategy, desire);
	}
	
	public void testStrategyDefaultIrrelevantRelevantOverrideMakeIrrelevant() throws Exception
	{
		Strategy strategyWithObjective = getProject().createStrategy();
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategyWithObjective);
		getProject().createFactorLink(strategyWithObjective.getRef(), strategy.getRef());
		
		verifyFactorDefaultIrrelevantRelevantOverrideMakeIrrelevant(strategy,	desire);
	}
	
	public void testActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant() throws Exception
	{
		Strategy strategyWithObjective = getProject().createStrategy();
		Desire desire = createDesire(strategyWithObjective);
		Task activity = getProject().createTask(strategyWithObjective);
			
		verifyFactorDefaultIrrelevantRelevantOverrideMakeIrrelevant(activity,	desire);
	}
	
	public void testActivityDefaultIrrelevantOverrideRelevantMakeRelevant() throws Exception
	{
		Strategy strategyWithObjective = getProject().createStrategy();
		Desire desire = createDesire(strategyWithObjective);
		Task activity = getProject().createTask(strategyWithObjective);
		
		verifyFactorDefaultIrrelevantOverrideRelevantMakeRelevant(activity, desire);
	}

	public void testStrategyDefaultIrrelevantOverrideRelevantMakeRelevant() throws Exception
	{
		Strategy strategyWithObjective = getProject().createStrategy();
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategyWithObjective);
		getProject().createFactorLink(strategyWithObjective.getRef(), strategy.getRef());
		
		verifyFactorDefaultIrrelevantOverrideRelevantMakeRelevant(strategy, desire);
	}

	private void verifyFactorDefaultIrrelevantNoOverrideMakeRelevant(Factor owner, Desire desire) throws Exception
	{
		CommandVector commandsToMakeRelevant = desire.createCommandsToEnsureFactorIsRelevant(owner.getRef());
		assertEquals("Should contain exactly one command to make irrelevant factor with no override, relevant with override?", 1, commandsToMakeRelevant.size());
		CommandSetObjectData makeRlevantCommand = (CommandSetObjectData) commandsToMakeRelevant.get(0);
		RelevancyOverrideSet overrides = new RelevancyOverrideSet();
		overrides.add(new RelevancyOverride(owner.getRef(), true));
		CommandSetObjectData expectedMakeRelevantCommand = new CommandSetObjectData(desire.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrides.toString());
		assertEquals("Incorrect method to make irrelevant factor with no override, relevant with override?", makeRlevantCommand, expectedMakeRelevantCommand);
	}

	private void verifyFactorDefaultIrrelevantNoOverrideMakeIrrelevant(Factor owner, Desire desire) throws Exception
	{
		CommandVector commands = desire.createCommandsToEnsureFactorIsIrrelevant(owner.getRef());
		assertTrue("Should not make already irrelevant factor irrelevant again?", commands.isEmpty());
	}
	
	private void verifyFactorDefaultIrrelevantRelevantOverrideMakeIrrelevant(Factor owner, Desire desire) throws Exception
	{
		CommandVector makeRelevantCommands = desire.createCommandsToEnsureFactorIsRelevant(owner.getRef());
		getProject().executeCommandsAsTransaction(makeRelevantCommands);
		
		assertTrue("Irrelevant factor should be relevant?", desire.getRelevantStrategyAndActivityRefs().contains(owner.getRef()));
		
		CommandVector makeIrrelevantCommands = desire.createCommandsToEnsureFactorIsIrrelevant(owner.getRef());
		assertEquals("Should contain one command to make relevant factor irrelevant?", 1, makeIrrelevantCommands.size());
		CommandSetObjectData makeIrrelevantCommand = (CommandSetObjectData) makeIrrelevantCommands.get(0);
		RelevancyOverrideSet overrides = new RelevancyOverrideSet();
		CommandSetObjectData expectedMakeIrrelevantCommand = new CommandSetObjectData(desire.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrides.toString());
		assertEquals("incorrect command to ensure already relevant factor?", expectedMakeIrrelevantCommand, makeIrrelevantCommand);
	}
	
	private void verifyFactorDefaultIrrelevantOverrideRelevantMakeRelevant(Factor owner, Desire desire) throws Exception
	{
		getProject().executeCommandsAsTransaction(desire.createCommandsToEnsureFactorIsRelevant(owner.getRef()));
		CommandVector commands = desire.createCommandsToEnsureFactorIsRelevant(owner.getRef());
		assertTrue("Should not make already relevant factor relevant again?", commands.isEmpty());
	}
	
	private void forceRelevancyForDefaultRelevantStrategy(Factor factor, Desire desire) throws Exception
	{
		forceOverride(factor, desire, true);
	}
	
	private void forceOverride(Factor factor, Desire desire, boolean isRelevant) throws Exception
	{
		RelevancyOverrideSet overrideSet = desire.getStrategyActivityRelevancyOverrideSet();		
		overrideSet.add(new RelevancyOverride(factor.getRef(), isRelevant));
		CommandSetObjectData forceRelevancyOverrideOnDefaultRelevantFactor = new CommandSetObjectData(desire.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrideSet.toString());
		getProject().executeCommand(forceRelevancyOverrideOnDefaultRelevantFactor);
	}
	
	private CommandSetObjectData createExpectedRelevancyOverrideCommand(Factor factor, Desire desire, boolean isRelevant)
	{
		RelevancyOverrideSet exepctedRelevancyOverride = new RelevancyOverrideSet();
		exepctedRelevancyOverride.add(new RelevancyOverride(factor.getRef(), isRelevant));
		CommandSetObjectData expectedCommand = new CommandSetObjectData(desire.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, exepctedRelevancyOverride.toString());
		return expectedCommand;
	}
	
	protected Desire createDesire(Factor owner) throws Exception
	{
		return getProject().createObjective(owner);
	}
}
