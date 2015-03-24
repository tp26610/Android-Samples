package brian.com.jnidemowithgradle;

/**
 * Created by Yeh on 2015/3/24.
 */
public class Jni {

    static {
        System.loadLibrary("MyLib");
    }

    public native String getStringFromNative();

}
