/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.diagram.cells.DiagramGroupBoxCell;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.views.diagram.InsertFactorDoer;

public class InsertGroupBoxDoer extends InsertFactorDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
				
		return true;
	}
	
	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramGroupBoxCell.class, true);
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Group Box");
	}

	public int getTypeToInsert()
	{
		return GroupBox.getObjectType();
	}
}
