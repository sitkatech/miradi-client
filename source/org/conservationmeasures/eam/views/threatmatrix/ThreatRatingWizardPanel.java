/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatRatingWizardPanel extends JPanel
{
	public ThreatRatingWizardPanel(ThreatMatrixView viewToUse) throws Exception
	{
		super(new BorderLayout());
		view = viewToUse;
		
		ThreatRatingWizardChooseBundle chooseBundleStep = new ThreatRatingWizardChooseBundle(this);
		
		steps = new ThreatRatingWizardStep[STEP_COUNT];
		steps[OVERVIEW] = new ThreatRatingWizardOverviewStep(this);
		steps[CHOOSE_BUNDLE] = chooseBundleStep;
		steps[SET_SCOPE] = ThreatRatingWizardScopeStep.create(this);
		steps[SET_SEVERITY] = ThreatRatingWizardSeverityStep.create(this);
		steps[SET_IRREVERSIBILITY] = ThreatRatingWizardIrreversibilityStep.create(this);
		steps[CHECK_BUNDLE] = new ThreatRatingWizardCheckBundleStep(this);
		steps[CHECK_TOTALS] = new ThreatRatingWizardCheckTotalsStep(this);

		selectBundle(null);
		setStep(OVERVIEW);
	}

	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		selectedBundle = bundle;
		refresh();
	}

	public ThreatRatingBundle getSelectedBundle() throws Exception
	{
		return selectedBundle;
	}
	
	public void bundleWasClicked(ThreatRatingBundle bundle) throws Exception
	{
		view.selectBundle(bundle);
	}
	
	public ThreatMatrixView getView()
	{
		return view;
	}
	
	public void next() throws Exception
	{
		int nextStep = currentStep + 1;
		if(nextStep >= steps.length)
			nextStep = 0;
		
		setStep(nextStep);
	}
	
	public void previous() throws Exception
	{
		int nextStep = currentStep - 1;
		if(nextStep < 0)
			return;
		
		setStep(nextStep);
	}
	
	public void setStep(int newStep) throws Exception
	{
		currentStep = newStep;
		steps[currentStep].refresh();
		setContents(steps[currentStep]);
	}
	
	public void refresh() throws Exception
	{
		for(int i = 0; i < steps.length; ++i)
			steps[i].refresh();
		validate();
	}
	
	public void setContents(JPanel contents)
	{
		removeAll();

		JComponent hotButtons = createHotButtons();

		add(contents, BorderLayout.CENTER);
		add(hotButtons, BorderLayout.AFTER_LINE_ENDS);
		validate();
	}
	
	private JComponent createHotButtons()
	{
		JComponent hotButtons = Box.createVerticalBox();
		hotButtons.add(new ProcessButton());
		hotButtons.add(new JLabel(" "));
		hotButtons.add(new ExamplesButton());
		hotButtons.add(new JLabel(" "));
		hotButtons.add(new WorkshopButton());
		hotButtons.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
		return hotButtons;
	}

	ThreatRatingFramework getFramework()
	{
		ThreatRatingFramework framework = getView().getProject().getThreatRatingFramework();
		return framework;
	}
	
	private static final int OVERVIEW = 0;
	static final int CHOOSE_BUNDLE = 1;
	private static final int SET_SCOPE = 2;
	private static final int SET_SEVERITY = 3;
	private static final int SET_IRREVERSIBILITY = 4;
	private static final int CHECK_BUNDLE = 5;
	static final int CHECK_TOTALS = 6;
	
	private static final int STEP_COUNT = 7;
	
	ThreatMatrixView view;
	ThreatRatingWizardStep[] steps;
	int currentStep;
	ThreatRatingBundle selectedBundle;
}

abstract class CommandButton extends JButton implements ActionListener
{
	public CommandButton(String text)
	{
		super(text);
		addActionListener(this);
	}

}

abstract class HotButton extends CommandButton
{
	public HotButton(String text, Color color)
	{
		super("");
		setText("<html><table cellpadding='0' cellspacing='0'><tr><td align='center' valign='center'>" + 
				text + 
				"</td></tr></table></html>");
		setBackground(color);
	}
}

class ProcessButton extends HotButton
{
	public ProcessButton()
	{
		super("Get More<br></br>Information", Color.GREEN);
	}

	public void actionPerformed(ActionEvent arg0)
	{
	}
}

class ExamplesButton extends HotButton
{
	public ExamplesButton()
	{
		super("Examples", Color.YELLOW);
	}

	public void actionPerformed(ActionEvent arg0)
	{
	}
}

class WorkshopButton extends HotButton
{
	public WorkshopButton()
	{
		super("Workshop<br></br>Hints", Color.CYAN);
	}

	public void actionPerformed(ActionEvent arg0)
	{
	}
}

