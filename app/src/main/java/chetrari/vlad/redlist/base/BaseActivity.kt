package chetrari.vlad.redlist.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity(
    @LayoutRes layoutResId: Int
) : AppCompatActivity(layoutResId), HasAndroidInjector {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = injector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        observeLiveData()
    }

    protected open fun observeLiveData() = Unit

    /*
    * usage:
    * private val viewModel by provide<ExampleViewModel>()
    */
    protected inline fun <reified VM : ViewModel> provide() = lazy {
        ViewModelProvider(this, factory).get(VM::class.java)
    }

    protected fun <T> LiveData<T>.observe(consumer: (T) -> Unit) = observe(this@BaseActivity, Observer(consumer))

    protected fun <T> LiveData<T?>.safeObserve(consumer: (T) -> Unit) = observe(this@BaseActivity, Observer {
        it?.let(consumer)
    })

    protected fun <T> LiveData<Event<T>>.observeEvent(
        onProgress: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
        onSuccess: (T) -> Unit
    ) = observe(this@BaseActivity, Observer {
        when (it) {
            is Event.Progress -> onProgress()
            is Event.Error -> {
                onError(it.error)
                onComplete()
            }
            is Event.Success -> {
                onSuccess(it.result)
                onComplete()
            }
        }
    })
}