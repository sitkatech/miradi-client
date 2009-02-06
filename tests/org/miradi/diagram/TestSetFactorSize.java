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

import java.awt.Dimension;

import org.martus.util.TestCaseEnhanced;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.EnhancedJsonObject;

public class TestSetFactorSize extends TestCaseEnhanced 
{
	public TestSetFactorSize(String name)
	{
		super(name);
	}

	public void testSetNodeSize() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());

		FactorCell found = project.createFactorCell(ObjectType.TARGET);
		String newSize = EnhancedJsonObject.convertFromDimension(new Dimension(200,300));
		
		CommandSetObjectData cmd = new CommandSetObjectData(found.getDiagramFactorRef(), DiagramFactor.TAG_SIZE, newSize);
		project.executeCommand(cmd);
		
		DiagramFactor diagramFactor = found.getDiagramFactor();
		String foundSize = EnhancedJsonObject.convertFromDimension(diagramFactor.getSize());
		assertEquals("wrong size?", newSize, foundSize);
		
		project.close();
	}

}
