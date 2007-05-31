/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.images;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.dialogs.fieldComponents.HtmlFormViewer;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.utils.MiradiResourceImageIcon;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

import com.jhlabs.awt.GridLayoutPlus;

public class ImagesView extends UmbrellaView
{
	public ImagesView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.IMAGES_VIEW_NAME;
	}

	public JToolBar createToolBar()
	{
		return new ImagesToolBar(getActions());
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		JPanel panel = new JPanel(new GridLayoutPlus(0,1));
		panel.add(getIntroText());
		panel.add(new ImagesComponent(), BorderLayout.CENTER);
		add(new FastScrollPane(panel));
	}

	public void becomeInactive() throws Exception
	{
		super.becomeInactive();
	}
	
	private HtmlFormViewer getIntroText() throws Exception
	{
		HtmlFormViewer htmlViewer = HtmlFormViewer.createHtmlViewerWithWizardFont("", null);
		htmlViewer.setText(EAM.loadResourceFile(this.getClass(), OVERVIEW_HTML));
		
		//TODO: Find a better way to calculate scroll bar width to subtract
		htmlViewer.setFixedWidth(htmlViewer,this.getSize().width - 20);

		return htmlViewer;
	}
	
	private String OVERVIEW_HTML = "Overview.html";

}


	class ImagesComponent extends JTabbedPane
	{

		public ImagesComponent()
		{
			setTabPlacement(JTabbedPane.TOP);
		
			String[] demoMaps = 
			{
				"Bay",
			};
			
			for(int i = 0; i < demoMaps.length; ++i)
			{
				String mapName = demoMaps[i] + ".jpg";
				JLabel image = new JLabel(new MiradiResourceImageIcon("images/" + mapName));
				image.setName(demoMaps[i]);
				add(image);
			}
		}
	
	}

