/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.targetviability;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttributeIndicator;
import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttributeMeasurement;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttributeIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttributeMeasurement;
import org.conservationmeasures.eam.dialogs.viability.TargetViabilityTreeManagementPanel;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.diagram.CreateKeyEcologicalAttributeIndicatorDoer;
import org.conservationmeasures.eam.views.diagram.CreateViabilityKeyEcologicalAttributeDoer;
import org.conservationmeasures.eam.views.diagram.DeleteKeyEcologicalAttributeDoer;
import org.conservationmeasures.eam.views.diagram.DeleteKeyEcologicalAttributeIndicatorDoer;
import org.conservationmeasures.eam.views.targetviability.doers.CreateKeyEcologicalAttributeMeasurementDoer;
import org.conservationmeasures.eam.views.targetviability.doers.DeleteKeyEcologicalAttributeMeasurementDoer;

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
		viabilityPanel = new TargetViabilityTreeManagementPanel(getProject(), getMainWindow(), getMainWindow().getActions());
		addNonScrollableTab(viabilityPanel);
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