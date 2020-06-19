package com.tzutalin.dlibtest.Utility;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tzutalin.dlibtest.RetrofitConnection;
import com.tzutalin.dlibtest.domain.ResponseLandmarkDTO;
import com.tzutalin.dlibtest.user.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerMinuteHandler extends Handler{
    public static final int MESSAGE_TIMER_START = 100;
    public static final int MESSAGE_TIMER_REPEAT = 101;
    public static final int MESSAGE_TIMER_STOP = 102;
    public static final int MESSAGE_TIMER_PAUSE = 103;

    private String TAG = "TimerMinuteHandler";
    RetrofitConnection retrofitConnection;
    Context mContext;
    int count = 0;

    public TimerMinuteHandler(RetrofitConnection retrofitConnection, Context mContext){
        this.retrofitConnection = retrofitConnection;
        this.mContext = mContext;
        int count = 0;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case MESSAGE_TIMER_START:
                // 타이머 초기화
                Log.d(TAG,"Timer START");
                this.removeMessages(MESSAGE_TIMER_REPEAT);
                this.removeMessages(MESSAGE_TIMER_PAUSE);
                this.sendEmptyMessage(MESSAGE_TIMER_REPEAT);
                break;
            case MESSAGE_TIMER_REPEAT:
                Log.d(TAG,"Timer count : " + count);
                //Toast.makeText(mContext.getApplicationContext(), "" + count,Toast.LENGTH_SHORT).show();
                // 60초마다 서버로 전송
                if(count % 60 == 0) {
                    //Toast.makeText(mContext.getApplicationContext(), "카운트 동작",Toast.LENGTH_SHORT).show();
                    Call<ResponseLandmarkDTO> call = retrofitConnection.getServer().timer(User.getInstance().getUserDTO());
                    call.enqueue(new retrofit2.Callback<ResponseLandmarkDTO>() {
                        @Override
                        public void onResponse(Call<ResponseLandmarkDTO> call, Response<ResponseLandmarkDTO> response) {
                            if (response.isSuccessful()) {
                                Log.e(TAG, "Handler Response Success : " + response.body().getAvgStage());
                            } else {
                                Log.e(TAG, "Handler Work Fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseLandmarkDTO> call, Throwable t) {
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
                this.removeMessages(MESSAGE_TIMER_PAUSE);
                break;
        }
    }

}
