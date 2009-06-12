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
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;

public class TestMeglerArranger extends TestCaseWithProject
{
	public TestMeglerArranger(String name)
	{
		super(name);
	}

	public void testHorizontalColumns() throws Exception
	{
		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramFactor threatDiagramFactor = createThreat();
		DiagramFactor strategyDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor, threatDiagramFactor);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor, targetDiagramFactor);

		MeglerArranger arranger = new MeglerArranger(getProject().getMainDiagramObject());
		arranger.arrange();
		
		assertEquals("Strategy not in column 1?", 180, strategyDiagramFactor.getLocation().x);
		assertEquals("Threat not in column 2?", 330, threatDiagramFactor.getLocation().x);
		assertEquals("Target not in column 3?", 480, targetDiagramFactor.getLocation().x);
		
		assertEquals("Strategy not at top?", 30, strategyDiagramFactor.getLocation().y);
		assertEquals("Threat not at top?", 30, threatDiagramFactor.getLocation().y);
		assertEquals("Target not at top?", 30, targetDiagramFactor.getLocation().y);
	}

	public void testVerticalSpacing() throws Exception
	{
		DiagramFactor strategyDiagramFactor1 = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		DiagramFactor strategyDiagramFactor2 = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		
		MeglerArranger arranger = new MeglerArranger(getProject().getMainDiagramObject());
		arranger.arrange();
		
		int y1 = strategyDiagramFactor1.getLocation().y;
		int y2 = strategyDiagramFactor2.getLocation().y;
		assertEquals("Strategies not spaced vertically?", 90, Math.abs(y1 - y2));
	}
	
	public void testLinklessAtFarLeft() throws Exception
	{
		DiagramFactor unlinkedTargetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramFactor unlinkedThreatDiagramFactor = createThreat();
		DiagramFactor unlinkedStrategyDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		
		DiagramFactor linkedTargetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramFactor linkedThreatDiagramFactor = createThreat();
		DiagramFactor linkedStrategyDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		
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
		DiagramFactor targetDiagramFactor1 = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramFactor targetDiagramFactor2 = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor, targetDiagramFactor1);
		getProject().createDiagramFactorLinkAndAddToDiagram(threatDiagramFactor, targetDiagramFactor2);

		DiagramObject diagram = getProject().getMainDiagramObject();
		MeglerArranger arranger = new MeglerArranger(diagram);
		arranger.arrange();
		
		Set<DiagramFactor> groupBoxDiagramFactors = diagram.getDiagramFactorsThatWrap(GroupBox.getObjectType());
		assertEquals("Didn't create one group?", 1, groupBoxDiagramFactors.size());
		ORefList children = groupBoxDiagramFactors.toArray(new DiagramFactor[0])[0].getGroupBoxChildrenRefs();
		assertEquals("Didn't group both targets?", 2, children.size());
		assertTrue("Didn't group target1?", children.contains(targetDiagramFactor1.getRef()));
		assertTrue("Didn't group target2?", children.contains(targetDiagramFactor2.getRef()));
		
	}
	
	public void testOneGroupWithOneExcludedTarget() throws Exception
	{
		DiagramFactor threatDiagramFactor1 = createThreat();
		DiagramFactor threatDiagramFactor2 = createThreat();
		DiagramFactor targetDiagramFactor1 = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramFactor targetDiagramFactor2 = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramFactor targetDiagramFactor3 = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		
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

	private DiagramFactor createThreat() throws Exception, UnexpectedNonSideEffectException, CommandFailedException
	{
		DiagramFactor threatDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		ORef threatRef = threatDiagramFactor.getWrappedORef();
		CommandSetObjectData toThreat = new CommandSetObjectData(threatRef, Cause.TAG_IS_DIRECT_THREAT, BooleanData.BOOLEAN_TRUE);
		getProject().executeCommand(toThreat);
		return threatDiagramFactor;
	}
	
}
