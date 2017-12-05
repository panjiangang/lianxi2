package panjiangang.bwie.com.my1205;

import panjiangang.bwie.com.my1205.bean.Bean;

/**
 * Created by lenovo on 2017/12/05.
 */

public class LoginPresenter extends BasePresenter<LoginView> {

    public LoginModelImpl loginModel;

    public LoginPresenter() {
        this.loginModel = new LoginModelImpl();
    }

    public void login(String username, String password) {
        //  判断
//        view.success();
        loginModel.getData(new LoginModelCallBack() {
            @Override
            public void success(Bean bean) {
                view.success(bean);
            }
        });

    }
}

