package panjiangang.bwie.com.my1205;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import panjiangang.bwie.com.my1205.bean.Bean;

/**
 * Created by lenovo on 2017/12/05.
 */

public class LoginModelImpl {
    public void getData(final LoginModelCallBack callBack) {

        Map<String, String> map = new HashMap<>();
        map.put("key", "71e58b5b2f930eaf1f937407acde08fe");
        map.put("num", "10");
        MyApplication.iInterface.get(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bean>() {
                    @Override
                    public void accept(Bean bean) throws Exception {
                        callBack.success(bean);
                    }
                });
    }
}

