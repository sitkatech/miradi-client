/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiScrollPane;


public class MatrixTableImageCreator  
{  
	
	static public BufferedImage getImage(Project project)
	{
		try
		{
			ThreatMatrixTableModel model = new ThreatMatrixTableModel(project);
			ThreatGridPanel grid = new ThreatGridPanel(null, model);
			//TODO: is there a better way to do this
			JFrame frame = new JFrame();
			frame.add(new UiScrollPane(grid));
			frame.pack();
			return createImage(grid.getThreatMatrixTable(), grid.getRowHeaderTable());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);  
		}
	}

	
    public static BufferedImage createImage(JTable table, JTable rowHeaderTable) 
    { 
		try
		{
			ValueOption myValueOption = new WhiteInvalidValueOption();
	    	((ThreatMatrixTableModel)table.getModel()).setDefaultValueOption(myValueOption);
	    	TableHeaderRenderer.SetOverRideColor(Color.WHITE);

	        JTableHeader tableHeaderSet = table.getTableHeader(); 
	        JTableHeader rowHeaderSet = rowHeaderTable.getTableHeader(); 
	         
	        int totalWidth	= table.getWidth() + rowHeaderTable.getWidth();
	        int totalHeight = tableHeaderSet.getHeight() + table.getHeight(); 
	
	        BufferedImage tableImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB); 
	        Graphics2D g2D = (Graphics2D)tableImage.getGraphics(); 
	 
	        writeData(rowHeaderSet, g2D, 0 ,0); 
	        writeData(tableHeaderSet, g2D, rowHeaderSet.getWidth(), 0); 
	        writeData(rowHeaderTable, g2D, -rowHeaderSet.getWidth(), rowHeaderSet.getHeight()); 
	        writeData(table, g2D, rowHeaderSet.getWidth(), 0); 

	        return tableImage;  
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);  
		}
		finally 
		{
	    	((ThreatMatrixTableModel)table.getModel()).setDefaultValueOption(null);
	    	TableHeaderRenderer.SetOverRideColor(null);
		}
		
    }


	private static void writeData(JComponent component, Graphics2D g2D, int offsetWidth, int offsetHeight)
	{
		g2D.translate(offsetWidth, offsetHeight);
		component.paint(g2D); 
	}

} 

 class WhiteInvalidValueOption extends ValueOption
	{
		public WhiteInvalidValueOption() throws Exception
		{
			super(new BaseId(-1), "", -1, Color.WHITE);
		}

		public Color getColor()
		{
			return Color.WHITE;
		}
	}