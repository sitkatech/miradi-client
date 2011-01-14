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

package org.miradi.dialogfields;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.dashboard.DashboardProgressPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;

abstract public class AbstractDashboardClickableField extends ObjectDataField
{
	public AbstractDashboardClickableField(Project projectToUse, ORef refToUse,	String tagToUse, String stringMapCodeToUse)
	{
		super(projectToUse, refToUse);
		
		tag = tagToUse;
		stringMapCode = stringMapCodeToUse;
		labelComponent = new PanelTitleLabel();
		configureComponent(labelComponent);
		labelComponent.addMouseListener(new ClickHandler());
	}
	
	protected void configureComponent(JComponent component)
	{
	}
	
	@Override
	public void updateEditableState()
	{
		labelComponent.setEnabled(true);
	}

	@Override
	public JComponent getComponent()
	{
		return labelComponent;
	}
	
	@Override
	public void updateFromObject()
	{
		try
		{
			ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
			Dashboard dashboard = Dashboard.find(getProject(), dashboardRef);
			String mapValue = getMapValue(dashboard, stringMapCode);
			updateLabelComponent(labelComponent, mapValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	@Override
	public void saveIfNeeded()
	{
		throw new RuntimeException("This is a readonly field and has no saveIfNeeded() implementation. Class = " + getClass().getName());
	}

	protected String getMapValue(Dashboard dashboard, String stringMapCodeToUse) throws Exception
	{
		AbstractStringKeyMap map = createStringKeyMap(dashboard);
		return map.get(stringMapCodeToUse);
	}
	
	@Override
	public String getTag()
	{
		return tag;
	}
	
	private String getEditorTitle()
	{
		return EAM.text("Edit....");
	}

	protected class ClickHandler extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent mouseEvent)
		{
			super.mouseClicked(mouseEvent);
			
			try
			{
				DisposablePanel editorPanel = new DashboardProgressPanel(getProject(), getORef(), stringMapCode);
				ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), getEditorTitle());
				dialog.setMainPanel(editorPanel);
				dialog.becomeActive();
				Utilities.centerDlg(dialog);
				dialog.setVisible(true);
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.unexpectedErrorDialog(e);
			}
		}
	}
	
	abstract protected AbstractStringKeyMap createStringKeyMap(Dashboard dashboard) throws Exception;
	
	abstract protected void updateLabelComponent(PanelTitleLabel labelComponentToUse, String mapValue) throws Exception;

	private String tag;
	protected String stringMapCode;
	private PanelTitleLabel labelComponent;
}
