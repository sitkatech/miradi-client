/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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

}

