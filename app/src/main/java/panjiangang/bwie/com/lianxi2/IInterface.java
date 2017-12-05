package panjiangang.bwie.com.lianxi2;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by lenovo on 2017/12/04.
 */

public interface IInterface {

    @GET("/yunifang/mobile/home")
    Call<ResponseBody> get();


    @GET("/yunifang/mobile/home")
    Observable<Bean> get1();

}