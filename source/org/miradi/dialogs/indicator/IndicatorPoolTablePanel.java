/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.indicator;

import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.Project;

public class IndicatorPoolTablePanel extends ObjectPoolTablePanel
{
	public IndicatorPoolTablePanel(Project projectToUse)
	{
		super(projectToUse, ObjectType.INDICATOR, 
				new IndicatorPoolTableModel(projectToUse)
		);
	}
}
