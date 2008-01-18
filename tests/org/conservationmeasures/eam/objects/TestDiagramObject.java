/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.ProjectForTesting;

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
	}

	public void testAreDiagramFactorsLinked() throws Exception
	{
		DiagramFactor cause = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor target = project.createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramObject diagramObject = project.getDiagramObject();
		assertFalse("link does exist?", diagramObject.areDiagramFactorsLinked(cause.getDiagramFactorId(), target.getDiagramFactorId()));
		
		BaseId diagramLinkId = project.createDiagramFactorLink(cause, target);
		project.addDiagramLinkToModel(diagramLinkId);
		assertTrue("link does not exist?", diagramObject.areDiagramFactorsLinked(cause.getDiagramFactorId(), target.getDiagramFactorId()));
		assertTrue("link does not exist?", diagramObject.areDiagramFactorsLinked(target.getDiagramFactorId(), cause.getDiagramFactorId()));
		
		DiagramFactor nonLinkedCause = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		assertFalse("link does exist?", diagramObject.areDiagramFactorsLinked(nonLinkedCause.getDiagramFactorId(), target.getDiagramFactorId()));
		assertFalse("link does exist?", diagramObject.areDiagramFactorsLinked(target.getDiagramFactorId(), nonLinkedCause.getDiagramFactorId()));
	}
	
	ProjectForTesting project;
}
