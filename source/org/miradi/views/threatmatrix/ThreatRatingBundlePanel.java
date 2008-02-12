/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.dialogs.fieldComponents.HtmlPanelLabel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.project.ThreatRatingBundle;

import com.jhlabs.awt.BasicGridLayout;

public class ThreatRatingBundlePanel extends DisposablePanel
{
	public ThreatRatingBundlePanel(ThreatMatrixView viewToUse) throws Exception
	{
	    super(new BasicGridLayout(0, 1));

		view = viewToUse;
		project = view.getProject();
		
		add(createHeader());
		add(createRatingPanel());
		
		threatMatrixCommentPanel = new ThreatMatrixCommentPanel(project);
		add(threatMatrixCommentPanel);
		add(Box.createVerticalGlue());
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		threatMatrixCommentPanel.dispose();
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
			FactorId threatId = workingBundle.getThreatId();
			FactorId targetId = workingBundle.getTargetId();
			threatName.setText(getNodeName(threatId));
			targetName.setText(getNodeName(targetId));
			threatName.setBorder(new LineBorder(Color.BLACK));
			targetName.setBorder(new LineBorder(Color.BLACK));
			
			Cause threat = Cause.find(project, new ORef(Cause.getObjectType(), threatId));
			Target target = Target.find(project, new ORef(Target.getObjectType(), targetId));
			ORef linkRef = project.getFactorLinkPool().getLinkedRef(threat, target);
			threatMatrixCommentPanel.setObjectRefs(new ORef[] {linkRef});
		}
	}
	
	private String getNodeName(FactorId nodeId) throws Exception
	{
		return project.findNode(nodeId).getLabel();
	}
	
	private JPanel createHeader() throws Exception
	{
		targetName = createNameArea();
		threatName = createNameArea();

		JPanel headerBox = new JPanel(new BasicGridLayout(2, 2));		
		headerBox.add(new PanelTitleLabel("Threat:"));
		headerBox.add(threatName);
		headerBox.add(new PanelTitleLabel("Target:"));
		headerBox.add(targetName);
		
		JPanel panel = new JPanel();
		panel.add(headerBox);

		panel.setBorder(new EmptyBorder(2, 5, 2, 5));
		return panel;
	}



	private HtmlFormViewer createNameArea()
	{
		HtmlFormViewer area = new HtmlPanelLabel(view.getMainWindow(),"", null);
		area.setOpaque(false);
		area.setPreferredSize(new Dimension(150, 60));
		area.setEditable(false);
		return area;
	}

	private ThreatRatingPanel createRatingPanel()
	{
		ratingPanel = new ThreatRatingPanel(view);
		return ratingPanel;
	}
		
	private ThreatMatrixView view;
	private Project project;
	private ThreatRatingBundle workingBundle;
	private ThreatRatingPanel ratingPanel;

	private HtmlFormViewer threatName;
	private HtmlFormViewer targetName;
	private ThreatMatrixCommentPanel threatMatrixCommentPanel;
}
