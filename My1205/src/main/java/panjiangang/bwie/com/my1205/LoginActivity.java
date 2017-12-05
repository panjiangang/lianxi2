package panjiangang.bwie.com.my1205;

import android.os.Bundle;
import android.view.View;

import panjiangang.bwie.com.my1205.bean.Bean;

/**
 * Created by lenovo on 2017/12/05.
 */

public class LoginActivity extends BaseMvpActivity<LoginView, LoginPresenter> implements LoginView {

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.login("muhanxi", "password");
            }
        });
    }

    @Override
    public void success(Bean bean) {
        System.out.println("ok" + bean.toString());

    }

    @Override
    public void failure() {

    }
}
