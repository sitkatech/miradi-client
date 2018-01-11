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

package org.miradi.diagram.cells;

import java.awt.Point;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Target;
import org.miradi.project.FactorDeleteHelper;
import org.miradi.schemas.TargetSchema;
import org.miradi.views.diagram.PointManipulator;

//TODO rename and move to correct package (also when moving to new package, update the code inside
// the main test suite)
public class TestFactorDataHelper extends TestCaseWithProject 
{
	public TestFactorDataHelper(String name)
	{
		super(name);
	}
	
	public void testGetNewLocation()
	{
		Point insertLocation = new Point(500, 500);
		Point diagramFactor1Location = new Point(100, 100);
		Point diagramFactor2Location = new Point(200, 200);
		Point upperMostLeftMostCorner = new Point(100, 100);
		PointManipulator helper = new PointManipulator(insertLocation, upperMostLeftMostCorner);
		
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
		
		PointManipulator helper = new PointManipulator(insertLocation, upperMostLeftMostCorner);
		Point translatedSnappedOffsettedPoint1 = helper.getSnappedTranslatedPoint(getProject(), diagramFactor1Location, offset);
		assertEquals("wrong location for diagram factor 1?", new Point(510, 510), translatedSnappedOffsettedPoint1);
	}
	
	public void testDeleteTargetIndicatorsWhileInTncMode() throws Exception
	{
		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(TargetSchema.getObjectType());
		Target target = Target.find(getProject(), targetDiagramFactor.getWrappedORef());
		getProject().createIndicator(target);
		getProject().turnOnTncMode(target);
		
		FactorDeleteHelper deleteHelper = FactorDeleteHelper.createFactorDeleteHelperForNonSelectedFactors(getProject().getMainDiagramObject());
		assertEquals("incorrect number of indictors?", 1, getProject().getIndicatorPool().size());
		
		deleteHelper.deleteAnnotations(target);
		assertEquals("indicator was not deleted?", 0, getProject().getIndicatorPool().size());
		
		assertEquals("target should not have indictors?", 0, target.getOnlyDirectIndicatorRefs().size());
	}
}
