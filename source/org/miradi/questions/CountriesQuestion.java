/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import java.util.Collections;

import org.miradi.objecthelpers.TwoLevelFileLoader;
import org.miradi.objecthelpers.WwfCountriesFileLoader;

public class CountriesQuestion extends MultiSelectTwoLevelQuestion
{
	public CountriesQuestion()
	{
		super(new WwfCountriesFileLoader(TwoLevelFileLoader.COUNTRIES_FILE));
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		sortChoices();
		return super.getChoices();
	}
	
	@Override
	public void sortChoices()
	{
		Collections.sort(getRawChoices(), new CountriesSorter());
	}
	
	public static final String CODE_FOR_FICTITIOUS = "XXX";
	public static final String CODE_FOR_OTHER = "ZZZ";
}
