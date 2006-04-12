/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;

public class ThreatRatingBundlePanel extends Box
{
	public ThreatRatingBundlePanel(Project projectToUse, ActionListener okListener, ActionListener cancelListener) throws Exception
	{
		super(BoxLayout.Y_AXIS);
	
		project = projectToUse;
		
		add(createHeader());
		add(createRatingPanel());
		add(createButtonBox(okListener, cancelListener));
		add(Box.createVerticalGlue());
	}
	
	public ThreatRatingBundle getBundle()
	{
		return workingBundle;
	}
	
	public void selectBundle(ThreatRatingBundle bundleToUse) throws Exception
	{
		if(bundleToUse == null)
			workingBundle = null;
		else
			workingBundle = new ThreatRatingBundle(bundleToUse);
		ratingPanel.setBundle(workingBundle);
		updateValues();
	}
	
	private void updateValues() throws Exception
	{
		if(workingBundle == null)
		{
			threatName.setText("");
			targetName.setText("");
			threatName.setBorder(null);
			targetName.setBorder(null);
		}
		else
		{
			int threatId = workingBundle.getThreatId();
			int targetId = workingBundle.getTargetId();
			threatName.setText(getNodeName(threatId));
			targetName.setText(getNodeName(targetId));
			threatName.setBorder(new LineBorder(Color.BLACK));
			targetName.setBorder(new LineBorder(Color.BLACK));
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
		UiButton okButton = new UiButton(EAM.text("Apply"));
		okButton.addActionListener(okListener);
		UiButton cancelButton = new UiButton(EAM.text("Revert"));
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
