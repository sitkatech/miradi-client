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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.ids.BaseId;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.DashboardSchema;
import org.miradi.schemas.FosProjectDataSchema;
import org.miradi.schemas.RareProjectDataSchema;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.schemas.WcpaProjectDataSchema;
import org.miradi.schemas.WcsProjectDataSchema;
import org.miradi.schemas.WwfProjectDataSchema;
import org.miradi.xml.wcs.TagToElementNameMap;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BaseObjectImporter implements XmpzXmlConstants
{
	public BaseObjectImporter(final Xmpz2XmlImporter importerToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		importer = importerToUse;
		baseObjectSchema = baseObjectSchemaToUse; 
	}
	
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		BaseObject baseObject = BaseObject.find(getProject(), refToUse);
		
		for(AbstractFieldSchema fieldSchema : getBaseObjectSchema())
		{
			ObjectData objectData = fieldSchema.createField(baseObject);
			if (objectData.isPseudoField())
				continue;

			if (isCustomImportField(fieldSchema.getTag()))
				continue;
			
			objectData.readAsXmpz2XmlData(importer, baseObjectNode, refToUse, baseObjectSchema, fieldSchema);
		}
	}
	
	protected boolean isCustomImportField(String tag)
	{
		return false;
	}

	public void importField(Node node, ORef destinationRef, String destinationTag) throws Exception
	{
		TagToElementNameMap map = new TagToElementNameMap();
		String elementName = map.findElementName(getBaseObjectSchema().getXmpz2ElementName(), destinationTag);
		getImporter().importField(node, getBaseObjectSchema().getXmpz2ElementName() + elementName, destinationRef, destinationTag);
	}
		
	public void importIds(Node node, ORef destinationRef, String destinationTag, int idsType, String idElementName) throws Exception
	{
		ORefList importedRefs = extractRefs(node, destinationTag, idsType, idElementName + ID);
		
		getImporter().setData(destinationRef, destinationTag, importedRefs.convertToIdList(idsType));
	}

	public void importRefs(Node node, String elementName, ORef destinationRef, String destinationTag, int idsType, String idElementName) throws Exception
	{
		ORefList importedRefs = extractRefs(node, elementName, idsType, idElementName + ID);
		
		getImporter().setData(destinationRef, destinationTag, importedRefs);
	}

	protected ORefList extractRefs(Node node, String idsElementName, int idsType, String idElementName) throws Exception
	{
		TagToElementNameMap map = new TagToElementNameMap();
		String elementName = map.findElementName(getPoolName(), idsElementName);
		NodeList idNodes = getImporter().getNodes(node, new String[]{getPoolName() + elementName, idElementName});
		ORefList importedRefs = new ORefList();
		for (int index = 0; index < idNodes.getLength(); ++index)
		{
			Node idNode = idNodes.item(index);
			String id = getImporter().getSafeNodeContent(idNode);
			importedRefs.add(new ORef(idsType, new BaseId(id)));
		}
		
		return importedRefs;
	}
	
	public void postCreateFix(ORef ref, Node baseObjectNode) throws Exception
	{
	}
	
	protected String getPoolName()
	{
		return getBaseObjectSchema().getXmpz2ElementName();
	}

	protected Xmpz2XmlImporter getImporter()
	{
		return importer;
	}
	
	public BaseObjectSchema getBaseObjectSchema()
	{
		return baseObjectSchema;
	}
	
	protected Project getProject()
	{
		return getImporter().getProject();
	}
	
	protected ORef getWcpaProjectDataRef()
	{
		return getSingletonObject(WcpaProjectDataSchema.getObjectType());
	}
	
	protected ORef getWcsProjectDataRef()
	{
		return getSingletonObject(WcsProjectDataSchema.getObjectType());
	}

	protected ORef getTncProjectDataRef()
	{
		return getSingletonObject(TncProjectDataSchema.getObjectType());
	}
	
	protected ORef getWwfProjectDataRef()
	{
		return getSingletonObject(WwfProjectDataSchema.getObjectType());
	}
	
	protected ORef getRareProjectDataRef()
	{
		return getSingletonObject(RareProjectDataSchema.getObjectType());
	}
	
	protected ORef getFosProjectDataRef()
	{
		return getSingletonObject(FosProjectDataSchema.getObjectType());
	}
	
	protected ORef getDashboardRef()
	{
		return getSingletonObject(DashboardSchema.getObjectType());
	}
	
	private ORef getSingletonObject(int objectType)
	{
		return getProject().getSingletonObjectRef(objectType);
	}
	
	private Xmpz2XmlImporter importer;
	private BaseObjectSchema baseObjectSchema;
}
