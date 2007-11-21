/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;

public class TargetThreatLinkTable extends TableWithColumnWidthSaver
{
	public TargetThreatLinkTable(TargetThreatLinkTableModel tableModel)
	{
		super(tableModel);
	}
	
	public TargetThreatLinkTableModel getTargetThreatLinkTableModel()
	{
		return (TargetThreatLinkTableModel) getModel();
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		int threatIndex = getSelectedRow();
		Factor directThreat = getTargetThreatLinkTableModel().getDirectThreat(threatIndex);
		
		int targetIndex = getSelectedColumn();
		int modelColumn = convertColumnIndexToModel(targetIndex);
		Target target = getTargetThreatLinkTableModel().getTarget(modelColumn);
		ORefList hierarchyRefs = new ORefList();
		hierarchyRefs.add(target.getRef());
		hierarchyRefs.add(directThreat.getRef());
		
		return new ORefList[]{hierarchyRefs};
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatsTable";
}
