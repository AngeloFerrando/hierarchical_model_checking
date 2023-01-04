package edu.toronto.cs.yasm.junit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.toronto.cs.algebra.AlgebraValue;
import edu.toronto.cs.ctl.antlr.CTLNodeParser;
import edu.toronto.cs.ctl.antlr.CTLNodeParser.CTLNodeParserException;
import edu.toronto.cs.yasm.YasmApp;
import junit.framework.TestCase;

/**
 * Created: Aug 10, 2004
 * 
 * @author jamir
 * @version 1.0
 */
public class GenericTestCase extends TestCase {

	protected String root;
	
	protected Properties properties;
	
	protected String expected;
	
	public GenericTestCase () {
		super();
		root = System.getProperty("ROOT");
		if (root == null) root = "";
	}

	
	public GenericTestCase (String name) {
		super(name);
		root = System.getProperty("ROOT");
		if (root == null) root = "";
	}
	
	
/* Reading configuration from a property file to setup
 * the initial configuration of YasmApp
 */
	
	protected void loadProperties (YasmApp yasmApp, Properties props) throws CTLNodeParser.CTLNodeParserException {
		String filename = root + "/" + props.getProperty("file.name");
		yasmApp.setBpFile(new File(filename));
		String property = props.getProperty("property");
		yasmApp.setProp(CTLNodeParser.parse(property));
		String once = props.getProperty("once");
		if (once != null) {
			yasmApp.setDoProof(!(new Boolean (once).booleanValue()));
		}
		String hyper = props.getProperty("hyper");
		if (hyper != null) {
			yasmApp.setHyper (new Boolean (hyper).booleanValue());
		}
		String cFile = props.getProperty("c.file");
		if (cFile != null) {
			yasmApp.setCFile(new Boolean (cFile).booleanValue());
		}
		String ndInit = props.getProperty("nd.init");
		if (ndInit != null) {
			yasmApp.setUnknownInit(!(new Boolean (ndInit).booleanValue()));
		}
		String refinerType = props.getProperty("refiner.type");
		if (refinerType != null) {
			yasmApp.setRefinerType(refinerType);
		}
		String pgType = props.getProperty("p.generator.type");
		if (pgType != null) {
			yasmApp.setPGeneratorType(pgType);
		}
		String initPred = props.getProperty("init.pred");
		if (initPred != null) {
			yasmApp.setInitPredFile (new File (root + "/" + initPred));
		}
		else {
			yasmApp.setInitPredFile(null);
		}

    String selectorType = props.getProperty ("selector.type");
    if (selectorType != null)
    {
      yasmApp.setSelectorType (selectorType);
    }
    else
    {
      yasmApp.setSelectorType ("dd");
    }
		expected = props.getProperty ("expected.value");
		
	}

	public void doTest() {
		System.out.println ("////////////////////////////////////////////");
		System.out.println ("////////////////////////////////////////////");
		System.out.println ("////////////////////////////////////////////");
		System.out.println ("///                                      ///");
		System.out.println ("///                                      ///");
		System.out.println ("///    Test " + getName() + " starts     ///");
		System.out.println ("///                                      ///");
		System.out.println ("///                                      ///");
		System.out.println ("////////////////////////////////////////////");
		System.out.println ("////////////////////////////////////////////");
		System.out.println ("////////////////////////////////////////////");
		System.err.println ("////////////////////////////////////////////");
		System.err.println ("////////////////////////////////////////////");
		System.err.println ("////////////////////////////////////////////");
		System.err.println ("///                                      ///");
		System.err.println ("///                                      ///");
		System.err.println ("///    Test " + getName() + " starts     ///");
		System.err.println ("///                                      ///");
		System.err.println ("///                                      ///");
		System.err.println ("////////////////////////////////////////////");
		System.err.println ("////////////////////////////////////////////");
		System.err.println ("////////////////////////////////////////////");
		AlgebraValue result = null;
		try {
			YasmApp yasmApp = new YasmApp();
			loadProperties(yasmApp, properties);
			result = yasmApp.run();
		} catch (CTLNodeParserException ex) {
			YasmApp.out.println("Could not parse CTL property: " + ex);
			ex.printStackTrace(YasmApp.err);
		} catch (Throwable ex) {
			YasmApp.out.println("Unhandled Exception: " + ex);
			ex.printStackTrace();
		}
		YasmApp.out.flush();
		YasmApp.err.flush();
		System.out.println ("Generic: result is: " + result + ".");
		assertNotNull(result);
		assertEquals(result.toString(), expected);
	}
	
	public void runTest () {
		doTest ();
	}
	
	/**
	 * @return Returns the root.
	 */
	public String getRoot() {
		return root;
	}
	/**
	 * @param root The root to set.
	 */
	public void setRoot(String root) {
		this.root = root;
	}
	/**
	 * @return Returns the properties.
	 */
	public Properties getProperties() {
		return properties;
	}
	/**
	 * @param properties The properties to set.
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
