/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.questions;

import java.util.Vector;

import org.miradi.objects.DiagramObject;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Stress;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;

public class FilteredDiagramFactorsQuestion extends ObjectQuestion
{
	public FilteredDiagramFactorsQuestion(DiagramObject diagramObject)
	{
		super(diagramObject.getFilteredWrappedFactors(getTypesWithSpecialProperties()));
	}
	
	private static Vector<Integer> getTypesWithSpecialProperties()
	{
		Vector<Integer> typesToExclude = new Vector();
		typesToExclude.add(TextBox.getObjectType());
		typesToExclude.add(GroupBox.getObjectType());
		typesToExclude.add(Stress.getObjectType());
		typesToExclude.add(Task.getObjectType());
		
		return typesToExclude;
	}
}
