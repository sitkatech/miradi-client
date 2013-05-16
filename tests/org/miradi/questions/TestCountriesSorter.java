/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.questions;

import java.util.Collections;
import java.util.Vector;

import org.miradi.main.MiradiTestCase;

public class TestCountriesSorter extends MiradiTestCase
{
	public TestCountriesSorter(String name)
	{
		super(name);
	}
	
	public void testComparable()
	{
		verifyFirstElementAfterSort(new String[] {CountriesQuestion.CODE_FOR_OTHER, CountriesQuestion.CODE_FOR_FICTITIOUS, });
		verifyFirstElementAfterSort(new String[] {CountriesQuestion.CODE_FOR_FICTITIOUS, CountriesQuestion.CODE_FOR_OTHER, });
		verifyFirstElementAfterSort(new String[] {"aRandomCode", CountriesQuestion.CODE_FOR_FICTITIOUS, CountriesQuestion.CODE_FOR_OTHER, });
		verifyFirstElementAfterSort(new String[] {CountriesQuestion.CODE_FOR_FICTITIOUS, "aRandomCode", CountriesQuestion.CODE_FOR_OTHER, });
		verifyFirstElementAfterSort(new String[] {CountriesQuestion.CODE_FOR_FICTITIOUS, CountriesQuestion.CODE_FOR_OTHER, "aRandomCode", });
	}

	private void verifyFirstElementAfterSort(String[] codes)
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		for(String code : codes)
		{
			choices.add(new ChoiceItem(code, code));
		}
		Collections.sort(choices, new CountriesSorter());
		
		assertEquals("incorrectly sorted?", CountriesQuestion.CODE_FOR_OTHER, choices.get(0).getCode());
		assertEquals("incorrectly sorted?", CountriesQuestion.CODE_FOR_FICTITIOUS, choices.get(1).getCode());
	}
	
}
