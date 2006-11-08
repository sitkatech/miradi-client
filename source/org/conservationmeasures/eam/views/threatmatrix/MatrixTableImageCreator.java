/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.table.*; 
import javax.swing.*; 
import java.awt.image.BufferedImage; 
import java.awt.Graphics2D; 


public class MatrixTableImageCreator  
{ 

    public static BufferedImage createImage(JTable table, JTable rowHeaderTable) 
    { 
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


	private static void writeData(JComponent component, Graphics2D g2D, int offsetWidth, int offsetHeight)
	{
		g2D.translate(offsetWidth, offsetHeight);
		component.paint(g2D); 
	}


	
    

} 