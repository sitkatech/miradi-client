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

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.martus.swing.UiTextField;

public class UiTextFieldWithLengthLimit extends UiTextField
{
	public UiTextFieldWithLengthLimit(int maxLength)
	{
		super(maxLength);
		setDocument(new PlainDocumentWithLengthLimit(maxLength));
	}
}

class PlainDocumentWithLengthLimit extends PlainDocument 
{
	public PlainDocumentWithLengthLimit(int maxLengthToUse) 
	{
		maxLength = maxLengthToUse;
	}
	
	public void insertString(int param, String str, javax.swing.text.AttributeSet attributeSet) throws BadLocationException 
	{
		int proposedLength = this.getLength() + str.length();
		if (str != null && proposedLength > maxLength) 
		{
			return;
		}
 
		super.insertString(param, str, attributeSet);
	}
	
	private int maxLength;
}
