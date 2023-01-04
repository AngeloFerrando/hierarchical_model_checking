package edu.toronto.cs.yasm.junit;

import java.io.*;
import java.util.*;
import junit.framework.*;

/**
 * Created: Aug 10, 2004
 * 
 * @author jamir
 * @version 1.0
 */
public class SuiteHelper {

	static class PropertyFileFilter implements FileFilter {
		public boolean accept(File file) {
			String name = file.getName();
			if (name.equals("default.properties"))
				return false;
			if (name.endsWith(".properties"))
				return true;
			return false;

		}
	}

	public static Test suite(File dir) {
		try {
		// set up the default properties
		File defaultPropsFile = new File(dir, "default.properties");
		Properties defaultProps = new Properties();
		if (defaultPropsFile.exists()) {
			defaultProps.load(new FileInputStream(defaultPropsFile));
		}
		// iterate through all files in the directory which
		// has extension .properties (except default.properties)
		// and create a test case from them
		File[] files = dir.listFiles(new PropertyFileFilter());
		Arrays.sort(files, new Comparator() {
			public int compare(Object obj1, Object obj2) {
				File file1 = (File) obj1;
				File file2 = (File) obj2;
				return file1.getName().compareTo(file2.getName());
			}
		});
		TestSuite suite = new TestSuite();
		for (int i = 0; i < files.length; i++)
		{
			File tmpFile = files[i];
			 // e.g. test1.properties
			String tmpFilename = files[i].getName();
			int dotIndex = tmpFilename.lastIndexOf('.');
			 // e.g. test1
			tmpFilename = tmpFilename.substring(0, dotIndex);
			GenericTestCase tmpTest = new GenericTestCase(tmpFilename);
			Properties tmpProps = new Properties(defaultProps);
			tmpProps.load(new FileInputStream(tmpFile));
			tmpTest.setProperties(tmpProps);
			suite.addTest(tmpTest);
		}
		return suite;
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return new TestSuite ();
		}
	}

}