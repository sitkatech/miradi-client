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

package org.miradi.diagram.cells;

import java.awt.Point;

import org.miradi.main.EAMTestCase;
import org.miradi.project.ProjectForTesting;
import org.miradi.views.diagram.PointManipulater;

//TODO rename and move to correct package (also when moving to new package, update the code inside
// the main test suite)
public class TestFactorDataHelper extends EAMTestCase 
{
	public TestFactorDataHelper(String name)
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

	public void testGetNewLocation()
	{
		Point insertLocation = new Point(500, 500);
		Point diagramFactor1Location = new Point(100, 100);
		Point diagramFactor2Location = new Point(200, 200);
		Point upperMostLeftMostCorner = new Point(100, 100);
		PointManipulater helper = new PointManipulater(insertLocation, upperMostLeftMostCorner);
		
		Point newDiagramFactor1Location = helper.getNewLocation(diagramFactor1Location);
		assertEquals("wrong location for diagram factor 1?", insertLocation, newDiagramFactor1Location);
		
		Point newDiagramFactor2Location = helper.getNewLocation(diagramFactor2Location);
		assertEquals("wrong location for diagram factor 2?", new Point(600, 600), newDiagramFactor2Location); 
	}
	
	public void testGetSnappedTranslatedPoint()
	{
		int offset = 9;
		Point insertLocation = new Point(500, 500);
		Point diagramFactor1Location = new Point(100, 100);
		Point upperMostLeftMostCorner = new Point(100, 100);
		
		PointManipulater helper = new PointManipulater(insertLocation, upperMostLeftMostCorner);
		Point translatedSnappedOffsettedPoint1 = helper.getSnappedTranslatedPoint(project, diagramFactor1Location, offset);
		assertEquals("wrong location for diagram factor 1?", new Point(510, 510), translatedSnappedOffsettedPoint1);
	}
	
	ProjectForTesting project;
}
