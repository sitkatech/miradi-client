/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.TaggedObjectSet;

public class TaggedObjectSetSchema extends BaseObjectSchema
{
	public TaggedObjectSetSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaSingleLineUserText(TaggedObjectSet.TAG_SHORT_LABEL);
		// TODO: field to be deprecated in post 5.0 release...only here to support migrations
		createFieldSchemaReflist(TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, TAGGED_FACTOR_IDS);
		createFieldSchemaMultiLineUserText(TaggedObjectSet.TAG_COMMENTS);
		createPseudoFieldSchemaRefList(TaggedObjectSet.PSEUDO_TAG_REFERRING_DIAGRAM_FACTOR_REFS);
	}

	public static int getObjectType()
	{
		return ObjectType.TAGGED_OBJECT_SET;
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getObjectName()
	{
		return OBJECT_NAME;
	}
	
	public static final String OBJECT_NAME = "TaggedObjectSet";
}
