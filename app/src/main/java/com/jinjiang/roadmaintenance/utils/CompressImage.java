package com.jinjiang.roadmaintenance.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.apkfuns.logutils.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CompressImage {


    public static File getSmallFile(String path, int maxNumOfPixels, int size,String savepath,String filename){

        return  savePhotoToSDCard(getSmallPath(path,maxNumOfPixels,size),savepath,filename);
    }
    public static File getSmallFile(String path,String savepath,String filename){

        return  savePhotoToSDCard(getSmallPath(path,0,0),savepath,filename);
    }
    /**
     * 根据文件路径限制图片尺寸大小与质量大小
     *
     * @param path           文件路径
     * @param maxNumOfPixels 最大像素尺寸如640*960
     * @return
     */
    public static Bitmap getSmallPath(String path, int maxNumOfPixels, int size) {
        /**
         * 尺寸压缩
         */
        if (maxNumOfPixels==0){
            maxNumOfPixels=320*480;
        }
        if (size==0){
            size=200;
        }
        Bitmap bm = getSmallBitmap(path, maxNumOfPixels);
        if (bm == null) {
            return null;
        }
        if (bm != null) {
//          FileOutputStream op = new FileOutputStream(path);
            /**
             * 质量压缩
             */
            int options = 100;//初始质量值100%，如果尺寸压缩后已经满足则不进行质量压缩
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
            LogUtils.i("压缩前：" + baos.toByteArray().length / 1024);
            while (baos.toByteArray().length / 1024 > size / 2) { // 循环判断如果压缩后图片是否大于sizekb,大于继续压缩
                LogUtils.i("循环判断如果压缩后图片是否大于sizekb1" + baos.toByteArray().length / 1024);
                options -= 5;// 每次都减少5
                baos.reset();// 重置baos即清空baos
                bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                LogUtils.i("循环判断如果压缩后图片是否大于sizekb2" + baos.toByteArray().length / 1024);
            }
            LogUtils.i("压缩后：" + baos.toByteArray().length / 1024);
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
            return bitmap;

        }
//            DebugLog.loge("", "getSmallBitmap width 44 = " + bm.getWidth()+ " height = " + bm.getHeight());
//            DebugLog.loge("", "getSmallBitmap file.getAbsolutePath() = " + file.getAbsolutePath());
        return null;
    }

    public static Bitmap getSmallBitmap(String filePath, int maxNumOfPixels) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
//        DebugLog.loge("TAG", "getSmallBitmap width 11 = " + opts.outWidth + " height = " + opts.outHeight);

        opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
//        DebugLog.loge("TAG", "getSmallBitmap inSampleSize = " + opts.inSampleSize);
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
//            DebugLog.loge("TAG", "getSmallBitmap width 22 = " + opts.outWidth + " height = " + opts.outHeight);
        } catch (Exception e) {
        }
        return bitmap;
    }


    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }


    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
    /**
     * 保存图片到Sdcard中
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static File savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
            }

            File photoFile = new File(path , photoName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
//						fileOutputStream.close();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally{
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return photoFile;
        }
        return null;
    }
}
