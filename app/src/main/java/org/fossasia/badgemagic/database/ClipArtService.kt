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
    private val resourceHelper: Resource by inject()

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
            resourceHelper.getDrawable(R.drawable.clip_pause),
            resourceHelper.getDrawable(R.drawable.clip_spider),
            resourceHelper.getDrawable(R.drawable.clip_sun),
            resourceHelper.getDrawable(R.drawable.clip_thumbs_up)
        )
        var lastIndex = 0
        listOfDrawables.forEachIndexed { index, drawable ->
            drawable?.let {
                lastIndex = index
                tempSparseArray.append(index, it)
            }
        }
        val drawablesInStorage = StorageUtils.getAllClips()
        drawablesInStorage.forEach {
            tempSparseArray.append(++lastIndex, it)
        }

        return tempSparseArray
    }

    fun updateClipArts() {
        clipArts.value = getAllClips()
    }

    fun getClipArts(): LiveData<SparseArray<Drawable>> = clipArts
}