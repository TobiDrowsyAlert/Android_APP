package com.tzutalin.dlibtest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import com.naver.speech.clientapi.SpeechRecognitionResult;
import com.tzutalin.dlibtest.Utility.MediaUtility;
import com.tzutalin.dlibtest.utils.AudioWriterPCM;

import java.lang.ref.WeakReference;
import java.util.List;

// 1. Main Activity 클래스
public class SpeechMainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CLIENT_ID = "lpsdk0tjp1"; // "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;
    private TextView txtResult;
    private Button btnStart;
    private Button btnRandomSound;
    private String mResult;
    private AudioWriterPCM writer;
    MediaUtility mediaUtility;


    Bitmap bitmap_mic;  // 마이크 이미지
    ImageView mic;   // 마이크 이미지

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady: // 음성인식 준비 가능
                txtResult.setText("Connected");
                writer = new AudioWriterPCM(Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;
            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;
            case R.id.partialResult:

                // 단점 '아~~' 이런 인식 안될 때는 마이크 이미지 안나옴 인식이 안되므로
                mic.bringToFront();  // 말 할때부터 마이크 이미지 보여주기
                mic.setImageBitmap(bitmap_mic);  // ~~
                mic.setVisibility(View.VISIBLE);  // ~~

                mResult = (String) (msg.obj);
                txtResult.setText(mResult);

                break;
            case R.id.finalResult: // 최종 인식 결과
                SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
                List<String> results = speechRecognitionResult.getResults();
                StringBuilder strBuf = new StringBuilder();
                for(String result : results) {
                    strBuf.append(result);
                    strBuf.append("\n");
                }
                mResult = strBuf.toString();
                txtResult.setText(mResult);

                mic.setVisibility(View.GONE);     // 출력 결과 이후 마이크 이미지 없애기

                break;
            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }
                mResult = "Error code : " + msg.obj.toString();
                txtResult.setText(mResult);
                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                break;
            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }
                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        txtResult = (TextView) findViewById(R.id.txt_result);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnRandomSound = (Button) findViewById(R.id.btn_random_speech);
        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID);
        mediaUtility = MediaUtility.getInstance(this);

        mic = (ImageView)findViewById(R.id.micimage);
        bitmap_mic = BitmapFactory.decodeResource(getResources(), R.drawable.mic_48);

        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) { }
*/

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
                    mResult = "";
                    txtResult.setText("Connecting...");
                    btnStart.setText(R.string.str_stop);
                    naverRecognizer.recognize();
                } else {
                    Log.d(TAG, "stop and wait Final Result");
                    btnStart.setEnabled(false);
                    naverRecognizer.getSpeechRecognizer().stop();
                }
            }
        });

        btnRandomSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaUtility.randomStart();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart(); // 음성인식 서버 초기화는 여기서
        naverRecognizer.getSpeechRecognizer().initialize();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mResult = "";
        txtResult.setText("");
        btnStart.setText(R.string.str_start);
        btnStart.setEnabled(true);
    }
    @Override
    protected void onStop() {
        super.onStop(); // 음성인식 서버 종료
        naverRecognizer.getSpeechRecognizer().release();
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<SpeechMainActivity> mActivity;
        RecognitionHandler(SpeechMainActivity activity) {
            mActivity = new WeakReference<SpeechMainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            SpeechMainActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}
