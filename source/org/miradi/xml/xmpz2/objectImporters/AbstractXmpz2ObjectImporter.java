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

import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.schemas.FosProjectDataSchema;
import org.miradi.schemas.RareProjectDataSchema;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.schemas.WcpaProjectDataSchema;
import org.miradi.schemas.WcsProjectDataSchema;
import org.miradi.schemas.WwfProjectDataSchema;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;

public class AbstractXmpz2ObjectImporter  implements XmpzXmlConstants
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
	
	protected ORef getSingletonObject(int objectType)
	{
		return getProject().getSingletonObjectRef(objectType);
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
