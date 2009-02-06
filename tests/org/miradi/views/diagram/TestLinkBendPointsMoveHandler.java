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

import java.awt.Point;

import org.miradi.main.EAMTestCase;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.PointList;

public class TestLinkBendPointsMoveHandler extends EAMTestCase
{
	public TestLinkBendPointsMoveHandler(String name)
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
	
	//FIXME create test for moving BendPoints
	//Disable test due to the need of a diagramComponent in the move handler
	public void testMoveBendPoints() throws Exception
	{
//		LinkBendPointsMoveHandler handler = new LinkBendPointsMoveHandler(project);
//		PointList bendPoints = createBendPointList();
//	
//		LinkCell linkCell = project.createLinkCellWithBendPoints(bendPoints);
//		DiagramFactorLink diagramLink = linkCell.getDiagramFactorLink();
//		assertEquals("bend points not set?", 3, diagramLink.getBendPoints().size());
//		
//		int deltaX = 10;
//		int deltaY = 10;
//		int selectionIndexes[] = {0, 1};
//		handler.moveBendPoints(linkCell, selectionIndexes, deltaX, deltaY);
//		
//		DiagramFactorLink diagramLinkWithMovedPoints = linkCell.getDiagramFactorLink();
//		PointList movedBendPoints = diagramLinkWithMovedPoints.getBendPoints();
//		assertEquals("lost bendpoint in move?", 3, movedBendPoints.size());
//		
//		Point movedPoint1 = movedBendPoints.get(selectionIndexes[0]);
//		Point snapped1 = project.getSnapped(movedPoint1);
//		Point expectedSnappedPoint1 = project.getSnapped(new Point(11, 11));
//		assertEquals("selected bend point not moved?", snapped1, expectedSnappedPoint1);
//		
//		Point movedPoint2 = movedBendPoints.get(selectionIndexes[1]);
//		Point snapped2 = project.getSnapped(movedPoint2);
//		Point expectedSnappedPoint2 = project.getSnapped(new Point(12, 12));
//		assertEquals("selected bend point not moved?", snapped2, expectedSnappedPoint2);
	}

	public static PointList createBendPointList()
	{
		PointList bendPoints = new PointList();
		
		bendPoints.add(new Point(1, 1));
		bendPoints.add(new Point(2, 2));
		bendPoints.add(new Point(3, 3));
		
		return bendPoints;
	}
	
	ProjectForTesting project;

}
