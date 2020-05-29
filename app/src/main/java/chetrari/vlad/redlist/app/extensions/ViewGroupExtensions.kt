package chetrari.vlad.redlist.app.extensions

import android.view.View
import android.view.ViewGroup

inline val ViewGroup.children: List<View>
    get() {
        val accumulator = mutableListOf<View>()
        for (index in 0 until childCount) accumulator += getChildAt(index)
        return accumulator
    }