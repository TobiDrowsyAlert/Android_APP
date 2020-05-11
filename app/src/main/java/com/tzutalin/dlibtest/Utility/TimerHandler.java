package com.tzutalin.dlibtest.Utility;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tzutalin.dlibtest.RetrofitConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerHandler extends Handler {
    public static final int MESSAGE_TIMER_START = 100;
    public static final int MESSAGE_TIMER_REPEAT = 101;
    public static final int MESSAGE_TIMER_STOP = 102;
    private String TAG = "TimerHandler";
    RetrofitConnection retrofitConnection;
    Context mContext;
    int count = 0;

    public TimerHandler(RetrofitConnection retrofitConnection, Context mContext){
        this.retrofitConnection = retrofitConnection;
        this.mContext = mContext;
        int count = 0;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case MESSAGE_TIMER_START:
                // 타이머 초기화
                count = 0;
                Log.d(TAG,"Timer START");
                this.removeMessages(MESSAGE_TIMER_REPEAT);
                this.sendEmptyMessage(MESSAGE_TIMER_REPEAT);
                break;
            case MESSAGE_TIMER_REPEAT:
                Log.d(TAG,"Timer count : " + count);

                // 60초마다 서버로 전송
                if(count % 10 == 0) {
                    Toast.makeText(mContext.getApplicationContext(), "카운트 동작",Toast.LENGTH_SHORT).show();
                    Call call = retrofitConnection.getServer().dropSleepStep();
                    call.enqueue(new retrofit2.Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                Log.e(TAG, "Sleep Step Drop Success");
                            } else {
                                Log.e(TAG, "Drop Fail");
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.e(TAG, "Network Error : " + t.toString());
                        }
                    });
                }

                count++;
                this.sendEmptyMessageDelayed(MESSAGE_TIMER_REPEAT, 1000);
                break;
            case MESSAGE_TIMER_STOP:
                Log.d(TAG, "Timer Stop");
                this.removeMessages(MESSAGE_TIMER_REPEAT);
                break;

        }
    }

}
