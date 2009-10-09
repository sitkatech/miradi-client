/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.wcs;

import org.miradi.main.TestCaseWithProject;

public class TestWcsExporter extends TestCaseWithProject
{
	public TestWcsExporter(String name)
	{
		super(name);
	}

//FIXME urgent - wcs exporter test commented out 
//	@Override
//	public void setUp() throws Exception
//	{
//		super.setUp();
//		
//		getProject().populateEverything();
//				 
//		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//		UnicodeWriter writer = new UnicodeWriter(bytes);
//		new WcsXmlExporter(getProject()).exportProject(writer);
//		
//		writer.flush();
//		bytes.close();
//		String xml = new String(bytes.toByteArray(), "UTF-8");
//		
//		InputSource inputSource = new InputSource(xml);
//		document = createDocument(inputSource);
//		
//		InputStreamWithSeek inputStream = new StringInputStreamWithSeek(xml);
//		inputStream.seek(0);
//		xPath = createXPath();
//	}
//	
//	public void testValidate() throws Exception
//	{
//		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//		UnicodeWriter writer = new UnicodeWriter(bytes);
//		new WcsXmlExporter(getProject()).exportProject(writer);
//		writer.flush();
//		bytes.close();
//		String xml = new String(bytes.toByteArray(), "UTF-8");
//		InputStreamWithSeek inputStream = new StringInputStreamWithSeek(xml);		
//		if (!new ConProMiradiXmlValidator().isValid(inputStream))
//		{
//			throw new ValidationException(EAM.text("File to import does not validate."));
//		}	
//	}
//	
//	public void testBasics() throws Exception
//	{
//		
//		Node projectSumaryNode = getNode(getRootNode(), WcsXmlConstants.PROJECT_SUMMARY);
//		Node urlNode = getNode(projectSumaryNode, "ProjectSummaryProjectURL");
//		assertEquals("wrong url value?", "www.miradi.com", urlNode.getNodeValue());		
//	}
//	
//	private Document createDocument(InputSource inputSource) throws ParserConfigurationException, SAXException, IOException
//	{
//		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
//		documentFactory.setNamespaceAware(true);
//		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
//		
//		return documentBuilder.parse(inputSource);
//	}
//
//	private XPath createXPath()
//	{
//		XPathFactory xPathFactory = XPathFactory.newInstance();
//		XPath  thisXPath = xPathFactory.newXPath();
//		thisXPath.setNamespaceContext(new ConProMiradiNameSpaceContext());
//		
//		return thisXPath;
//	}
//	
//	private Node getRootNode() throws Exception
//	{
//		return getNode(generatePath(new String[]{WcsXmlConstants.CONSERVATION_PROJECT}));
//	}
//	
//	private Node getNode(String path) throws Exception
//	{
//		XPathExpression expression = getXPath().compile(path);
//		return (Node) expression.evaluate(getDocument(), XPathConstants.NODE);
//	}
//	
//	private Node getNode(Node node, String element) throws Exception
//	{
//		String path = generatePath(new String[]{element});
//		XPathExpression expression = getXPath().compile(path);
//		return (Node) expression.evaluate(node, XPathConstants.NODE);
//	}
//	
//	public String generatePath(String[] pathElements)
//	{
//		String path = "";
//		for (int index = 0; index < pathElements.length; ++index)
//		{
//			path += getPrefixedElement(pathElements[index]);
//			if ((index + 1) < pathElements.length)
//				path += "/";
//		}
//		return path;
//	}
//	
//	private String getPrefixedElement(String elementName)
//	{
//		return WcsXmlConstants.PREFIX + elementName;
//	}
//	
//	private XPath getXPath()
//	{
//		return xPath;
//	}
//	
//	private Document getDocument()
//	{
//		return document;
//	}
//	
//	private XPath xPath;
//	private Document document;
}
