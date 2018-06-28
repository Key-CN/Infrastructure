package io.keyss.keytools.utils;

import android.Manifest;
import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;

/**
 * @author Key
 * Time: 2018/6/27 18:31
 * Description:
 */
public class KeyAudioRecorderUtil {
    private MediaRecorder mMediaRecorder;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 文件夹路径
     */
    private String FolderPath;
    /**
     * 最大录音时长1000*60*10;
     */
    public static final int MAX_LENGTH = 1000 * 60 * 10;
    private long startTime;
    private long endTime;
    private int BASE = 1;
    /**
     * 间隔取样时间
     */
    private int SPACE = 100;
    /**
     * 录音监听回调
     */
    private OnAudioStatusUpdateListener audioStatusUpdateListener;
    private RxPermissions mRxPermissions;
    private boolean isRunning;

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        @Override
        public void run() {
            updateMicStatus();
        }
    };


    /**
     * 文件存储默认sdcard/record
     */
    public KeyAudioRecorderUtil(Activity activity) {
        //默认保存路径为/sdcard/record/下
        this(activity, Environment.getExternalStorageDirectory() + "/record/");
    }

    public KeyAudioRecorderUtil(Activity activity, String filePath) {

        mRxPermissions = new RxPermissions(activity);


        File path = new File(filePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        this.FolderPath = filePath;
    }

    /**
     * 开始录音 使用amr格式
     * 录音文件
     *
     * @return 录音保存的路径
     */
    public void startRecord() {
        if (isRunning) {
            KeyToastUtil.showToast("当前正在录制");
            return;
        }
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .subscribe(granted -> {
                    if (granted) {
                        Logger.e("开始");
                        // 开始录音
                        if (mMediaRecorder == null) {
                            mMediaRecorder = new MediaRecorder();
                        }

                        try {
                            /* setAudioSource/setVedioSource */
                            // 设置麦克风
                            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            /* 设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
                            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                            /*
                             * 设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
                             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
                             */
                            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                            filePath = FolderPath + System.currentTimeMillis() + ".amr";
                            /* 准备 */
                            mMediaRecorder.setOutputFile(filePath);
                            mMediaRecorder.setMaxDuration(MAX_LENGTH);
                            mMediaRecorder.prepare();
                            /* 开始 */
                            mMediaRecorder.start();
                            // AudioRecord audioRecord.
                            /* 获取开始时间* */
                            startTime = System.currentTimeMillis();
                            updateMicStatus();
                            isRunning = true;
                        } catch (IllegalStateException | IOException e) {
                            Logger.e("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
                        }
                    } else {
                        KeyToastUtil.showToast("获取权限失败，无法使用录音功能");
                    }
                });
    }

    /**
     * 停止录音
     *
     * @return 录音时长
     */
    public long stopRecord() {
        if (!isRunning) {
            return 0;
        }
        if (mMediaRecorder == null) {
            return 0L;
        }
        endTime = System.currentTimeMillis();

        //有一些网友反应在5.0以上在调用stop的时候会报错，翻阅了一下谷歌文档发现上面确实写的有可能会报错的情况，捕获异常清理一下就行了，感谢大家反馈！
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            if (null != audioStatusUpdateListener) {
                audioStatusUpdateListener.onStop(filePath);
            }
            filePath = "";
        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            filePath = "";
            Logger.e("stopRecord error: " + e.getMessage());
        }
        isRunning = false;
        return endTime - startTime;
    }

    /**
     * 取消录音
     */
    public void cancelRecord() {
        if (!isRunning) {
            return;
        }
        if (mMediaRecorder == null) {
            return;
        }
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        filePath = "";
        isRunning = false;
    }

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * 更新麦克状态
     */
    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            // 分贝
            double db = 0;
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onUpdate(db, System.currentTimeMillis() - startTime);
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         *
         * @param db   当前声音分贝
         * @param time 录音时长
         */
        void onUpdate(double db, long time);

        /**
         * 停止录音
         *
         * @param filePath 保存路径
         */
        void onStop(String filePath);
    }
}
