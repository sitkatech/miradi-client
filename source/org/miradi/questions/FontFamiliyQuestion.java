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
package org.miradi.questions;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.miradi.main.EAM;

public class FontFamiliyQuestion extends StaticChoiceQuestion
{
	public FontFamiliyQuestion()
	{
		super(getFamilyChoices());
	}
	
	static ChoiceItem[] getFamilyChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", EAM.text("Cross-platform Sans-serif")),
			new ChoiceItem(ARIAL_CODE, EAM.text("Arial")),
			new ChoiceItem(CALIBRI_CODE, EAM.text("Calibri")),
			new ChoiceItem(TAHOMA_CODE, EAM.text("Tahoma")),
			new ChoiceItem(VERDANA_CODE, EAM.text("Verdana")),
			new ChoiceItem(SERIF_CODE, EAM.text("Cross-platform Serif")),
			new ChoiceItem(TIMES_CODE, EAM.text("Times New Roman")),
		};
	}

	public String getFontsString(ChoiceItem fontFamilyChoice)
	{
		String[] families = getRawDesiredFamilies(fontFamilyChoice.getCode());
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < families.length; ++i)
		{
			if(i > 0)
				buffer.append(", ");
			buffer.append("'");
			buffer.append(families[i]);
			buffer.append("'");
		}
		
		return buffer.toString();
	}
	
	public static Font createFont(ChoiceItem fontFamilyChoice, int fontSize)
	{
		String[] desiredFamilies = getRawDesiredFamilies(fontFamilyChoice.getCode());
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Set<String> availableFamilies = new HashSet(Arrays.asList(ge.getAvailableFontFamilyNames()));
		for(int i = 0; i < desiredFamilies.length; ++ i)
		{
			if(availableFamilies.contains(desiredFamilies[i]))
			{
				return new Font(desiredFamilies[i], Font.PLAIN, fontSize);
			}
		}
		
		throw new RuntimeException("Unable to create font: " + fontFamilyChoice.getCode());
	}
	
	private static String[] getRawDesiredFamilies(String familyCode)
	{
		if(familyCode.equals(SERIF_CODE))
			return serifFamilies;
		if(familyCode.equals(ARIAL_CODE))
			return arialFamilies;
		if(familyCode.equals(VERDANA_CODE))
			return verdanaFamilies;
		if(familyCode.equals(TAHOMA_CODE))
			return tahomaFamilies;
		if(familyCode.equals(CALIBRI_CODE))
			return calibriFamilies;
		if(familyCode.equals(TIMES_CODE))
			return timesFamilies;
		
		return sansFamilies;
	}
	
	private static final String SERIF_CODE = "serif";
	private static final String ARIAL_CODE = "Arial";
	private static final String VERDANA_CODE = "Verdana";
	private static final String TAHOMA_CODE = "Tahoma";
	private static final String CALIBRI_CODE = "Calibri";
	private static final String TIMES_CODE = "Times";
	
	private static final String SANS_FAMILY_NAME = "SansSerif";
	private static final String SERIF_FAMILY_NAME = "Serif";

	private static final String[] sansFamilies = new String[] {
		SANS_FAMILY_NAME,
		};
	private static final String[] arialFamilies = new String[] {
		"Arial", 
		"Helvetica", 
		"Liberation Sans", 
		"DejaVu Sans", 
		"FreeSans", 
		SANS_FAMILY_NAME,
		};
	private static final String[] verdanaFamilies = new String[] {
		"Verdana", 
		SANS_FAMILY_NAME,
		};
	private static final String[] tahomaFamilies = new String[] {
		"Tahoma", 
		SANS_FAMILY_NAME,
		};
	private static final String[] calibriFamilies = new String[] {
		"Calibri", 
		SANS_FAMILY_NAME,
		};
	private static final String[] serifFamilies = new String[] {
		SERIF_FAMILY_NAME,
		};
	private static final String[] timesFamilies = new String[] {
		"Times New Roman",
		"Times",
		"Nimbus Roman", 
		"Liberation Serif", 
		"DejaVu Serif", 
		"FreeSerif", 
		SERIF_FAMILY_NAME,
		};
}
