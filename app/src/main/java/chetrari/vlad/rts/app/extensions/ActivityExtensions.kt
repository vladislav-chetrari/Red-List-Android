package chetrari.vlad.rts.app.extensions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

fun AppCompatActivity.startActivity(kClass: KClass<*>) = startActivity(Intent(this, kClass.java))