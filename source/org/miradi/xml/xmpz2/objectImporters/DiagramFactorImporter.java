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
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class DiagramFactorImporter extends BaseObjectImporter
{
	public DiagramFactorImporter(Xmpz2XmlImporter importerToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(importerToUse, baseObjectSchemaToUse);
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(DiagramFactor.TAG_WRAPPED_REF))
			return true;
		
		return super.isCustomImportField(tag);
	}
	
	@Override
	public void postCreateFix(ORef ref, Node node) throws Exception
	{
		ORef wrappedRef = importWrappedRef(getImporter(), getPoolName(),  node);
		getProject().setObjectData(ref, DiagramFactor.TAG_WRAPPED_REF, wrappedRef.toString());
	}
	
	private  static ORef importWrappedRef(Xmpz2XmlImporter importer, String poolName, Node parentNode) throws Exception
	{
		Node wrappedFactorIdNode = importer.getNode(parentNode, poolName + WRAPPED_FACTOR_ID_ELEMENT_NAME);

		return importWrappedRef(importer, wrappedFactorIdNode);
	}

	private static ORef importWrappedRef(Xmpz2XmlImporter importer, Node wrappedFactorIdNode) throws Exception
	{
		Node wrappedByDiagamFactorIdNode = importer.getNode(wrappedFactorIdNode, WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		
		return getWrappedRef(importer, wrappedByDiagamFactorIdNode);
	}

	public static ORef getWrappedRef(Xmpz2XmlImporter importer, Node wrappedByDiagamFactorIdNode)
	{
		//TODO Should avoid relying on getFirst()
		final int ONE_CHILD_NODE_FOR_ELEMENT = 1;
		final int ONE_CHILD_FOR_TEXT = 1;
		final int EXPECTED_CHILDREN_COUNT = ONE_CHILD_NODE_FOR_ELEMENT + ONE_CHILD_FOR_TEXT;
		int childrenNodeCount = wrappedByDiagamFactorIdNode.getChildNodes().getLength();
		if (childrenNodeCount != EXPECTED_CHILDREN_COUNT)
			throw new RuntimeException("DiagramFactor wrapped factor id node does not have an id node and a text id. children count= " +  childrenNodeCount);		
		Node typedIdNode = wrappedByDiagamFactorIdNode.getFirstChild();
		
		BaseId wrappedId = new BaseId(typedIdNode.getTextContent());
		return new ORef(importer.getObjectTypeOfNode(typedIdNode), wrappedId);
	}

}
