/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.questions.CustomPlanningAllRowsQuestion;
import org.miradi.schemas.ObjectTreeTableConfigurationSchema;
import org.miradi.xml.xmpz2.objectImporters.BaseObjectImporter;
import org.w3c.dom.Node;

public class ObjectTreeTableConfigurationImporter extends BaseObjectImporter
{
	public ObjectTreeTableConfigurationImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, new ObjectTreeTableConfigurationSchema(importerToUse.getProject()));
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);
		
		getImporter().importCodeListField(baseObjectNode, OBJECT_TREE_TABLE_CONFIGURATION, refToUse, ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION, new CustomPlanningAllRowsQuestion());
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION))
			return true;
		
		if (tag.equals(ObjectTreeTableConfiguration.TAG_DIAGRAM_FILTER))
			return true;

		return super.isCustomImportField(tag);
	}

	@Override
	public void postCreateFix(ORef ref, Node baseObjectNode) throws Exception
	{
		ORef diagramFilterRef = getDiagramFilterRef(baseObjectNode);
		if (diagramFilterRef.isValid())
			getProject().setObjectData(ref, ObjectTreeTableConfiguration.TAG_DIAGRAM_FILTER, diagramFilterRef.toString());
	}

	private ORef getDiagramFilterRef(Node baseObjectNode) throws Exception
	{
		ORef diagramFilterRef = new ORef(ObjectType.FAKE, BaseId.INVALID);

		Node diagramFilterNode = getImporter().getNamedChildNode(baseObjectNode, getXmpz2ElementName() + DIAGRAM_FILTER);
		if (diagramFilterNode != null)
		{
			Node diagramIdNode = getImporter().getNamedChildNode(diagramFilterNode, DIAGRAM_ID);
			if (diagramIdNode != null)
			{
				Node conceptualModelIdNode = getImporter().getNamedChildNode(diagramIdNode, CONCEPTUAL_MODEL + ID);
				if (conceptualModelIdNode != null)
				{
					String conceptualModelIdAsString = conceptualModelIdNode.getTextContent().trim();
					diagramFilterRef = new ORef(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, new BaseId(conceptualModelIdAsString));
				}
				else
				{
					Node resultsChainIdNode = getImporter().getNamedChildNode(diagramIdNode, RESULTS_CHAIN + ID);
					if (resultsChainIdNode != null)
					{
						String resultsChainIdAsString = resultsChainIdNode.getTextContent().trim();
						diagramFilterRef = new ORef(ObjectType.RESULTS_CHAIN_DIAGRAM, new BaseId(resultsChainIdAsString));
					}
				}
			}
		}

		return diagramFilterRef;
	}
}
