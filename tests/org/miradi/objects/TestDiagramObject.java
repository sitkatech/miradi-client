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
package org.miradi.objects;

import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.ProjectForTesting;

public class TestDiagramObject extends ObjectTestCase
{

	public TestDiagramObject(String name)
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

	public void testAreDiagramFactorsLinked() throws Exception
	{
		DiagramFactor cause = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor target = project.createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramObject diagramObject = project.getTestingDiagramObject();
		assertFalse("link does exist?", diagramObject.areDiagramFactorsLinked(cause.getDiagramFactorId(), target.getDiagramFactorId()));
		
		BaseId diagramLinkId = project.createDiagramFactorLink(cause, target);
		project.addDiagramLinkToModel(diagramLinkId);
		assertTrue("link does not exist?", diagramObject.areDiagramFactorsLinked(cause.getDiagramFactorId(), target.getDiagramFactorId()));
		assertTrue("link does not exist?", diagramObject.areDiagramFactorsLinked(target.getDiagramFactorId(), cause.getDiagramFactorId()));
		
		DiagramFactor nonLinkedCause = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		assertFalse("link does exist?", diagramObject.areDiagramFactorsLinked(nonLinkedCause.getDiagramFactorId(), target.getDiagramFactorId()));
		assertFalse("link does exist?", diagramObject.areDiagramFactorsLinked(target.getDiagramFactorId(), nonLinkedCause.getDiagramFactorId()));
	}
	
	public void testFindReferrersOnSameDiagram() throws Exception
	{
		DiagramObject diagramObject = project.getTestingDiagramObject();
		ORef stressRef = project.createFactorAndReturnRef(Stress.getObjectType());
		DiagramFactor targetDiagramFactor = project.createDiagramFactorAndAddToDiagram(Target.getObjectType());
		
		ORefList foundTargetReferrerRefs1 = diagramObject.findReferrersOnSameDiagram(stressRef, Target.getObjectType());
		assertEquals("has referrers?", 0, foundTargetReferrerRefs1.size());
		
		ORefList stressRefs = new ORefList(stressRef);
		Target target = (Target) targetDiagramFactor.getWrappedFactor();
		target.setData(Target.TAG_STRESS_REFS, stressRefs.toString());
		ORefList foundTargetReferrerRefs2 = diagramObject.findReferrersOnSameDiagram(stressRef, Target.getObjectType());
		assertEquals("has no referrers?", 1, foundTargetReferrerRefs2.size());
	}
	
	public void testGetFilteredWrappedFactors() throws Exception
	{
		DiagramObject diagramObject = getProject().getDiagramModel().getDiagramObject();
		assertEquals("diagram was not empty", 0, diagramObject.getAllWrappedFactors().length);
		getProject().createAndAddFactorToDiagram(Cause.getObjectType());
		getProject().createAndAddFactorToDiagram(Target.getObjectType());
		
		Factor[] allWrappedFactors = diagramObject.getAllWrappedFactors();
		assertEquals("wrong factor count?", 2, allWrappedFactors.length);
		
		Vector<Integer> typesToFilterBy = new Vector();
		typesToFilterBy.add(Target.getObjectType());
		
		Factor[] filteredFactors = diagramObject.getFactorsExcludingTypes(typesToFilterBy);
		assertEquals("wrong factor count?", 1, filteredFactors.length);
		
		Factor factor = filteredFactors[0];
		assertTrue("is not cause?", Cause.isFactor(factor));
	}
	
	ProjectForTesting project;
}
