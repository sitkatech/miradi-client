/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.dashboard;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.views.umbrella.PersistentNonPercentageHorizontalSplitPane;

abstract public class AbstractDashboardTag extends ObjectDataInputPanel
{
	public AbstractDashboardTag(Project projectToUse, int objectType)
	{
		super(projectToUse, objectType);
		
		splitPane = new PersistentNonPercentageHorizontalSplitPane(getMainWindow(), getMainWindow(), getPanelDescription());	
		clickableComponentToContentsFileNameMap = new HashMap<SelectableRow, String>();
	}
	
	private class ClickHandler extends MouseAdapter
	{
		public ClickHandler(SelectableRow clickableComponentToUse)
		{
			selectableComponent = clickableComponentToUse;
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			super.mouseClicked(e);
			
			try
			{
				clearSelection();
				selectableComponent.selectRow();
				
				String resourceFileName = clickableComponentToContentsFileNameMap.get(selectableComponent);
				rightSideDescriptionPanel.setRightSidePanelContent(resourceFileName);
			}
			catch (Exception exception)
			{
				EAM.logException(exception);
				EAM.unexpectedErrorDialog(exception);
			}
		}
	
		private void clearSelection() throws Exception
		{
			Set<SelectableRow> selectableRows = clickableComponentToContentsFileNameMap.keySet();
			for(SelectableRow selectableRow : selectableRows)
			{
				selectableRow.clearSelection();
			}
		}
		
		private SelectableRow selectableComponent;
	}
	
	protected class SelectableRow
	{
		protected SelectableRow(JComponent leftSideToUse, JComponent rightSideToUse)
		{
			leftSide = leftSideToUse;
			rightSide = rightSideToUse;
			
			leftSide.addMouseListener(new ClickHandler(this));
			rightSide.addMouseListener(new ClickHandler(this));
		}
		
		protected void selectRow()
		{
			leftSide.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
			rightSide.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
		}
		
		protected void clearSelection()
		{
			leftSide.setBorder(BorderFactory.createEtchedBorder());
			rightSide.setBorder(BorderFactory.createEtchedBorder());
		}
		
		protected void setBackgroundColor(Color backgroundColor)
		{
			setBackgroundColor(leftSide, backgroundColor);
			setBackgroundColor(rightSide, backgroundColor);
		}
		
		private void setBackgroundColor(JComponent component, Color backgroundColor)
		{
			component.setOpaque(true);
			component.setBackground(backgroundColor);
		}
		
		private JComponent leftSide;
		private JComponent rightSide;
	}
	
	protected HashMap<SelectableRow, String> clickableComponentToContentsFileNameMap;
	protected DashboardRightSideDescriptionPanel rightSideDescriptionPanel;
	protected PersistentNonPercentageHorizontalSplitPane splitPane;
	protected static final int INDENT_PER_LEVEL = 20;
}
