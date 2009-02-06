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
along with Miradi.  If not, see <http://www.gnu.org/licenses/>.*/ 
package org.miradi.questions;

import java.awt.Color;
import java.io.IOException;

import org.martus.util.UnicodeWriter;
import org.miradi.main.EAM;
import org.miradi.utils.CodeList;
import org.miradi.utils.ColorManager;


public abstract class ChoiceQuestion
{
	public int size()
	{
		return getChoices().length;
	}
	
	public String getCode(int index)
	{
		return getChoices()[index].getCode();
	}
	
	public CodeList getAllCodes()
	{
		CodeList allCodes = new CodeList();
		for (int index = 0; index < size(); ++index)
		{
			allCodes.add(getCode(index));
		}
		
		return allCodes;
	}
	
	abstract public ChoiceItem[] getChoices();
	
	public ChoiceItem findChoiceByCode(String code)
	{
		int index = findIndexByCode(code);
		if (index<0)
			return null;
		return getChoices()[index];
	}
	
	public ChoiceItem findChoiceByLabel(String labelToFind)
	{
		int index = findIndexByLabel(labelToFind);
		if (index < 0)
			return null;
		
		return getChoices()[index];
	}
	
	public int findIndexByCode(String code)
	{
		ChoiceItem[] choices = getChoices();
		for(int i = 0; i < choices.length; ++i)
		{
			if(choices[i].getCode().equals(code))
				return i;
		}
		
		return -1;
	}
	
	public int findIndexByLabel(String labelToFind)
	{
		ChoiceItem[] choices = getChoices();
		for(int i = 0; i < choices.length; ++i)
		{
			if(choices[i].getLabel().equals(labelToFind))
				return i;
		}
		
		return -1;
	}
	
	public String getValue(String code)
	{
		ChoiceItem choice = findChoiceByCode(code);
		if(choice == null)
			return "[" + EAM.text("Unknown code: ") + code + "]";
		return choice.getLabel();
	}
	
	public void toXml(UnicodeWriter out) throws IOException
	{
		out.writeln("<Choices question='" + getClass().getSimpleName() + "'>");
		ChoiceItem[] choices = getChoices();
		for(int i = 0; i < choices.length; ++i)
		{
			choices[i].toXml(out);
		}
		out.writeln("</Choices>");
	}

	public static final Color COLOR_ALERT = Color.RED;
	public static final Color COLOR_CAUTION = ColorManager.DARK_YELLOW;
	public static final Color COLOR_OK = ColorManager.LIGHT_GREEN;
	public static final Color COLOR_GREAT = ColorManager.DARK_GREEN;
}
