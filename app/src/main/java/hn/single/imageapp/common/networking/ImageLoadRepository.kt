package hn.single.imageapp.common.networking

import hn.single.imageapp.features.show_image.models.ImageDetail
import hn.single.imageapp.features.show_image.models.Popular
import io.reactivex.rxjava3.core.Single

class ImageLoadRepository {

    fun getPostItem(apiKey: String): String {
        return RetrofitInstance.getRetrofit.getItem(apiKey)
    }

    fun getPopularCategories(apiKey: String): Single<Popular> {
        return RetrofitInstance.getRetrofit.getPopularCategories(apiKey)
    }

    fun getImagesByIs(apiKey: String, id: String): Single<ImageDetail> {
        return RetrofitInstance.getRetrofit.getImagesById(apiKey, id)
    }
}