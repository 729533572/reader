package com.hysoso.www.utillibrary;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by LuHe on 2016/11/10.
 */

public class ImageUtil {
    private static String TAG = ImageUtil.class.getSimpleName();
    /**
     * 给图片上打水印
     *
     * @param gContext
     * @param gResId
     * @param gText
     * @return Bitmap
     */
    public static Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

        Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61,61,61));
        // text size in pixels
        paint.setTextSize((int) (14 * scale*5));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
//		int x = (bitmap.getWidth() - bounds.width()) / 2;
//		int y = (bitmap.getHeight() + bounds.height()) / 2;
        //draw  text  to the bottom
        int x = (bitmap.getWidth() - bounds.width())/10*9 ;
        int y = (bitmap.getHeight() + bounds.height())/10*9;
        canvas.drawText(gText, x , y, paint);

        return bitmap;
    }

    /**
     * 将图片保存到本地时进行压缩, 即将图片从Bitmap形式变为File形式时进行压缩,
     * 特点是: File形式的图片确实被压缩了, 但是当你重新读取压缩后的file为 Bitmap是,它占用的内存并没有改变
     *
     * @param bmp
     * @param file
     */
    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;// 个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  将图片从本地读到内存时,进行压缩 ,即图片从File形式变为Bitmap形式
     *  特点: 通过设置采样率, 减少图片的像素, 达到对内存中的Bitmap进行压缩
     * @param srcPath
     * @return
     */
    public static Bitmap compressImageFromFile(String srcPath, float pixWidth, float pixHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);

        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;
        //float pixWidth = 800f;//
        //float pixHeight = 480f;//
        int scale = 1;
        if (w > h && w > pixWidth) {
            scale = (int) (options.outWidth / pixWidth);
        } else if (w < h && h > pixHeight) {
            scale = (int) (options.outHeight / pixHeight);
        }
        if (scale <= 0)
            scale = 1;
        options.inSampleSize = scale;// 设置采样率

        options.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
        options.inPurgeable = true;// 同时设置才会有效
        options.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, options);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        return bitmap;
    }

    /**
     *   指定分辨率和清晰度的图片压缩
     */
    public void transImage(String fromFile, String toFile, int width, int height, int quality)
    {
        try
        {
            Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            // 缩放图片的尺寸
            float scaleWidth = (float) width / bitmapWidth;
            float scaleHeight = (float) height / bitmapHeight;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 产生缩放后的Bitmap对象
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
            // save file
            File myCaptureFile = new File(toFile);
            FileOutputStream out = new FileOutputStream(myCaptureFile);
            if(resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)){
                out.flush();
                out.close();
            }
            if(!bitmap.isRecycled()){
                bitmap.recycle();//记得释放资源，否则会内存溢出
            }
            if(!resizeBitmap.isRecycled()){
                resizeBitmap.recycle();
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 缩小图片到指定大小
     *
     * @param bitMap
     * @param maxSize
     * @return Bitmap
     */
    public static Bitmap imageZoom(Bitmap bitMap,double maxSize) {
//        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = calculateImgSize(bitMap);
        LogUtil.e(TAG,"原来图片尺寸:"+mid+"/"+"w="+bitMap.getWidth()+":"+"h="+bitMap.getHeight());
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            double with = ((double) bitMap.getWidth())/ Math.sqrt(i);
            double height = ((double) bitMap.getHeight()) / Math.sqrt(i);
            LogUtil.e(TAG,"压缩图片目标尺寸:"+maxSize+"/"+"w="+with+":"+"h="+height);
            bitMap = zoomImage(bitMap,with,height);
            LogUtil.e(TAG,"压缩图片后尺寸:"+calculateImgSize(bitMap)+"/"+"w="+bitMap.getWidth()+":"+"h="+bitMap.getHeight());
        }
        return bitMap;
    }

    public static Bitmap rotateImage(Bitmap bitMap, Integer angle) {
        // 创建操作图片用的matrix对象
        Matrix m = new Matrix();
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        m.setRotate(angle); // 旋转angle度

        Bitmap bitmap = Bitmap.createBitmap(bitMap, 0, 0, width, height,m, true);// 从新生成图片

        return bitmap;

    }

    public static Double calculateImgSize(Bitmap bitmap){
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length/1024;

        return mid;
    }


    /**
     * 压缩图片到指定长宽
     *
     * @param bgimage
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    public static Bitmap imageFromResource(Context context,Integer resourceId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        InputStream is = context.getResources().openRawResource(resourceId);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        return bitmap;
    }

    public static void releaseImgMemory(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if (drawable!=null){
            if (drawable instanceof BitmapDrawable){
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap oldBitmap = bitmapDrawable.getBitmap();
                if (oldBitmap!=null&&!oldBitmap.isRecycled()) {
                    oldBitmap.recycle();
                    oldBitmap = null;
                }
            }
        }
    }
    /***
     * 保存图片至SD卡
     *
     * @param bm
     * @param url
     * @param quantity
     */
    private static int FREE_SD_SPACE_NEEDED_TO_CACHE = 1;
    private static int MB = 1024 * 1024;
    public final static String DIR = "/sdcard/hypers";

    public static void saveBmpToSd(Bitmap bm, String url, int quantity) {
        // 判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            return;
        }
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()))
            return;
        String filename = url;
        // 目录不存在就创建
        File dirPath = new File(DIR);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        File file = new File(DIR + "/" + filename);
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, quantity, outStream);
            outStream.flush();
            outStream.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     * 获取SD卡图片
     *
     * @param url
     * @param quantity
     * @return
     */
    public static Bitmap GetBitmap(String url, int quantity) {
        InputStream inputStream = null;
        String filename = "";
        Bitmap map = null;
        URL url_Image = null;
        String LOCALURL = "";
        if (url == null)
            return null;
        try {
            filename = url;
        } catch (Exception err) {
        }

        LOCALURL = URLEncoder.encode(filename);
        if (Exist(DIR + "/" + LOCALURL)) {
            map = BitmapFactory.decodeFile(DIR + "/" + LOCALURL);
        } else {
            try {
                url_Image = new URL(url);
                inputStream = url_Image.openStream();
                map = BitmapFactory.decodeStream(inputStream);
                // url = URLEncoder.encode(url, "UTF-8");
                if (map != null) {
                    saveBmpToSd(map, LOCALURL, quantity);
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return map;
    }

    /***
     * 判断图片是存在
     *
     * @param url
     * @return
     */
    public static boolean Exist(String url) {
        File file = new File(DIR + url);
        return file.exists();
    }

    /** * 计算sdcard上的剩余空间 * @return */
    private static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize()) / MB;

        return (int) sdFreeMB;
    }

    public static File getCameraHighPic(Context context,String localTempImgDir,String localTempImgFileName,Integer requestCode) {
        String status=Environment.getExternalStorageState();
        File file =null;
         try {
             if(status.equals(Environment.MEDIA_MOUNTED))
             {
                 File dir=new File(Environment.getExternalStorageDirectory()+"/"+localTempImgDir);
                 if(!dir.exists()){
                     dir.mkdirs();
                 }
                 Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 file=new File(dir,localTempImgFileName);//localTempImgDir和localTempImageFileName是自己定义的名字

                 Uri uri=Uri.fromFile(file);
                 intent.putExtra(MediaStore.Images.Media.ORIENTATION,0);
                 intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                 ((Activity)context).startActivityForResult(intent,requestCode);

             }else ToastUtil.showShort(context, "没有储存卡");
         } catch (ActivityNotFoundException e) {
        ToastUtil.showShort(context, "没有找到储存目录");
        }
        return file;
    }
    public static File zoomImgTOFile(final Bitmap bitmap) {
        final Bitmap temp_bitmap = ImageUtil.imageZoom(bitmap,100);
        LogUtil.e(TAG,ImageUtil.calculateImgSize(bitmap)+"/"+ImageUtil.calculateImgSize(temp_bitmap));

        String tempFilePath = Environment.getExternalStorageDirectory()+"/tempImg.jpg";
        ImageUtil.compressBmpToFile(temp_bitmap,new File(tempFilePath));

        return new File(tempFilePath);
    }
    public static File saveImgTOFile(final Bitmap bitmap) {
        LogUtil.e(TAG,ImageUtil.calculateImgSize(bitmap)+"/"+ImageUtil.calculateImgSize(bitmap));

        String tempFilePath = Environment.getExternalStorageDirectory()+"/tempImg.jpg";
        ImageUtil.compressBmpToFile(bitmap,new File(tempFilePath));

        return new File(tempFilePath);
    }
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    /**
     * 图片等比例压缩
     *
     * @param filePath
     * @param reqWidth  期望的宽
     * @param reqHeight 期望的高
     * @return
     */
    public static Bitmap decodeSampledBitmap(String filePath, int reqWidth,
                                             int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //bitmap is null
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    /**
     * 计算InSampleSize
     * 宽的压缩比和高的压缩比的较小值  取接近的2的次幂的值
     * 比如宽的压缩比是3 高的压缩比是5 取较小值3  而InSampleSize必须是2的次幂，取接近的2的次幂4
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            int ratio = heightRatio < widthRatio ? heightRatio : widthRatio;
            // inSampleSize只能是2的次幂  将ratio就近取2的次幂的值
            if (ratio < 3)
                inSampleSize = ratio;
            else if (ratio < 6.5)
                inSampleSize = 4;
            else if (ratio < 8)
                inSampleSize = 8;
            else
                inSampleSize = ratio;
        }

        return inSampleSize;
    }
}
