/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.utils.HtmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//FIXME urgent - this class is under construction
public class Xmpz2ForwardMigration implements Xmpz2XmlConstants
{
	public Xmpz2ForwardMigration(Document documentToUse)
	{
		document = documentToUse;
	}
	
	public Document migrate() throws Exception
	{
		//System.out.println(HtmlUtilities.toXmlString(getDocument()));
		Element rootElement = getDocument().getDocumentElement();
		final String nameSpaceAttributeName = XMLNS + COLON + RAW_PREFIX;
		System.out.println("nameSpace=" + rootElement.getAttribute(nameSpaceAttributeName));
		String newNameSpaceValue = PARTIAL_NAME_SPACE + "3333";
		rootElement.setAttribute(nameSpaceAttributeName, newNameSpaceValue);
		
		//getDocument().
		System.out.println(HtmlUtilities.toXmlString(getDocument()));
		
		return null;
	}

	private Document getDocument()
	{
		return document;
	}
	
	private Document document;
}
