/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.treetables;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.miradi.main.MainWindow;

public class VariableHeightTreeCellRenderer extends DefaultTreeCellRenderer
{
	public VariableHeightTreeCellRenderer(TreeTableWithRowHeightSaver treeTableToUse)
	{
		treeTable = treeTableToUse;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocusToUse)
	{
		Component rendererComponent = getTreeCellRendererComponentWithPreferredHeight(tree, value, sel, expanded, leaf, row, hasFocusToUse);
		Dimension size = new Dimension(rendererComponent.getPreferredSize());
		size.height = treeTable.getRowHeight(row);
		rendererComponent.setPreferredSize(size);
		return rendererComponent;
	}

	public Component getTreeCellRendererComponentWithPreferredHeight(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocusToUse)
	{
		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	}

	public MainWindow getMainWindow()
	{
		return treeTable.getMainWindow();
	}
	
	private TreeTableWithRowHeightSaver treeTable;

}
