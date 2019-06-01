package org.fossasia.badgemagic.database

import android.graphics.drawable.Drawable
import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.util.Resource

class ClipArtService {
    private val clipArts = MutableLiveData<SparseArray<Drawable>>()
    private val resourceHelper = Resource()

    init {
        val tempSparseArray = SparseArray<Drawable>()
        val listOfDrawables = listOf(
            resourceHelper.getDrawable(R.drawable.apple),
            resourceHelper.getDrawable(R.drawable.clock),
            resourceHelper.getDrawable(R.drawable.dustbin),
            resourceHelper.getDrawable(R.drawable.face),
            resourceHelper.getDrawable(R.drawable.heart),
            resourceHelper.getDrawable(R.drawable.home),
            resourceHelper.getDrawable(R.drawable.invader),
            resourceHelper.getDrawable(R.drawable.mail),
            resourceHelper.getDrawable(R.drawable.mix1),
            resourceHelper.getDrawable(R.drawable.mix2),
            resourceHelper.getDrawable(R.drawable.mushroom),
            resourceHelper.getDrawable(R.drawable.mustache),
            resourceHelper.getDrawable(R.drawable.oneup),
            resourceHelper.getDrawable(R.drawable.pause),
            resourceHelper.getDrawable(R.drawable.spider),
            resourceHelper.getDrawable(R.drawable.sun),
            resourceHelper.getDrawable(R.drawable.thumbs_up)
        )
        listOfDrawables.forEachIndexed { index, drawable ->
            drawable?.let {
                tempSparseArray.append(index, it)
            }
        }
        clipArts.value = tempSparseArray
    }

    fun getClipArts(): LiveData<SparseArray<Drawable>> = clipArts
}