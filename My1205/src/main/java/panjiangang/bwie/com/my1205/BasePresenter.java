package panjiangang.bwie.com.my1205;

/**
 * Created by lenovo on 2017/12/05.
 */

public class BasePresenter<V> {

    // V 相当于V的接口
    public V view;

    /**
     * Presenter 持有View 的接口
     *
     * @param v
     */
    public void attach(V v) {
        this.view = v;
    }


    /**
     * Presenter 释放持有View的接口， 防止内存泄漏
     */
    public void detach() {
        this.view = null;
    }


}
