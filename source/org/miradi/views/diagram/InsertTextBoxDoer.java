/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.diagram.cells.DiagramTextBoxCell;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;

public class InsertTextBoxDoer extends InsertFactorDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
				
		if (!isInDiagram())
			return false;
		
		return true;
	}
	
	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramTextBoxCell.class, true);
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Text Box");
	}

	public int getTypeToInsert()
	{
		return ObjectType.TEXT_BOX;
	}
}
