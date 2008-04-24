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
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.xml.conpro.ConProMiradiXml;
import org.miradi.xml.conpro.exporter.ConProMiradiXmlValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


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

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			documentFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			document =  documentBuilder.parse(fileToImport);

			XPathFactory xPathFactory = XPathFactory.newInstance();
			xPath = xPathFactory.newXPath();
			xPath.setNamespaceContext(new ConProMiradiNameSpaceContext());

			importProjectSummaryElement();
			importTargetElements();
		}
		finally
		{
			fileInputStream.close();
		}
	}

	private void importTargetElements() throws Exception 
	{
		//TODO still under development
		extractNodesAsNewBaseObjects(TARGETS, TARGET);
	}

	private void importProjectSummaryElement() throws Exception
	{
		ORef metadataRef = getProject().getMetadata().getRef();
		loadData(PROJECT_SUMMARY, NAME, metadataRef, ProjectMetadata.TAG_PROJECT_NAME);
		loadData(PROJECT_SUMMARY, START_DATE, metadataRef, ProjectMetadata.TAG_START_DATE);
		loadData(PROJECT_SUMMARY, AREA_SIZE, metadataRef, ProjectMetadata.TAG_TNC_SIZE_IN_HECTARES);
		loadData(GEOSPATIAL_LOCATION, LATITUDE, metadataRef, ProjectMetadata.TAG_PROJECT_LATITUDE);
		loadData(GEOSPATIAL_LOCATION, LONGITUDE, metadataRef, ProjectMetadata.TAG_PROJECT_LONGITUDE);
		loadData(PROJECT_SUMMARY, DESCRIPTION_COMMENT, metadataRef, ProjectMetadata.TAG_PROJECT_SCOPE);
		loadData(PROJECT_SUMMARY, GOAL_COMMENT, metadataRef, ProjectMetadata.TAG_PROJECT_VISION);
		loadData(PROJECT_SUMMARY, PLANNING_TEAM_COMMENT, metadataRef, ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT);
		loadData(PROJECT_SUMMARY, LESSONS_LEARNED, metadataRef, ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
		
		String[] allEcoregionCodes = extractNodesAsList(ECOREGIONS, ECOREGION_CODE);
		loadData(metadataRef, ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, extractEcoregions(allEcoregionCodes, TNC_ECOREGION_TERRESTRIAL_PREFIX).toString());
		loadData(metadataRef, ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, extractEcoregions(allEcoregionCodes, TNC_ECOREGION_MARINE_PREFIX).toString());
		loadData(metadataRef, ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, extractEcoregions(allEcoregionCodes, TNC_ECOREGION_FRESHWATER_PREFIX).toString());
		
		loadCodeListData(COUNTRIES, COUNTRY_CODE, metadataRef, ProjectMetadata.TAG_COUNTRIES);
		loadCodeListData(OUS, OU_CODE, metadataRef, ProjectMetadata.TAG_TNC_OPERATING_UNITS);
	}
	
	private CodeList extractEcoregions(String[] allEcoregionCodes, String ecoregionType)
	{
		CodeList ecoregionCodes = new CodeList();
		for (int i= 0; i < allEcoregionCodes.length; ++i)
		{
			if (allEcoregionCodes[i].startsWith(ecoregionType))
				ecoregionCodes.add(allEcoregionCodes[i]);
		}
		
		return ecoregionCodes;
	}

	private void loadCodeListData(String parentElement, String childElement, ORef objectRef, String tag) throws Exception
	{
		CodeList codes = new CodeList(extractNodesAsList(parentElement, childElement));
		loadData(objectRef, tag, codes.toString());
	}
	
	private void loadData(String parentElement, String childElement, ORef objectRef, String tag) throws Exception
	{
		String path = generateXPath(new String[] {parentElement, childElement});
		XPathExpression expression = getXPath().compile(path);
		String data = expression.evaluate(getDocument());
		loadData(objectRef, tag, data);
	}
	
	private void loadData(ORef ref, String tag, String data) throws Exception
	{
		getProject().setObjectData(ref, tag, data);
	}
	
	public String generateXPath(String[] pathElements)
	{
		String path = "//";
		for (int index = 0; index < pathElements.length; ++index)
		{
			path += getPrefixedElement(pathElements[index]);
			if (index < pathElements.length);
				path += "/";
		}
		path += "text()";
		
		return path;
	}
	
	public String[] extractNodesAsList(String parentElement, String childElement) throws Exception
	{
		XPathExpression expression = getXPath().compile("//" + getPrefixedElement(parentElement) + "/"+ getPrefixedElement(childElement));
		NodeList nodeList = (NodeList) expression.evaluate(getDocument(), XPathConstants.NODESET);
		Vector<String> nodes = new Vector();
		for (int i = 0; i < nodeList.getLength(); i++) 
		{
			nodes.add(nodeList.item(i).getTextContent());
		}
		
		return nodes.toArray(new String[0]);
	}
	
	public void extractNodesAsNewBaseObjects(String parentElement, String childElement) throws Exception
	{
		XPathExpression expression = getXPath().compile("//" + getPrefixedElement(parentElement) + "/"+ getPrefixedElement(childElement));
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
	public static final String TNC_ECOREGION_TERRESTRIAL_PREFIX = "1";
	public static final String TNC_ECOREGION_MARINE_PREFIX = "2";
	public static final String TNC_ECOREGION_FRESHWATER_PREFIX = "3";
}
