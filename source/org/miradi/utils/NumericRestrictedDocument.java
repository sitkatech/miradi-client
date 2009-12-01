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

package org.miradi.utils;


public class NumericRestrictedDocument extends AbstractRestrictedDocument
{	
	@Override
	protected boolean isValidCharacter(char character)
	{
		if(LEGAL_NON_NUMERIC_CHARACTERS.indexOf(character) >= 0)
			return true;
		
		return Character.isDigit(character); 
	}
	
	@Override
	protected int getMaxValueLength()
	{
		return REASONABLE_MAX_DIGITS_IN_NUMERIC_FIELD;
	}
	
	private static final int REASONABLE_MAX_DIGITS_IN_NUMERIC_FIELD = 25;
	private static final String LEGAL_NON_NUMERIC_CHARACTERS = "-+.,";
}
