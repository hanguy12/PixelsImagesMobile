package hn.single.imageapp.features.show_image.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PexelsImages(val photos: List<PhotoResponse>)

@Keep
data class PhotoResponse(val url: String, val resource: ImageFile)

@Keep
data class ImageFile(val mediumImage: String)

@Keep
data class PexelsVideo(val videos: List<VideoResponse>, val page: Int)

@Keep
data class VideoResponse(
    val image: String, // thumbnail image
    val url: String, //it gives us the title of the video after we manipulate the string
    @SerializedName("video_files")
    val videoFiles: ArrayList<VideoFilesModel>, //it brings us a list of video files object of different sizes
)

@Keep
data class VideoFilesModel(
    val quality: String, // gives us the quality type like sd/hd
    val link: String, // gives us the actual video we want to play
)

@Keep
data class Popular(
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val collections: List<CollectionModel>,
    @SerializedName("total_results")
    val totalResults: Int,
)

@Keep
data class CollectionModel(
    val id: String,
    val title: String,
    val description: String?,
    @SerializedName("private")
    val isPrivate: Boolean,
    @SerializedName("media_count")
    val mediaCount: Int,
    @SerializedName("photos_count")
    val photosCount: Int,
    @SerializedName("videos_count")
    val videosCount: Int,
)


@Keep
data class ImageDetail(
    @SerializedName("page")
    var page: Int? = null,
    @SerializedName("per_page")
    var perPage: Int? = null,
    @SerializedName("media")
    var media: ArrayList<Media> = arrayListOf(),
    @SerializedName("total_results")
    var totalResults: Int? = null,
    @SerializedName("id")
    var id: String? = null,
)

@Keep
data class Media(
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("width")
    var width: Int? = null,
    @SerializedName("height")
    var height: Int? = null,
    @SerializedName("url")
    var url: String? = null,
    @SerializedName("photographer")
    var photographer: String? = null,
    @SerializedName("photographer_url")
    var photographerUrl: String? = null,
    @SerializedName("photographer_id")
    var photographerId: Int? = null,
    @SerializedName("avg_color")
    var avgColor: String? = null,
    @SerializedName("src")
    var src: Src? = Src(),
    @SerializedName("liked")
    var liked: Boolean? = null,
    @SerializedName("alt")
    var alt: String? = null
)

@Keep
data class Src(
    @SerializedName("original")
    var original: String? = null,
    @SerializedName("large2x")
    var large2x: String? = null,
    @SerializedName("large")
    var large: String? = null,
    @SerializedName("medium")
    var medium: String? = null,
    @SerializedName("small")
    var small: String? = null,
    @SerializedName("portrait")
    var portrait: String? = null,
    @SerializedName("landscape")
    var landscape: String? = null,
    @SerializedName("tiny")
    var tiny: String? = null,
)
