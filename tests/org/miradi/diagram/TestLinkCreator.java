/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.views.diagram.LinkCreator;


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