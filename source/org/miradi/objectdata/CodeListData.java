/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectdata;

import java.text.ParseException;

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class CodeListData extends ObjectData
{
	public CodeListData(String tagToUse, ChoiceQuestion questionToUse)
	{
		super(tagToUse);
		question = questionToUse;
		codes = new CodeList();
	}
	
	public void set(String newValue) throws ParseException
	{
		set(new CodeList(newValue));
	}
	
	public String get()
	{
		return codes.toString();
	}
	
	public void set(CodeList newCodes)
	{
		codes = newCodes;
	}
	
	public CodeList getCodeList()
	{
		return codes;
	}
	
	public int size()
	{
		return codes.size();
	}
	
	public String get(int index)
	{
		return codes.get(index);
	}
	
	public void add(String code)
	{
		codes.add(code);
	}
	
	public void removeCode(String code)
	{
		codes.removeCode(code);
	}
	
	public void toXml(UnicodeWriter out) throws Exception
	{
		startTagToXml(out);
		out.writeln("<CodeList>");
		for(int i = 0; i < codes.size(); ++i)
		{
			String code = codes.get(i);
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

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof CodeListData))
			return false;
		
		CodeListData other = (CodeListData)rawOther;
		return codes.equals(other.codes);
	}

	public int hashCode()
	{
		return codes.hashCode();
	}
	
	private ChoiceQuestion question;
	private CodeList codes;
}
