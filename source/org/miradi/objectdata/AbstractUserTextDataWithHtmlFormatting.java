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

import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.HtmlUtilitiesRelatedToShef;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;

public class AbstractUserTextDataWithHtmlFormatting extends UserTextData
{
	public AbstractUserTextDataWithHtmlFormatting(String tagToUse)
	{
		super(tagToUse);
	}

	@Override
	public boolean isUserTextWithHtmlFormatting()
	{
		return true;
	}
	
	@Override
	public void set(String newValue) throws Exception
	{
		super.set(HtmlUtilitiesRelatedToShef.getNormalizedAndSanitizedHtmlText(newValue, getAllowedHtmlTags()));
	}
	
	@Override
	public boolean isCurrentValue(String text)
	{
		String currentValue = HtmlUtilitiesRelatedToShef.getNormalizedAndSanitizedHtmlText(get(), getAllowedHtmlTags());
		String otherValue = HtmlUtilitiesRelatedToShef.getNormalizedAndSanitizedHtmlText(text, getAllowedHtmlTags());
		return currentValue.equals(otherValue);
	}

	public static String[] getAllowedHtmlTags()
	{
		return new String[] {"br", "b", "i", "ul", "ol", "li", "u", "strike", "a", };
	}
	
	@Override
	public String createXmpz2SchemaElementString(Xmpz2XmlSchemaCreator creator, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		return creator.createUserTextSchemaElement(baseObjectSchema, fieldSchema);
	}
}
