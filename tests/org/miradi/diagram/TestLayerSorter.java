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
package org.miradi.diagram;

import java.util.Collections;
import java.util.List;

import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.GroupBox;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.GroupBoxSchema;
import org.miradi.schemas.ScopeBoxSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.views.diagram.LayerSorter;

public class TestLayerSorter extends TestCaseWithProject
{
	public TestLayerSorter(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		getProject().createDiagramFactorAndAddToDiagram(ScopeBoxSchema.getObjectType());
		getProject().createDiagramFactorAndAddToDiagram(StrategySchema.getObjectType());
		getProject().createDiagramFactorAndAddToDiagram(GroupBoxSchema.getObjectType());
		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(TargetSchema.getObjectType());
		DiagramFactor causeDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());
		getProject().createDiagramFactorAndAddToDiagram(GroupBoxSchema.getObjectType());
		ORef diagramLinkRef = getProject().createDiagramLink(targetDiagramFactor, causeDiagramFactor);
		DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRef);
		getProject().addDiagramLinkToModel(diagramLink);
		
		getProject().createDiagramFactorAndAddToDiagram(StressSchema.getObjectType());
		
		List<EAMGraphCell> orderedCells = getProject().getTestingDiagramModel().getAllRootCells();	
		Collections.sort(orderedCells, new LayerSorter());
		assertEquals("wrong cell count?", 8, orderedCells.size());
		
		assertTrue("element 0 is not scopbox", orderedCells.get(0).isScopeBox());
		assertTrue("element 1 is not groupbox", isGroupBox(orderedCells.get(1).getWrappedFactorRef()));
		assertTrue("element 2 is not groupbox", isGroupBox(orderedCells.get(2).getWrappedFactorRef()));
		assertTrue("element 3 is not factor", orderedCells.get(3).isFactor());
		assertTrue("element 4 is not factor", orderedCells.get(4).isFactor());
		assertTrue("element 6 is not factor", orderedCells.get(5).isFactor());
		assertTrue("element 5 is not factor link", orderedCells.get(6).isFactorLink());
		
		assertTrue("element 6 is not factor", orderedCells.get(7).isFactor());
	}

	private boolean isGroupBox(ORef ref)
	{
		return GroupBox.is(ref);
	}
}
