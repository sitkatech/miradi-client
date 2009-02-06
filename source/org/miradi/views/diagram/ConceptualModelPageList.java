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
package org.miradi.views.diagram;

import org.miradi.dialogs.diagram.ConceptualModelPoolTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ConceptualModelDiagram;


public class ConceptualModelPageList extends DiagramPageList
{
	public ConceptualModelPageList(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, new ConceptualModelPoolTableModel(mainWindowToUse.getProject(), ObjectType.CONCEPTUAL_MODEL_DIAGRAM, getTags()), UNIQUE_IDENTIFIER);
	}

	private static String[] getTags()
	{
		return new String[] {ConceptualModelDiagram.PSEUDO_COMBINED_LABEL};
	}
	
	public boolean isConceptualModelPageList()
	{
		return true;
	}

	public boolean isResultsChainPageList()
	{
		return false;
	}

	public int getManagedDiagramType()
	{
		return ObjectType.CONCEPTUAL_MODEL_DIAGRAM;
	}
	
	public static final String UNIQUE_IDENTIFIER = "ConceptualModelPageList";
}
