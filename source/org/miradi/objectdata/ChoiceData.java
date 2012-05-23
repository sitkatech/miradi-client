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
package org.miradi.objectdata;

import org.miradi.objecthelpers.ORef;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.w3c.dom.Node;


public class ChoiceData extends StringData
{
	public ChoiceData(String tagToUse, ChoiceQuestion questionToUse)
	{
		super(tagToUse);
		
		question = questionToUse;
	}
	
	@Override
	public boolean isChoiceItemData()
	{
		return true;
	}
	
	@Override
	public ChoiceQuestion getChoiceQuestion()
	{
		return question;
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writeChoiceData(baseObjectSchema, fieldSchema, this);
	}
	
	@Override
	public void readAsXmpz2XmlData(Xmpz2XmlImporter importer, Node node, ORef destinationRefToUse, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		importer.importChoiceField(node, baseObjectSchema.getXmpz2ElementName(), destinationRefToUse, fieldSchema.getTag(), getChoiceQuestion());
	}

	private ChoiceQuestion question;
}
