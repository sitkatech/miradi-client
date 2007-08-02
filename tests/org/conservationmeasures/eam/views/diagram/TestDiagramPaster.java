/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.util.HashMap;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.main.TransferableMiradiList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestDiagramPaster extends EAMTestCase
{
	public TestDiagramPaster(String name)
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
		project.close();
		super.tearDown();
	}

	public void testFixupAllIndicatorRefs() throws Exception
	{
		fixupRefs(Cause.getObjectType(), Indicator.getObjectType(), Factor.TAG_INDICATOR_IDS);
		fixupRefs(Cause.getObjectType(), Objective.getObjectType(), Factor.TAG_OBJECTIVE_IDS);
		fixupRefs(Cause.getObjectType(), Goal.getObjectType(), Factor.TAG_GOAL_IDS);
		fixupRefs(Cause.getObjectType(), KeyEcologicalAttribute.getObjectType(), Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
	}
		
	public void fixupRefs(int factorType, int annotationType, String annotationFactorTag) throws Exception
	{
		ORef factorRef = project.createFactorAndReturnRef(factorType);
		ORef oldRef1 = project.createObject(annotationType);
		ORef oldRef2 = project.createObject(annotationType);
		
		ORef newRef1 = project.createObject(annotationType);
		ORef newRef2 = project.createObject(annotationType);
		
		IdList annotationIds = new IdList(annotationType);
		annotationIds.addRef(oldRef1);
		annotationIds.addRef(oldRef2);
		
		CommandSetObjectData setFactorAnnotationIds = new CommandSetObjectData(factorRef, annotationFactorTag, annotationIds.toString());
		project.executeCommand(setFactorAnnotationIds);
		
		HashMap oldToNewRefMap = new HashMap();
		oldToNewRefMap.put(oldRef1, newRef1);
		oldToNewRefMap.put(oldRef2, newRef2);
		
		TransferableMiradiList transferableList = new TransferableMiradiList(project);
		Factor factor = (Factor) project.findObject(factorRef);
		DiagramPaster paster = new DiagramPaster(project.getDiagramModel(), transferableList);
		Command[] commandToFixRefs = paster.createCommandToFixupRefLists(factor, oldToNewRefMap);
		project.executeCommands(commandToFixRefs);
		
		IdList newAnnotationIds = new IdList(annotationType, factor.getData(annotationFactorTag));
		assertFalse("contains wrong old id?", newAnnotationIds.contains(oldRef1));
		assertFalse("contains wrong old id?", newAnnotationIds.contains(oldRef2));
		
		assertTrue("does not contain new id?", newAnnotationIds.contains(newRef1));
		assertTrue("does not contain new id?", newAnnotationIds.contains(newRef2));
	}

	ProjectForTesting project;
}
