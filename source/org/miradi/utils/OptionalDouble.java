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

import org.miradi.main.EAM;

public class OptionalDouble
{	
	public OptionalDouble()
	{
		this(null);
	}
	
	public OptionalDouble(Double doubleToUse)
	{
		optionalDouble = doubleToUse;
		
		resetHasValue(optionalDouble);
	}
	
	private void resetHasValue(Double valueToUse)
	{
		if (valueToUse != null)
			hasValue = true;
	}
	
	public OptionalDouble add(OptionalDouble optionalDoubleToAdd)
	{ 
		if (isValidValue(optionalDoubleToAdd) && hasValue)
			return new OptionalDouble(optionalDouble + optionalDoubleToAdd.getValue()); 

		if (isValidValue(optionalDoubleToAdd))
			return new OptionalDouble(optionalDoubleToAdd.getValue());
		
		return new OptionalDouble(getValue());
	}

	private boolean isValidValue(OptionalDouble optionalDoubleToUse)
	{
		if (optionalDoubleToUse == null)
			return false;
		
		return optionalDoubleToUse.hasValue;
	}
	
	public boolean hasValue()
	{
		return hasValue;
	}
	
	public Double getValue()
	{
		return optionalDouble;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		throw new RuntimeException("OptionalDouble.equals()" + EAM.text(" has no implementation"));
	}
	
	@Override
	public String toString()
	{
		throw new RuntimeException("OptionalDouble.toString()" + EAM.text(" has no implementation"));
	}
	
	@Override
	public int hashCode()
	{
		return optionalDouble.hashCode();
	}
	
	private boolean hasValue;
	private Double optionalDouble;
}
