/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2.xmpz2schema;

import java.util.Vector;

import org.miradi.objects.Cause;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.generic.XmlSchemaCreator;

public class CauseSchemaWriter extends BaseObjectSchemaWriterWithTaxonomyClassificationContainer
{
	public CauseSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(creatorToUse, baseObjectSchemaToUse);
	}

	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(Cause.TAG_STANDARD_CLASSIFICATION_V11_CODE))
			return true;

		if (tag.equals(Cause.TAG_STANDARD_CLASSIFICATION_V20_CODE))
			return true;

		return super.doesFieldRequireSpecialHandling(tag);
	}

	@Override
	public Vector<String> createFieldSchemas() throws Exception
	{
		Vector<String> schemaElements = super.createFieldSchemas();
		
		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalSchemaElement(getXmpz2ElementName() + CALCULATED_THREAT_RATING, XmlSchemaCreator.VOCABULARY_THREAT_RATING));
		
		return schemaElements;
	}

	@Override
	protected Vector<String> createCustomSchemaFields()
	{
		Vector<String> schemaElements = super.createCustomSchemaFields();

		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalDotElement(CAUSE_STANDARD_CLASSIFICATION_CONTAINER));

		return schemaElements;
	}
}
