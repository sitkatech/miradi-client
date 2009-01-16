/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.project;

import org.miradi.diagram.cells.FactorCell;
import org.miradi.ids.DiagramFactorId;
import org.miradi.main.EAMTestCase;
import org.miradi.objectpools.CausePool;
import org.miradi.objectpools.IndicatorPool;
import org.miradi.objectpools.ObjectivePool;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.project.FactorDeleteHelper;

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
		project = null;
	}
	
	public void testDeleteFactor() throws Exception
	{
		FactorCell causeCell = project.createFactorCell(Cause.getObjectType());
		
		assertEquals("wrong indicator count?", 0, causeCell.getIndicators().size());
		assertEquals("wrong objective count?", 0, causeCell.getIndicators().size());
		project.addItemToFactorList(causeCell.getWrappedFactorRef(), Indicator.getObjectType(), Factor.TAG_INDICATOR_IDS);
		project.addItemToFactorList(causeCell.getWrappedFactorRef(), Indicator.getObjectType(), Factor.TAG_INDICATOR_IDS);
		project.addItemToFactorList(causeCell.getWrappedFactorRef(), Objective.getObjectType(), Factor.TAG_OBJECTIVE_IDS);
		
		assertEquals("didn't add indicators?", 2, causeCell.getIndicators().size());
		assertEquals("didn't add objective?", 1, causeCell.getObjectives().size());
		
		CausePool causePool = project.getCausePool();
		IndicatorPool indicatorPool = project.getIndicatorPool();
		ObjectivePool objectivePool = project.getObjectivePool();
		
		assertEquals("didn't add cause?", 1, causePool.getIdList().size());
		assertEquals("didn't add inidicator?", 2, indicatorPool.getIdList().size());
		assertEquals("didn't add objective?", 1, objectivePool.getIdList().size());
		FactorDeleteHelper deleteHelper = FactorDeleteHelper.createFactorDeleteHelper(project.getDiagramModel().getDiagramObject());
		deleteHelper.deleteFactorAndDiagramFactor(causeCell.getDiagramFactor());
		
		assertEquals("didnt delete cause?", 0, causePool.getIdList().size());
		assertEquals("didnt delete indicators?", 0, indicatorPool.getIdList().size());
		assertEquals("didnt delete objective?", 0, objectivePool.getIdList().size());
		
		DiagramFactorId[] diagramFactorIds = project.getAllDiagramFactorIds();
		assertEquals("didnt remove from diagram?", 0, diagramFactorIds.length);
	}
	
	ProjectForTesting project;
}
