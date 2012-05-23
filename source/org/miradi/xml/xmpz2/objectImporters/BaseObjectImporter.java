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

import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.CodeList;
import org.miradi.xml.generic.XmlSchemaCreator;
import org.miradi.xml.wcs.TagToElementNameMap;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BaseObjectImporter implements XmpzXmlConstants
{
	public BaseObjectImporter(final Xmpz2XmlImporter importerToUse, final BaseObjectSchema baseObjectSchemaToUse)
	{
		importer = importerToUse;
		baseObjectSchema = baseObjectSchemaToUse; 
	}
	
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		BaseObject baseObject = BaseObject.find(getProject(), refToUse);
		
		for(AbstractFieldSchema fieldSchema : getBaseObjectSchema())
		{
			final String fieldTag = fieldSchema.getTag();
			ObjectData objectData = fieldSchema.createField(baseObject);
			if (objectData.isCodeListData())
				importCodeListField(baseObjectNode, refToUse, fieldTag);
			else
				importField(baseObjectNode, refToUse, fieldTag);
		}
	}
	
	protected void importField(Node node, ORef destinationRef, String destinationTag) throws Exception
	{
		TagToElementNameMap map = new TagToElementNameMap();
		String elementName = map.findElementName(getBaseObjectSchema().getXmpz2ElementName(), destinationTag);
		getImporter().importField(node, getBaseObjectSchema().getXmpz2ElementName() + elementName, destinationRef, destinationTag);
	}
	
	protected void importCodeListField(Node node, ORef destinationRef, String destinationTag) throws Exception
	{
		importCodeListField(node, getPoolName(), destinationRef, destinationTag);
	}
	
	protected void importCodeListField(Node node, String elementContainerName, ORef destinationRef, String destinationTag) throws Exception
	{
		TagToElementNameMap map = new TagToElementNameMap();
		String elementName = map.findElementName(elementContainerName, destinationTag);
		String containerElementName = elementContainerName + elementName + XmpzXmlConstants.CONTAINER_ELEMENT_TAG;
		CodeList codesToImport = getCodeList(node, containerElementName);
		
		getImporter().setData(destinationRef, destinationTag, codesToImport.toString());
	}

	protected CodeList getCodeList(Node node, String containerElementName) throws Exception
	{
		NodeList codeNodes = getImporter().getNodes(node, new String[]{containerElementName, XmlSchemaCreator.CODE_ELEMENT_NAME});
		CodeList codesToImport = new CodeList();
		for (int index = 0; index < codeNodes.getLength(); ++index)
		{
			Node codeNode = codeNodes.item(index);
			String code = getImporter().getSafeNodeContent(codeNode);
			codesToImport.add(code);
		}
		return codesToImport;
	}
	
	protected String getPoolName()
	{
		return getBaseObjectSchema().getXmpz2ElementName();
	}

	protected Xmpz2XmlImporter getImporter()
	{
		return importer;
	}
	
	protected BaseObjectSchema getBaseObjectSchema()
	{
		return baseObjectSchema;
	}
	
	protected Project getProject()
	{
		return getImporter().getProject();
	}
	
	private Xmpz2XmlImporter importer;
	private BaseObjectSchema baseObjectSchema;
}
