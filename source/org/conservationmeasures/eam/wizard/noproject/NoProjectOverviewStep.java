/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class NoProjectOverviewStep extends NoProjectWizardStep
{
	public NoProjectOverviewStep(WizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse);
		
		String html = EAM.loadResourceFile(getClass(), "WelcomeNew.html");
		leftTop = new LeftSideTextPanel(getMainWindow(), html, this);
		
		JPanel left = new JPanel(new BorderLayout());
		left.add(leftTop, BorderLayout.BEFORE_FIRST_LINE);
		left.add(projectList, BorderLayout.CENTER);
		
		NewsPanel newsPanel = new NewsPanel(getMainWindow(), null);
		FastScrollPane newsScrollPane = new FastScrollPane(newsPanel);
		newsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		mainPanel.setBackground(AppPreferences.WIZARD_BACKGROUND);
		mainPanel.add(left);
		mainPanel.add(newsScrollPane);
		
		add(mainPanel, BorderLayout.CENTER);
	}
	
	public void refresh() throws Exception
	{
		super.refresh();
		leftTop.refresh();
		projectList.refresh();
	}
	
	LeftSideTextPanel leftTop;
}
