/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fieldComponents;

import org.miradi.utils.Translation;

public class PanelFieldLabel extends PanelTitleLabel
{
	public PanelFieldLabel(int objectType, String fieldTag)
	{
		super(Translation.fieldLabel(objectType, fieldTag));
	}
	
	public PanelFieldLabel(int objectType, String fieldTag, int horizontalAlignment)
	{
		this(objectType, fieldTag);
		setHorizontalAlignment(horizontalAlignment);
	}
}
