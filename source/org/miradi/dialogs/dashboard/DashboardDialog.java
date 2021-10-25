/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import java.awt.*;
import java.util.Vector;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.fieldComponents.RemovedFeatureLabel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class DashboardDialog extends ModalDialogWithClose
{
	public DashboardDialog(MainWindow parent) throws Exception
	{
		super(parent, EAM.text("Dashboard"));
		
		createPanel();
		addPanel();
	}
	
	@Override
	public void dispose()
	{
		AppPreferences preferences = getMainWindow().getAppPreferences();
		preferences.setDashboardWindowSize(getSize());
		preferences.setDashboardWindowPosition(getLocation());

		super.dispose();
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		
		setSize(getSavedDialogSize());
		setLocation(getSavedDialogPosition());
	}

	@Override
	protected Vector<Component> getButtonBarComponents()
	{
		Vector<Component> defaultButtonBarComponents = super.getButtonBarComponents();

		RemovedFeatureLabel removedFeatureLabel = new RemovedFeatureLabel();
		defaultButtonBarComponents.add(0, removedFeatureLabel);

		return defaultButtonBarComponents;
	}

	private Dimension getSavedDialogSize()
	{
		AppPreferences preferences = getMainWindow().getAppPreferences();
		Dimension savedSize = preferences.getDashboardWindowSize();
		if (savedSize.equals(new Dimension()))
		{
			Dimension screenRect = Utilities.getViewableScreenSize();
			int width = screenRect.width * 3 / 4;
			int height = screenRect.height * 3 / 4;

			return new Dimension(width, height);
		}
		
		return savedSize;
	}
	
	private Point getSavedDialogPosition()
	{
		AppPreferences preferences = getMainWindow().getAppPreferences();
		Point savedLocation = preferences.getDashboardWindowPosition();
		if (savedLocation.equals(new Point()))
			return new Point();
		
		return savedLocation;
	}
	
	private void createPanel() throws Exception
	{
		mainPanel = new DashboardMainPanel(getMainWindow());
	}

	private void addPanel()
	{
		setMainPanel(mainPanel);
	}
	
	private DashboardMainPanel mainPanel;
}
