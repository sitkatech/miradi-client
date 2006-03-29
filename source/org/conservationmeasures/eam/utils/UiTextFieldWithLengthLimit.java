/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
			java.awt.Toolkit.getDefaultToolkit().beep();
			return;
		}
 
		super.insertString(param, str, attributeSet);
	}
	
	private int maxLength;
}
