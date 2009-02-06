/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.diagram;

import org.miradi.diagram.cells.DiagramStrategyCell;
import org.miradi.diagram.cells.DiagramTargetCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.project.ProjectForTesting;

public class TestLayerManager extends EAMTestCase
{
	public TestLayerManager(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		
		project = new ProjectForTesting(getName());
		ORef targetRef = project.createObject(Target.getObjectType());
		cmTarget = (Target) Factor.findFactor(project, targetRef);
		cmTarget.setLabel("Target");
		ORef causeRef = project.createObject(Cause.getObjectType());
		cmFactor = (Cause) Factor.findFactor(project, causeRef);
		cmFactor.setLabel("Factor");
		ORef strategyRef = project.createObject(Strategy.getObjectType());
		cmIntervention = (Strategy) Factor.findFactor(project, strategyRef);
		cmIntervention.setLabel("Strategy");
		
		target = project.createFactorCell(ObjectType.TARGET);
		factor = project.createFactorCell(ObjectType.CAUSE);
		intervention = project.createFactorCell(ObjectType.STRATEGY);
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		project = null;
		super.tearDown();
	}

	public void testDefaultAllVisible() throws Exception
	{
		LayerManager manager = new LayerManager(getProject().getTestingDiagramObject());
		verifyVisibility("default visible", true, intervention, manager);
		verifyVisibility("default visible", true, factor, manager);
		verifyVisibility("default visible", true, target, manager);
		
		assertTrue("All layers not visible by default?", manager.areAllNodesVisible());
	}
	
	public void testHide() throws Exception
	{
		LayerManager manager = new LayerManager(getProject().getTestingDiagramObject());
		manager.setVisibility(DiagramStrategyCell.class, false);
		
		DiagramFactor strategyDiagramFactor = getDiagramFactor(45);
		DiagramFactor targetDiagramFactor = getDiagramFactor(67);
		DiagramFactor targetDiagramFactor2 = getDiagramFactor(98);
		
		verifyVisibility("hidden type", false, new DiagramStrategyCell(cmIntervention, strategyDiagramFactor), manager);
		verifyVisibility("non-hidden type", true, new DiagramTargetCell(cmTarget, targetDiagramFactor), manager);
		assertFalse("All layers still visible?", manager.areAllNodesVisible());
		
		manager.setVisibility(DiagramStrategyCell.class, true);
		verifyVisibility("unhidden type", true, new DiagramTargetCell(cmTarget, targetDiagramFactor2), manager);
		assertTrue("All layers not visible again?", manager.areAllNodesVisible());
	}

	private DiagramFactor getDiagramFactor(int id) throws Exception
	{
		final int SOME_RANDOM_NUMBER = 5;
		DiagramFactorId diagramFactorId = new DiagramFactorId(id + SOME_RANDOM_NUMBER);
		FactorId strategyFactorId = new FactorId(id);
		CreateDiagramFactorParameter strategyExtraInfo = new CreateDiagramFactorParameter(new ORef(ObjectType.STRATEGY, strategyFactorId));
		DiagramFactor strategyDiagramFactor = new DiagramFactor(project.getObjectManager(), diagramFactorId, strategyExtraInfo);
		
		return strategyDiagramFactor;
	}
	
	public void testHideIds() throws Exception
	{
		LayerManager manager = new LayerManager(getProject().getTestingDiagramObject());
		assertTrue("all nodes not visible to start?", manager.areAllNodesVisible());
		ORefList ORefsToHide = new ORefList();
		ORefsToHide.add(target.getWrappedFactorRef());
		ORefsToHide.add(factor.getWrappedFactorRef());
		manager.setHiddenORefs(ORefsToHide);
		assertFalse("thinks all nodes are visible?", manager.areAllNodesVisible());
		for(int i = 0; i < ORefsToHide.size(); ++i)
		{
			verifyNodeVisibility("hide ids", false, target, manager);
			verifyNodeVisibility("hide ids", false, factor, manager);
			verifyNodeVisibility("hide ids", true, intervention, manager);
		}
	}
	
	public void testGoals() throws Exception
	{
		LayerManager manager = new LayerManager(getProject().getTestingDiagramObject());
		assertTrue("Goals not visible by default?", manager.areGoalsVisible());
		manager.setGoalsVisible(false);
		assertFalse("Didn't set invisible?", manager.areGoalsVisible());
		manager.setGoalsVisible(true);
		assertTrue("Didn't set visible?", manager.areGoalsVisible());
	}
	
	
	public void testObjectives() throws Exception
	{
		LayerManager manager = new LayerManager(getProject().getTestingDiagramObject());
		assertTrue("Objectives not visible by default?", manager.areObjectivesVisible());
		manager.setObjectivesVisible(false);
		assertFalse("Didn't set invisible?", manager.areObjectivesVisible());
		manager.setObjectivesVisible(true);
		assertTrue("Didn't set visible?", manager.areObjectivesVisible());
	}

	public void testIndicators() throws Exception
	{
		LayerManager manager = new LayerManager(getProject().getTestingDiagramObject());
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
		assertEquals("node: " + text + " (" + node.getLabel() + ") ",expected, manager.isVisible(project.getTestingDiagramObject(), node));
	}
	
	private ProjectForTesting getProject()
	{
		return project;
	}
	
	private Target cmTarget;
	private Cause cmFactor;
	private Strategy cmIntervention;
	
	private ProjectForTesting project;
	private FactorCell target;
	private FactorCell factor;
	private FactorCell intervention;
}
