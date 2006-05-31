/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.martus.swing.UiTextField;

public class DialogField
{
	public DialogField(String tagToUse, String labelToUse, String value)
	{
		tag = tagToUse;
		label = labelToUse;
		component = new UiTextField(50);
		component.setText(value);
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public UiTextField getComponent()
	{
		return component;
	}
	
	String tag;
	String label;
	UiTextField component;
}