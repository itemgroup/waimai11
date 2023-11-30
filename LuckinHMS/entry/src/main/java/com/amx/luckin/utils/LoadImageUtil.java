package com.amx.luckin.utils;

import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import ohos.media.photokit.metadata.AVThumbnailUtils;
import ohos.utils.net.Uri;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LoadImageUtil {

    public static void loadImg(Context context, String netImgUrl, Image image){

        //每个图片的加载都单独创建一个线程实现
        TaskDispatcher globalTaskDispatcher = context.getGlobalTaskDispatcher(TaskPriority.DEFAULT);
        globalTaskDispatcher.asyncDispatch(()->{
            HttpsURLConnection connection = null;
            try {
                //建立与网络图片之间的 http连接
                URL url = new URL(netImgUrl);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                System.out.println("我已成功获得图片资源 -----------------------------------");
                //从连接中获取输入流（可以读取到图片数据）
                InputStream inputStream = connection.getInputStream();
                //根据数据流将图片数据缓存到ImageSource对象，创建图片对象
                ImageSource imageSource = ImageSource.create(inputStream,new ImageSource.SourceOptions());
                //图片数据解码的参数
                ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
//                decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;

                //PixelMap对象就表示一个图片
                PixelMap pixelmap = imageSource.createPixelmap(decodingOptions);
                //将图片载入到组件中: 在鸿蒙应用将图片载入到组件 推荐在一个独立的UI线程中完成
                context.getUITaskDispatcher().asyncDispatch(()->{
                    image.setPixelMap(pixelmap);
                    pixelmap.release(); //释放图片
                });
            }catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
