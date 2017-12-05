package panjiangang.bwie.com.my1205;

import java.util.Map;

import io.reactivex.Observable;
import panjiangang.bwie.com.my1205.bean.Bean;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by lenovo on 2017/12/05.
 */

public interface IInterface {

    @GET("/nba")
    Observable<Bean> get(@QueryMap Map<String,String> map);

    @POST("/nba")
    Observable<Bean> post(@FieldMap Map<String,String> map);

}
