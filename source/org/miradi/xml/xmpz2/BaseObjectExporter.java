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

package org.miradi.xml.xmpz2;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class BaseObjectExporter implements XmpzXmlConstants
{
	public BaseObjectExporter(final Xmpz2XmlWriter writerToUse, final int objectTypeToUse)
	{
		writer = writerToUse;
		objectType = objectTypeToUse;
	}
	
	public void writeBaseObjectDataSchemaElement(final BaseObject baseObject) throws Exception
	{
		BaseObjectSchema baseObjectSchema = baseObject.getSchema();
		getWriter().writeObjectElementStart(baseObject);
		writeFields(baseObject, baseObjectSchema);
		getWriter().writeObjectElementEnd(baseObjectSchema);
	}

	protected void writeFields(final BaseObject baseObject,	BaseObjectSchema baseObjectSchema) throws Exception
	{
		for(AbstractFieldSchema fieldSchema : baseObjectSchema)
		{
			if (!doesFieldRequireSpecialHandling(fieldSchema.getTag()))
			{
				writeField(baseObject, fieldSchema);
			}
		}
	}

	protected void writeField(final BaseObject baseObject, final AbstractFieldSchema fieldSchema) throws Exception
	{
		getWriter().writeFieldElement(baseObject, fieldSchema);
	}

	protected boolean doesFieldRequireSpecialHandling(final String tag)
	{
		return false;
	}
	
	public String getExporterContainerName(final int objectTypeToUse)
	{
		final String internalObjectTypeName = getProject().getObjectManager().getInternalObjectTypeName(objectTypeToUse);
		
		return internalObjectTypeName;
	}

	protected Xmpz2XmlWriter getWriter()
	{
		return writer;
	}
	
	protected Project getProject()
	{
		return getWriter().getProject();
	}
	
	protected String getFactorTypeName(Factor wrappedFactor)
	{
		if (Target.is(wrappedFactor))
			return XmpzXmlConstants.BIODIVERSITY_TARGET;
		
		if (Cause.is(wrappedFactor))
			return XmpzXmlConstants.CAUSE;
		
		return wrappedFactor.getTypeName();
	}
	
	public int getObjectType()
	{
		return objectType;
	}

	private Xmpz2XmlWriter writer;
	private int objectType;
}
