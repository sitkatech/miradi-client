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

import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.Project;
import org.miradi.xml.AbstractXmlImporter;
import org.miradi.xml.MiradiXmlValidator;
import org.miradi.xml.wcs.Xmpz2XmlValidator;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz.XmpzNameSpaceContext;

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
	}
	
	private void importPools() throws Exception
	{
		for(int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
		{
			importBaseObjects(objectType);
		}
	}

	private void importBaseObjects(final int objectType) throws Exception
	{
		//FIXME urgent - uncomment and make work
//		final BaseObjectImporter baseObjectImporter = new BaseObjectImporter(this, objectType);
//		final String containerName = baseObjectImporter.getExporterContainerName(objectType);
//		final String poolName = getWriter().createPoolElementName(containerName);
//		for(ORef ref : sortedRefList)
//		{
//			BaseObject baseObject = BaseObject.find(getProject(), ref);
//			baseObjectImporter.writeBaseObjectDataSchemaElement(baseObject);
//		}
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
