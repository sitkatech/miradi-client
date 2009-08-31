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

package org.miradi.xml.generic;

import java.io.IOException;
import java.util.Vector;


class ProjectSchemaElement extends SchemaElement
{
	public ProjectSchemaElement()
	{
		objectTypes = new Vector<ObjectSchemaElement>();
		
		objectTypes.add(new ProjectSummarySchemaElement());

		objectTypes.add(new ConceptualModelSchemaElement());
		objectTypes.add(new ResultsChainSchemaElement());
				
		objectTypes.add(new BiodiversityTargetObjectSchemaElement());
		objectTypes.add(new CauseObjectSchemaElement());
	}
	
	public void output(SchemaWriter writer) throws IOException
	{
		writer.defineAlias(getDotElement(getProjectElementName()), "element miradi:" + getProjectElementName());
		writer.startBlock();
		for(ObjectSchemaElement objectElement: objectTypes)
		{
			writer.printlnIndented(getDotElement(objectElement.getObjectTypeName()) + "&");
		}
		writer.endBlock();
		
		for(ObjectSchemaElement objectElement: objectTypes)
		{
			objectElement.output(writer);
		}
		
	}
	
	String getProjectElementName()
	{
		return "ConservationProject";
	}

	private Vector<ObjectSchemaElement> objectTypes;
}
