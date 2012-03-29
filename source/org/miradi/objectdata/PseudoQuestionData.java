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

package org.miradi.objectdata;

import java.util.HashSet;

import org.miradi.objects.BaseObject;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlUnicodeWriter;

public class PseudoQuestionData  extends ObjectData
{
	public PseudoQuestionData(BaseObject owningObject, String tagToUse)
	{
		super(tagToUse);

		object = owningObject;
	}
	
	// TODO: Do we really need this dependencyTags, and if so can it be simplified?
	// It seems like the only user passes a single tag
	public PseudoQuestionData(BaseObject owningObject, String tagToUse, HashSet<String> dependencyTagsToUse)
	{
		super(tagToUse, dependencyTagsToUse);
		
		object = owningObject;
	}
	
	@Override
	public boolean isPseudoField()
	{
		return true;
	}
	
	@Override
	public void set(String newValue) throws Exception
	{
	}

	@Override
	public String get()
	{
		return object.getPseudoData(getTag());
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof PseudoQuestionData))
			return false;
		
		PseudoQuestionData other = (PseudoQuestionData)rawOther;
		return get().equals(other.get());
	}

	@Override
	public int hashCode()
	{
		return get().hashCode();
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlUnicodeWriter writer, BaseObjectSchema schema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writePseudoQuetionData(schema, fieldSchema, get());
	}

	private BaseObject object;
}