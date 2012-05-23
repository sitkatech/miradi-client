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

import java.util.Collection;
import java.util.LinkedHashMap;

import javax.xml.namespace.NamespaceContext;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.BaseObjectPool;
import org.miradi.objects.Dashboard;
import org.miradi.objects.RatingCriterion;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.DiagramLinkSchema;
import org.miradi.schemas.GroupBoxSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.ProgressPercentSchema;
import org.miradi.schemas.ScopeBoxSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.schemas.TextBoxSchema;
import org.miradi.schemas.ThreatReductionResultSchema;
import org.miradi.schemas.ValueOptionSchema;
import org.miradi.utils.CodeList;
import org.miradi.xml.AbstractXmlImporter;
import org.miradi.xml.MiradiXmlValidator;
import org.miradi.xml.generic.XmlSchemaCreator;
import org.miradi.xml.wcs.TagToElementNameMap;
import org.miradi.xml.wcs.Xmpz2XmlValidator;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz.XmpzNameSpaceContext;
import org.miradi.xml.xmpz2.objectImporters.BaseObjectImporter;
import org.miradi.xml.xmpz2.objectImporters.DiagramFactorImporter;
import org.miradi.xml.xmpz2.objectImporters.DiagramLinkImporter;
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
		LinkedHashMap<Integer, BaseObjectImporter> typeToImporterMap = fillTypeToImporterMap();
		importPools(typeToImporterMap);
		importExtraData();
	}
	
	private LinkedHashMap<Integer, BaseObjectImporter> fillTypeToImporterMap()
	{
		LinkedHashMap<Integer, BaseObjectImporter> typeToImporterMap = new LinkedHashMap<Integer, BaseObjectImporter>();
		typeToImporterMap.put(DiagramFactorSchema.getObjectType(), new DiagramFactorImporter(this, new DiagramFactorSchema()));
		typeToImporterMap.put(DiagramLinkSchema.getObjectType(), new DiagramLinkImporter(this, new DiagramLinkSchema()));
		
		for(int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
		{
			if (isCustomImport(objectType))
				continue;
			
			BaseObjectPool pool = (BaseObjectPool) getProject().getPool(objectType);
			if (pool == null)
				continue;
			
			if (typeToImporterMap.containsKey(objectType))
				continue;
			
			BaseObjectSchema baseObjectSchema = pool.createBaseObjectSchema(getProject());
			typeToImporterMap.put(objectType, new BaseObjectImporter(this, baseObjectSchema));
		}
		
		return typeToImporterMap;
	}

	private void importPools(LinkedHashMap<Integer, BaseObjectImporter> typeToImporterMap) throws Exception
	{
		Collection<BaseObjectImporter> importers = typeToImporterMap.values();
		for(BaseObjectImporter importer : importers)
		{
			importBaseObjects(importer);
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

	private void importBaseObjects(final BaseObjectImporter importer) throws Exception
	{
		final String elementObjectName = importer.getBaseObjectSchema().getXmpz2ElementName();
		final String containerElementName = elementObjectName + XmpzXmlConstants.POOL_ELEMENT_TAG;
		final Node rootNode = getRootNode();
		final NodeList baseObjectNodes = getNodes(rootNode, new String[]{containerElementName, elementObjectName, });
		for (int index = 0; index < baseObjectNodes.getLength(); ++index)
		{
			Node baseObjectNode = baseObjectNodes.item(index);
			String intIdAsString = getAttributeValue(baseObjectNode, XmpzXmlConstants.ID);
			ORef ref = getProject().createObject(importer.getBaseObjectSchema().getType(), new BaseId(intIdAsString));
			
			importer.importFields(baseObjectNode, ref);
			importer.postCreateFix(ref, baseObjectNode);
		}
	}

	public void importRefs(Node node, ORef destinationRef, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String reflistTypeName) throws Exception
	{
		importRefs(node, baseObjectSchema.getXmpz2ElementName(), fieldSchema, destinationRef, reflistTypeName);
	}
	
	public void importRefs(Node node, String poolName, AbstractFieldSchema fieldSchema, ORef destinationRef, String idElementName) throws Exception
	{
		ORefList importedRefs = extractRefs(node, poolName, fieldSchema.getTag(), idElementName + ID);
		
		setData(destinationRef, fieldSchema.getTag(), importedRefs);
	}
	
	public void importRefs(Node node, ORef destinationRefToUse,	BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema,	int idListType) throws Exception
	{
		ORefList importedRefs = extractRefs(node, baseObjectSchema.getXmpz2ElementName(), fieldSchema, idListType);
		setData(destinationRefToUse, fieldSchema.getTag(), importedRefs);
	}
	
	private ORefList extractRefs(Node node, String poolName, AbstractFieldSchema fieldSchema, int listObjectType) throws Exception
	{
		TagToElementNameMap map = new TagToElementNameMap();
		String elementName = map.findElementName(poolName, fieldSchema.getTag());
		NodeList idNodes = getNodes(node, new String[]{poolName + elementName, fieldSchema.getTag()});
		ORefList importedRefs = new ORefList();
		for (int index = 0; index < idNodes.getLength(); ++index)
		{
			Node idNode = idNodes.item(index);
			String id = getSafeNodeContent(idNode);
			int idsType = getObjectTypeOfNode(idNode);
			importedRefs.add(new ORef(idsType, new BaseId(id)));
		}
		
		return importedRefs;
	}

	protected ORefList extractRefs(Node node, String poolName, String idsElementName, String idElementName) throws Exception
	{
		TagToElementNameMap map = new TagToElementNameMap();
		String elementName = map.findElementName(poolName, idsElementName);
		NodeList idNodes = getNodes(node, new String[]{poolName + elementName, idElementName});
		ORefList importedRefs = new ORefList();
		for (int index = 0; index < idNodes.getLength(); ++index)
		{
			Node idNode = idNodes.item(index);
			String id = getSafeNodeContent(idNode);
			int idsType = getObjectTypeOfNode(idNode);
			importedRefs.add(new ORef(idsType, new BaseId(id)));
		}
		
		return importedRefs;
	}
	
	public void importCodeListField(Node node, String elementContainerName, ORef destinationRef, String destinationTag) throws Exception
	{
		TagToElementNameMap map = new TagToElementNameMap();
		String elementName = map.findElementName(elementContainerName, destinationTag);
		String containerElementName = elementContainerName + elementName + XmpzXmlConstants.CONTAINER_ELEMENT_TAG;
		CodeList codesToImport = getCodeList(node, containerElementName);
		
		setData(destinationRef, destinationTag, codesToImport.toString());
	}

	public CodeList getCodeList(Node node, String containerElementName) throws Exception
	{
		NodeList codeNodes = getNodes(node, new String[]{containerElementName, XmlSchemaCreator.CODE_ELEMENT_NAME});
		CodeList codesToImport = new CodeList();
		for (int index = 0; index < codeNodes.getLength(); ++index)
		{
			Node codeNode = codeNodes.item(index);
			String code = getSafeNodeContent(codeNode);
			codesToImport.add(code);
		}
		
		return codesToImport;
	}
	
	public int getObjectTypeOfNode(Node typedIdNode)
	{
		String nodeName = typedIdNode.getNodeName();
		String objectTypeNameWithNamespace = removeAppendedId(nodeName);
		String objectTypeName = removeNamepsacePrefix(objectTypeNameWithNamespace);
		
		if (objectTypeName.equals(SCOPE_BOX))
			return ScopeBoxSchema.getObjectType();
		
		if (objectTypeName.equals(BIODIVERSITY_TARGET))
			return TargetSchema.getObjectType();
		
		if (objectTypeName.equals(HUMAN_WELFARE_TARGET))
			return HumanWelfareTargetSchema.getObjectType();
		
		if (objectTypeName.equals(CAUSE) || objectTypeName.equals(THREAT))
			return CauseSchema.getObjectType();
		
		if (objectTypeName.equals(STRATEGY))
			return StrategySchema.getObjectType();
		
		if (objectTypeName.equals(INTERMEDIATE_RESULTS))
			return IntermediateResultSchema.getObjectType();
		
		if (objectTypeName.equals(THREAT_REDUCTION_RESULTS))
			return ThreatReductionResultSchema.getObjectType();
		
		if (objectTypeName.equals(TEXT_BOX))
			return TextBoxSchema.getObjectType();
		
		if (objectTypeName.equals(GROUP_BOX))
			return GroupBoxSchema.getObjectType();
		
		if (isTask(objectTypeName))
			return TaskSchema.getObjectType();
		
		if (objectTypeName.equals(STRESS))
			return StressSchema.getObjectType();
		
		if (objectTypeName.equals(PROGRESS_PERCENT))
			return ProgressPercentSchema.getObjectType();
		
		if (objectTypeName.equals(BIODIVERSITY_TARGET))
			return TargetSchema.getObjectType();
		
		EAM.logError("Could not find type for node: " + objectTypeName);
		return ObjectType.FAKE;
	}
	
	private String removeNamepsacePrefix(String objectTypeNameWithNamespace)
	{
		return objectTypeNameWithNamespace.replaceFirst(getPrefix(), "");
	}

	private static String removeAppendedId(String nodeName)
	{
		return nodeName.replaceFirst(ID, "");
	}
	
	private static boolean isTask(String objectTypeName)
	{
		if (objectTypeName.equals(TaskSchema.ACTIVITY_NAME))
			return true;
		
		if (objectTypeName.equals(TaskSchema.METHOD_NAME))
			return true;
		
		return objectTypeName.equals(TASK);
	}
	
	public void importBooleanField(Node node, ORef destinationRefToUse,	BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		Node booleanNode = getNode(node, baseObjectSchema.getXmpz2ElementName() + fieldSchema.getTag());
		String isValue = BooleanData.BOOLEAN_FALSE;
		if (booleanNode != null && isTrue(booleanNode.getTextContent()))
			isValue = BooleanData.BOOLEAN_TRUE;

		setData(destinationRefToUse, fieldSchema.getTag(), isValue);
	}
	
	public void importChoiceField(Node node, String parentElementName, ORef destinationRefToUse, String tag, ChoiceQuestion question) throws Exception
	{
		Xmpz2TagToElementNameMap map = new Xmpz2TagToElementNameMap();
		String choiceElementName = map.findElementName(parentElementName, tag);
		String importedReadableCode = getPathData(node, new String[]{parentElementName + choiceElementName, });
		String internalCode = question.convertToInternalCode(importedReadableCode);
		importField(destinationRefToUse, tag, internalCode);
	}

	public void importStringField(Node node, String poolName, ORef destinationRef, String destinationTag) throws Exception
	{
		Xmpz2TagToElementNameMap map = new Xmpz2TagToElementNameMap();
		String elementName = map.findElementName(poolName, destinationTag);
		importField(node, poolName + elementName, destinationRef, destinationTag);
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
