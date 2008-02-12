/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.diagram.cells.DiagramTargetCell;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;

public class InsertTargetDoer extends InsertFactorDoer
{
	public int getTypeToInsert()
	{
		return ObjectType.TARGET;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Target");
	}

	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramTargetCell.class, true);
	}
}
