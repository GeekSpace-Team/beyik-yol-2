package com.android.beyikyol2.core.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory


class Utils{
    companion object Utils {
        fun setPreference(name: String?, value: String?, context: Context) {
            val editor: SharedPreferences.Editor =
                context.getSharedPreferences(name, MODE_PRIVATE).edit()
            editor.putString(name, value)
            editor.apply()
        }

        fun getSharedPreference(context: Context, name: String?): String {
            val prefs: SharedPreferences =
                context.getSharedPreferences(name, MODE_PRIVATE)
            return prefs.getString(name, "")?:""
        }

        fun isLogin(context: Context): Boolean {
            return !getSharedPreference(context,"token").isBlank()
        }

        fun getToken(context: Context): String{
            return "Bearer ${getSharedPreference(context,"token")}"
        }

        fun getLanguage(context: Context): String{
            return getSharedPreference(context,"language").toString().ifBlank { "tm" }
        }

        fun isDark(context: Context): Boolean{
            return getSharedPreference(context,"mode").toString().ifBlank { "light" }=="dark"
        }

        fun bitmapDescriptor(
            context: Context,
            vectorResId: Int
        ): BitmapDescriptor? {

            // retrieve the actual drawable
            val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            val bm = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )

            // draw it onto the bitmap
            val canvas = android.graphics.Canvas(bm)
            drawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bm)
        }

        fun isPackageInstalled(
            packageName: String,
            packageManager: PackageManager
        ): Boolean {
            return try {
                packageManager.getPackageInfo(packageName, 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                false
            }
        }
    }
}