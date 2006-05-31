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

import java.awt.Component;

abstract public class DialogField
{
	public DialogField(String tagToUse, String labelToUse)
	{
		tag = tagToUse;
		label = labelToUse;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	abstract public Component getComponent();
	abstract public String getText();
	
	String tag;
	String label;
}