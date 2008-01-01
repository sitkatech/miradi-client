/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objectpools.CausePool;
import org.conservationmeasures.eam.objectpools.IndicatorPool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;

public class TestFactorDeleteHelper extends EAMTestCase
{
	public TestFactorDeleteHelper(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}

	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}
	
	public void testDeleteFactor() throws Exception
	{
		FactorCell causeCell = project.createFactorCell(Cause.getObjectType());
		
		assertEquals("wrong indicator count?", 0, causeCell.getIndicators().size());
		assertEquals("wrong objective count?", 0, causeCell.getIndicators().size());
		project.addItemToFactorList(causeCell.getWrappedId(), Indicator.getObjectType(), Factor.TAG_INDICATOR_IDS);
		project.addItemToFactorList(causeCell.getWrappedId(), Indicator.getObjectType(), Factor.TAG_INDICATOR_IDS);
		project.addItemToFactorList(causeCell.getWrappedId(), Objective.getObjectType(), Factor.TAG_OBJECTIVE_IDS);
		
		assertEquals("didn't add indicators?", 2, causeCell.getIndicators().size());
		assertEquals("didn't add objective?", 1, causeCell.getObjectives().size());
		
		CausePool causePool = project.getCausePool();
		IndicatorPool indicatorPool = project.getIndicatorPool();
		ObjectivePool objectivePool = project.getObjectivePool();
		
		assertEquals("didn't add cause?", 1, causePool.getIdList().size());
		assertEquals("didn't add inidicator?", 2, indicatorPool.getIdList().size());
		assertEquals("didn't add objective?", 1, objectivePool.getIdList().size());
		FactorDeleteHelper deleteHelper = new FactorDeleteHelper(project.getDiagramModel());
		deleteHelper.deleteFactor(causeCell.getDiagramFactor());
		
		assertEquals("didnt delete cause?", 0, causePool.getIdList().size());
		assertEquals("didnt delete indicators?", 0, indicatorPool.getIdList().size());
		assertEquals("didnt delete objective?", 0, objectivePool.getIdList().size());
		
		DiagramFactorId[] diagramFactorIds = project.getAllDiagramFactorIds();
		assertEquals("didnt remove from diagram?", 0, diagramFactorIds.length);
	}
	
	ProjectForTesting project;
}
