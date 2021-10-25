/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.objects.BaseObject;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class PseudoQuestionData  extends ObjectData
{
	public PseudoQuestionData(BaseObject owningObject, String tagToUse)
	{
		super(tagToUse);

		object = owningObject;
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
	public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writePseudoQuestionData(baseObjectSchema, fieldSchema, get());
	}

	private BaseObject object;
}