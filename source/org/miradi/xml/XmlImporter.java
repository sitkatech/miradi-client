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

package org.miradi.xml;

import javax.xml.namespace.NamespaceContext;

import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;

public class XmlImporter extends AbstractXmlImporter implements WcsXmlConstants
{
	public XmlImporter(Project projectToFill) throws Exception
	{
		super(projectToFill);
	}
	
	@Override
	protected void importXml() throws Exception
	{
		importProjectSummaryElement();
	}

	private void importProjectSummaryElement() throws Exception
	{
		Node projectSumaryNode = getParentNode(getRootNode(), PROJECT_SUMMARY);
				
		importField(projectSumaryNode, ProjectMetadata.TAG_PROJECT_NAME, getProjectMetadataRef(), ProjectMetadata.TAG_PROJECT_NAME);
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
	
	protected NamespaceContext getNameSpaceContext()
	{
		return new XmlMiradiNameSpaceContext();
	}
}
