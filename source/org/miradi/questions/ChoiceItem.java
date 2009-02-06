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

import java.awt.Color;
import java.io.IOException;

import javax.swing.Icon;

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;

public class ChoiceItem implements Comparable
{
	public ChoiceItem(String codeToUse, String labelToUse)
	{
		this(codeToUse, labelToUse, (Color)null);
	}
	
	public ChoiceItem(String codeToUse, String labelToUse, Color colorToUse)
	{
		code = codeToUse;
		label = labelToUse;
		color = colorToUse;
	}
	
	public ChoiceItem(String codeToUse, String labelToUse, Icon iconToUse)
	{
		code = codeToUse;
		label = labelToUse;
		icon = iconToUse;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public Icon getIcon()
	{
		return icon;
	}
	
	public void setIcon(Icon iconToUse)
	{
		icon = iconToUse;
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	public boolean isSelectable()
	{
		return selectable;
	}
	
	public void setSelectable(boolean selectableToUse)
	{
		selectable = selectableToUse;
	}
	
	public void toXml(UnicodeWriter out) throws IOException
	{
		out.write("<Choice code='" + 
				XmlUtilities.getXmlEncoded(getCode()) + 
				"'><Value>" + 
				XmlUtilities.getXmlEncoded(getLabel()) + 
				"</Value></Choice>");
	}

	public boolean equals(Object other)
	{
		if (! (other instanceof ChoiceItem))
			return false;
		
		ChoiceItem otherChoiceItem = (ChoiceItem) other;
		if (! otherChoiceItem.getCode().equals(getCode()))
			return false;
		
		if (! otherChoiceItem.getLabel().equalsIgnoreCase(getLabel()))
			return false;
		
		return true;
	}
	
	public int hashCode()
	{
		return code.hashCode();
	}

	public int compareTo(Object rawObject)
	{
		String thisString = toString();
		String otherString = rawObject.toString();
		return thisString.compareToIgnoreCase(otherString);
	}

	private boolean selectable;
	private String code;
	private String label;
	private Color color;
	private Icon icon;
}
