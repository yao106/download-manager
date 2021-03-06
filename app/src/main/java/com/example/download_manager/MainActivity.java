package com.example.download_manager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.dldmgr.DownloadInfo;
import com.sd.lib.dldmgr.DownloadManager;
import com.sd.lib.dldmgr.FDownloadManager;
import com.sd.lib.dldmgr.TransmitParam;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String URL = "https://dldir1.qq.com/weixin/Windows/WeChatSetup.exe";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 添加下载回调
        FDownloadManager.getDefault().addCallback(mDownloadCallback);
    }

    private final DownloadManager.Callback mDownloadCallback = new DownloadManager.Callback()
    {
        @Override
        public void onPrepare(DownloadInfo info)
        {
            Log.i(TAG, "onPrepare:" + info.getUrl());
        }

        @Override
        public void onProgress(DownloadInfo info)
        {
            final TransmitParam param = info.getTransmitParam();

            // 下载进度
            final int progress = param.getProgress();
            // 下载速率
            final int speed = param.getSpeedKBps();

            Log.i(TAG, "onProgress:" + progress + " " + speed);
        }

        @Override
        public void onSuccess(DownloadInfo info, File file)
        {
            Log.i(TAG, "onSuccess:" + info.getUrl() + "\r\n"
                    + " file:" + file.getAbsolutePath());
        }

        @Override
        public void onError(DownloadInfo info)
        {
            Log.e(TAG, "onError:" + info.getError());
        }
    };

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_download:
                // 添加下载任务
                FDownloadManager.getDefault().addTask(URL);
                break;
            case R.id.btn_cancel:
                // 取消下载任务
                FDownloadManager.getDefault().cancelTask(URL);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // 移除下载回调
        FDownloadManager.getDefault().removeCallback(mDownloadCallback);
        // 删除所有临时文件(下载中的临时文件不会被删除)
        FDownloadManager.getDefault().deleteTempFile();
        // 删除下载文件（临时文件不会被删除）
        FDownloadManager.getDefault().deleteDownloadFile(null);
    }
}
