/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.actions.ActionAbout;
import org.conservationmeasures.eam.actions.ActionClose;
import org.conservationmeasures.eam.actions.ActionExit;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionViewBudget;
import org.conservationmeasures.eam.actions.ActionViewCalendar;
import org.conservationmeasures.eam.actions.ActionViewDiagram;
import org.conservationmeasures.eam.actions.ActionViewImages;
import org.conservationmeasures.eam.actions.ActionViewInterview;
import org.conservationmeasures.eam.actions.ActionViewMap;
import org.conservationmeasures.eam.actions.ActionViewStrategicPlan;
import org.conservationmeasures.eam.actions.ActionViewTask;
import org.conservationmeasures.eam.actions.ActionViewThreatMatrix;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.ViewChangeListener;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.Doer;
import org.conservationmeasures.eam.views.NullDoer;
import org.martus.swing.UiLabel;

abstract public class UmbrellaView extends JPanel implements ViewChangeListener
{
	public UmbrellaView(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		nullDoer = new NullDoer();
		actionToDoerMap = new HashMap();
		addUmbrellaDoersToMap();
		getProject().addViewChangeListener(this);
	}
	
	abstract public String cardName();
	
	public void switchToView(String viewName) throws Exception
	{
		if(cardName().equals(viewName))
			becomeActive();
		else
			becomeInactive();
	}

	abstract public void becomeActive() throws Exception;
	abstract public void becomeInactive() throws Exception;
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	public Actions getActions()
	{
		return getMainWindow().getActions();
	}
	
	public JComponent getToolBar()
	{
		return toolBar;
	}
	
	protected void setToolBar(JComponent newToolBar)
	{
		toolBar = newToolBar;
	}
	
	public BufferedImage getImage()
	{
		throw new RuntimeException("This view doesn't support getImage");
	}
	
	public JComponent getPrintableComponent()
	{
		throw new RuntimeException("This view doesn't support getPrintableComponent");
	}
	
	protected UiLabel createScreenShotLabel()
	{
		UiLabel label = new UiLabel("Demo Screen Shot");
		label.setBorder(new LineBorder(Color.BLACK));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}

	////////////////////////////////////////////////////////////
	// these doers are available in this class
	
	private void addUmbrellaDoersToMap()
	{
		addDoerToMap(ActionAbout.class, new About());
		addDoerToMap(ActionNewProject.class, new NewProject());
		addDoerToMap(ActionClose.class, new Close());
		addDoerToMap(ActionExit.class, new Exit());
		addDoerToMap(ActionUndo.class, new Undo());
		addDoerToMap(ActionRedo.class, new Redo());
		addDoerToMap(ActionViewDiagram.class, new ViewDiagram());
		addDoerToMap(ActionViewInterview.class, new ViewInterview());
		addDoerToMap(ActionViewThreatMatrix.class, new ViewThreatMatrix());
		addDoerToMap(ActionViewBudget.class, new ViewBudget());
		addDoerToMap(ActionViewTask.class, new ViewTask());
		addDoerToMap(ActionViewMap.class, new ViewMap());
		addDoerToMap(ActionViewImages.class, new ViewImages());
		addDoerToMap(ActionViewCalendar.class, new ViewCalendar());
		addDoerToMap(ActionViewStrategicPlan.class, new ViewStrategicPlan());
	}
	
	public void addDoerToMap(Class actionClass, Doer doer)
	{
		actionToDoerMap.put(actionClass, doer);
	}
	
	public Doer getDoer(Class actionClass)
	{
		Doer doer = (Doer)actionToDoerMap.get(actionClass);
		if(doer == null)
			doer = nullDoer;
		
		doer.setView(this);
		doer.setProject(getProject());
		return doer;
	}
	
	protected ViewData getViewData() throws Exception
	{
		ViewData ourViewData = getProject().getViewData(cardName());
		return ourViewData;
	}

	private MainWindow mainWindow;
	private NullDoer nullDoer;
	private JComponent toolBar;
	private HashMap actionToDoerMap;
}
