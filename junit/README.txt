To create your own test suite, follow these instructions:

1) create a directory under the junit directory, e.g.
junit/mydir

2) Copy the class SampleTestSuite.java and rename it to
MydirTestSuite.java. Edit the file and replace any occurrences
of SampleTestSuite in the code with MydirTestSuite.

3) Compile the class (this is not necessary any more. The ant
task will compile these classes by default.

4) create some test case files, as explained in the comments
inside test1.properties file in the sample directory.

The test suite class will include one test case for each .properties
file in the directory where the class is located, except
for the default.properties file.
For each test cases, properties are first loaded from
default.properties, and then from the corresponding .properties
file. Thus, properties in the .properties files can override
the defaults.

To run all test suite under junit directory, run the "yasm.test"
target of the testing.xml file. Since testing.xml is inculded
by build.xml, all that you need to run is "ant yasm.test" from
the command line.

The results of the tests (in html format) can be found at
/w/10/share/junitreports/yasm.