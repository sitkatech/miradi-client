/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2.xmpz2schema;

import org.miradi.objects.Indicator;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;

public class IndicatorSchemaWriter extends BaseObjectSchemaWriter
{
	public IndicatorSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(creatorToUse, baseObjectSchemaToUse);
	}
	
	@Override
	protected void writeCustomField(AbstractFieldSchema fieldSchema)
	{
		if (shouldWriteFieldOnlyOnce(fieldSchema))
			getCreator().writeThresholdsSchemaElement(getBaseObjectSchema());
	}
	
	@Override
	protected boolean shouldOmitField(String tag)
	{
		final boolean IS_ALREADY_WRITTEN_DUE_TO_SIBLING_FIELD = tag.equals(Indicator.TAG_THRESHOLD_DETAILS_MAP);
		if (IS_ALREADY_WRITTEN_DUE_TO_SIBLING_FIELD)
			return true;
		
		return super.shouldOmitField(tag);
	}

	private boolean shouldWriteFieldOnlyOnce(AbstractFieldSchema fieldSchema)
	{
		return fieldSchema.getTag().equals(Indicator.TAG_THRESHOLDS_MAP);
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(Indicator.TAG_THRESHOLDS_MAP))
			return true;
		
		if (tag.equals(Indicator.TAG_THRESHOLD_DETAILS_MAP))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
}
