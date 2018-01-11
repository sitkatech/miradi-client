/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TargetSchema;

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

		strategyActivityRelevancy = new TestStrategyActivityRelevancy(getName(), getProject());

		DiagramFactor strategyDiagramFactor = getProject().createAndAddFactorToDiagram(StrategySchema.getObjectType());
		strategy = (Strategy) strategyDiagramFactor.getWrappedFactor();
		otherActivity = getProject().createTask(strategy);

		DiagramFactor strategyWithObjectiveDiagramFactor = getProject().createAndAddFactorToDiagram(StrategySchema.getObjectType());
		strategyWithObjective = (Strategy) strategyWithObjectiveDiagramFactor.getWrappedFactor();
		activity = getProject().createTask(strategyWithObjective);
		objective = createObjective(strategyWithObjective);

		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(TargetSchema.getObjectType());
		targetWithGoal = (Target) targetDiagramFactor.getWrappedFactor();
		goal = getProject().createGoal(targetWithGoal);
		getProject().createDiagramLinkAndAddToDiagram(strategyWithObjectiveDiagramFactor, targetDiagramFactor);
	}

    //region Strategy Relevancy tests
    public void testStrategyDefaultRelevantNoOverridesMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyDefaultRelevantNoOverridesMakeIrrelevant(strategyWithObjective, goal);
		strategyActivityRelevancy.verifyStrategyDefaultRelevantNoOverridesMakeIrrelevant(strategyWithObjective, objective);
	}

	public void testStrategyDefaultRelevantNoOverrideMakeRelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyDefaultRelevantNoOverrideMakeRelevant(strategyWithObjective, objective);
		strategyActivityRelevancy.verifyStrategyDefaultRelevantNoOverrideMakeRelevant(strategyWithObjective, goal);
	}
	
	public void testStrategyDefaultRelevantOverrideIrrelevantMakeRelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyDefaultRelevantOverrideIrrelevantMakeRelevant(strategyWithObjective, objective);
		strategyActivityRelevancy.verifyStrategyDefaultRelevantOverrideIrrelevantMakeRelevant(strategyWithObjective, goal);
	}
	
	public void testStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant(strategyWithObjective, objective);
		strategyActivityRelevancy.verifyStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant(strategyWithObjective, goal);
	}
	
	public void testStrategyDefaultRelevantOverrideRelevantMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyDefaultRelevantOverrideRelevantMakeIrrelevant(strategyWithObjective, objective);
		strategyActivityRelevancy.verifyStrategyDefaultRelevantOverrideRelevantMakeIrrelevant(strategyWithObjective, goal);
	}

	public void testStrategyDefaultIrrelevantNoOverrideMakeRelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(strategy, objective);
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(strategy, goal);
	}

    public void testStrategyDefaultIrrelevantNoOverrideMakeIrrelevant() throws Exception
    {
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeIrrelevant(strategy, objective);
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeIrrelevant(strategy, goal);
    }

    public void testStrategyDefaultIrrelevantRelevantOverrideMakeIrrelevant() throws Exception
    {
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(strategy, objective);
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(strategy, goal);
    }

    public void testStrategyDefaultIrrelevantOverrideRelevantMakeRelevant() throws Exception
    {
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(strategy, objective);
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(strategy, goal);
    }
    //endregion

    //region Activity Relevancy tests
    public void testActivityDefaultRelevantNoOverridesMakeIrrelevant() throws Exception
    {
		strategyActivityRelevancy.verifyActivityDefaultRelevantNoOverridesMakeIrrelevant(activity, goal);
		strategyActivityRelevancy.verifyActivityDefaultRelevantNoOverridesMakeIrrelevant(activity, objective);
    }

    public void testActivityDefaultRelevantNoOverrideMakeRelevant() throws Exception
    {
		strategyActivityRelevancy.verifyActivityDefaultRelevantNoOverrideMakeRelevant(activity, objective);
		strategyActivityRelevancy.verifyActivityDefaultRelevantNoOverrideMakeRelevant(activity, goal);
    }

    public void testActivityDefaultRelevantOverrideIrrelevantMakeRelevant() throws Exception
    {
		strategyActivityRelevancy.verifyActivityDefaultRelevantOverrideIrrelevantMakeRelevant(activity, objective);
		strategyActivityRelevancy.verifyActivityDefaultRelevantOverrideIrrelevantMakeRelevant(activity, goal);
    }

    public void testActivityDefaultRelevantOverrideIrrelevantMakeIrrelevant() throws Exception
    {
		strategyActivityRelevancy.verifyActivityDefaultRelevantOverrideIrrelevantMakeIrrelevant(activity, objective);
		strategyActivityRelevancy.verifyActivityDefaultRelevantOverrideIrrelevantMakeIrrelevant(activity, goal);
    }

    public void testActivityDefaultRelevantOverrideRelevantMakeIrrelevant() throws Exception
    {
		strategyActivityRelevancy.verifyActivityDefaultRelevantOverrideRelevantMakeIrrelevant(activity, objective);
		strategyActivityRelevancy.verifyActivityDefaultRelevantOverrideRelevantMakeIrrelevant(activity, goal);
    }

    public void testActivityDefaultIrrelevantNoOverrideMakeRelevant() throws Exception
    {
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(otherActivity, objective);
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(otherActivity, goal);
    }

    public void testActivityDefaultIrrelevantNoOverrideMakeIrrelevant() throws Exception
    {
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeIrrelevant(otherActivity, objective);
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeIrrelevant(otherActivity, goal);
    }

    public void testActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant() throws Exception
    {
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(otherActivity, objective);
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(otherActivity, goal);
    }

    public void testActivityDefaultIrrelevantOverrideRelevantMakeRelevant() throws Exception
    {
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(otherActivity, objective);
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(otherActivity, goal);
    }
    //endregion


	protected Desire createObjective(Factor owner) throws Exception
	{
		return getProject().createObjective(owner);
	}

	private TestStrategyActivityRelevancy strategyActivityRelevancy;
	private Target targetWithGoal;
	private Strategy strategy;
	private Strategy strategyWithObjective;
	private Desire objective;
	private Desire goal;
	private Task activity;
	private Task otherActivity;
}
