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
package org.miradi.views.noproject;

import javax.swing.JToolBar;

import org.miradi.actions.jump.ActionJumpWelcomeCreateStep;
import org.miradi.actions.jump.ActionJumpWelcomeImportStep;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.views.umbrella.UmbrellaView;

public class NoProjectView extends UmbrellaView
{
	public NoProjectView(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		addDoersToMap();
	}
	
	public JToolBar createToolBar()
	{
		return new NoProjectToolBar(getActions());
	}

	private void addDoersToMap()
	{
		addJumpDoerToMap(ActionJumpWelcomeCreateStep.class);
		addJumpDoerToMap(ActionJumpWelcomeImportStep.class);
	}
	
	public void becomeActive() throws Exception
	{
		super.becomeActive();
		getMainWindow().hideDivider();
	}

	public void becomeInactive() throws Exception
	{
		getMainWindow().showDivider();
		super.becomeInactive();
	}
	
	public void refreshText() throws Exception
	{
		getMainWindow().getWizard().refresh();
	}

	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.NO_PROJECT_VIEW_NAME;
	}

	public static boolean is(UmbrellaView view)
	{
		return view.cardName().equals(getViewName());
	}

}

