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
package org.miradi.objects;

import java.util.Vector;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.Project;

public class TestDiagramObject extends ObjectTestCase
{

	public TestDiagramObject(String name)
	{
		super(name);
	}
	
	public void testAreDiagramFactorsLinkedFromToNonBidirectional() throws Exception
	{
		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor target = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramObject diagramObject = getProject().getTestingDiagramObject();
		assertFalse("link does exist?", diagramObject.areDiagramFactorsLinkedFromToNonBidirectional(cause.getRef(), target.getRef()));
		
		BaseId diagramLinkId = getProject().createDiagramFactorLink(cause, target);
		ORef diagramLinkRef = new ORef(DiagramLink.getObjectType(), diagramLinkId);
		getProject().addDiagramLinkToModel(diagramLinkId);
		assertTrue("link does not exist?", diagramObject.areDiagramFactorsLinkedFromToNonBidirectional(cause.getRef(), target.getRef()));
		assertFalse("wrong direction appears linked?", diagramObject.areDiagramFactorsLinkedFromToNonBidirectional(target.getRef(), cause.getRef()));
		makeLinkBidirectional(getProject(), diagramLinkRef);
		assertFalse("Bidi appears linked?", diagramObject.areDiagramFactorsLinkedFromToNonBidirectional(cause.getRef(), target.getRef()));
		assertFalse("Bidi other direction appears linked?", diagramObject.areDiagramFactorsLinkedFromToNonBidirectional(target.getRef(), cause.getRef()));
		
		DiagramFactor nonLinkedCause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		assertFalse("link does exist?", diagramObject.areDiagramFactorsLinkedFromToNonBidirectional(nonLinkedCause.getRef(), target.getRef()));
		assertFalse("link does exist?", diagramObject.areDiagramFactorsLinkedFromToNonBidirectional(target.getRef(), nonLinkedCause.getRef()));
	}
	
	public static void makeLinkBidirectional(Project project, ORef diagramLinkRef) throws Exception
	{
		DiagramLink diagramLink = DiagramLink.find(project, diagramLinkRef);
		CommandSetObjectData cmd = new CommandSetObjectData(diagramLink, DiagramLink.TAG_IS_BIDIRECTIONAL_LINK, DiagramLink.BIDIRECTIONAL_LINK);
		project.executeCommand(cmd);
	}

	public void testFindReferrersOnSameDiagram() throws Exception
	{
		DiagramObject diagramObject = getProject().getTestingDiagramObject();
		ORef stressRef = getProject().createFactorAndReturnRef(Stress.getObjectType());
		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		
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
		DiagramObject diagramObject = getProject().getTestingDiagramModel().getDiagramObject();
		assertEquals("diagram was not empty", 0, diagramObject.getAllWrappedFactors().length);
		getProject().createAndAddFactorToDiagram(Cause.getObjectType());
		getProject().createAndAddFactorToDiagram(Target.getObjectType());
		
		Factor[] allWrappedFactors = diagramObject.getAllWrappedFactors();
		assertEquals("wrong factor count?", 2, allWrappedFactors.length);
		
		Vector<Integer> typesToFilterBy = new Vector<Integer>();
		typesToFilterBy.add(Target.getObjectType());
		
		Factor[] filteredFactors = diagramObject.getFactorsExcludingTypes(typesToFilterBy);
		assertEquals("wrong factor count?", 1, filteredFactors.length);
		
		Factor factor = filteredFactors[0];
		assertTrue("is not cause?", Cause.isFactor(factor));
	}
	
	public void testGetDiagramLinkByWrappedRef() throws Exception
	{
		DiagramObject diagramObject = getProject().getTestingDiagramModel().getDiagramObject();

		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor target = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramLink diagramLink = createLinkAndAddToDiagram(cause, target);

		assertEquals(diagramLink.getRef(), diagramObject.getDiagramLinkByWrappedRef(diagramLink.getWrappedRef()).getRef());
	}

	public void testAreLinkedEitherDirection() throws Exception
	{
		DiagramObject diagramObject = getProject().getTestingDiagramModel().getDiagramObject();

		DiagramFactor strategy = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor target = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		createLinkAndAddToDiagram(cause, target);
		
		assertFalse("strategy is linked to cause?", diagramObject.areLinkedEitherDirection(strategy.getWrappedORef(), cause.getWrappedORef()));
		assertTrue("cause not linked to target?", diagramObject.areLinkedEitherDirection(cause.getWrappedORef(), target.getWrappedORef()));
		assertTrue("target not linked to cause?", diagramObject.areLinkedEitherDirection(target.getWrappedORef(), cause.getWrappedORef()));
		
	}
	
	public void testGetDiagramLink() throws Exception
	{
		DiagramObject diagramObject = getProject().getTestingDiagramModel().getDiagramObject();

		DiagramFactor strategy = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor target = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramLink diagramLink = createLinkAndAddToDiagram(cause, target);
		
		assertNull("found a non-existant link?", diagramObject.getDiagramLink(strategy.getWrappedORef(), cause.getWrappedORef()));
		assertEquals("Didn't find normal link?", diagramLink.getRef(), diagramObject.getDiagramLink(cause.getWrappedORef(), target.getWrappedORef()).getRef());
		assertEquals("Didn't find normal link opposite direction?", diagramLink.getRef(), diagramObject.getDiagramLink(cause.getWrappedORef(), target.getWrappedORef()).getRef());
	}
	
	public void testGetDiagramLinkFromDiagramFactors() throws Exception
	{
		DiagramObject diagramObject = getProject().getTestingDiagramModel().getDiagramObject();

		DiagramFactor strategy = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor target = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramLink diagramLink = createLinkAndAddToDiagram(cause, target);
		
		assertNull("found a non-existant link?", getDiagramLinkRefFromDiagramFactors(diagramObject, strategy, cause));
		assertEquals("Didn't find normal link?", diagramLink.getRef(), getDiagramLinkRefFromDiagramFactors(diagramObject, cause, target));
		assertEquals("Didn't find normal link opposite direction?", diagramLink.getRef(), getDiagramLinkRefFromDiagramFactors(diagramObject, cause, target));
	}

	private ORef getDiagramLinkRefFromDiagramFactors(DiagramObject diagramObject, DiagramFactor strategy, DiagramFactor cause)
	{
		ORefList diagramLinkRefs = diagramObject.getDiagramLinkFromDiagramFactors(strategy.getRef(), cause.getRef());
		if(diagramLinkRefs.size() == 0)
			return null;
		if(diagramLinkRefs.size() > 1)
			throw new RuntimeException("Found multiple links");
		return diagramLinkRefs.get(0);
	}
	
	private DiagramLink createLinkAndAddToDiagram(DiagramFactor cause, DiagramFactor target) throws Exception
	{
		BaseId diagramLinkId = getProject().createDiagramFactorLink(cause, target);
		ORef diagramLinkRef = new ORef(DiagramLink.getObjectType(), diagramLinkId);
		DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRef);
		getProject().addDiagramLinkToModel(diagramLink.getId());
		return diagramLink;
	}
	
}
