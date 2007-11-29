/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import javax.swing.JTable;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Factor;

public class TargetThreatLinkTable extends TableWithSetPreferredScrollableViewportHeight
{
	public TargetThreatLinkTable(TargetThreatLinkTableModel tableModel)
	{
		super(tableModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setForcedPreferredScrollableViewportWidth(TargetSummaryRowTable.PREFERRED_VIEWPORT_WIDTH);
	}
	
	public TargetThreatLinkTableModel getTargetThreatLinkTableModel()
	{
		return (TargetThreatLinkTableModel) getModel();
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		int threatIndex = getSelectedRow();
		Factor directThreat = getTargetThreatLinkTableModel().getDirectThreat(threatIndex);
		
		int tableColumn = getSelectedColumn();
		int modelColumn = convertColumnIndexToModel(tableColumn);
		ORefList hierarchyRefs = new ORefList();
		if (modelColumn < 0)
			return new ORefList[0];
		
		ORef targetRef = getTargetThreatLinkTableModel().getTarget(modelColumn).getRef();
		if (getTargetThreatLinkTableModel().isLinked(directThreat.getRef(), targetRef))
		{
			ORef linkRef = getTargetThreatLinkTableModel().getLinkRef(directThreat.getRef(), targetRef);
			hierarchyRefs.add(linkRef);
		}
		hierarchyRefs.add(targetRef);
		hierarchyRefs.add(directThreat.getRef());
		
		return new ORefList[]{hierarchyRefs};
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "TargetThreatLinkTable";
}
