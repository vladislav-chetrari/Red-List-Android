package chetrari.vlad.redlist.app.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

inline fun Fragment.doOnDestroy(crossinline action: () -> Unit) = viewLifecycleOwner.lifecycle.run {
    addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            action()
            removeObserver(this)
        }
    })
}