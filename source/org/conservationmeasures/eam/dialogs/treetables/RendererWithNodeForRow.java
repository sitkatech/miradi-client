/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.treetables;

import javax.swing.table.DefaultTableCellRenderer;

abstract public class RendererWithNodeForRow extends DefaultTableCellRenderer
{
	abstract public TreeTableNode getNodeForRow(int row);
}
