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

package org.miradi.diagram.arranger;

import java.util.Set;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.UnexpectedNonSideEffectException;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.TestDiagramObject;

public class TestMeglerArranger extends TestCaseWithProject
{
	public TestMeglerArranger(String name)
	{
		super(name);
	}

	public void testHorizontalColumns() throws Exception
	{
		DiagramFactor targetDiagramFactor = createTarget();
		DiagramFactor threatDiagramFactor = createThreat();
		DiagramFactor strategyDiagramFactor = createStrategy();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor, threatDiagramFactor);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor, targetDiagramFactor);

		MeglerArranger arranger = new MeglerArranger(getProject().getMainDiagramObject());
		arranger.arrange();
		
		assertEquals("Strategy not in column 1?", 330, strategyDiagramFactor.getLocation().x);
		assertEquals("Threat not in column 2?", 630, threatDiagramFactor.getLocation().x);
		assertEquals("Target not in column 3?", 930, targetDiagramFactor.getLocation().x);
		
		assertEquals("Strategy not at top?", 90, strategyDiagramFactor.getLocation().y);
		assertEquals("Threat not at top?", 90, threatDiagramFactor.getLocation().y);
		assertEquals("Target not at top?", 90, targetDiagramFactor.getLocation().y);
	}

	public void testVerticalSpacing() throws Exception
	{
		DiagramFactor strategyDiagramFactor1 = createStrategy();
		DiagramFactor strategyDiagramFactor2 = createStrategy();
		
		MeglerArranger arranger = new MeglerArranger(getProject().getMainDiagramObject());
		arranger.arrange();
		
		int y1 = strategyDiagramFactor1.getLocation().y;
		int y2 = strategyDiagramFactor2.getLocation().y;
		assertEquals("Strategies not spaced vertically?", 105, Math.abs(y1 - y2));
	}
	
	public void testLinklessAtFarLeft() throws Exception
	{
		DiagramFactor unlinkedTargetDiagramFactor = createTarget();
		DiagramFactor unlinkedThreatDiagramFactor = createThreat();
		DiagramFactor unlinkedStrategyDiagramFactor = createStrategy();
		
		DiagramFactor linkedTargetDiagramFactor = createTarget();
		DiagramFactor linkedThreatDiagramFactor = createThreat();
		DiagramFactor linkedStrategyDiagramFactor = createStrategy();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(linkedStrategyDiagramFactor, linkedThreatDiagramFactor);
		getProject().createDiagramFactorLinkAndAddToDiagram(linkedThreatDiagramFactor, linkedTargetDiagramFactor);
		
		MeglerArranger arranger = new MeglerArranger(getProject().getMainDiagramObject());
		arranger.arrange();
		
		assertEquals("unlinked strategy not in column 0?", 30, unlinkedStrategyDiagramFactor.getLocation().x);
		assertEquals("unlinked threat not in column 0?", 30, unlinkedThreatDiagramFactor.getLocation().x);
		assertEquals("unlinked target not in column 0?", 30, unlinkedTargetDiagramFactor.getLocation().x);
	}
	
	public void testOneObviousTargetGroup() throws Exception
	{
		DiagramFactor threatDiagramFactor = createThreat();
		DiagramFactor targetDiagramFactor1 = createTarget();
		DiagramFactor targetDiagramFactor2 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor, targetDiagramFactor2);

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();
		
		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Didn't create one group?", 1, groupBoxDiagramFactors.size());
		DiagramFactor groupBoxDiagramFactor = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[0];
		ORefList children = groupBoxDiagramFactor.getGroupBoxChildrenRefs();
		assertEquals("Didn't group both targets?", 2, children.size());
		assertTrue("Didn't group target1?", children.contains(targetDiagramFactor1.getRef()));
		assertTrue("Didn't group target2?", children.contains(targetDiagramFactor2.getRef()));
		
		assertTrue("Didn't create group link?", diagram.areDiagramFactorsLinkedFromToNonBidirectional(threatDiagramFactor.getRef(), groupBoxDiagramFactor.getRef()));
	}
	
	public void testOneGroupWithOneExcludedTarget() throws Exception
	{
		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();
		DiagramFactor targetDiagramFactor1 = createTarget();
		DiagramFactor targetDiagramFactor2 = createTarget();
		DiagramFactor targetDiagramFactor3 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, targetDiagramFactor2);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor2, targetDiagramFactor3);

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();

		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Didn't create one group?", 1, groupBoxDiagramFactors.size());
		ORefList children = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[0].getGroupBoxChildrenRefs();
		assertEquals("Didn't group top two targets?", 2, children.size());
		assertTrue("Didn't group target1?", children.contains(targetDiagramFactor1.getRef()));
		assertTrue("Didn't group target2?", children.contains(targetDiagramFactor2.getRef()));
	}

	public void testDontGroupIfBidirectionalLink() throws Exception
	{
		DiagramFactor threatDiagramFactor = createThreat();
		DiagramFactor targetDiagramFactor1 = createTarget();
		DiagramFactor targetDiagramFactor2 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor, targetDiagramFactor1);
		ORef factorLink2Ref = getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor, targetDiagramFactor2);
		TestDiagramObject.makeLinkBidirectional(getProject(), factorLink2Ref);
		
		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();
		
		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Created a group?", 0, groupBoxDiagramFactors.size());
	}
	
	public void testTwoTargetGroups() throws Exception
	{
		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();
		DiagramFactor targetDiagramFactor1 = createTarget();
		DiagramFactor targetDiagramFactor2 = createTarget();
		DiagramFactor targetDiagramFactor3 = createTarget();
		DiagramFactor targetDiagramFactor4 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, targetDiagramFactor2);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor2, targetDiagramFactor3);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor2, targetDiagramFactor4);

		ORefList targets1And2 = new ORefList(new DiagramFactor[] {targetDiagramFactor1, targetDiagramFactor2});
		ORefList targets3And4 = new ORefList(new DiagramFactor[] {targetDiagramFactor3, targetDiagramFactor4});

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();

		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Didn't create two groups?", 2, groupBoxDiagramFactors.size());
		DiagramFactor groupBoxDiagramFactor1 = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[0];
		ORefList children1 = groupBoxDiagramFactor1.getGroupBoxChildrenRefs();
		assertEquals("First group doesn't contain two targets?", 2, children1.size());
		DiagramFactor groupBoxDiagramFactor2 = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[1];
		ORefList children2 = groupBoxDiagramFactor2.getGroupBoxChildrenRefs();
		assertEquals("Second group doesn't contain two targets?", 2, children2.size());
		
		assertTrue("Didn't group target1 with target2?", children1.equals(targets1And2) || children2.equals(targets1And2));
		assertTrue("Didn't group target3 with target4?", children1.equals(targets3And4) || children2.equals(targets3And4));

		DiagramFactor targetGroup1 = getGroup(targetDiagramFactor1);
		DiagramFactor targetGroup2 = getGroup(targetDiagramFactor3);
		
		assertTrue("Didn't link threat 1 to group 1?", diagram.areDiagramFactorsLinkedFromToNonBidirectional(threatDiagramFactor1.getRef(), targetGroup1.getRef()));
		assertTrue("Didn't link threat 2 to group 2?", diagram.areDiagramFactorsLinkedFromToNonBidirectional(threatDiagramFactor2.getRef(), targetGroup2.getRef()));
	}
	
	public void testAvoidGroupConflicts() throws Exception
	{
		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();
		DiagramFactor targetDiagramFactor1 = createTarget();
		DiagramFactor targetDiagramFactor2 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, targetDiagramFactor2);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor2, targetDiagramFactor2);

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();
		
		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Didn't create one group?", 1, groupBoxDiagramFactors.size());
		DiagramFactor targetGroup = getGroup(targetDiagramFactor1);
		
		assertTrue("Didn't link threat 1 to group 1?", diagram.areDiagramFactorsLinkedFromToNonBidirectional(threatDiagramFactor1.getRef(), targetGroup.getRef()));
		assertTrue("linked threat 2 to 1 group 1?", diagram.areDiagramFactorsLinkedFromToNonBidirectional(threatDiagramFactor1.getRef(), targetGroup.getRef()));
	}

	public void testSkipAlreadyGroupedTargets() throws Exception
	{
		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();
		DiagramFactor targetDiagramFactor1 = createTarget();
		DiagramFactor targetDiagramFactor2 = createTarget();
		DiagramFactor targetDiagramFactor3 = createTarget();
		DiagramFactor targetDiagramFactor4 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, targetDiagramFactor2);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor2, targetDiagramFactor3);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor2, targetDiagramFactor4);

		ORefList targets1And2 = new ORefList(new DiagramFactor[] {targetDiagramFactor1, targetDiagramFactor2});
		ORefList targets3And4 = new ORefList(new DiagramFactor[] {targetDiagramFactor3, targetDiagramFactor4});

		DiagramFactor groupDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(GroupBox.getObjectType());
		CommandSetObjectData addChildren = new CommandSetObjectData(groupDiagramFactor.getRef(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, targets1And2.toString());
		getProject().executeCommand(addChildren);
		
		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();
		
		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Didn't create one new group?", 2, groupBoxDiagramFactors.size());
		DiagramFactor groupBoxDiagramFactor1 = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[0];
		ORefList children1 = groupBoxDiagramFactor1.getGroupBoxChildrenRefs();
		assertEquals("First group doesn't contain two targets?", 2, children1.size());
		DiagramFactor groupBoxDiagramFactor2 = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[1];
		ORefList children2 = groupBoxDiagramFactor2.getGroupBoxChildrenRefs();
		assertEquals("Second group doesn't contain two targets?", 2, children2.size());
		
		assertTrue("Didn't keep existing group?", children1.equals(targets1And2) || children2.equals(targets1And2));
		assertTrue("Didn't group target3 with target4?", children1.equals(targets3And4) || children2.equals(targets3And4));
	}
	
	public void testDontGroupTargetsUnlessLinkedByCauses() throws Exception
	{
		DiagramFactor strategyDiagramFactor1 = createStrategy();

		DiagramFactor targetDiagramFactor1 = createTarget();
		DiagramFactor targetDiagramFactor2 = createTarget();
		DiagramFactor targetDiagramFactor3 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(targetDiagramFactor3, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(targetDiagramFactor3, targetDiagramFactor2);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, targetDiagramFactor2);

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();
		
		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Created a group?", 0, groupBoxDiagramFactors.size());
	}
	
	public void testThreatGroupingToTargets() throws Exception
	{
		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();
		DiagramFactor threatDiagramFactor3 = createThreat();
		DiagramFactor threatDiagramFactor4 = createThreat();

		DiagramFactor targetDiagramFactor1 = createTarget();
		DiagramFactor targetDiagramFactor2 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor2, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor3, targetDiagramFactor2);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor4, targetDiagramFactor2);

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();

		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Didn't create two threat groups?", 2, groupBoxDiagramFactors.size());
		DiagramFactor groupBoxDiagramFactor1 = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[0];
		ORefSet children1 = new ORefSet(groupBoxDiagramFactor1.getGroupBoxChildrenRefs());
		assertEquals("First group doesn't contain two threats?", 2, children1.size());
		DiagramFactor groupBoxDiagramFactor2 = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[1];
		ORefSet children2 = new ORefSet(groupBoxDiagramFactor2.getGroupBoxChildrenRefs());
		assertEquals("Second group doesn't contain two threats?", 2, children2.size());

		ORefSet allGroupedThreats = new ORefSet(children1);
		allGroupedThreats.addAll(children2);
		assertContains("Threat 1 not in group?", threatDiagramFactor1.getRef(), allGroupedThreats);
		assertContains("Threat 2 not in group?", threatDiagramFactor2.getRef(), allGroupedThreats);
		assertContains("Threat 3 not in group?", threatDiagramFactor3.getRef(), allGroupedThreats);
		assertContains("Threat 4 not in group?", threatDiagramFactor4.getRef(), allGroupedThreats);
		
		DiagramFactor threatGroup1 = getGroup(threatDiagramFactor1);
		DiagramFactor threatGroup2 = getGroup(threatDiagramFactor3);
		
		assertTrue("Didn't link group 1 to target 1?", diagram.areDiagramFactorsLinkedFromToNonBidirectional(threatGroup1.getRef(), targetDiagramFactor1.getRef()));
		assertTrue("Didn't link group 2 to target 2?", diagram.areDiagramFactorsLinkedFromToNonBidirectional(threatGroup2.getRef(), targetDiagramFactor2.getRef()));
	}

	public void testThreatGroupingFromStrategies() throws Exception
	{
		DiagramFactor strategyDiagramFactor1 = createStrategy();
		
		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();

		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, threatDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, threatDiagramFactor2);

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();

		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Didn't create one threat group?", 1, groupBoxDiagramFactors.size());
		DiagramFactor groupBoxDiagramFactor1 = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[0];
		ORefSet children1 = new ORefSet(groupBoxDiagramFactor1.getGroupBoxChildrenRefs());
		assertEquals("Group doesn't contain two threats?", 2, children1.size());

		ORefSet allGroupedThreats = new ORefSet(children1);
		assertContains("Threat 1 not in group?", threatDiagramFactor1.getRef(), allGroupedThreats);
		assertContains("Threat 2 not in group?", threatDiagramFactor2.getRef(), allGroupedThreats);
	}
	
	public void testGroupThreatsBothDirections() throws Exception
	{
		DiagramFactor strategyDiagramFactor1 = createStrategy();
		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();
		DiagramFactor targetDiagramFactor1 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, threatDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, threatDiagramFactor2);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor2, targetDiagramFactor1);

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();
		
		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Created more than one group?", 1, groupBoxDiagramFactors.size());
	}

	public void testDontGroupThreatsUnlessLinkedToTargetsOrStrategies() throws Exception
	{
		DiagramFactor groupDiagramFactor1 = createEmptyGroupBox();

		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();
		DiagramFactor threatDiagramFactor3 = createThreat();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, threatDiagramFactor3);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor2, threatDiagramFactor3);
		getProject().createDiagramFactorLinkAndAddToDiagram(groupDiagramFactor1, threatDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(groupDiagramFactor1, threatDiagramFactor2);

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();
		
		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Created another group?", 1, groupBoxDiagramFactors.size());
	}
	
	public void testStrategyGrouping() throws Exception
	{
		DiagramFactor strategyDiagramFactor1 = createStrategy();
		DiagramFactor strategyDiagramFactor2 = createStrategy();
		DiagramFactor strategyDiagramFactor3 = createStrategy();
		DiagramFactor strategyDiagramFactor4 = createStrategy();
		
		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();

		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, threatDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor2, threatDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor3, threatDiagramFactor2);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor4, threatDiagramFactor2);

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();

		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Didn't create two strategy groups?", 2, groupBoxDiagramFactors.size());
		DiagramFactor groupBoxDiagramFactor1 = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[0];
		ORefSet children1 = new ORefSet(groupBoxDiagramFactor1.getGroupBoxChildrenRefs());
		assertEquals("First group doesn't contain two strategies?", 2, children1.size());
		DiagramFactor groupBoxDiagramFactor2 = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[1];
		ORefSet children2 = new ORefSet(groupBoxDiagramFactor2.getGroupBoxChildrenRefs());
		assertEquals("Second group doesn't contain two strategies?", 2, children2.size());

		ORefSet allGroupedStrategies= new ORefSet(children1);
		allGroupedStrategies.addAll(children2);
		assertContains("Strategy 1 not in group?", strategyDiagramFactor1.getRef(), allGroupedStrategies);
		assertContains("Strategy 2 not in group?", strategyDiagramFactor2.getRef(), allGroupedStrategies);
		assertContains("Strategy 3 not in group?", strategyDiagramFactor3.getRef(), allGroupedStrategies);
		assertContains("Strategy 4 not in group?", strategyDiagramFactor4.getRef(), allGroupedStrategies);

		DiagramFactor strategyGroup1 = getGroup(strategyDiagramFactor1);
		DiagramFactor strategyGroup2 = getGroup(strategyDiagramFactor3);
		
		assertTrue("Didn't link group 1 to threat 1?", diagram.areDiagramFactorsLinkedFromToNonBidirectional(strategyGroup1.getRef(), threatDiagramFactor1.getRef()));
		assertTrue("Didn't link group 2 to threat 2?", diagram.areDiagramFactorsLinkedFromToNonBidirectional(strategyGroup2.getRef(), threatDiagramFactor2.getRef()));
	}
	
	public void testDontGroupStrategiesUnlessLinkedToCauses() throws Exception
	{
		DiagramFactor strategyDiagramFactor1 = createStrategy();
		DiagramFactor strategyDiagramFactor2 = createStrategy();
		DiagramFactor strategyDiagramFactor3 = createStrategy();

		DiagramFactor targetDiagramFactor1 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, strategyDiagramFactor3);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor2, strategyDiagramFactor3);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor2, targetDiagramFactor1);

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();
		
		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Created a group?", 0, groupBoxDiagramFactors.size());
	}
	
	public void testArrangeThreatsVertically() throws Exception
	{
		DiagramFactor strategyDiagramFactor1 = createStrategy();
		DiagramFactor strategyDiagramFactor2 = createStrategy();
		DiagramFactor strategyDiagramFactor3 = createStrategy();

		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();

		DiagramFactor targetDiagramFactor1 = createTarget();
		DiagramFactor targetDiagramFactor2 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, threatDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor2, threatDiagramFactor2);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor3, threatDiagramFactor2);

		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor1, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor2, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor2, targetDiagramFactor2);
		
		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();
		
		int yThreat1 = threatDiagramFactor1.getLocation().y;
		int yThreat2 = threatDiagramFactor2.getLocation().y;
		assertTrue("threat 2 not first?", yThreat2 < yThreat1);

		int yStrategy1 = strategyDiagramFactor1.getLocation().y;
		int yStrategy2 = strategyDiagramFactor2.getLocation().y;
		int yStrategy3 = strategyDiagramFactor3.getLocation().y;
		assertTrue("strategy 2 not first?", yStrategy2 < yStrategy1 && yStrategy2 < yStrategy3);
	}
	
	public void testSortColumnByRelevantLinks() throws Exception
	{
		DiagramFactor strategyDiagramFactor1 = createStrategy();
		DiagramFactor strategyDiagramFactor2 = createStrategy();
		DiagramFactor strategyDiagramFactor3 = createStrategy();

		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();
		DiagramFactor threatDiagramFactor3 = createThreat();
		DiagramFactor threatDiagramFactor4 = createThreat();
		DiagramFactor threatDiagramFactor5 = createThreat();

		DiagramFactor targetDiagramFactor1 = createTarget();
		
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, threatDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor1, threatDiagramFactor2);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor2, threatDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor2, threatDiagramFactor2);

		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor3, threatDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor3, threatDiagramFactor2);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor3, threatDiagramFactor3);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor3, threatDiagramFactor4);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor3, threatDiagramFactor5);

		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor3, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor4, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor5, targetDiagramFactor1);
		
		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();
		
		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Didn't create two threat groups?", 2, groupBoxDiagramFactors.size());
		
		DiagramFactor groupBoxDiagramFactor1 = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[0];
		DiagramFactor groupBoxDiagramFactor2 = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[1];
		ORefSet children1 = new ORefSet(groupBoxDiagramFactor1.getGroupBoxChildrenRefs());
		ORefSet children2 = new ORefSet(groupBoxDiagramFactor2.getGroupBoxChildrenRefs());

		if(children1.size() != 3)
		{
			ORefSet temp = children1;
			children1 = children2;
			children2 = temp;
		}
		
		assertEquals("First group doesn't contain three threats?", 3, children1.size());
		assertContains("First group doesn't contain threat 3?", threatDiagramFactor3.getRef(), children1);
		assertContains("First group doesn't contain threat 4?", threatDiagramFactor4.getRef(), children1);
		assertContains("First group doesn't contain threat 5?", threatDiagramFactor5.getRef(), children1);
		assertEquals("Second group doesn't contain two threats?", 2, children2.size());
		assertContains("Second group doesn't contain threat 1?", threatDiagramFactor1.getRef(), children2);
		assertContains("Second group doesn't contain threat 2?", threatDiagramFactor2.getRef(), children2);
		
	}
	
	private DiagramFactor createStrategy() throws Exception
	{
		return getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
	}

	private DiagramFactor createThreat() throws Exception, UnexpectedNonSideEffectException, CommandFailedException
	{
		DiagramFactor threatDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		ORef threatRef = threatDiagramFactor.getWrappedORef();
		CommandSetObjectData toThreat = new CommandSetObjectData(threatRef, Cause.TAG_IS_DIRECT_THREAT, BooleanData.BOOLEAN_TRUE);
		getProject().executeCommand(toThreat);
		return threatDiagramFactor;
	}
	
	private DiagramFactor createTarget() throws Exception
	{
		return getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
	}

	private DiagramFactor createEmptyGroupBox() throws Exception
	{
		return getProject().createDiagramFactorAndAddToDiagram(GroupBox.getObjectType());
	}

	private DiagramFactor getGroup(DiagramFactor diagramfactor)
	{
		ORef groupRef = diagramfactor.findObjectsThatReferToUs(DiagramFactor.getObjectType()).get(0);
		DiagramFactor group = DiagramFactor.find(getProject(), groupRef);
		return group;
	}
	
}
