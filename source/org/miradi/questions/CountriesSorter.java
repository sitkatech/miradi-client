/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.questions;

import java.util.Comparator;

public class CountriesSorter implements Comparator<ChoiceItem>
{
	public int compare(ChoiceItem choiceItem1, ChoiceItem choiceItem2)
	{
		if (choiceItem1.getLabel().equalsIgnoreCase(choiceItem2.getLabel()))
			return 0;

		if (isOther(choiceItem1) && isFictitious(choiceItem2))
			return -1;
		
		if (isFictitious(choiceItem1) && isOther(choiceItem2))
			return 1;
		
		if (isOther(choiceItem1) && !isFictitious(choiceItem2))
			return -1;
		
		if (!isOther(choiceItem1) && isFictitious(choiceItem2))
			return 1;
		
		if (isFictitious(choiceItem1) && !isOther(choiceItem2))
			return -1;
		
		if (!isFictitious(choiceItem1) && isOther(choiceItem2))
			return 1;
		
		return choiceItem1.getLabel().compareToIgnoreCase(choiceItem2.getLabel());
	}

	private boolean isOther(ChoiceItem choiceItem)
	{
		return choiceItem.isSameCode(CountriesQuestion.CODE_FOR_OTHER);
	}

	private boolean isFictitious(ChoiceItem choiceItem)
	{
		return choiceItem.isSameCode(CountriesQuestion.CODE_FOR_FICTITIOUS);
	}
}