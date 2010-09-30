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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.views.umbrella.PersistentNonPercentageHorizontalSplitPane;

abstract public class AbstractDashboardTag extends ObjectDataInputPanel
{
	public AbstractDashboardTag(Project projectToUse)
	{
		super(projectToUse, Dashboard.getObjectType());
		
		splitPane = new PersistentNonPercentageHorizontalSplitPane(getMainWindow(), getMainWindow(), getPanelDescription());	
		clickableComponentToContentsFileNameMap = new HashMap<SelectableRow, String>();
	}
	
	protected void addSubHeaderRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightPanelHtmlFileName)
	{
		SelectableRow selectableRow = createSubHeaderRow(leftMainPanel, leftColumnTranslatedText, "", rightPanelHtmlFileName);
		selectableRow.setBackgroundColor(Color.GREEN.darker());
	}
	
	protected SelectableRow createDataRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName)
	{
		Box firstColumnBox = createBorderedBox();
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		
		return createRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, descriptionFileName, firstColumnBox);
	}
	
	private SelectableRow createSubHeaderRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName)
	{
		Box firstColumnBox = createBorderedBox();
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		
		return createRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, descriptionFileName, firstColumnBox);
	}
	
	private SelectableRow createRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, Box firstColumnBox)
	{
		firstColumnBox.add(new PanelTitleLabel(leftColumnTranslatedText), BorderLayout.BEFORE_FIRST_LINE);
		
		Box secondColumnBox = createBorderedBox();
		secondColumnBox.add(new PanelTitleLabel(rightColumnTranslatedText));
		
		SelectableRow selectableRow = new SelectableRow(firstColumnBox, secondColumnBox);
		clickableComponentToContentsFileNameMap.put(selectableRow, descriptionFileName);
		
		leftMainPanel.add(firstColumnBox);
		leftMainPanel.add(secondColumnBox);
		
		return selectableRow;
	}

	private Box createBorderedBox()
	{
		Box box = Box.createHorizontalBox();
		box.setBorder(BorderFactory.createEtchedBorder());
		
		return box;
	}
	
	protected String getDashboardData(String tag)
	{
		return getDashboard().getData(tag);
	}
	
	private Dashboard getDashboard()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		return Dashboard.find(getProject(), dashboardRef);
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
