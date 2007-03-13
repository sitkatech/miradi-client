/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.DiagramCause;
import org.conservationmeasures.eam.diagram.cells.DiagramStrategy;
import org.conservationmeasures.eam.diagram.cells.DiagramTarget;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;

public class TestLayerManager extends EAMTestCase
{
	public TestLayerManager(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		idAssigner = new IdAssigner();
		cmTarget = new Target(takeNextModelNodeId());
		cmTarget.setLabel("Target");
		cmFactor = new Cause(takeNextModelNodeId());
		cmFactor.setLabel("Factor");
		cmIntervention = new Strategy(takeNextModelNodeId());
		cmIntervention.setLabel("Strategy");
		
		//FIXME create cell using project.createFactorCell() for below code
		DiagramFactorId targetNodeId = new DiagramFactorId(44);
		FactorId factorId = new FactorId(23);
		CreateDiagramFactorParameter extraInfo = new CreateDiagramFactorParameter(factorId);
		//Target targetFactor = new Target(factorId);
		DiagramFactor diagramFactorTarget = new DiagramFactor(targetNodeId, extraInfo);
		target = new DiagramTarget(cmTarget, diagramFactorTarget);
		
		DiagramFactorId factorNodeId = new DiagramFactorId(49);
		FactorId causeFactorId = new FactorId(26);
		CreateDiagramFactorParameter causeExtraInfo = new CreateDiagramFactorParameter(causeFactorId);
		//Target causeFactor = new Target(causeFactorId);
		DiagramFactor causeDiagramFactor = new DiagramFactor(factorNodeId, causeExtraInfo);
		factor = new DiagramCause(cmFactor, causeDiagramFactor);
		
		DiagramFactorId interventionFactorNodeId = new DiagramFactorId(55);
		FactorId interventionFactorId = new FactorId(40);
		CreateDiagramFactorParameter interventionExtraInfo = new CreateDiagramFactorParameter(interventionFactorId);
		//Target interventionFactor = new Target(interventionFactorId);
		DiagramFactor interventionDiagramFactor = new DiagramFactor(interventionFactorNodeId, interventionExtraInfo);
		intervention = new DiagramStrategy(cmIntervention, interventionDiagramFactor);
	}

	public void testDefaultAllVisible() throws Exception
	{
		LayerManager manager = new LayerManager();
		verifyVisibility("default visible", true, intervention, manager);
		verifyVisibility("default visible", true, factor, manager);
		verifyVisibility("default visible", true, target, manager);
		
		assertTrue("All layers not visible by default?", manager.areAllNodesVisible());
	}
	
	public void testHide() throws Exception
	{
		LayerManager manager = new LayerManager();
		manager.setVisibility(DiagramStrategy.class, false);
		
		DiagramFactor strategyDiagramFactor = getDiagramFactor(45);
		DiagramFactor targetDiagramFactor = getDiagramFactor(67);
		DiagramFactor targetDiagramFactor2 = getDiagramFactor(98);
		
		verifyVisibility("hidden type", false, new DiagramStrategy(cmIntervention, strategyDiagramFactor), manager);
		verifyVisibility("non-hidden type", true, new DiagramTarget(cmTarget, targetDiagramFactor), manager);
		assertFalse("All layers still visible?", manager.areAllNodesVisible());
		
		manager.setVisibility(DiagramStrategy.class, true);
		verifyVisibility("unhidden type", true, new DiagramTarget(cmTarget, targetDiagramFactor2), manager);
		assertTrue("All layers not visible again?", manager.areAllNodesVisible());
	}

	private DiagramFactor getDiagramFactor(int id)
	{
		final int SOME_RANDOM_NUMBER = 5;
		DiagramFactorId diagramFactorId = new DiagramFactorId(id + SOME_RANDOM_NUMBER);
		FactorId strategyFactorId = new FactorId(id);
		CreateDiagramFactorParameter strategyExtraInfo = new CreateDiagramFactorParameter(strategyFactorId);
		DiagramFactor strategyDiagramFactor = new DiagramFactor(diagramFactorId, strategyExtraInfo);
		
		return strategyDiagramFactor;
	}
	
	public void testHideIds() throws Exception
	{
		LayerManager manager = new LayerManager();
		assertTrue("all nodes not visible to start?", manager.areAllNodesVisible());
		IdList idsToHide = new IdList();
		idsToHide.add(target.getWrappedId());
		idsToHide.add(factor.getWrappedId());
		manager.setHiddenIds(idsToHide);
		assertFalse("thinks all nodes are visible?", manager.areAllNodesVisible());
		for(int i = 0; i < idsToHide.size(); ++i)
		{
			verifyNodeVisibility("hide ids", false, target, manager);
			verifyNodeVisibility("hide ids", false, factor, manager);
			verifyNodeVisibility("hide ids", true, intervention, manager);
		}
	}
	
	public void testGoals() throws Exception
	{
		LayerManager manager = new LayerManager();
		assertTrue("Goals not visible by default?", manager.areGoalsVisible());
		manager.setGoalsVisible(false);
		assertFalse("Didn't set invisible?", manager.areGoalsVisible());
		manager.setGoalsVisible(true);
		assertTrue("Didn't set visible?", manager.areGoalsVisible());
	}
	
	
	public void testObjectives() throws Exception
	{
		LayerManager manager = new LayerManager();
		assertTrue("Objectives not visible by default?", manager.areObjectivesVisible());
		manager.setObjectivesVisible(false);
		assertFalse("Didn't set invisible?", manager.areObjectivesVisible());
		manager.setObjectivesVisible(true);
		assertTrue("Didn't set visible?", manager.areObjectivesVisible());
	}

	public void testIndicators() throws Exception
	{
		LayerManager manager = new LayerManager();
		assertTrue("Indicators not visible by default?", manager.areIndicatorsVisible());
		manager.setIndicatorsVisible(false);
		assertFalse("Didn't set invisible?", manager.areIndicatorsVisible());
		manager.setIndicatorsVisible(true);
		assertTrue("Didn't set visible?", manager.areIndicatorsVisible());
	}

	private void verifyVisibility(String text, boolean expected, FactorCell node, LayerManager manager)
	{
		assertEquals("type: " + text + " (" + node + ") ",expected, manager.isTypeVisible(node.getClass()));
		verifyNodeVisibility(text, expected, node, manager);
	}

	private void verifyNodeVisibility(String text, boolean expected, FactorCell node, LayerManager manager)
	{
		assertEquals("node: " + text + " (" + node.getLabel() + ") ",expected, manager.isVisible(node));
	}
	
	private FactorId takeNextModelNodeId()
	{
		return new FactorId(idAssigner.takeNextId().asInt());
	}
	
	IdAssigner idAssigner;

	Target cmTarget;
	Cause cmFactor;
	Strategy cmIntervention;
	
	FactorCell target;
	FactorCell factor;
	FactorCell intervention;
}
