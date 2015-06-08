/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import org.miradi.icons.BiophysicalFactorIcon;
import org.miradi.icons.ContributingFactorIcon;
import org.miradi.main.EAM;

public class FactorModeQuestion extends StaticChoiceQuestion
{
	public FactorModeQuestion()
	{
		super(getChoiceItems());
	}

	public static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem(DEFAULT_CODE, EAM.text("Contributing Factors and Intermediate Results Only (default)"), new ContributingFactorIcon()),
				new ChoiceItem(BIOPHYSICAL_FACTOR_CODE, EAM.text("Include Biophysical Factors and Results"), new BiophysicalFactorIcon()),
		};
	}
	
	@Override
	protected boolean hasReadableAlternativeDefaultCode()
	{
		return true;
	}
	
	@Override
	protected String getReadableAlternativeDefaultCode()
	{
		return READABLE_DEFAULT_CODE;
	}
	
	@Override
	public String convertToReadableCode(String code)
	{
		if (code.equals(DEFAULT_CODE))
			return READABLE_DEFAULT_CODE;
		
		if (code.equals(BIOPHYSICAL_FACTOR_CODE))
			return READABLE_BIOPHYSICAL_FACTOR_CODE;
		
		return getReadableAlternativeDefaultCode();
	}
	
	@Override
	public String convertToInternalCode(String code)
	{
		if (code.equals(READABLE_DEFAULT_CODE))
			return DEFAULT_CODE;
		
		if (code.equals(READABLE_BIOPHYSICAL_FACTOR_CODE))
			return BIOPHYSICAL_FACTOR_CODE;
		
		return code;
	}

	
	public static final String DEFAULT_CODE = "";
	public static final String BIOPHYSICAL_FACTOR_CODE = "BiophysicalFactorMode";
	
	public static final String READABLE_DEFAULT_CODE = "ContributingFactorsAndIntermediateResultsOnly";
	public static final String READABLE_BIOPHYSICAL_FACTOR_CODE = "IncludeBiophysicalFactorsAndResults";
}
