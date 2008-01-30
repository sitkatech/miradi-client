/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;


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
		String value = getValue(code);

		startTagToXml(out);
		out.writeln("<code>");
		out.write(XmlUtilities.getXmlEncoded(code));
		out.writeln("</code>");
		out.writeln("<value>");
		out.write(XmlUtilities.getXmlEncoded(value));
		out.writeln("</value>");
		endTagToXml(out);
	}

	private String getValue(String code)
	{
		if(question == null)
		{
			EAM.logError("ChoiceData without question: " + getTag());
			return "";
		}
		ChoiceItem choice = question.findChoiceByCode(code);
		if(choice == null)
			return "[Unknown code: " + code + "]";
		String value = choice.getLabel();
		return value;
	}
	
	private ChoiceQuestion question;
}
