package com.spoonart.movieclip.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog


/**
 * Created by Lafran on 10/30/17.
 */
object PermissionUtil {
    const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 122
    const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    const val MY_PERMISSIONS_REQUEST_READ_DEVICE_ID = 124
    const val MY_PERMISSIONS_REQUEST_VIBRATE = 125
    const val MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 126

    fun requestExternalPermission(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (isNotGranted(context, permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                    showAlertDialog(context, "Ijinkan Aplikasi?", "Ijinkan aplikasi untuk mengakses penyimpanan eksternal?",
                            arrayOf(permission), MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                } else {
                    requestPermission(context, arrayOf(permission), MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    fun requestWriteExternalPermission(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (isNotGranted(context, permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                    showAlertDialog(context, "Ijinkan Aplikasi?", "Ijinkan aplikasi untuk menyimpan ke data eksternal?",
                            arrayOf(permission), MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
                } else {
                    requestPermission(context, arrayOf(permission), MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    fun requestDeviceIdPermission(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        val permission = Manifest.permission.READ_PHONE_STATE
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (isNotGranted(context, permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                    showAlertDialog(context, "Ijinkan Aplikasi?", "Aplikasi membutuhkan perangkat id Anda, ijinkan?",
                            arrayOf(permission), MY_PERMISSIONS_REQUEST_READ_DEVICE_ID)
                } else {
                    requestPermission(context, arrayOf(permission), MY_PERMISSIONS_REQUEST_READ_DEVICE_ID)
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    fun requestVibrate(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        val permission = Manifest.permission.VIBRATE
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (isNotGranted(context, permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                    showAlertDialog(context, "Aktifkan Fitur Vibrasi?",
                            "Ijinkan aplikasi untuk mengelola fitur vibrasi pada perangkat Anda?",
                            arrayOf(permission), MY_PERMISSIONS_REQUEST_VIBRATE)
                } else {
                    requestPermission(context, arrayOf(permission), MY_PERMISSIONS_REQUEST_VIBRATE)
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    fun requestAccessNetworkState(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        val permission = Manifest.permission.ACCESS_NETWORK_STATE
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (isNotGranted(context, permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                    showAlertDialog(context, "Ijinkan Aplikasi?", "Ijinkan Aplikasi untuk membaca koneksi jaringan Anda?",
                            arrayOf(permission), MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE)
                } else {
                    requestPermission(context, arrayOf(permission), MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE)
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    private fun showAlertDialog(context: Context, title: String, message: String, permissions: Array<String>, flag: Int) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle(title)
        alertBuilder.setMessage(message)
        alertBuilder.setPositiveButton(android.R.string.yes, { dialog, which ->
            ActivityCompat.requestPermissions(context as Activity, permissions, flag)
        })
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun isNotGranted(context: Context, manifestPermission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, manifestPermission) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(context: Activity, permissions: Array<String>, flag: Int) {
        ActivityCompat.requestPermissions(context, permissions, flag)
    }
}