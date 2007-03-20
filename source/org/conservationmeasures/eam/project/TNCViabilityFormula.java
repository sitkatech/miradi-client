/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.project;

import java.util.Arrays;
import java.util.HashSet;

public class TNCViabilityFormula
{
	public static double getValueFromRatingCode(String code)
	{
		if(code.equals(UNSPECIFIED))
			return 0;
		if(code.equals(POOR))
			return 1;
		if(code.equals(FAIR))
			return 2.5;
		if(code.equals(GOOD))
			return 3.5;
		if(code.equals(VERY_GOOD))
			return 4;
		
		throw new UnexpectedValueException("getValueFromRatingCode", code);
	}

	public static String getRatingCodeFromValue(double d)
	{
		if(d >= 3.75)
			return VERY_GOOD;
		if(d >= 3)
			return GOOD;
		if(d >= 1.75)
			return FAIR;
		if(d >= 0)
			return POOR;
		
		return UNSPECIFIED;
	}

	public static String getAverageRatingCode(String[] codes)
	{
		int total = 0;
		for(int i = 0; i < codes.length; ++i)
		{
			if(codes[i].equals(UNSPECIFIED))
				return UNSPECIFIED;
			total += getValueFromRatingCode(codes[i]);
		}
		
		double average = ((double)total)/codes.length;
		
		return getRatingCodeFromValue(average);
	}

	public static String getTotalCategoryRatingCode(String[] keaCodes)
	{
		HashSet codes = new HashSet(Arrays.asList(keaCodes));
		if(codes.size() == 0 || codes.contains(UNSPECIFIED))
			return UNSPECIFIED;
		if(codes.contains(POOR))
			return POOR;
		if(codes.contains(FAIR))
			return FAIR;
		
		int goods = 0;
		int veryGoods = 0;
		for(int i = 0; i < keaCodes.length; ++i)
		{
			String code = keaCodes[i];
			if(code.equals(GOOD))
				++goods;
			else if(code.equals(VERY_GOOD))
				++veryGoods;
			else
			{
				String method = "getTotalCategoryRatingCode";
				throw new UnexpectedValueException(code, method);
			}
		}
		
		if(veryGoods > goods)
			return VERY_GOOD;
		return GOOD;
	}

	static class UnexpectedValueException extends RuntimeException
	{
		public UnexpectedValueException(String code, String method)
		{
			super(method + " found unexpected value: " + code);
		}
	}

	// FIXME: These should be shared with the Rating Question classes
	public static final String UNSPECIFIED = "";
	public static final String POOR = "1";
	public static final String FAIR = "2";
	public static final String GOOD = "3";
	public static final String VERY_GOOD = "4";
}
