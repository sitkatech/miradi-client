/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.TestCaseWithProject;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.views.diagram.LinkCreator;


public class TestLinkCreator extends TestCaseWithProject
{
	public TestLinkCreator(String name)
	{
		super(name);
	}

	public void testCreateThreatStressRatingsFromTarget() throws Exception
	{
		LinkCreator linkCreator = new LinkCreator(getProject());
		ORef stressRef1 = getProject().createObject(Stress.getObjectType());
		ORef stressRef2 = getProject().createObject(Stress.getObjectType());
		ORefList stressRefList = new ORefList();
		stressRefList.add(stressRef1);
		stressRefList.add(stressRef2);
		
		DiagramFactor diagramTarget = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		CommandSetObjectData appendStressRefs = new CommandSetObjectData(diagramTarget.getWrappedORef(), Target.TAG_STRESS_REFS, stressRefList.toString());
		getProject().executeCommand(appendStressRefs);
		
		Target target = (Target) getProject().findObject(diagramTarget.getWrappedORef());
		assertEquals("wrong stress count?", 2, target.getStressRefs().size());
		
		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		ORef diagramLinkRef = getProject().createDiagramLinkAndAddToDiagram(cause, diagramTarget);
		DiagramLink diagramLink = (DiagramLink) getProject().findObject(diagramLinkRef);
	
		linkCreator.createAndAddThreatStressRatingsFromTarget(diagramLink.getWrappedRef(), diagramTarget.getWrappedORef());
		FactorLink factorLink = diagramLink.getUnderlyingLink();
		assertEquals("wrong threat stress rating count?", 2, factorLink.getThreatStressRatingRefs().size());
	}
}