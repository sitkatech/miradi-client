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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.xml.conpro.exporter.ConProMiradiXmlValidator;
import org.w3c.dom.Document;


public class ConProXmlImporter
{
	public void populateProjectFromFile(File fileToImport, Project projectToFill) throws Exception
	{
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
			Document document =  documentBuilder.parse(fileToImport);

			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			xPath.setNamespaceContext(new ConProMiradiNameSpaceContext());

			String result = xPath.evaluate("//cp:project_summary/cp:name/text()", document );
			System.out.println("value = " + result);

//			TODO, code is only reference on how to use list of nodes.  Remove when done importing.
//			XPathExpression expr = xpath.compile("//project_summary/name/text()");
//			Object result = expr.evaluate(doc, XPathConstants.NODESET);
//			NodeList nodes = (NodeList) result;
//			System.out.println(nodes.getLength());
//			for (int i = 0; i < nodes.getLength(); i++) 
//			{
//			System.out.println(nodes.item(i).getNodeValue()); 
//			}

		}
		finally
		{
			fileInputStream.close();
		}
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
}
