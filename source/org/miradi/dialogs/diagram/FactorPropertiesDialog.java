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
package org.miradi.dialogs.diagram;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.ModelessDialogPanel;
import org.miradi.dialogs.base.ModelessDialogWithDirections;
import org.miradi.main.MainWindow;

public class FactorPropertiesDialog extends ModelessDialogWithDirections
{
	public FactorPropertiesDialog(MainWindow parent, FactorPropertiesPanel panel, String headingText)
	{
		super(parent, panel, headingText);
		
		factorPanel = panel;
	}
	
	protected JComponent createMainPanel()
	{
		return getWrappedPanel();
	}
	
	public void updatePreferredSize()
	{
		Rectangle screenRect = Utilities.getViewableRectangle();
		int tenPercentWiderDiaglogWidth = getWidth() * 11 / 10;
		int width = Math.min(tenPercentWiderDiaglogWidth, screenRect.width * 9 / 10);
		int height = Math.min(getHeight(), screenRect.height * 9 / 10);
		Dimension size = new Dimension(width, height);
		
		setPreferredSize(size);
	}
	
	@Override
	protected Class getJumpAction()
	{
		if(factorPanel == null)
			return null;
		
		ModelessDialogPanel selectedComponent = (ModelessDialogPanel)factorPanel.tabs.getSelectedComponent();
		if(selectedComponent == null)
			return null;
		return selectedComponent.getJumpActionClass();
	}
	
	private FactorPropertiesPanel factorPanel;
}
