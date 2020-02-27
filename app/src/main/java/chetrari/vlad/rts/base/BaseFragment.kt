package chetrari.vlad.rts.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    override fun androidInjector() = injector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLiveData()
    }

    /*
    * method needs to be overridden for special back button behavior as well as parent activity's onBackPressed()
    */
    protected open fun onBackPressed() = true

    protected open fun observeLiveData() = Unit

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

    protected fun <T> LiveData<Event<T>>.observe(
        onProgress: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
        consumer: (T) -> Unit
    ) = observe(viewLifecycleOwner, Observer {
        when (it) {
            is Event.Progress -> onProgress()
            is Event.Error -> onError(it.error)
            is Event.Success -> consumer(it.result)
        }
    })

    protected fun <T> LiveData<T>.observe(consumer: (T) -> Unit) = observe(viewLifecycleOwner, Observer { consumer(it) })
}