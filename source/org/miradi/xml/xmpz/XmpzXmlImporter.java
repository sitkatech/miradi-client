/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz;

import javax.xml.namespace.NamespaceContext;

import org.miradi.project.Project;
import org.miradi.xml.AbstractXmlImporter;
import org.miradi.xml.wcs.WcsXmlConstants;

public class XmpzXmlImporter extends AbstractXmlImporter implements WcsXmlConstants
{
	public XmpzXmlImporter(Project projectToFill) throws Exception
	{
		super(projectToFill);
	}
	
	@Override
	protected void importXml() throws Exception
	{
		new ProjectSummaryImporter(this).importElement();
		new ProjectResourceImporter(this).importElement();
		new OrganizationImporter(this).importElement();
		new ProjectSummaryScopeImporter(this).importElement();
		new ProjectSummaryLocationImporter(this).importElement();
		new ProjectSummaryPlanningImporter(this).importElement();
		new TncProjectDataImporter(this).importElement();
		new WwfProjectDataImporter(this).importElement();
		new WcsProjectDataImporter(this).importElement();
		new RareProjetDataImporter(this).importElement();
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
}
