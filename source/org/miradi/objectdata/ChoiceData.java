/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectdata;

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.questions.ChoiceQuestion;


public class ChoiceData extends StringData
{
	public ChoiceData(String tagToUse, ChoiceQuestion questionToUse)
	{
		super(tagToUse);
		question = questionToUse;
	}
	
	public void toXml(UnicodeWriter out) throws Exception
	{
		String code = get();
		String value = question.getValue(code);

		startTagToXml(out);
		out.writeln("<code>");
		out.write(XmlUtilities.getXmlEncoded(code));
		out.writeln("</code>");
		out.writeln("<value>");
		out.write(XmlUtilities.getXmlEncoded(value));
		out.writeln("</value>");
		endTagToXml(out);
	}

	private ChoiceQuestion question;
}
