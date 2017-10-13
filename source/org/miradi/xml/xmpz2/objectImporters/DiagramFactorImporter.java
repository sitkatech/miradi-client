/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import javax.xml.xpath.XPathConstants;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2GroupedConstants;
import org.w3c.dom.Node;

public class DiagramFactorImporter extends BaseObjectImporter
{
	public DiagramFactorImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, new DiagramFactorSchema());
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);
		
		importFontStylingElements(baseObjectNode, refToUse);
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
		ORef wrappedRef = importWrappedRef(getImporter(), getXmpz2ElementName(),  node);
		getProject().setObjectData(ref, DiagramFactor.TAG_WRAPPED_REF, wrappedRef.toString());
	}
	
	private void importFontStylingElements(Node node, ORef destinationRef) throws Exception
	{
		Node diagramFactorStyleNode = getImporter().getNamedChildNode(node, getXmpz2ElementName() + STYLE);
		if (diagramFactorStyleNode == null)
			return;
		
		Node styleNode = getImporter().getNamedChildNode(diagramFactorStyleNode, STYLE);
		getImporter().importCodeField(styleNode, getBaseObjectSchema().getXmpz2ElementName(), destinationRef, DiagramFactor.TAG_FONT_SIZE, new DiagramFactorFontSizeQuestion());
		getImporter().importCodeField(styleNode, getBaseObjectSchema().getXmpz2ElementName(), destinationRef, DiagramFactor.TAG_FONT_STYLE, new DiagramFactorFontStyleQuestion());
        getImporter().importFieldQualifiedByContainerName(styleNode, getBaseObjectSchema().getXmpz2ElementName(), destinationRef, DiagramFactor.TAG_HEADER_HEIGHT);
		getImporter().importCodeField(styleNode, getBaseObjectSchema().getXmpz2ElementName(), destinationRef, DiagramFactor.TAG_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion());
		getImporter().importCodeField(styleNode, getBaseObjectSchema().getXmpz2ElementName(), destinationRef, DiagramFactor.TAG_BACKGROUND_COLOR, new DiagramFactorBackgroundQuestion());
	}
	
	private  static ORef importWrappedRef(Xmpz2XmlImporter importer, String poolName, Node parentNode) throws Exception
	{
		Node wrappedFactorIdNode = importer.getNamedChildNode(parentNode, poolName + WRAPPED_FACTOR_ID_ELEMENT_NAME);

		return importWrappedRef(importer, wrappedFactorIdNode);
	}

	private static ORef importWrappedRef(Xmpz2XmlImporter importer, Node wrappedFactorIdNode) throws Exception
	{
		Node wrappedByDiagramFactorIdNode = importer.getNamedChildNode(wrappedFactorIdNode, WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		
		return getWrappedRef(importer, wrappedByDiagramFactorIdNode);
	}

	public static ORef getWrappedRef(Xmpz2XmlImporter importer, Node wrappedByDiagramFactorIdNode) throws Exception
	{
		String oredWrappedFactorNames = Xmpz2GroupedConstants.createOredWrappableFactorNames();
		Node node = (Node) importer.evaluate(wrappedByDiagramFactorIdNode, oredWrappedFactorNames, XPathConstants.NODE);
		BaseId wrappedId = new BaseId(node.getTextContent());
		final int objectTypeOfNode = importer.getObjectTypeOfNode(node);
		
		return new ORef(objectTypeOfNode, wrappedId);
	}
}
