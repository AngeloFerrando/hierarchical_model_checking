package junit.sample;

import java.io.File;
import java.io.IOException;

import edu.toronto.cs.yasm.junit.SuiteHelper;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created: Aug 10, 2004
 * 
 * @author jamir
 * @version 1.0
 */
public class SampleTestSuite extends TestSuite {

	public static Test suite () throws IOException {
		// getting the directory containing this class
		SampleTestSuite x = new SampleTestSuite ();
		File dir = new File (x.getClass().getResource(".").getPath());
		return SuiteHelper.suite (dir);
	}

}
