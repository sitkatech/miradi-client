/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.Component;

import org.martus.swing.UiTextField;

public class StringDialogField extends DialogField
{
	public StringDialogField(String tagToUse, String labelToUse, String value)
	{
		super(tagToUse, labelToUse);
		textField = new UiTextField(50);
		textField.setText(value);
	}
	
	public UiTextField getStringComponent()
	{
		return textField;
	}

	public Component getComponent()
	{
		return getStringComponent();
	}
	
	public String getText()
	{
		return getStringComponent().getText();
	}

	UiTextField textField;

}
