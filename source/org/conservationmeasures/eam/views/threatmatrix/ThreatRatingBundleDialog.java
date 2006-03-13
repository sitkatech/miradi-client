/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
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

public class ThreatRatingBundleDialog extends JDialog
{
	public ThreatRatingBundleDialog(JFrame parent, Project projectToUse, ThreatRatingBundle bundleToUse) throws Exception
	{
		super(parent);
		project = projectToUse;
		originalBundle = bundleToUse;
		workingBundle = new ThreatRatingBundle(originalBundle);
		
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		contentPane.add(createHeader(), BorderLayout.BEFORE_FIRST_LINE);
		contentPane.add(createRatingPanel(), BorderLayout.CENTER);
		contentPane.add(createButtonBox(), BorderLayout.AFTER_LAST_LINE);
		pack();
	}

	private JPanel createHeader() throws Exception
	{
		JPanel panel = new JPanel(new BasicGridLayout(2, 2));
		addFieldLabelAndValue(panel, "Threat:  ", workingBundle.getThreatId());
		addFieldLabelAndValue(panel, "Target:  ", workingBundle.getTargetId());
		panel.setBorder(new EmptyBorder(2, 5, 2, 5));
		return panel;
	}

	private void addFieldLabelAndValue(JPanel panel, String fieldName, int nodeId) throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		panel.add(new UiLabel(EAM.text(fieldName)));
		UiLabel fieldValue = new UiLabel(model.getNodeById(nodeId).getName());
		fieldValue.setBorder(new LineBorder(Color.BLACK));
		panel.add(fieldValue);
	}
	
	private ThreatRatingPanel createRatingPanel()
	{
		ThreatRatingFramework framework = project.getThreatRatingFramework();
		ThreatRatingPanel threatRatingPanel = new ThreatRatingPanel(framework, workingBundle);
		return threatRatingPanel;
	}
	
	private Box createButtonBox()
	{
		Box buttonBox = Box.createHorizontalBox();
		UiButton okButton = new UiButton("OK");
		okButton.addActionListener(new OkButtonListener());
		UiButton cancelButton = new UiButton("Cancel");
		cancelButton.addActionListener(new ButtonListener());
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(okButton);
		buttonBox.add(cancelButton);
		buttonBox.add(Box.createHorizontalGlue());
		return buttonBox;
	}
	
	public class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			dispose();
		}
	}

	public class OkButtonListener extends ButtonListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				originalBundle.pullDataFrom(workingBundle);
				project.getThreatRatingFramework().saveBundle(originalBundle);
				super.actionPerformed(event);
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
			
		}
	}
	
	Project project;
	ThreatRatingBundle originalBundle;
	ThreatRatingBundle workingBundle;
}
