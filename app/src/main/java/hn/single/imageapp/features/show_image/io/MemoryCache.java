package hn.single.imageapp.features.show_image.io;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import hn.single.imageapp.common.utils.Logger;

public class MemoryCache {

    //Last argument true for LRU ordering
    private final Map<String, Bitmap> cache = Collections
            .synchronizedMap(new LinkedHashMap<>(10, 1.5f, true));
    //current allocated size
    private long size = 0;
    //max memory cache folder used to download images in bytes
    private long limit = 1000000;

    public MemoryCache() {
        //use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long newLimit) {
        limit = newLimit;
        Logger.INSTANCE.d("LoadImage -- MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
    }

    public Bitmap get(String id) {
        Logger.INSTANCE.d("LoadImage -- cache: " + cache + " String id get: " + id);
        try {
            if (!cache.containsKey(id))
                return null;
            return cache.get(id);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void put(String id, Bitmap bitmap) {
        Logger.INSTANCE.d("LoadImage -- put: id =   " + id);
        try {
            if (cache.containsKey(id)) {
                size -= getSizeInBytes(cache.get(id));
            }
            cache.put(id, bitmap);
            size += getSizeInBytes(bitmap);
            Logger.INSTANCE.d("LoadImage -- put:try --- size =   " + size);
            checkSize();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void checkSize() {
        Logger.INSTANCE.d("LoadImage -- cache size= " + size + " length=" + cache.size());
        if (size > limit) {
            //least recently accessed item will be the first one iterated
            Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (size <= limit)
                    break;
            }
            Logger.INSTANCE.d("LoadImage -- Clean cache. New size " + cache.size());
        }
    }

    public void clear() {
        try {
            // Clear cache
            cache.clear();
            size = 0;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        return (long) bitmap.getRowBytes() * bitmap.getHeight();
    }
}