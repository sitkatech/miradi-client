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
package org.miradi.diagram;

import java.util.Vector;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.objects.GroupBox;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ThreatStressRating;
import org.miradi.utils.ThreatStressRatingHelper;
import org.miradi.views.diagram.LinkCreator;


public class TestLinkCreator extends TestCaseWithProject
{
	public TestLinkCreator(String name)
	{
		super(name);
	}
	
	public void testIsValidLinakableType()
	{
		for (int type = ObjectType.FIRST_OBJECT_TYPE; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			if (Strategy.is(type) || Cause.is(type) || IntermediateResult.is(type) || ThreatReductionResult.is(type) || Target.is(type) || GroupBox.is(type))
				assertTrue("not a linkable type?", LinkCreator.isValidLinkableType(type));
			else
				assertFalse("is a linkable type?", LinkCreator.isValidLinkableType(type));
		}
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
		
		DiagramFactor causeDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		ORef diagramLinkRef = getProject().createDiagramLinkAndAddToDiagram(causeDiagramFactor, diagramTarget);
		DiagramLink diagramLink = (DiagramLink) getProject().findObject(diagramLinkRef);
	
		linkCreator.createAndAddThreatStressRatingsFromTarget(diagramLink.getWrappedRef(), diagramTarget.getWrappedORef());
		ThreatStressRatingHelper helper = new ThreatStressRatingHelper(getProject());
		Vector<ThreatStressRating> threatStressRatings = helper.getRelatedThreatStressRatings(causeDiagramFactor.getWrappedORef(), diagramTarget.getWrappedORef());
		assertEquals("wrong threat stress rating count?", 2, threatStressRatings.size());
	}
	
	public void testAreGroupBoxOwnedFactorsLinked() throws Exception
	{
		DiagramFactor fromDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor toDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		
		LinkCreator linkCreator = new LinkCreator(getProject());
		assertFalse("diagram factors are not linked?", linkCreator.areGroupBoxOwnedFactorsLinked(getDiagramModel(), fromDiagramFactor, toDiagramFactor));
		
		DiagramFactor groupBoxDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(GroupBox.getObjectType());
		ORefList groupBoxChildrenRefs = new ORefList(fromDiagramFactor.getRef());
		getProject().setObjectData(groupBoxDiagramFactor.getRef(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, groupBoxChildrenRefs.toString());
		assertTrue("diagramFactor is not groupBox?", groupBoxDiagramFactor.isGroupBoxFactor());
		
		getProject().createDiagramLinkAndAddToDiagram(groupBoxDiagramFactor, toDiagramFactor);
		getDiagramModel().fillFrom(getProject().getTestingDiagramObject());
		assertEquals("model has wrong link count?", 1, getDiagramModel().getAllDiagramFactorLinks().size());
		
		assertTrue("factors are not linked?", getDiagramModel().areLinked(groupBoxDiagramFactor.getWrappedORef(), toDiagramFactor.getWrappedORef()));
		
		assertTrue("child of groupBox covered cause is not linked to target diagramFactor?", linkCreator.areGroupBoxOwnedFactorsLinked(getDiagramModel(), toDiagramFactor, groupBoxDiagramFactor));
		assertTrue("from is not linked to to diagramFactor?", linkCreator.areGroupBoxOwnedFactorsLinked(getDiagramModel(), fromDiagramFactor, toDiagramFactor));
	}
	
	public void testSplitSelectedLinkToIncludeFactor() throws Exception
	{
		verifyNonGroupBoxLinkedDiagramFactors();
		verifyGroupBoxLinkedDiagramFactors();
	}

	private void verifyGroupBoxLinkedDiagramFactors() throws Exception
	{
		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor groupBoxDiagramFactor = getProject().createAndPopulateDiagramFactorGroupBox(cause);
		DiagramFactor strategy = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		
		ORef groupBoxDiagramLinkRef = getProject().createDiagramLinkAndAddToDiagram(groupBoxDiagramFactor, strategy);
		DiagramLink groupBoxDiagramLink = DiagramLink.find(getProject(), groupBoxDiagramLinkRef);
		ORefList groupBoxChildrenDiagramLinkRefs = groupBoxDiagramLink.getGroupedDiagramLinkRefs();
		assertTrue("is not groupbox link?", groupBoxDiagramLink.isGroupBoxLink());
		LinkCreator linkCreator = new LinkCreator(getProject());
	
		DiagramFactor newCauseToBeLinkedIn = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		linkCreator.splitSelectedLinkToIncludeFactor(getDiagramModel(), groupBoxDiagramLink, newCauseToBeLinkedIn);
		
		DiagramLink deletedDiagramLink = DiagramLink.find(getProject(), groupBoxDiagramLinkRef);
		assertNull("diagram link was not deleted?", deletedDiagramLink);
		
		for (int index = 0; index < groupBoxChildrenDiagramLinkRefs.size(); ++index)
		{
			DiagramLink childDiagramLink = DiagramLink.find(getProject(), groupBoxChildrenDiagramLinkRefs.get(index));
			assertNull("groupbox child not deleted?", childDiagramLink);
		}
		
		assertTrue("diagram factors are not linked?" , getProject().areDiagramFactorsLinked(cause.getRef(), newCauseToBeLinkedIn.getRef()));
		assertTrue("diagram factors are not linked?" , getProject().areDiagramFactorsLinked(newCauseToBeLinkedIn.getRef(), strategy.getRef()));
	}

	private void verifyNonGroupBoxLinkedDiagramFactors() throws Exception
	{
		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor strategy = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		ORef diagramLinkRef = getProject().createDiagramLinkAndAddToDiagram(cause, strategy);
		DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRef);
		ORef factorLinkRef = diagramLink.getWrappedRef();
		LinkCreator linkCreator = new LinkCreator(getProject());
		
		DiagramFactor newCauseToBeLinkedIn = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		linkCreator.splitSelectedLinkToIncludeFactor(getDiagramModel(), diagramLink, newCauseToBeLinkedIn);
		
		DiagramLink deletedDiagramLink = DiagramLink.find(getProject(), diagramLinkRef);
		assertNull("diagram link was not deleted?", deletedDiagramLink);
		
		FactorLink factorLink = FactorLink.find(getProject(), factorLinkRef);
		assertNull("factor link was not deleted?", factorLink);
		
		assertTrue("diagram factors are not linked?" , getProject().areDiagramFactorsLinked(cause.getRef(), newCauseToBeLinkedIn.getRef()));
		assertTrue("diagram factors are not linked?" , getProject().areDiagramFactorsLinked(newCauseToBeLinkedIn.getRef(), strategy.getRef()));
	}
}