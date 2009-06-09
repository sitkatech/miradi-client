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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;

public class TestMeglerArranger extends TestCaseWithProject
{
	public TestMeglerArranger(String name)
	{
		super(name);
	}

	public void testOneOfEachType() throws Exception
	{
		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramFactor threatDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		ORef threatRef = threatDiagramFactor.getWrappedORef();
		CommandSetObjectData toThreat = new CommandSetObjectData(threatRef, Cause.TAG_IS_DIRECT_THREAT, BooleanData.BOOLEAN_TRUE);
		getProject().executeCommand(toThreat);
		DiagramFactor strategyDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		MeglerArranger arranger = new MeglerArranger(getProject().getMainDiagramObject());
		arranger.arrange();
		
		assertEquals("Strategy not in column 0?", 30, strategyDiagramFactor.getLocation().x);
		assertEquals("Threat not in column 1?", 180, threatDiagramFactor.getLocation().x);
		assertEquals("Target not in column 2?", 330, targetDiagramFactor.getLocation().x);
		
		assertEquals("Strategy not at top?", 30, strategyDiagramFactor.getLocation().y);
		assertEquals("Threat not at top?", 30, threatDiagramFactor.getLocation().y);
		assertEquals("Target not at top?", 30, targetDiagramFactor.getLocation().y);
	}
}
