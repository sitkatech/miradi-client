/* 
 * Copyright 2005-2007, Wildlife Conservation Society, 
 * Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */
package org.conservationmeasures.eam.views.umbrella;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.MainWindow;

public interface IWizardPanel
{

	public abstract void setContents(JPanel contents);

	public abstract void next() throws Exception;

	public abstract void previous() throws Exception;

	public abstract void setStep(int newStep) throws Exception;

	public abstract void refresh() throws Exception;

	public abstract void jump(Class stepMarker) throws Exception;

	public abstract int getCurrentStep();

	public abstract MainWindow getMainWindow();

}