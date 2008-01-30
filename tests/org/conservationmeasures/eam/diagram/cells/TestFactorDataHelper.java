/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.cells;

import java.awt.Point;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.views.diagram.PointManipulater;

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
