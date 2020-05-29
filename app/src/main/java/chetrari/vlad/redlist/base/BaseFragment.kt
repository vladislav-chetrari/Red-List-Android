package chetrari.vlad.redlist.base

import android.content.Context
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.app.extensions.setNavIconColor
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment(
    @LayoutRes layoutResId: Int
) : Fragment(layoutResId), HasAndroidInjector {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    protected val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() = this@BaseFragment.handleOnBackPressed()
    }

    override fun androidInjector() = injector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLiveData()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    protected open fun observeLiveData() = Unit

    protected open fun handleOnBackPressed() = Unit

    /*
    * provided ViewModel depends on fragment lifecycle!
    * usage:
    * private val viewModel by provide<ExampleViewModel>()
    * or
    * private val viewModel by provide<ExampleViewModel>{ parentFragment!! }
    */
    protected inline fun <reified VM : ViewModel> provide(
        crossinline fragment: () -> Fragment = { this }
    ) = lazy { ViewModelProvider(fragment(), factory).get(VM::class.java) }

    /*
    * provided ViewModel depends on parent activity lifecycle!
    * usage:
    * private val viewModel by provideFromActivity<ExampleViewModel>()
    */
    protected inline fun <reified VM : ViewModel> provideFromActivity() = lazy {
        ViewModelProvider(requireActivity(), factory).get(VM::class.java)
    }

    protected fun <T> LiveData<T>.observe(consumer: (T) -> Unit) = observe(viewLifecycleOwner, Observer(consumer))

    protected fun <T> LiveData<T?>.safeObserve(consumer: (T) -> Unit) = observe(viewLifecycleOwner, Observer {
        it?.let(consumer)
    })

    protected fun <T> LiveData<Event<T>>.observeEvent(
        onProgress: (Boolean) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
        onSuccess: (T) -> Unit
    ) = observe(viewLifecycleOwner, Observer {
        val completionHandler = {
            onProgress(false)
            onComplete()
        }
        when (it) {
            is Event.Progress -> onProgress(true)
            is Event.Error -> {
                onError(it.error)
                completionHandler()
            }
            is Event.Success -> {
                onSuccess(it.result)
                completionHandler()
            }
        }
    })

    protected fun NavController.setupToolbar(
        toolbar: Toolbar,
        @ColorInt color: Int = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
    ) {
        toolbar.setupWithNavController(this)
        toolbar.setNavIconColor(color)
    }
}