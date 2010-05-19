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
package org.miradi.xml;

import java.util.Vector;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.exceptions.CpmzVersionTooOldException;
import org.miradi.exceptions.UnsupportedNewVersionSchemaException;
import org.miradi.exceptions.ValidationException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.Project;
import org.miradi.xml.wcs.WcsMiradiXmlValidator;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

abstract public class AbstractXmlImporter 
{
	public AbstractXmlImporter(Project projectToFill) throws Exception
	{
		project = projectToFill;
	}
		
	public void importProject(InputStreamWithSeek projectAsInputStream) throws Exception
	{
		loadXml(projectAsInputStream);
		importXml();
	}
	
	private void loadXml(InputStreamWithSeek projectAsInputStream) throws Exception
	{
		InputSource inputSource = new InputSource(projectAsInputStream);
		document = createDocument(inputSource);
				
		String nameSpaceUri = document.getDocumentElement().getNamespaceURI();
		if (!isSameNameSpace(nameSpaceUri))
		{
			throw new Exception("Name space mismatch should be: " + getPartialNameSpace() + " <br> however it is: " + nameSpaceUri); 
		}
				
		if (isUnsupportedNewVersion(nameSpaceUri))
		{
			throw new UnsupportedNewVersionSchemaException();
		}
		
		if (isUnsupportedOldVersion(nameSpaceUri))
		{
			throw new CpmzVersionTooOldException();
		}
		
		projectAsInputStream.seek(0);			
		if (!new WcsMiradiXmlValidator().isValid(projectAsInputStream))
		{
			throw new ValidationException(EAM.text("File to import does not validate."));
		}
		
		xPath = createXPath();
	}

	protected boolean isTrue(String value)
	{
		if (value.length() == 0)
			return false;
		
		if (value.equals(BooleanData.BOOLEAN_TRUE))
			return true;
		
		if (value.equals(BooleanData.BOOLEAN_FALSE))
			return false;
		
		return Boolean.parseBoolean(value);
	}

	private Document createDocument(InputSource inputSource) throws Exception
	{
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		documentFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		
		return documentBuilder.parse(inputSource);
	}

	private XPath createXPath()
	{
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath  thisXPath = xPathFactory.newXPath();
		thisXPath.setNamespaceContext(getNameSpaceContext());
		
		return thisXPath;
	}

	protected void importField(Node node, String path, ORef ref, String tag) throws Exception
	{
		importField(node, new String[]{path,}, ref, tag);
	}
	
	private void importField(Node node, String[] elements, ORef ref, String tag) throws Exception 
	{
		String data = getPathData(node, elements);
		importField(ref, tag, data);
	}

	private String getPathData(Node node, String[] elements) throws XPathExpressionException
	{
		String generatedPath = generatePath(node, elements);
		return getXPath().evaluate(generatedPath, node);
	}

	private void importField(ORef ref, String tag, String data)	throws Exception
	{
		setData(ref, tag, data);
	}
		
	private void setData(ORef ref, String tag, String data) throws Exception
	{
		getProject().setObjectData(ref, tag, data.trim());
	}
	
	protected void setData(ORef ref, String tag, ORefList refList) throws Exception
	{
		getProject().setObjectData(ref, tag, refList.toString());
	}

	public String generatePath(Node parentNode, String[] pathElements)
	{
		StringBuffer path = new StringBuffer();
		for (int index = 0; index < pathElements.length; ++index)
		{
			String elementName = getSafeParentNodeName(parentNode) + pathElements[index];
			
			String prefixedElement = getPrefixedElement(elementName);
			path.append(prefixedElement);
			if ((index + 1) < pathElements.length)
				path.append("/");
		}
		
		return path.toString();
	}

	private String getSafeParentNodeName(Node parentNode)
	{
		if (parentNode == null)
			return "";
		
		return parentNode.getNodeName();
	}
	
	public String[] extractNodesAsList(String path) throws Exception
	{
		XPathExpression expression = getXPath().compile(path);
		NodeList nodeList = (NodeList) expression.evaluate(getDocument(), XPathConstants.NODESET);
		Vector<String> nodes = new Vector();
		for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); ++nodeIndex) 
		{
			nodes.add(nodeList.item(nodeIndex).getTextContent());
		}
		
		return nodes.toArray(new String[0]);
	}
	
	protected String getAttributeValue(Node elementNode, String attributeName)
	{
		NamedNodeMap attributes = elementNode.getAttributes();
		Node attributeNode = attributes.getNamedItem(attributeName);
		return attributeNode.getNodeValue();
	}
	
	protected ORef getNodeAsRef(Node node, String element, int type) throws Exception
	{
		String idAsString = getNodeContent(node, element);
		return new ORef(type, new BaseId(idAsString));
	}
	
	protected Node getNode(Node node, String element) throws Exception
	{
		String path = generatePath(node, new String[]{element});
		XPathExpression expression = getXPath().compile(path);
		return (Node) expression.evaluate(node, XPathConstants.NODE);
	}
	
	public Node getParentNode(Node node, String element) throws Exception
	{
		String path = generatePath(null, new String[]{element});
		XPathExpression expression = getXPath().compile(path);
		return (Node) expression.evaluate(node, XPathConstants.NODE);
	}
	
	public Node getRootNode() throws Exception
	{
		return getNode(generatePath(null, new String[]{getRootNodeName()}));
	}
	
	private Node getNode(String path) throws Exception
	{
		XPathExpression expression = getXPath().compile(path);
		return (Node) expression.evaluate(getDocument(), XPathConstants.NODE);
	}
	
	private String getNodeContent(Node node, String element) throws Exception
	{
		Node foundNode = getNode(node, element);
		return getSafeNodeContent(foundNode);
	}

	private String getSafeNodeContent(Node foundNode)
	{
		if (foundNode == null)
			return "";
		
		return foundNode.getTextContent();
	}
	
	private NodeList getNodes(Node node, String[] pathElements) throws Exception
	{
		String path = generatePath(node, pathElements);
		XPathExpression expression = getXPath().compile(path);
		
		return (NodeList) expression.evaluate(node, XPathConstants.NODESET);
	}
	
	protected NodeList getNodes(Node node, String containerName, String contentName) throws Exception
	{
		return getNodes(node, new String[]{containerName, contentName});
	}
	
	private String getPrefixedElement(String elementName)
	{
		return getPrefix() + elementName;
	}
	
	private boolean isUnsupportedNewVersion(String nameSpaceUri) throws Exception
	{
		return getSchemaVersionToImport(nameSpaceUri).compareTo(getNameSpaceVersion()) > 0;
	}

	private boolean isUnsupportedOldVersion(String nameSpaceUri) throws Exception
	{
		return getSchemaVersionToImport(nameSpaceUri).compareTo(getNameSpaceVersion()) < 0;
	}

	private boolean isSameNameSpace(String nameSpaceUri) throws Exception
	{
		return nameSpaceUri.startsWith(getPartialNameSpace());
	}
	
	private String getSchemaVersionToImport(String nameSpaceUri) throws Exception
	{
		int lastSlashIndexBeforeVersion = nameSpaceUri.lastIndexOf("/");
		String versionAsString = nameSpaceUri.substring(lastSlashIndexBeforeVersion + 1, nameSpaceUri.length());
		
		return versionAsString;
	}
	
	protected ORef getProjectMetadataRef()
	{
		return getProject().getMetadata().getRef();
	}
	
	public Document getDocument()
	{
		return document;
	}

	public XPath getXPath()
	{
		return xPath;
	}
	
	protected Project getProject()
	{
		return project;
	}
	
	abstract protected void importXml() throws Exception;
	
	abstract protected String getRootNodeName();
	
	abstract protected String getNameSpaceVersion();
	
	abstract protected String getPartialNameSpace();
	
	abstract protected String getPrefix();
	
	abstract protected NamespaceContext getNameSpaceContext();

	private Project project;
	private XPath xPath;
	private Document document;	
}
