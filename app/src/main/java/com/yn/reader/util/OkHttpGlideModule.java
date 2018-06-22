package com.yn.reader.util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * A {@link GlideModule} implementation to replace Glide's default
 * {@link java.net.HttpURLConnection} based {@link com.bumptech.glide.load.model.ModelLoader}
 * with an OkHttp based {@link com.bumptech.glide.load.model.ModelLoader}.
 * <p/>
 * <p> If you're using gradle, you can include this module simply by depending on the aar, the
 * module will be merged in by manifest merger. For other build systems or for more more
 * information, see {@link GlideModule}. </p>
 */
public class OkHttpGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置BitmapPool缓存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int memoryCacheSize = maxMemory /8;
        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));
        //设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        //设置Glide磁盘缓存大小
        int diskSize = 1024 * 1024 * 200;
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,diskSize));
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,diskSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
