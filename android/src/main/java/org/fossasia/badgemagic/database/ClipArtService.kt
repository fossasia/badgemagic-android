package org.fossasia.badgemagic.database

import android.graphics.drawable.Drawable
import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.util.Resource
import org.fossasia.badgemagic.util.StorageUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class ClipArtService : KoinComponent {
    private val clipArts = MutableLiveData<SparseArray<Drawable>>()
    private val storageClipArts = MutableLiveData<MutableMap<String, Drawable?>>()
    private val resourceHelper: Resource by inject()
    private val storageUtils: StorageUtils by inject()

    init {
        updateClipArts()
    }

    private fun getAllClips(): SparseArray<Drawable> {
        val tempSparseArray = SparseArray<Drawable>()
        val listOfDrawables = listOf(
            resourceHelper.getDrawable(R.drawable.clip_apple),
            resourceHelper.getDrawable(R.drawable.clip_clock),
            resourceHelper.getDrawable(R.drawable.clip_dustbin),
            resourceHelper.getDrawable(R.drawable.clip_face),
            resourceHelper.getDrawable(R.drawable.clip_heart),
            resourceHelper.getDrawable(R.drawable.clip_home),
            resourceHelper.getDrawable(R.drawable.clip_invader),
            resourceHelper.getDrawable(R.drawable.clip_mail),
            resourceHelper.getDrawable(R.drawable.clip_mix1),
            resourceHelper.getDrawable(R.drawable.clip_mix2),
            resourceHelper.getDrawable(R.drawable.clip_mushroom),
            resourceHelper.getDrawable(R.drawable.clip_mustache),
            resourceHelper.getDrawable(R.drawable.clip_oneup),
            resourceHelper.getDrawable(R.drawable.clip_play_pause),
            resourceHelper.getDrawable(R.drawable.clip_spider),
            resourceHelper.getDrawable(R.drawable.clip_sun),
            resourceHelper.getDrawable(R.drawable.clip_thumbs_up),
            resourceHelper.getDrawable(R.drawable.clip_heart_filled),
            resourceHelper.getDrawable(R.drawable.clip_expressionless_face),
            resourceHelper.getDrawable(R.drawable.clip_smile_face),
            resourceHelper.getDrawable(R.drawable.clip_sad_face),
            resourceHelper.getDrawable(R.drawable.clip_clock_twelve_oclock),
            resourceHelper.getDrawable(R.drawable.clip_clock_six_oclock),
            resourceHelper.getDrawable(R.drawable.clip_clock_nine_oclock),
            resourceHelper.getDrawable(R.drawable.clip_block),
            resourceHelper.getDrawable(R.drawable.clip_thumb_up),
            resourceHelper.getDrawable(R.drawable.clip_thumb_down),
            resourceHelper.getDrawable(R.drawable.clip_flag),
            resourceHelper.getDrawable(R.drawable.clip_play),
            resourceHelper.getDrawable(R.drawable.clip_pause),
            resourceHelper.getDrawable(R.drawable.clip_tick),
            resourceHelper.getDrawable(R.drawable.clip_cross),
            resourceHelper.getDrawable(R.drawable.clip_right_arrow),
            resourceHelper.getDrawable(R.drawable.clip_left_arrow),
            resourceHelper.getDrawable(R.drawable.clip_north_east_arrow),
            resourceHelper.getDrawable(R.drawable.clip_north_west_arrow),
            resourceHelper.getDrawable(R.drawable.clip_up_arrow),
            resourceHelper.getDrawable(R.drawable.clip_down_arrow),
            resourceHelper.getDrawable(R.drawable.clip_south_east_arrow),
            resourceHelper.getDrawable(R.drawable.clip_south_west_arrow),
            resourceHelper.getDrawable(R.drawable.clip_subdirectory_right),
            resourceHelper.getDrawable(R.drawable.clip_subdirectory_left)
        )
        var lastIndex = 0
        listOfDrawables.forEachIndexed { index, drawable ->
            drawable?.let {
                lastIndex = index
                tempSparseArray.append(index, it)
            }
        }
        val drawablesInStorage = getClipsFromStorage().value
        drawablesInStorage?.forEach {
            tempSparseArray.append(++lastIndex, it.value)
        }

        return tempSparseArray
    }

    fun updateClipArts() {
        storageClipArts.value = storageUtils.getAllClips()
        clipArts.value = getAllClips()
    }

    fun getClipArts(): LiveData<SparseArray<Drawable>> = clipArts

    fun getClipsFromStorage(): LiveData<MutableMap<String, Drawable?>> = storageClipArts

    fun deleteClipart(fileName: String) {
        storageUtils.deleteClipart(fileName)
        updateClipArts()
    }
}
