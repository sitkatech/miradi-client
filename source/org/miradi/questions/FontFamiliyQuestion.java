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
		if(fontFamilyChoice.getCode().equals(SERIF_CODE))
			return "'serif'";
		if(fontFamilyChoice.getCode().equals(ARIAL_CODE))
			return "'Arial', 'Helvetica', 'Liberation Sans', 'DejaVu Sans', 'FreeSans', 'sans-serif'";
		if(fontFamilyChoice.getCode().equals(VERDANA_CODE))
			return "'Verdana', 'sans-serif'";
		if(fontFamilyChoice.getCode().equals(TAHOMA_CODE))
			return "'Tahoma', 'sans-serif'";
		if(fontFamilyChoice.getCode().equals(CALIBRI_CODE))
			return "'Calibri', 'sans-serif'";
		if(fontFamilyChoice.getCode().equals(TIMES_CODE))
			return "'Times New Roman', 'Times', 'Nimbus Roman', 'Liberation Serif', 'DejaVu Serif', 'FreeSerif', 'serif'";
		
		return "'sans-serif'";
	}
	
	private static final String SERIF_CODE = "serif";
	private static final String ARIAL_CODE = "Arial";
	private static final String VERDANA_CODE = "Verdana";
	private static final String TAHOMA_CODE = "Tahoma";
	private static final String CALIBRI_CODE = "Calibri";
	private static final String TIMES_CODE = "Times";
}
