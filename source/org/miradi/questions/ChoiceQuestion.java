/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;


public abstract class ChoiceQuestion implements Comparable<ChoiceQuestion>
{
	public ChoiceQuestion()
	{
		this("");
	}

	public ChoiceQuestion(String questionDescriptionToUse)
	{
		questionDescription = questionDescriptionToUse;
	}
	
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
	
	public CodeList getCodesAsReadableCodes()
	{
		CodeList convertedToReadableCodes = new CodeList();
		CodeList allCodes = getAllCodes();
		for(int index = 0; index < allCodes.size(); ++index)
		{
			String code = allCodes.get(index);
			convertedToReadableCodes.add(convertToReadableCode(code));
		}
		
		return convertedToReadableCodes;
	}
	
	public Vector<ChoiceItem> getChoicesAsVector()
	{
		return new Vector<ChoiceItem>(Arrays.asList(getChoices()));
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
	
	public int findIndexByCode(ChoiceItem choiceItem)
	{
		return findIndexByCode(choiceItem.getCode());
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
	
	public String getQuestionDescription()
	{
		return questionDescription;
	}

	public ChoiceItem findChoiceByNumericValue(int value)
	{
		String code = "";
		if(value > 0)
			code = Integer.toString(value);
			
		return findChoiceByCode(code);
	}
	
	public Comparator<ChoiceItem> getComparator()
	{
		return new ChoiceItemLabelComparator();
	}
	
	public String convertToReadableCode(String code)
	{
		if (code.length() == 0 && hasReadableAlternativeDefaultCode())
			return getReadableAlternativeDefaultCode();
		
		return code;
	}
	
	public String convertToInternalCode(String code)
	{
		if (hasReadableAlternativeDefaultCode() && isReadableAlternativeDefaultCode(code))
			return getInternalDefaultCode(code);
		
		return code;
	}
	
	protected String getInternalDefaultCode(String code)
	{
		return "";
	}

	protected boolean isReadableAlternativeDefaultCode(String code)
	{
		if (code.equals(getReadableAlternativeDefaultCode()))
			return true;
		
		return false;
	}

	protected boolean hasReadableAlternativeDefaultCode()
	{
		return false;
	}
	
	protected String getReadableAlternativeDefaultCode()
	{
		throw new RuntimeException("Question should override this method if it provides an alternative to a blank default code.");
	}
	
	public void reloadQuestion()
	{
	}
	
	public void reloadQuestion(Project project)
	{
	}

	public boolean hasAdditionalText()
	{
		return false;
	}
	
	public boolean hasLongDescriptionProvider()
	{
		return false;
	}
	
	public boolean doesCodeExist(final String code)
	{
		ChoiceItem choiceItem = findChoiceByCode(code);
		if (choiceItem == null)
			return false;
		
		return true;
	}
	
	public int compareTo(ChoiceQuestion other)
	{
		return getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
	}
	
	public boolean isProjectBasedDynamicQuestion()
	{
		return false;
	}
	
	public boolean canSelectSingleOnly()
	{
		return !canSelectMultiple();
	}
	
	abstract public boolean canSelectMultiple();

	private String questionDescription;
}
