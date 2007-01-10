/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.images;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.HtmlViewer;
import org.martus.swing.ResourceImageIcon;
import org.martus.swing.UiScrollPane;

import com.jhlabs.awt.GridLayoutPlus;

public class ImagesView extends UmbrellaView
{
	public ImagesView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new ImagesToolBar(mainWindowToUse.getActions()));
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.IMAGES_VIEW_NAME;
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		JPanel panel = new JPanel(new GridLayoutPlus(0,1));
		panel.add(getIntroText());
		panel.add(new ImagesComponent(), BorderLayout.CENTER);
		add(new UiScrollPane(panel));
	}

	public void becomeInactive() throws Exception
	{
		super.becomeInactive();
	}
	
	private HtmlViewer getIntroText()
	{
		HtmlViewer htmlViewer= new HtmlViewer("",null);
		htmlViewer.setText(loadResourceFile(OVERVIEW_HTML));
		
		//TODO: Find a better way to calculate scroll bar width to subtract
		setFixedWidth(htmlViewer,this.getSize().width - 20);

		return htmlViewer;
	}
	
	public static void setFixedWidth( Component component, int width )
	{
		component.setSize( new Dimension( width, Short.MAX_VALUE ) );
		Dimension preferredSize = component.getPreferredSize();
		component.setPreferredSize( new Dimension( width, preferredSize.height ) );
	}
	
	
	private String loadResourceFile(String htmlFile)
	{
		try
		{
			return EAM.loadResourceFile(this.getClass(), htmlFile);
		}
		catch (Exception e)
		{
			EAM.errorDialog("ERROR: file not found: " + this.getClass() + "/" + htmlFile );
			return null;
		}
	}
	
	private String OVERVIEW_HTML = "Overview.html";

}


	class ImagesComponent extends JTabbedPane
	{

		public ImagesComponent()
		{
			setTabPlacement(JTabbedPane.BOTTOM);
		
			String[] demoMaps = 
			{
				"Bay",
			};
			
			for(int i = 0; i < demoMaps.length; ++i)
			{
				String mapName = demoMaps[i] + ".jpg";
				JLabel image = new JLabel(new ResourceImageIcon("images/" + mapName));
				image.setName(demoMaps[i]);
				add(image);
			}
		}
	
	}

