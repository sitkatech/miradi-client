/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.targetviability;

import javax.swing.JToolBar;

import org.miradi.actions.ActionCreateKeyEcologicalAttribute;
import org.miradi.actions.ActionCreateKeyEcologicalAttributeIndicator;
import org.miradi.actions.ActionCreateKeyEcologicalAttributeMeasurement;
import org.miradi.actions.ActionDeleteKeyEcologicalAttribute;
import org.miradi.actions.ActionDeleteKeyEcologicalAttributeIndicator;
import org.miradi.actions.ActionDeleteKeyEcologicalAttributeMeasurement;
import org.miradi.dialogs.viability.TargetViabilityTreeManagementPanel;
import org.miradi.dialogs.viability.ViabilityViewTreeManagementPanel;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.views.TabbedView;
import org.miradi.views.diagram.CreateKeyEcologicalAttributeIndicatorDoer;
import org.miradi.views.diagram.CreateViabilityKeyEcologicalAttributeDoer;
import org.miradi.views.diagram.DeleteKeyEcologicalAttributeDoer;
import org.miradi.views.diagram.DeleteKeyEcologicalAttributeIndicatorDoer;
import org.miradi.views.targetviability.doers.CreateKeyEcologicalAttributeMeasurementDoer;
import org.miradi.views.targetviability.doers.DeleteKeyEcologicalAttributeMeasurementDoer;

public class TargetViabilityView extends TabbedView
{
	public TargetViabilityView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		addDoersToMap();
	}
	
	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.TARGET_VIABILITY_NAME;
	}

	public JToolBar createToolBar()
	{
		return new TargetViabilityToolBar(getActions());
	}

	public void createTabs() throws Exception
	{
		viabilityPanel = new ViabilityViewTreeManagementPanel(getMainWindow(), getMainWindow());
		addNonScrollingTab(viabilityPanel);
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		
		viabilityPanel.updateSplitterLocation();
	}
	
	public void deleteTabs() throws Exception
	{
		viabilityPanel.dispose();
		viabilityPanel = null;
	}

	
	private void addDoersToMap()
	{
		addDoerToMap(ActionCreateKeyEcologicalAttribute.class, new CreateViabilityKeyEcologicalAttributeDoer());
		addDoerToMap(ActionDeleteKeyEcologicalAttribute.class, new DeleteKeyEcologicalAttributeDoer());
		addDoerToMap(ActionCreateKeyEcologicalAttributeIndicator.class, new CreateKeyEcologicalAttributeIndicatorDoer());
		addDoerToMap(ActionDeleteKeyEcologicalAttributeIndicator.class, new DeleteKeyEcologicalAttributeIndicatorDoer());
		addDoerToMap(ActionCreateKeyEcologicalAttributeMeasurement.class, new CreateKeyEcologicalAttributeMeasurementDoer());
		addDoerToMap(ActionDeleteKeyEcologicalAttributeMeasurement.class, new DeleteKeyEcologicalAttributeMeasurementDoer());
	}
	
	
	public BaseObject getSelectedObject()
	{
		if (viabilityPanel != null)
			return viabilityPanel.getObject();
		
		return null;
	}
	
	
	private TargetViabilityTreeManagementPanel viabilityPanel;
}