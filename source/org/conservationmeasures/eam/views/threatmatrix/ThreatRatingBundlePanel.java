/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;

public class ThreatRatingBundlePanel extends JPanel
{
	public ThreatRatingBundlePanel(Project projectToUse, ActionListener okListener, ActionListener cancelListener) throws Exception
	{
		super(new BorderLayout());
	
		project = projectToUse;
		
		add(createHeader(), BorderLayout.BEFORE_FIRST_LINE);
		add(createRatingPanel(), BorderLayout.CENTER);
		add(createButtonBox(okListener, cancelListener), BorderLayout.AFTER_LAST_LINE);
		
	}
	
	public void setBundle(ThreatRatingBundle bundleToUse) throws Exception
	{
		workingBundle = bundleToUse;
		ratingPanel.setBundle(workingBundle);
		updateValues();
	}
	
	private void updateValues() throws Exception
	{
		if(workingBundle == null)
		{
			threatName.setText("");
			targetName.setText("");
		}
		else
		{
			threatName.setText(getNodeName(workingBundle.getThreatId()));
			targetName.setText(getNodeName(workingBundle.getTargetId()));
		}
	}
	
	private String getNodeName(int nodeId) throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		return model.getNodeById(nodeId).getName();

	}
	
	private JPanel createHeader() throws Exception
	{
		threatName = new UiLabel();
		targetName = new UiLabel();
		
		JPanel panel = new JPanel(new BasicGridLayout(2, 2));
		panel.add(new UiLabel("Threat:"));
		panel.add(threatName);
		panel.add(new UiLabel("Target:"));
		panel.add(targetName);
		panel.setBorder(new EmptyBorder(2, 5, 2, 5));
		return panel;
	}

	private ThreatRatingPanel createRatingPanel()
	{
		ThreatRatingFramework framework = project.getThreatRatingFramework();
		ratingPanel = new ThreatRatingPanel(framework);
		return ratingPanel;
	}
	
	private Box createButtonBox(ActionListener okListener, ActionListener cancelListener)
	{
		Box buttonBox = Box.createHorizontalBox();
		UiButton okButton = new UiButton("OK");
		okButton.addActionListener(okListener);
		UiButton cancelButton = new UiButton("Cancel");
		cancelButton.addActionListener(cancelListener);
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(okButton);
		buttonBox.add(cancelButton);
		buttonBox.add(Box.createHorizontalGlue());
		return buttonBox;
	}
	
	Project project;
	ThreatRatingBundle workingBundle;
	ThreatRatingPanel ratingPanel;

	UiLabel threatName;
	UiLabel targetName;
}
