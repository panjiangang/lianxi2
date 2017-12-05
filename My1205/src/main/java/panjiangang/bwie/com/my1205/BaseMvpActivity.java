package panjiangang.bwie.com.my1205;

import android.app.Activity;
import android.os.Bundle;

/**
 * @param <V> 表示的View
 * @param <T> 表示的Presenter
 */
public abstract class BaseMvpActivity<V, T extends BasePresenter<V>> extends Activity {
    //              class BaseMvpActivity<LoginView,LoginPresenter extends BasePresneter<LoginView>> extends Activity {
    public T t;

    public abstract T initPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t = initPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        t.attach((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        t.detach();
    }
}
