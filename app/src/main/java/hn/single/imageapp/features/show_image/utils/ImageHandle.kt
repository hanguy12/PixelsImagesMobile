package hn.single.imageapp.features.show_image.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.widget.ImageView
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import kotlin.concurrent.thread


object ImageHandle {

    @Throws(IOException::class, RuntimeException::class)
    fun fetchImage(imageUrl: String): Bitmap? {
        val conn: URLConnection = URL(imageUrl).openConnection()
        conn.connect()
        val inputStream: InputStream = conn.getInputStream()
        //val option = BitmapFactory.Options()
        //val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
        val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
        //option.inSampleSize = 2
        //bitmap?.recycle()
        inputStream.close()
        return bitmap
    }


    fun setImageFromUrl(imageView: ImageView, urlString: String) {
        Observable.just(urlString)
            .filter { url -> url.isNotEmpty() }
            .map {
                Function<String, Drawable> { t ->
                    val url = URL(t)
                    Drawable.createFromStream(url.content as InputStream, "")!!
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Consumer<Drawable> { drawable -> imageView.setImageDrawable(drawable) }
            }.dispose()
    }
}

@Throws(IOException::class, RuntimeException::class)
fun fillImageWithCoroutine(imageView: ImageView, url: String) {
    val job = CoroutineScope(Dispatchers.IO).launch {
        val bitmap = ImageHandle.fetchImage(imageUrl = url)
        val bitmapResize = bitmap?.compressImage()
        withContext(Dispatchers.Main) {
            imageView.setImageBitmap(bitmapResize)
        }
    }
    //job.cancel()
}

@Throws(IOException::class)
fun Bitmap.compressImage(): Bitmap? {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream)
    val bytesArray = byteArrayOutputStream.toByteArray()
    val bitmapCompressed = BitmapFactory.decodeByteArray(bytesArray, 0, bytesArray.size)
    //imageView.setImageBitmap(bitmapCompressedImage)
    return bitmapCompressed
}

@Throws(IOException::class, RuntimeException::class)
fun decreaseSizeFile(imageUrl: String?, width: Int, height: Int): Bitmap? {
    val conn: URLConnection = URL(imageUrl).openConnection()
    conn.connect()
    val inputStream: InputStream = conn.getInputStream()
    val option = BitmapFactory.Options()
    //val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
    val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream, null, option)
    //option.inSampleSize = 2
    //bitmap?.recycle()
    /*inputStream.close()
    return bitmap*/

    val options1 = BitmapFactory.Options()
    options1.inJustDecodeBounds = true
    BitmapFactory.decodeStream(inputStream, null, options1)
    var scale = 1
    while (options1.outWidth / scale / 2 >= width
        && options1.outHeight / scale / 2 >= height
    ) {
        scale *= 2
    }
    val options2 = BitmapFactory.Options()
    options2.inSampleSize = scale
    return BitmapFactory.decodeStream(inputStream, null, options2)
}

fun ImageView.fillImageStrictMode(url: String) {
    StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitNetwork().build())
    val bitmap = ImageHandle.fetchImage(imageUrl = url)
    this.setImageBitmap(bitmap)
}

fun ImageView.fillImageHandler(url: String) {
    val handler = Handler(Looper.getMainLooper())
    thread(true) {
        //val bitmap = ImageHandle.fetchImage(imageUrl = url)
        val bitmap = decreaseSizeFile(imageUrl = url, 400, 400)
        handler.post {
            this.setImageBitmap(bitmap)
        }
    }
}


fun ImageView.fillImageByGlide(url: String) {
    Glide.with(this.context).load(url)
        /*.thumbnail(
            Glide.with(this.context).load(
                AppCompatResources.getDrawable(this.context, R.drawable.ic_launcher)
            )
        )*/.into(this)
}
