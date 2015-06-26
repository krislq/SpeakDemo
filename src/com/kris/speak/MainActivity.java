package com.kris.speak;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechSynthesizer;
import com.iflytek.speech.SpeechUtility;
import com.iflytek.speech.SynthesizeToUrlListener;
import com.iflytek.speech.SynthesizerListener;

public class MainActivity extends Activity {

    private SpeechSynthesizer mTts = null;
    private EditText mEtContent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtContent = (EditText)findViewById(R.id.et_content);

        mEtContent.setText("确认请按1，拒绝请按2.");
        init();
    }
    
    private void init() {

        SpeechUtility.getUtility(this).setAppid("53043ae5");
        // 初始化合成对象
        mTts = new SpeechSynthesizer(this, mTtsInitListener);
        // 设置引擎类型
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");//local
        // 设置发音人
        mTts.setParameter(SpeechSynthesizer.VOICE_NAME, "xiaoyan");
        // 设置语速
        mTts.setParameter(SpeechSynthesizer.SPEED, "50");
        // 设置音调
        mTts.setParameter(SpeechSynthesizer.PITCH, "50");
//        mTts.setParameter("tts_audio_path", "/sdcard/kkk.mp3");
        mTts.setParameter(SpeechConstant.PARAMS, "tts_audio_path=/sdcard/process_4.pcm");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void l(String msg) {
        Log.e("Speak", msg);
    }

    public void OnSpeak(View v) {
        String content = mEtContent.getText().toString();
        // 开始合成
        int code = mTts.startSpeaking(content, mTtsListener);
//        int code = mTts.synthesizeToUrl("开始合成", mSynthesizeToUrlListener);
        l("code:" + code);
        // 停止
//        mTts.stopSpeaking(mTtsListener);
//        // 暂停播放
//        mTts.pauseSpeaking(mTtsListener);
//        // 恢复播放
//        mTts.resumeSpeaking(mTtsListener);
    }
    
    public SynthesizeToUrlListener mSynthesizeToUrlListener = new SynthesizeToUrlListener() {
        
        @Override
        public IBinder asBinder() {
            return null;
        }
        
        @Override
        public void onSynthesizeCompleted(String arg0, int arg1)
                throws RemoteException {
            l("mSynthesizeToUrlListener onSynthesizeCompleted:" + arg0+"#"+arg1);
            
        }
    };

    InitListener mTtsInitListener = new InitListener() {

        @Override
        public void onInit(ISpeechModule arg0, int arg1) {
            l("onInit:" + arg1);
        }
    };

    // 合成回调监听
    SynthesizerListener mTtsListener = new SynthesizerListener.Stub() {
        @Override
        public void onBufferProgress(int progress) throws RemoteException {
            // 缓冲进度回调
            l("SynthesizerListener onBufferProgress:" + progress);
        }

        @Override
        public void onCompleted(int code) throws RemoteException {
            // 结束回调
            l("SynthesizerListener onCompleted:" + code);
        }

        @Override
        public void onSpeakBegin() throws RemoteException {
            // 开始播放回调
            l("SynthesizerListener onSpeakBegin");
        }

        @Override
        public void onSpeakPaused() throws RemoteException {
            // 暂停回调
            l("SynthesizerListener onSpeakPaused");
        }

        @Override
        public void onSpeakProgress(int progress) throws RemoteException {
            // 播放进度回调
            l("SynthesizerListener onSpeakProgress:" + progress);
        }

        @Override
        public void onSpeakResumed() throws RemoteException {
            // 重新播放回调
            l("SynthesizerListener onSpeakResumed");
        }
    };
}
