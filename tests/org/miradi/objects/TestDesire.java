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
		CommandVector createCommandsToEnsureStrategyIsRelevant = desire.createCommandsToEnsureStrategyIsIrrelevant(strategy.getRef());
		assertEquals("Should contain one command to make default strategy irrelevant?", 1, createCommandsToEnsureStrategyIsRelevant.size());
		CommandSetObjectData expectedMakeIrrelevantCommand = (CommandSetObjectData) createCommandsToEnsureStrategyIsRelevant.get(0);
		RelevancyOverrideSet overrides = new RelevancyOverrideSet();
		overrides.add(new RelevancyOverride(strategy.getRef(), false));
		CommandSetObjectData makeIrrelevantCommand = new CommandSetObjectData(desire.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrides.toString());
		assertEquals("incorrect command to ensure already relevant strategy?", expectedMakeIrrelevantCommand, makeIrrelevantCommand);
	}
	
	public void testStrategyDefaultRelevantNoOverrideMakeRelevant() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategy);
		CommandVector commands = desire.createCommandsToEnsureStrategyIsRelevant(strategy.getRef());
		assertTrue("Should not make already relevant strategy relevant again", commands.isEmpty());
	}
	
	public void testStrategyDefaultRelevantOverrideIrrelevantMakeRelevant() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategy);
		CommandVector commandsToMakeDefaultStrategyIrrelevant = desire.createCommandsToEnsureStrategyIsIrrelevant(strategy.getRef());
		getProject().executeCommandsAsTransaction(commandsToMakeDefaultStrategyIrrelevant);
		
		CommandVector commandsToMakeIrrelevantStrategyRelevant = desire.createCommandsToEnsureStrategyIsRelevant(strategy.getRef());
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
		getProject().executeCommandsAsTransaction(desire.createCommandsToEnsureStrategyIsIrrelevant(strategy.getRef()));
		
		assertFalse("default relevant stratgey should be irrelevant override?", desire.getRelevantStrategyRefs().contains(strategy.getRef()));
		
		CommandVector commands = desire.createCommandsToEnsureStrategyIsIrrelevant(strategy.getRef());
		assertTrue("Should not make already irrelevant strategy irrelevant again", commands.isEmpty());
	}
	
	public void testStrategyDefaultIrrelevantNoOverrideMakeRelevant() throws Exception
	{
		Strategy strategyWithObjective = getProject().createStrategy();
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategyWithObjective);
		getProject().createFactorLink(strategyWithObjective.getRef(), strategy.getRef());
		
		CommandVector commandsToMakeRelevant = desire.createCommandsToEnsureStrategyIsRelevant(strategy.getRef());
		assertEquals("Should contain exactly one command to make irrelevant strategy with no override, relevant with override?", 1, commandsToMakeRelevant.size());
		CommandSetObjectData makeRlevantCommand = (CommandSetObjectData) commandsToMakeRelevant.get(0);
		RelevancyOverrideSet overrides = new RelevancyOverrideSet();
		overrides.add(new RelevancyOverride(strategy.getRef(), true));
		CommandSetObjectData expectedMakeRelevantCommand = new CommandSetObjectData(desire.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrides.toString());
		assertEquals("Incorrect method to make irrelevant strategy with no override, relevant with override?", makeRlevantCommand, expectedMakeRelevantCommand);
	}
	
	public void testStrategyDefaultIrrelevantRelevantOverrideMakeIrrelevant() throws Exception
	{
		Strategy strategyWithObjective = getProject().createStrategy();
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategyWithObjective);
		getProject().createFactorLink(strategyWithObjective.getRef(), strategy.getRef());
		
		CommandVector makeRelevantCommands = desire.createCommandsToEnsureStrategyIsRelevant(strategy.getRef());
		getProject().executeCommandsAsTransaction(makeRelevantCommands);
		
		assertTrue("Irrelevant strategy should be relevant?", desire.getRelevantStrategyRefs().contains(strategy.getRef()));
		
		CommandVector makeIrrelevantCommands = desire.createCommandsToEnsureStrategyIsIrrelevant(strategy.getRef());
		assertEquals("Should contain one command to make relevant upstream strategy irrelevant?", 1, makeIrrelevantCommands.size());
		CommandSetObjectData makeIrrelevantCommand = (CommandSetObjectData) makeIrrelevantCommands.get(0);
		RelevancyOverrideSet overrides = new RelevancyOverrideSet();
		CommandSetObjectData expectedMakeIrrelevantCommand = new CommandSetObjectData(desire.getRef(), Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, overrides.toString());
		assertEquals("incorrect command to ensure already relevant strategy?", expectedMakeIrrelevantCommand, makeIrrelevantCommand);
	}
	
	public void testStrategyDefaultIrrelevantNoOverrideMakeIrrelevant() throws Exception
	{
		Strategy strategyWithObjective = getProject().createStrategy();
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategyWithObjective);
		getProject().createFactorLink(strategyWithObjective.getRef(), strategy.getRef());
		
		CommandVector commands = desire.createCommandsToEnsureStrategyIsIrrelevant(strategy.getRef());
		assertTrue("Should not make already irrelevant strategy irrelevant again?", commands.isEmpty());
	}
	
	public void testStrategyDefaultIrrelevantOverrideRelevantMakeRelevant() throws Exception
	{
		Strategy strategyWithObjective = getProject().createStrategy();
		Strategy strategy = getProject().createStrategy();
		Desire desire = createDesire(strategyWithObjective);
		getProject().createFactorLink(strategyWithObjective.getRef(), strategy.getRef());
		
		getProject().executeCommandsAsTransaction(desire.createCommandsToEnsureStrategyIsRelevant(strategy.getRef()));
		CommandVector commands = desire.createCommandsToEnsureStrategyIsRelevant(strategy.getRef());
		assertTrue("Should not make already relevant strategy relevant again?", commands.isEmpty());
	}
	
	protected Desire createDesire(Strategy strategy) throws Exception
	{
		return getProject().createObjective(strategy);
	}
}
