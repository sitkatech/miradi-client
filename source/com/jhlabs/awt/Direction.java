/*
 * This code was downloaded from: http://www.jhlabs.com/java/layout/index.html
 * on 2006-02-06. According to that page, here is the license:
 * 
 * 
 * License

 The downloadable source code on this page is released under the Apache License. 
 Basically, this means that you are free to do whatever you like with this code, 
 including commercial use, but it's not my fault if your satellite/nuclear 
 power station/missile system fails as a result.

 Licensed under the Apache License, Version 2.0 (the "License"); 
 you may not use this code except in compliance with the License. 
 You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 Unless required by applicable law or agreed to in writing, software distributed under 
 the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS 
 OF ANY KIND, either express or implied. See the License for the specific language 
 governing permissions and limitations under the License.
 */
package com.jhlabs.awt;

/**
 * Defines commonly used constants for directions and positions.
 */

public interface Direction {
	public final static int CENTER = 0;
	public final static int NORTH = 1;
	public final static int NORTHEAST = 2;
	public final static int EAST = 3;
	public final static int SOUTHEAST = 4;
	public final static int SOUTH = 5;
	public final static int SOUTHWEST = 6;
	public final static int WEST = 7;
	public final static int NORTHWEST = 8;

	public final static int LEFT = WEST;
	public final static int RIGHT = EAST;
	public final static int TOP	= NORTH;
	public final static int BOTTOM = SOUTH;
	public final static int UP	= NORTH;
	public final static int DOWN = SOUTH;
}

