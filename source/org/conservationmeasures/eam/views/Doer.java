/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.exceptions.CommandFailedException;

abstract public class Doer
{
	abstract public boolean isAvailable();
	abstract public void doIt() throws CommandFailedException;
}
