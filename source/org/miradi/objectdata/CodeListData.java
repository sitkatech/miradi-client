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
package org.miradi.objectdata;

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.questions.ChoiceQuestion;

public class CodeListData extends AbstractStringListData
{
	public CodeListData(String tagToUse, ChoiceQuestion questionToUse)
	{
		super(tagToUse);
		
		question = questionToUse;
	}
	
	public boolean isCodeListData()
	{
		return true;
	}
	
	public ChoiceQuestion getChoiceQuestion()
	{
		return question;
	}
	
	public void toXml(UnicodeWriter out) throws Exception
	{
		startTagToXml(out);
		out.writeln("<CodeList>");
		for(int i = 0; i < getCodeList().size(); ++i)
		{
			String code = getCodeList().get(i);
			String value = question.getValue(code);
			
			out.writeln("<Entry>");
			out.writeln("<Code>" + XmlUtilities.getXmlEncoded(code) + "</Code>");
			out.writeln("<Value>" + XmlUtilities.getXmlEncoded(value) + "</Value>");
			out.writeln("</Entry>");
		}
		out.writeln();
		out.writeln("</CodeList>");
		endTagToXml(out);
	}
	
	private ChoiceQuestion question;
}
