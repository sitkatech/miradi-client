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

import javax.xml.namespace.NamespaceContext;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.BaseObjectPool;
import org.miradi.objects.Dashboard;
import org.miradi.objects.RatingCriterion;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.ValueOptionSchema;
import org.miradi.xml.AbstractXmlImporter;
import org.miradi.xml.MiradiXmlValidator;
import org.miradi.xml.wcs.Xmpz2XmlValidator;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz.XmpzNameSpaceContext;
import org.miradi.xml.xmpz2.objectImporters.BaseObjectImporter;
import org.miradi.xml.xmpz2.objectImporters.Xmpz2ExtraDataImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Xmpz2XmlImporter extends AbstractXmlImporter implements XmpzXmlConstants
{
	public Xmpz2XmlImporter(Project projectToFill) throws Exception
	{
		super(projectToFill);
	}
		
	@Override
	protected void importXml() throws Exception
	{
		importPools();
		importExtraData();
	}
	
	private void importPools() throws Exception
	{
		for(int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
		{
			if (isCustomImport(objectType))
				continue;
			
			BaseObjectPool pool = (BaseObjectPool) getProject().getPool(objectType);
			if (pool == null)
				continue;
			
			BaseObjectSchema baseObjectSchema = pool.createBaseObjectSchema(getProject());
			
			importBaseObjects(baseObjectSchema);
		}
	}

	private boolean isCustomImport(int objectType)
	{
		if (RatingCriterion.is(objectType))
			return true;
		
		if (ValueOptionSchema.getObjectType() == objectType)
			return true;
		
		if (Dashboard.is(objectType))
			return true;
		
		return false;
	}

	private void importBaseObjects(final BaseObjectSchema baseObjectSchema) throws Exception
	{
		final String elementObjectName = baseObjectSchema.getXmpz2ElementName();
		final String containerElementName = elementObjectName + XmpzXmlConstants.POOL_ELEMENT_TAG;
		final Node rootNode = getRootNode();
		final NodeList baseObjectNodes = getNodes(rootNode, new String[]{containerElementName, elementObjectName, });
		for (int index = 0; index < baseObjectNodes.getLength(); ++index)
		{
			Node baseObjectNode = baseObjectNodes.item(index);
			String intIdAsString = getAttributeValue(baseObjectNode, XmpzXmlConstants.ID);
			ORef ref = getProject().createObject(baseObjectSchema.getType(), new BaseId(intIdAsString));
			
			//FIXME urgent - this loop is still under construction, but postCreateFix needs to be uncommented and 
			//needs to hook DF to its F and DL to FL.  See xmpz1 for pattern.
			//postCreateFix(ref, baseObjectNode);
			
			final BaseObjectImporter baseObjectImporter = new BaseObjectImporter(this, baseObjectSchema);
			baseObjectImporter.importFields(baseObjectNode, ref); 
		}
	}
	
	private void importExtraData() throws Exception
	{
		new Xmpz2ExtraDataImporter(this).importFields(null, null);
	}

	@Override
	protected String getNameSpaceVersion()
	{
		return NAME_SPACE_VERSION;
	}

	@Override
	protected String getPartialNameSpace()
	{
		return PARTIAL_NAME_SPACE;
	}

	@Override
	protected String getRootNodeName()
	{
		return CONSERVATION_PROJECT;
	}
	
	@Override
	protected String getPrefix()
	{
		return PREFIX;
	}
	
	@Override
	protected NamespaceContext getNamespaceContext()
	{
		return new XmpzNameSpaceContext();
	}
	
	@Override
	protected MiradiXmlValidator createXmlValidator()
	{
		return new Xmpz2XmlValidator();
	}

	@Override
	protected String getNamespaceURI()
	{
		return getDocument().lookupNamespaceURI("miradi");
	}
}
