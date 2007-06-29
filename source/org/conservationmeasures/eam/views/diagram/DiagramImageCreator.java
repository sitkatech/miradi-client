package org.conservationmeasures.eam.views.diagram;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.utils.CodeList;
import org.martus.swing.UiScrollPane;

public class DiagramImageCreator
{
	static public BufferedImage getImage(MainWindow mainWindow, DiagramObject diagramObject)
	{
		try
		{
			DiagramComponent comp =  DiagramSplitPane.createDiagram(mainWindow, diagramObject);
			comp.getDiagramModel().updateVisibilityOfFactors();
			//TODO: is there a better way to do this
			JFrame frame = new JFrame();
			frame.add(new UiScrollPane(comp));
			frame.pack();
			return comp.getImage();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return null;
		}
	}
	
	static public BufferedImage getImageWithLegendSetting(MainWindow mainWindow, DiagramObject diagramObject, CodeList list)
	{
		try
		{
			DiagramComponent comp =  DiagramSplitPane.createDiagram(mainWindow, diagramObject);
			updateLegendPanel(mainWindow, list);
			comp.getDiagramModel().updateVisibilityOfFactors();
			//TODO: is there a better way to do this
			JFrame frame = new JFrame();
			frame.add(new UiScrollPane(comp));
			frame.pack();
			return comp.getImage();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return null;
		}
	}
	
	static public void updateLegendPanel(MainWindow mainWindow, CodeList list)
	{
		DiagramLegendPanel panel = mainWindow.getDiagramView().getDiagramPanel().getDiagramLegendPanel();
		panel.updateLegendPanel(list);
	}
}
