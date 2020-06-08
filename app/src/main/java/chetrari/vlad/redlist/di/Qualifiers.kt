package chetrari.vlad.redlist.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Scope
@Retention
annotation class ActivityScoped

@Scope
@Retention
annotation class FragmentScoped

@Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
@Retention
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Qualifier
annotation class RedList