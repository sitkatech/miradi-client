/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.objecthelpers;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.IntermediateResult;

public class TestOldToNewDiagramFactorMap extends TestCaseWithProject
{
	public TestOldToNewDiagramFactorMap(String name)
	{
		super(name);
	}
	
	public void testPut() throws Exception
	{
		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor intermediateResult = getProject().createAndAddFactorToDiagram(IntermediateResult.getObjectType());
		
		OldToNewDiagramFactorMap map = new OldToNewDiagramFactorMap();
		map.put(cause, intermediateResult);
		
		try
		{
			map.put(cause, intermediateResult);
			fail("putting duplicate key in map should have failed");
		}
		catch (Exception ignoreExpectedException)
		{
		}
	}
	
	public void testPutAll() throws Exception
	{
		DiagramFactor cause = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor intermediateResult = getProject().createAndAddFactorToDiagram(IntermediateResult.getObjectType());
		
		OldToNewDiagramFactorMap map1 = new OldToNewDiagramFactorMap();
		map1.put(cause, intermediateResult);
		
		OldToNewDiagramFactorMap map2 = new OldToNewDiagramFactorMap();
		map2.put(cause, intermediateResult);
		
		try
		{
			map1.putAll(map2);
			fail("putall should have failed due to duplicate key?");
		}
		catch (Exception ignoreExpectedException)
		{
		}
	}
}
