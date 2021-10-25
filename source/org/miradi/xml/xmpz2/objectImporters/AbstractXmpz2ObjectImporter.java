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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.schemas.UnspecifiedBaseObjectSchema;
import org.miradi.schemas.WcpaProjectDataSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class AbstractXmpz2ObjectImporter  implements Xmpz2XmlConstants
{
	public AbstractXmpz2ObjectImporter(Xmpz2XmlImporter importerToUse)
	{
		importer = importerToUse;
	}
	
	protected ORef getMetadataRef()
	{
		return getProject().getMetadata().getRef();
	}
	
	protected ORef getWcpaProjectDataRef()
	{
		return getSingletonObject(WcpaProjectDataSchema.getObjectType());
	}
	
	protected ORef getTncProjectDataRef()
	{
		return getSingletonObject(TncProjectDataSchema.getObjectType());
	}
	
	protected ORef getSingletonObject(int objectType)
	{
		return getProject().getSingletonObjectRef(objectType);
	}
	
	protected void importField(Node node, String singletonName, ORef refToUse, String destinationTag) throws Exception
	{
		BaseObjectSchema baseObjectSchema = new UnspecifiedBaseObjectSchema(singletonName);
		importField(node, baseObjectSchema, refToUse, destinationTag);
	}
	
	protected void importField(Node node, BaseObjectSchema baseObjectSchemaToUse, ORef destinationRef, String destinationTag) throws Exception
	{
		BaseObject baseObject = BaseObject.find(getProject(), destinationRef);
		AbstractFieldSchema fieldSchema = baseObject.getSchema().getFieldSchema(destinationTag);
		importField(node, baseObjectSchemaToUse, baseObject, fieldSchema);
	}
	
	protected void importField(Node baseObjectNode, BaseObjectSchema baseObjectSchemaToUse, BaseObject baseObject, AbstractFieldSchema fieldSchema)	throws Exception
	{
		ObjectData objectData = baseObject.getField(fieldSchema.getTag());
		objectData.readAsXmpz2XmlData(getImporter(), baseObjectNode, baseObject.getRef(), baseObjectSchemaToUse, fieldSchema);
	}
	
	protected Xmpz2XmlImporter getImporter()
	{
		return importer;
	}
	
	protected Project getProject()
	{
		return getImporter().getProject();
	}

	private Xmpz2XmlImporter importer;
}
