package brian.com.jnidemouseso;

import android.app.Application;
import android.test.ApplicationTestCase;

import brian.com.jnidemowithgradle.Jni;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    public void testJniMethod() {
        Jni jni = new Jni();
        assertEquals("Hello From JNI !", jni.getStringFromNative());
    }
}