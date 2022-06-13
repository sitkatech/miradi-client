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

import org.miradi.objects.Strategy;
import org.miradi.schemas.BaseObjectSchema;

import java.util.Vector;

public class StrategySchemaWriter extends BaseObjectSchemaWriterWithCalculatedCostsElement
{
	public StrategySchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(creatorToUse, baseObjectSchemaToUse);
	}

	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(Strategy.TAG_STANDARD_CLASSIFICATION_V11_CODE))
			return true;

		if (tag.equals(Strategy.TAG_STANDARD_CLASSIFICATION_V20_CODE))
			return true;

		return super.doesFieldRequireSpecialHandling(tag);
	}

	@Override
	protected Vector<String> createCustomSchemaFields()
	{
		Vector<String> schemaElements = super.createCustomSchemaFields();

		schemaElements.add(getXmpz2XmlSchemaCreator().getSchemaWriter().createOptionalDotElement(STRATEGY_STANDARD_CLASSIFICATION_CONTAINER));

		return schemaElements;
	}
}
