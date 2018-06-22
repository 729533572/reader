package com.yn.reader.util;

/**
 * Created by luhe on 2018/5/22.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.yn.reader.MiniApp;
import com.yn.reader.R;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by zhaoyong on 2016/6/21.
 * Glide缓存工具类
 */
public class ImageCacheUtil {

    private static ImageCacheUtil inst;

    public static ImageCacheUtil getInstance() {
        if (inst == null) {
            inst = new ImageCacheUtil();
        }
        return inst;
    }

    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(MiniApp.getInstance()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(MiniApp.getInstance()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(MiniApp.getInstance()).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache() {
        clearImageDiskCache();
        clearImageMemoryCache();
    }
    public void getDiskCacheSize(Context context, TextView textView) {
        //获取data下
        new GetDiskCacheSizeTask(textView).
                execute(new File(context.getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
        //获取sd卡下 //
        new GetDiskCacheSizeTask(textView).
                execute(new File(context.getExternalCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
    }

    class GetDiskCacheSizeTask extends AsyncTask<File, Long, Long> {
        private final TextView resultView;

        public GetDiskCacheSizeTask(TextView resultView) {
            this.resultView = resultView;
        }

        @Override
        protected void onPreExecute() {
            resultView.setText(R.string.calculating);
        }

        @Override
        protected void onProgressUpdate(Long... values) { /* onPostExecute(values[values.length - 1]); */ }

        @Override
        protected Long doInBackground(File... dirs) {
            try {
                long totalSize = 0;
                for (File dir : dirs) {
                    publishProgress(totalSize);
                    totalSize += calculateSize(dir);
                }
                return totalSize;
            } catch (RuntimeException ex) {
                final String message = String.format("Cannot get size of %s: %s", Arrays.toString(dirs), ex);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        resultView.setText("error");
                        Toast.makeText(resultView.getContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
            }
            return 0L;
        }

        @Override
        protected void onPostExecute(Long size) {
            String sizeText = android.text.format.Formatter.formatFileSize(resultView.getContext(), size);
            resultView.setText(sizeText);
        }

        private long calculateSize(File dir) {
            if (dir == null) return 0;
            if (!dir.isDirectory()) return dir.length();
            long result = 0;
            File[] children = dir.listFiles();
            if (children != null) for (File child : children) result += calculateSize(child);
            return result;
        }
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */

    public long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */

    public void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

}
