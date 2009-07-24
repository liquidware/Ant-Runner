/**
 * 
   Copyright (C) 2009 Christopher Ladden All rights reserved.
 
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.
 
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this library; if not, write to the Free Software
   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301
   USA
 * 
 */

package org.arduino.tools;

import java.io.File;
import java.io.Writer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Main;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.listener.AnsiColorLogger;

public class AntRunner {

	/**
	 * 
	 * @param buildfile
	 *            The buildfile.
	 */
	public void compile(String buildfile, String target) {
		run(new String[] { buildfile, target});
	}

	/**
	 * 
	 * @param args
	 *            The first argument = buildfile
	 *            The second argument = target
	 */
	public void run(final String args[]) {

		File buildFile = new File(args[0]);
		
		Project p = new Project();
		p.setUserProperty("ant.file", buildFile.getAbsolutePath());
		DefaultLogger consoleLogger = new DefaultLogger();
		consoleLogger.setErrorPrintStream(System.err);
		consoleLogger.setOutputPrintStream(System.out);
		consoleLogger.setMessageOutputLevel(Project.MSG_VERBOSE);
		p.addBuildListener(consoleLogger);

		try {
			p.fireBuildStarted();
			p.init();
			ProjectHelper helper = ProjectHelper.getProjectHelper();
			p.addReference("ant.projectHelper", helper);
			helper.parse(p, buildFile);
			
			if (args[1] != null) {
			   p.executeTarget(args[1]);
			} else {
			   p.executeTarget(p.getDefaultTarget());
			}
			
			p.fireBuildFinished(null);
		} catch (BuildException e) {
			p.fireBuildFinished(e);
		}

	}

	public static void main(String args[]) {
		AntRunner runner = new AntRunner();
	    runner.run(args);
	}
}
