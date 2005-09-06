/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.table;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionViewTable;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.ViewSwitcher;
import org.conservationmeasures.eam.utils.ToolBarButton;

public class TableToolBar extends JToolBar
	{
		public TableToolBar(Actions actions)
		{
			setFloatable(false);

			add(ViewSwitcher.create(actions, ActionViewTable.class));
			addSeparator();
			add(new ToolBarButton(actions, ActionPrint.class));
		}

	}
