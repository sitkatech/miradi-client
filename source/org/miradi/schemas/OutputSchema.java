/*
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objects.Factor;
import org.miradi.objects.Output;

public class OutputSchema extends BaseObjectSchema
{
	public OutputSchema()
	{
		super();
	}

	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();

		createFieldSchemaMultiLineUserText(Factor.TAG_COMMENTS);
		addDetailsField();
		createFieldSchemaSingleLineUserText(Factor.TAG_SHORT_LABEL);
		createFieldSchemaSingleLineUserText(Output.TAG_URL);
		createFieldSchemaDate(Output.TAG_DUE_DATE);

		createProgressReportSchema();
		createTaxonomyClassificationSchemaField();
	}

	protected void addDetailsField()
	{
		createFieldSchemaMultiLineUserText(Factor.TAG_TEXT);
	}

	public static int getObjectType()
	{
		return ObjectType.OUTPUT;
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

	public static final String OBJECT_NAME = "Output";
}
