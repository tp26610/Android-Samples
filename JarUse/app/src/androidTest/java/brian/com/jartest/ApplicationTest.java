package brian.com.jartest;

import android.app.Application;
import android.test.ApplicationTestCase;

import brian.com.jar.test.Jar;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testUseJar() {
        Jar jar = new Jar();
        assertEquals("Hello world From JAR !" ,jar.getStringFromJar());
    }
}