/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields.legacy;

import java.awt.Component;

import org.martus.swing.UiLabel;

public class LegacyMessageField extends LegacyDialogField
{
	public LegacyMessageField(String tag, String label, String valueToUse, String translatedMessage)
	{
		super(tag, label);
		dataValue = valueToUse;
		component = new UiLabel(translatedMessage);
	}
	
	public Component getComponent()
	{
		return component;
	}

	public String getText()
	{
		return dataValue;
	}

	String dataValue;
	UiLabel component;
}
