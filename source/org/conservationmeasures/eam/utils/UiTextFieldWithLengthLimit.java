/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

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
			// FIXME: Avoid beeping when loading legacy projects
			// Also similar code in ObjectStringInputField, 
			// ObjectAdjustableStringInputField, and 
			// UiTextFieldWithLengthLimit
			//java.awt.Toolkit.getDefaultToolkit().beep();
			return;
		}
 
		super.insertString(param, str, attributeSet);
	}
	
	private int maxLength;
}
