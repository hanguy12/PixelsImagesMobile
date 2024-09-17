package hn.single.imageapp.features.show_image.io;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hn.single.imageapp.R;
import hn.single.imageapp.common.utils.Logger;

public class ImageLoader {

    // Initialize MemoryCache
    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;

    //Create Map (collection) to store image and image url in key value pair
    private final Map<ImageView, String> imageViews =
            Collections.synchronizedMap(new WeakHashMap<>());
    ExecutorService executorService;

    //handler to display images in UI thread
    Handler handler = new Handler(Looper.getMainLooper());

    public ImageLoader(Context context) {
        fileCache = new FileCache(context);
        // Creates a thread pool that reuses a fixed number of
        // threads operating off a shared unbounded queue.
        executorService = Executors.newFixedThreadPool(5);
    }

    // default image show in list (Before online image download)
    int stubId = R.drawable.ic_launcher;

    public void displayImage(String url, ImageView imageView) {
        //Store image and url in Map
        Logger.INSTANCE.d("LoadImage -- url: == " + url + "   imageView =:   " + imageView);
        imageViews.put(imageView, url);

        //Check image is stored in MemoryCache Map or not (see MemoryCache.java)
        Bitmap bitmap = memoryCache.get(url);
        Logger.INSTANCE.d("LoadImage -- bitmap != null " + (bitmap != null));

        if (bitmap != null) {
            // if image is stored in MemoryCache Map then, show image in listview row
            imageView.setImageBitmap(bitmap);
        } else {
            //queue Photo to download from url
            queuePhoto(url, imageView);
            //Before downloading image show default image
            imageView.setImageResource(stubId);
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        // Store image and url in PhotoToLoad object
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        // pass PhotoToLoad object to PhotosLoader runnable class
        // and submit PhotosLoader runnable to executers to run runnable
        // Submits a PhotosLoader runnable task for execution
        executorService.submit(new PhotosLoader(p));
    }

    //Task for the queue
    private static class PhotoToLoad {
        private final String url;
        private final ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }

        public String getUrl() {
            return url;
        }

        public ImageView getImageView() {
            return imageView;
        }

    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                //Check if image already downloaded
                if (imageViewReused(photoToLoad))
                    return;
                // download image from web url
                Bitmap bmp = getBitmap(photoToLoad.getUrl());
                // set image data in Memory Cache
                memoryCache.put(photoToLoad.getUrl(), bmp);
                if (imageViewReused(photoToLoad))
                    return;
                // Get bitmap to display
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                // Causes the Runnable bd (BitmapDisplayer) to be added to the message queue.
                // The runnable will be run on the thread to which this handler is attached.
                // BitmapDisplayer run method will call
                handler.post(bd);
            } catch (Exception ex) {
                Logger.INSTANCE.d("LoadImage -- Exception == " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    @Nullable
    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url);
        //from SD cache
        //CHECK : if trying to decode file which not exist in cache return null
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;
        // Download image file from web
        try {
            Bitmap bitmap;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            // Constructs a new FileOutputStream that writes to file
            // if file not exist then it will create file
            OutputStream os;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                os = Files.newOutputStream(f.toPath());
            } else {
                os = new FileOutputStream(f);
            }

            // See Utils class CopyStream method
            // It will each pixel from input stream and
            // write pixels to output stream (file)
            Utils.copyStream(is, os);
            os.close();
            conn.disconnect();
            //Now file created and going to resize file with defined height
            // Decodes image and scales it to reduce memory consumption
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    //Decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            //Decode image size
            BitmapFactory.Options optionsOriginal = new BitmapFactory.Options();
            optionsOriginal.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, optionsOriginal);
            stream1.close();
            //Find the correct scale value. It should be the power of 2.
            // Set width/height of recreated image
            final int REQUIRED_SIZE = 85;
            int widthTmp = optionsOriginal.outWidth;
            int heightTmp = optionsOriginal.outHeight;
            int scale = 1;
            while (widthTmp / 2 >= REQUIRED_SIZE && heightTmp / 2 >= REQUIRED_SIZE) {
                widthTmp /= 2;
                heightTmp /= 2;
                scale *= 2;
            }
            //decode with current scale values
            BitmapFactory.Options optionsAfter = new BitmapFactory.Options();
            optionsAfter.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, optionsAfter);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            Logger.INSTANCE.d("LoadImage -- FileNotFoundException == " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Logger.INSTANCE.d("LoadImage -- IOException == " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.getImageView());
        Logger.INSTANCE.d("LoadImage -- tag == " + tag);
        //Check url is already exist in imageViews MAP
        return tag == null || !tag.equals(photoToLoad.getUrl());
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            // Show bitmap on UI
            if (bitmap != null)
                photoToLoad.getImageView().setImageBitmap(bitmap);
            else
                photoToLoad.getImageView().setImageResource(stubId);
        }
    }

    public void clearCache() {
        //Clear cache directory downloaded images and stored data in maps
        memoryCache.clear();
        fileCache.clear();
    }

}