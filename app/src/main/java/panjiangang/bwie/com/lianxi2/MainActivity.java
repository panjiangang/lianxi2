package panjiangang.bwie.com.lianxi2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                zip1();//rxjava 与 retrofit网络请求结合
//                onObserverable();//观察者  被观察者
//                test1();//休眠
//                test2();//一共只存储128个    从下标0到下标126  加上  最后一个下标
//                test3();
//                test4();
//                test5();
//                zip();
                concatMap();
            }
        });
    }

    private void zip1() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://m.yunifang.com")
                .addConverterFactory(GsonConverterFactory.create())
                // call 转化成 Observerable
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        IInterface iInterface = retrofit.create(IInterface.class);

        iInterface.get1()
                // 指定 被观察者 所在一个IO线程
                .subscribeOn(Schedulers.io())
                //指定观察者所在 主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bean>() {
                    @Override
                    public void accept(Bean bean) throws Exception {
                        Toast.makeText(MainActivity.this, "" + bean.getData().getSubjects().get(0).getDetail(), Toast.LENGTH_SHORT).show();
                        System.out.println("bean = " + bean.toString());
                    }
                });
    }

    Disposable disposable = null;

    private void onObserverable() {
        //创建了一个被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e 发射器
                //发送onnext事件
                for (int i = 0; i < 100; i++) {
//                    if(i == 50){
//                        disposable.dispose();//断开 被观察者和观察者之间的关系
//                    }
                    e.onNext("" + i);
                    if (i == 50) {
//                        e.onComplete();//事件完成
//                        e.onError(new Exception("1"));//发生异常
                    }
                    //只要执行了  onComplete  onError ， 后续在发送的onNext 事件， 再也无法接受
                }
//                e.onError(new Exception(""));
//                e.onComplete();
            }
        });


        //创建观察者
        Observer observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
//                断开 被观察者和观察者之间的关系
//                disposable.dispose();
                // 判断 被观察者和观察者 之间是否还订阅关系
//                disposable.isDisposed()
            }

            @Override
            public void onNext(String s) {
                //用于接受  被观察者所方法的事件
                System.out.println("Observer s = " + s);
            }

            @Override
            public void onError(Throwable e) {
                //发生异常
                System.out.println("onError s = " + e.getMessage());
            }

            @Override
            public void onComplete() {
//                 事件完成
                System.out.println("onComplete s = ");
            }
        };

        //订阅
        observable.subscribe(observer);
    }

    private void test1() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                System.out.println("Observable  0" + Thread.currentThread().getName());
                for (int i = 0; i < 10; i++) {
                    e.onNext(i + "");//要传到下面的内容
                }
                System.out.println("Observable over 1");
            }
//            subscribeOn 指定 被观察者所在的线程
        }).subscribeOn(Schedulers.io())
//                指定 观察者所在的线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("Observer  2" + Thread.currentThread().getName());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(" " + s);// s 是上面传下来的内容
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void test2() {//一共只存储128个    从下标0到下标126  加上  最后一个下标

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                for (int i = 0; i < 500; i++) {
                    e.onNext("" + i);
                }
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Thread.sleep(100);
                        System.out.println("Consumer   " + s);
                    }
                });
    }

    private void test3() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("onnext");
//                e.onError(new Exception("1"));
//                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
//                        onnext
                        System.out.println("Consumer  onnext" + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                      onerror
                        System.out.println("Consumer  onerror" + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
//                          onComplet
                        System.out.println("Consumer  Action");
                    }
                });
    }

    private void test4() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                e.onNext(1);
//                e.onError(new Exception(""));
            }
        }, BackpressureStrategy.BUFFER).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(" onNext");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println(" onError");
            }
        });
    }

    private void test5() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(final FlowableEmitter<String> e) throws Exception {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://m.yunifang.com")
                        .build();

                IInterface iInterface = retrofit.create(IInterface.class);

                iInterface.get().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            e.onNext(result);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }, BackpressureStrategy.BUFFER)
                .map(new Function<String, Bean>() {
                    @Override
                    public Bean apply(String s) throws Exception {
                        Gson gson = new Gson();
                        return gson.fromJson(s, Bean.class);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bean>() {
                    @Override
                    public void accept(Bean bean) throws Exception {

                    }
                });
    }

    private void zip() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
            }
        });

        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("A");
                e.onNext("B");
                e.onNext("C");
            }
        });

        Observable.zip(observable, observable1, new BiFunction<String, String, String>() {
            @Override
            public String apply(String s, String s2) throws Exception {
                return s + s2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s = " + s);
            }
        });
    }

    private void concatMap() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
            }
        }).concatMap(new Function<String, ObservableSource<ArrayList>>() {
            @Override
            public ObservableSource<ArrayList> apply(String s) throws Exception {
                ArrayList list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add(s + " " + i);
                }
                return Observable.fromArray(list).delay(1, TimeUnit.SECONDS);
//                return null;
            }
        }).subscribe(new Consumer<ArrayList>() {
            @Override
            public void accept(ArrayList arrayList) throws Exception {
                for (int i = 0; i < arrayList.size(); i++) {
                    System.out.println("arrayList = " + arrayList.get(i));
                }
            }
        });
    }

}
