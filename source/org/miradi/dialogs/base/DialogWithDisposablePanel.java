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

package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.martus.swing.Utilities;
import org.miradi.utils.MiradiScrollPane;

public class DialogWithDisposablePanel extends DialogWithButtonBar
{
	public DialogWithDisposablePanel(JDialog owner)
	{
		super(owner);
	}
	
	public DialogWithDisposablePanel(JFrame parent)
	{
		super(parent);
	}
	
	public void setMainPanel(DisposablePanel panelToUse)
	{
		setRawMainPanel(panelToUse, panelToUse);
	}
	
	public void setScrollableMainPanel(DisposablePanel panelToUse)
	{
		setRawMainPanel(panelToUse, new MiradiScrollPane(wrappedPanel));
	}

	protected void setRawMainPanel(DisposablePanel panelToUse, Component comp)
	{
		wrappedPanel = panelToUse;
		getContentPane().add(comp, BorderLayout.CENTER);
	}

	@Override
	public void dispose()
	{
		if(wrappedPanel != null)
		{
			becomeInactive();
			wrappedPanel.dispose();
			wrappedPanel = null;
		}
		super.dispose();
	}
	
	public void becomeActive()
	{
		wrappedPanel.becomeActive();
	}

	public void becomeInactive()
	{
		wrappedPanel.becomeInactive();
	}

	public JPanel getWrappedPanel()
	{
		return wrappedPanel;
	}
	
	public void showDialog()
	{
		Utilities.fitInScreen(this);
		Utilities.centerDlg(this);
		becomeActive();
		setVisible(true);
	}
	
	private DisposablePanel wrappedPanel;
}
