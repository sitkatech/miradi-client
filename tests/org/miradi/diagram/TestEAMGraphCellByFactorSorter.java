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
package org.miradi.diagram;

import java.util.Arrays;
import java.util.Vector;

import org.miradi.diagram.cells.DiagramCauseCell;
import org.miradi.diagram.cells.DiagramStrategyCell;
import org.miradi.diagram.cells.DiagramTargetCell;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.EAMGraphCellByFactorRefSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;

public class TestEAMGraphCellByFactorSorter extends TestCaseWithProject
{
	public TestEAMGraphCellByFactorSorter(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramCauseCell causeCell = new DiagramCauseCell((Cause) cause.getWrappedFactor(), cause);
		
		DiagramFactor target = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramTargetCell targetCell = new DiagramTargetCell((Target) target.getWrappedFactor(), target);
		
		DiagramFactor strategy = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		DiagramStrategyCell strategyCell = new DiagramStrategyCell((Strategy) strategy.getWrappedFactor(), strategy);
		
		ORef diagramLinkRef = getProject().createDiagramLink(cause, target);
		DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRef);
		LinkCell linkCell = new LinkCell(diagramLink.getWrappedFactorLink(), diagramLink, causeCell, targetCell);
		
		Vector<EAMGraphCell> cellsToSort = new Vector();
		cellsToSort.add(strategyCell);
		cellsToSort.add(targetCell);
		cellsToSort.add(linkCell);
		cellsToSort.add(causeCell);
		cellsToSort.add(targetCell);
		cellsToSort.add(linkCell);
		
		EAMGraphCell[] cells = cellsToSort.toArray(new EAMGraphCell[0]);
		Arrays.sort(cells, new EAMGraphCellByFactorRefSorter());
		assertEquals("wrong cell for index 1?", cells[0], causeCell);
		assertEquals("wrong cell for index 1?", cells[1], strategyCell);
		assertEquals("wrong cell for index 1?", cells[2], targetCell);
		assertEquals("wrong cell for index 1?", cells[3], targetCell);
		assertEquals("wrong cell for index 1?", cells[4], linkCell);
		assertEquals("wrong cell for index 1?", cells[5], linkCell);
	}
}
