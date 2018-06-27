package io.keyss.keytools.utils;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;

/**
 * @author Key
 * Time: 2018/6/27 18:31
 * Description:
 */
public class KeyAudioRecoderUtil {
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
    public KeyAudioRecoderUtil() {
        //默认保存路径为/sdcard/record/下
        this(Environment.getExternalStorageDirectory() + "/record/");
    }

    public KeyAudioRecoderUtil(String filePath) {
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
    public String startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        try {
            /* ②setAudioSource/setVedioSource */
            // 设置麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            filePath = FolderPath + System.currentTimeMillis() + ".amr";
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            // AudioRecord audioRecord.
            /* 获取开始时间* */
            startTime = System.currentTimeMillis();
            updateMicStatus();
            Logger.e("startTime" + startTime);
            return filePath;
        } catch (IllegalStateException | IOException e) {
            Logger.e("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
            return null;
        }
    }

    /**
     * 停止录音
     *
     * @return 录音时长
     */
    public long stopRecord() {
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
        return endTime - startTime;
    }

    /**
     * 取消录音
     */
    public void cancelRecord() {
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
