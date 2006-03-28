/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.conservationmeasures.eam.actions.ActionConfigureLayers;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionInsertConnection;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertIndirectFactor;
import org.conservationmeasures.eam.actions.ActionInsertIntervention;
import org.conservationmeasures.eam.actions.ActionInsertStress;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionNudgeNodeDown;
import org.conservationmeasures.eam.actions.ActionNudgeNodeLeft;
import org.conservationmeasures.eam.actions.ActionNudgeNodeRight;
import org.conservationmeasures.eam.actions.ActionNudgeNodeUp;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionProperties;
import org.conservationmeasures.eam.actions.ActionSaveImage;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramToolBar;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.ViewChangeListener;
import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.utils.HyperlinkHandler;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.martus.swing.UiScrollPane;

public class DiagramView extends UmbrellaView implements ViewChangeListener
{
	public DiagramView(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		diagram = new DiagramComponent(getProject(), getActions());
		getProject().setSelectionModel(diagram.getSelectionModel());
		
		addDiagramViewDoersToMap();
		
		setToolBar(new DiagramToolBar(getActions()));

		setLayout(new BorderLayout());
		getProject().addViewChangeListener(this);
	}
	
	public DiagramComponent getDiagramComponent()
	{
		return diagram;
	}

	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Diagram";
	}
	
	public BufferedImage getImage()
	{
		return diagram.getImage();
	}
	
	public JComponent getPrintableComponent()
	{
		return diagram.getPrintableComponent();
	}

	private void addDiagramViewDoersToMap()
	{
		addDoerToMap(ActionInsertTarget.class, new InsertTarget());
		addDoerToMap(ActionInsertIndirectFactor.class, new InsertIndirectFactor());
		addDoerToMap(ActionInsertDirectThreat.class, new InsertDirectThreat());
		addDoerToMap(ActionInsertStress.class, new InsertStress());
		addDoerToMap(ActionInsertIntervention.class, new InsertIntervention());
		addDoerToMap(ActionInsertConnection.class, new InsertConnection());
		addDoerToMap(ActionCopy.class, new Copy());
		addDoerToMap(ActionCut.class, new Cut());
		addDoerToMap(ActionDelete.class, new Delete());
		addDoerToMap(ActionPaste.class, new Paste());
		addDoerToMap(ActionPasteWithoutLinks.class, new PasteWithoutLinks());
		addDoerToMap(ActionProperties.class, new Properties(diagram));
		addDoerToMap(ActionPrint.class, new Print());
		addDoerToMap(ActionSaveImage.class, new SaveImage());
		addDoerToMap(ActionConfigureLayers.class, new ConfigureLayers());
		addDoerToMap(ActionZoomIn.class, new ZoomIn());
		addDoerToMap(ActionZoomOut.class, new ZoomOut());
		addDoerToMap(ActionNudgeNodeUp.class, new NudgeNode(KeyEvent.VK_UP)); 
		addDoerToMap(ActionNudgeNodeDown.class, new NudgeNode(KeyEvent.VK_DOWN));
		addDoerToMap(ActionNudgeNodeLeft.class, new NudgeNode(KeyEvent.VK_LEFT));
		addDoerToMap(ActionNudgeNodeRight.class, new NudgeNode(KeyEvent.VK_RIGHT));
	}
	
	public void switchToView(String viewName) throws Exception
	{
		boolean isSwitchingToThisView = viewName.equals(getViewName());
		if(!isSwitchingToThisView)
			return;
		
		removeAll();
		
		UiScrollPane uiScrollPane = new UiScrollPane(diagram);
		uiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		uiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JSplitPane bigSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		bigSplitter.setResizeWeight(.5);
		bigSplitter.setTopComponent(createWizard());
		bigSplitter.setBottomComponent(uiScrollPane);
		add(bigSplitter);
		
		add(bigSplitter, BorderLayout.CENTER);

	}
	

	public JPanel createWizard() throws Exception
	{
		WizardPanel wizard = new WizardPanel();
		DiagramWizardOverviewStep step = new DiagramWizardOverviewStep();
		wizard.setContents(step);
		step.refresh();
		return wizard;
	}
	
	DiagramComponent diagram;

}

class DoNothingHyperLinkHandler implements HyperlinkHandler
{
	public void linkClicked(String linkDescription)
	{
		// TODO Auto-generated method stub
		
	}

	public void valueChanged(String widget, String newValue)
	{
		// TODO Auto-generated method stub
		
	}

	public void buttonPressed(String buttonName)
	{
		// TODO Auto-generated method stub
		
	}

}
class DiagramWizardHtml extends HtmlBuilder
{
	public static String text()
	{
		return "";
	}
}