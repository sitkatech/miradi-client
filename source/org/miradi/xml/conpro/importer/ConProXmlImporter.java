/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.xml.conpro.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.TncFreshwaterEcoRegionQuestion;
import org.miradi.questions.TncMarineEcoRegionQuestion;
import org.miradi.questions.TncTerrestrialEcoRegionQuestion;
import org.miradi.utils.CodeList;
import org.miradi.xml.conpro.ConProMiradiXml;
import org.miradi.xml.conpro.exporter.ConProMiradiXmlValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ConProXmlImporter implements ConProMiradiXml
{
	public void populateProjectFromFile(File fileToImport, Project projectToFill) throws Exception
	{
		project = projectToFill;
		importConProProject(fileToImport);
	}

	public void importConProProject(File fileToImport) throws Exception
	{
		FileInputStream fileInputStream = new FileInputStream(fileToImport);
		try
		{
			if (!new ConProMiradiXmlValidator().isValid(fileInputStream))
				throw new Exception("Could not validate file for importing.");

			document = createDocument(fileToImport);
			xPath = createXPath();

			importProjectSummaryElement();
			importTargetElements();
		}
		finally
		{
			fileInputStream.close();
		}
	}

	private Document createDocument(File fileToImport) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		documentFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		
		return documentBuilder.parse(fileToImport);
	}

	private XPath createXPath()
	{
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath  thisXPath = xPathFactory.newXPath();
		thisXPath.setNamespaceContext(new ConProMiradiNameSpaceContext());
		
		return thisXPath;
	}

	private void importTargetElements() throws Exception 
	{
		//TODO still under development
		extractTargets(generateDataPath(new String[] {CONSERVATION_PROJECT,TARGETS, TARGET}));
	}

	private void importProjectSummaryElement() throws Exception
	{
		ORef metadataRef = getProject().getMetadata().getRef();
		Node projectSumaryNode = getNode(generatePath(new String[] {CONSERVATION_PROJECT, PROJECT_SUMMARY,}));
		
		importField(projectSumaryNode, NAME, metadataRef,ProjectMetadata.TAG_PROJECT_NAME);
		importField(projectSumaryNode, START_DATE, metadataRef, ProjectMetadata.TAG_START_DATE);
		importField(projectSumaryNode, AREA_SIZE, metadataRef, ProjectMetadata.TAG_TNC_SIZE_IN_HECTARES);	
		importField(projectSumaryNode, new String[]{GEOSPATIAL_LOCATION, LATITUDE}, metadataRef, ProjectMetadata.TAG_PROJECT_LATITUDE);
		importField(projectSumaryNode, new String[]{GEOSPATIAL_LOCATION, LONGITUDE}, metadataRef, ProjectMetadata.TAG_PROJECT_LONGITUDE);
		importField(projectSumaryNode, DESCRIPTION_COMMENT, metadataRef, ProjectMetadata.TAG_PROJECT_SCOPE);
		importField(projectSumaryNode, GOAL_COMMENT, metadataRef, ProjectMetadata.TAG_PROJECT_VISION);
		importField(projectSumaryNode, PLANNING_TEAM_COMMENT, metadataRef, ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT);
		importField(projectSumaryNode, LESSONS_LEARNED, metadataRef, ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
		
		String[] ecoRegionElementHierarchy = new String[] {CONSERVATION_PROJECT, PROJECT_SUMMARY, ECOREGIONS, ECOREGION_CODE}; 
		String[] allEcoregionCodes = extractNodesAsList(generatePath(ecoRegionElementHierarchy));
		setData(metadataRef, ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, extractEcoregions(allEcoregionCodes, TncTerrestrialEcoRegionQuestion.class).toString());
		setData(metadataRef, ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, extractEcoregions(allEcoregionCodes, TncMarineEcoRegionQuestion.class).toString());
		setData(metadataRef, ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, extractEcoregions(allEcoregionCodes, TncFreshwaterEcoRegionQuestion.class).toString());
		
		importCodeListField(generateDataPath(new String[] {CONSERVATION_PROJECT, PROJECT_SUMMARY, COUNTRIES, COUNTRY_CODE}), metadataRef, ProjectMetadata.TAG_COUNTRIES);
		importCodeListField(generateDataPath(new String[] {CONSERVATION_PROJECT, PROJECT_SUMMARY, OUS, OU_CODE}), metadataRef, ProjectMetadata.TAG_TNC_OPERATING_UNITS);
	}
	
	private CodeList extractEcoregions(String[] allEcoregionCodes, Class questionClass)
	{
		ChoiceQuestion question = getProject().getQuestion(questionClass);
		CodeList ecoregionCodes = new CodeList();
		for (int i= 0; i < allEcoregionCodes.length; ++i)
		{
			ChoiceItem choiceItem = question.findChoiceByCode(allEcoregionCodes[i]);
			if (choiceItem != null)
				ecoregionCodes.add(allEcoregionCodes[i]);
		}
		
		return ecoregionCodes;
	}

	private void importCodeListField(String path, ORef objectRef, String tag) throws Exception
	{
		CodeList codes = new CodeList(extractNodesAsList(path));
		setData(objectRef, tag, codes.toString());
	}

	private void importField(Node node, String path, ORef ref, String tag) throws Exception
	{
		importField(node, new String[]{path,}, ref, tag);
	}
	
	private void importField(Node node, String[] elements, ORef ref, String tag) throws Exception 
	{
		String generatedPath = generatePath(elements);
		String data = getXPath().evaluate(generatedPath, node);
		setData(ref, tag, data);
	}
	
	private void setData(ORef ref, String tag, String data) throws Exception
	{
		getProject().setObjectData(ref, tag, data);
	}
	
	public String generateDataPath(String[] pathElements)
	{
		String path = generatePath(pathElements);
		path += "/text()";
		
		return path;
	}

	private String generatePath(String[] pathElements)
	{
		String path = "";
		for (int index = 0; index < pathElements.length; ++index)
		{
			path += getPrefixedElement(pathElements[index]);
			if ((index + 1) < pathElements.length)
				path += "/";
		}
		return path;
	}
	
	public String[] extractNodesAsList(String path) throws Exception
	{
		XPathExpression expression = getXPath().compile(path);
		NodeList nodeList = (NodeList) expression.evaluate(getDocument(), XPathConstants.NODESET);
		Vector<String> nodes = new Vector();
		for (int i = 0; i < nodeList.getLength(); i++) 
		{
			nodes.add(nodeList.item(i).getTextContent());
		}
		
		return nodes.toArray(new String[0]);
	}
	
	public void extractTargets(String path) throws Exception
	{
		XPathExpression expression = getXPath().compile(path);
		NodeList nodeList = (NodeList) expression.evaluate(getDocument(), XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) 
		{
			ORef targetRef = getProject().createObject(Target.getObjectType());
			
			Node node = nodeList.item(i);
			String name = getXPath().evaluate(getPrefixedElement(TARGET_NAME), node);
			getProject().setObjectData(targetRef, Target.TAG_LABEL, name);
			
			String description = getXPath().evaluate(getPrefixedElement(TARGET_DESCRIPTION), node);
			getProject().setObjectData(targetRef, Target.TAG_TEXT, description);
		}
	}
		
	private Node getNode(String path) throws Exception
	{
		XPathExpression expression = getXPath().compile(path);
		return (Node) expression.evaluate(getDocument(), XPathConstants.NODE);
	}
	
	private String getPrefixedElement(String elementName)
	{
		return PREFIX + elementName;
	}
	
	private Project getProject()
	{
		return project;
	}
	
	public Document getDocument()
	{
		return document;
	}

	public XPath getXPath()
	{
		return xPath;
	}
	
	public static void main(String[] args)
	{
		try
		{
			new ConProXmlImporter().importConProProject(new File("c:/temp/Conpro.xml"));
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private Project project;
	private XPath xPath;
	private Document document;
	
	public static final String PREFIX = "cp:";
}
