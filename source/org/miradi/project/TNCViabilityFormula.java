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

package org.miradi.project;

import java.util.Arrays;
import java.util.HashSet;

import org.miradi.questions.StatusQuestion;
import org.miradi.utils.CodeList;

public class TNCViabilityFormula
{
	public static double getValueFromRatingCode(String code)
	{
		if(code.equals(StatusQuestion.UNSPECIFIED))
			return 0;
		if(code.equals(StatusQuestion.POOR))
			return 1;
		if(code.equals(StatusQuestion.FAIR))
			return 2.5;
		if(code.equals(StatusQuestion.GOOD))
			return 3.5;
		if(code.equals(StatusQuestion.VERY_GOOD))
			return 4;
		
		throw new UnexpectedValueException("getValueFromRatingCode", code);
	}

	public static String getRatingCodeFromValue(double d)
	{
		if(d >= 3.75)
			return StatusQuestion.VERY_GOOD;
		if(d >= 3)
			return StatusQuestion.GOOD;
		if(d >= 1.75)
			return StatusQuestion.FAIR;
		return StatusQuestion.POOR;
	}

	public static String getAverageRatingCode(CodeList codes)
	{
		int validCount = 0;
		double total = 0;
		for(int i = 0; i < codes.size(); ++i)
		{
			String code = codes.get(i);
			if(code.equals(StatusQuestion.UNSPECIFIED))
				continue;
			++validCount;
			total += getValueFromRatingCode(code);
		}
		
		if(validCount == 0)
			return StatusQuestion.UNSPECIFIED;
		
		double average = total/validCount;
		return getRatingCodeFromValue(average);
	}

	public static String getTotalCategoryRatingCode(CodeList keaCodes)
	{
		HashSet codes = new HashSet(Arrays.asList(keaCodes.toArray()));
		if(codes.size() == 0)
			return StatusQuestion.UNSPECIFIED;
		if(codes.contains(StatusQuestion.POOR))
			return StatusQuestion.POOR;
		if(codes.contains(StatusQuestion.FAIR))
			return StatusQuestion.FAIR;
		
		int goods = 0;
		int veryGoods = 0;
		for(int i = 0; i < keaCodes.size(); ++i)
		{
			String code = keaCodes.get(i);
			if(code.equals(StatusQuestion.UNSPECIFIED))
				continue;
			
			if(code.equals(StatusQuestion.GOOD))
				++goods;
			else if(code.equals(StatusQuestion.VERY_GOOD))
				++veryGoods;
			else
			{
				String method = "getTotalCategoryRatingCode";
				throw new UnexpectedValueException(code, method);
			}
		}
		
		if(goods + veryGoods == 0)
			return StatusQuestion.UNSPECIFIED;
		
		if(veryGoods > goods)
			return StatusQuestion.VERY_GOOD;
		return StatusQuestion.GOOD;
	}

	static class UnexpectedValueException extends RuntimeException
	{
		public UnexpectedValueException(String code, String method)
		{
			super(method + " found unexpected value: " + code);
		}
	}
}
