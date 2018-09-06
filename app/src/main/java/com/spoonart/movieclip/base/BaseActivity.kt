package com.spoonart.libs.base

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.spoonart.movieclip.R
import com.spoonart.movieclip.util.KeyboardUtil

/**
 * Created by Lafran on 1/15/18.
 */
abstract class BaseActivity<T : BasePresenter<*>> : AppCompatActivity() {

    protected var presenter: T? = null
    protected var toolbar: Toolbar? = null
    private var dialog: AlertDialog? = null
    private lateinit var progressDialog: ProgressDialog

    val contentView: View
        get() = findViewById(android.R.id.content)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initializeProgressDialog()
        this.presenter = attachPresenter()
    }

    private fun initializeProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage(getString(R.string.text_loading_progress))
    }

    abstract fun attachPresenter(): T

    override fun onResumeFragments() {
        super.onResumeFragments()
    }

    fun showProgressDialog() {
        if (progressDialog.isShowing)
            return

        try {
            progressDialog.show()
        }catch (e:Exception){
            showToast("an error occured")
            finish()
        }
    }

    fun dismissProgressDialog() {
        if (!progressDialog.isShowing)
            return

        progressDialog.dismiss()
    }

    fun setStatusBarColor(window: Window, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    fun setupToolbar(@IdRes toolbarId: Int, @ColorRes colorRes: Int) {
        this.toolbar = findViewById(toolbarId)
        if (this.toolbar != null) {
            setSupportActionBar(toolbar)
            this.toolbar!!.setTitleTextColor(ContextCompat.getColor(this, colorRes))
            this.toolbar!!.title = ""
        }
    }

    fun enableHomeAsUp(@DrawableRes icon: Int) {
        setDisplayHome(true)
        setHomeAsUpIndicator(icon)
    }

    fun setDisplayHome(showBackButton: Boolean) {
        val actionBar = supportActionBar
        if (actionBar == null) {
            throw Error("Action bar is null")
        }
        actionBar.setHomeButtonEnabled(showBackButton)
        actionBar.setDisplayHomeAsUpEnabled(showBackButton)
    }

    fun setHomeAsUpIndicator(@DrawableRes drawable: Int) {
        val actionBar = supportActionBar
        if (actionBar == null) {
            throw Error("Action bar is null")
        }
        actionBar.setHomeAsUpIndicator(drawable)
    }

    fun setDisplayLogo(@DrawableRes drawable: Int) {
        val actionBar = supportActionBar
        if (actionBar == null) {
            throw Error("Action bar is null")
        }
        actionBar.setLogo(drawable)
    }

    fun setIcon(@DrawableRes drawable: Int) {
        val actionBar = supportActionBar
        if (actionBar == null) {
            throw Error("Action bar is null")
        }
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setIcon(ContextCompat.getDrawable(this, drawable))
        actionBar.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    fun setTitle(title: String) {
        val actionBar = supportActionBar
        if (actionBar == null) {
            throw Error("Action bar is null")
        }
        actionBar.setDisplayShowTitleEnabled(!TextUtils.isEmpty(title))
        actionBar.setTitle(Html.fromHtml(title))
    }

    fun setSubtitle(subtitle: String) {
        val actionBar = supportActionBar
        if (actionBar == null) {
            throw Error("Action bar is null")
        }
        actionBar.setDisplayShowTitleEnabled(!TextUtils.isEmpty(subtitle))
        actionBar.setSubtitle(subtitle)
    }

    override fun onSupportNavigateUp(): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }

    fun showAlert(title: String?, message: String, listener: DialogInterface.OnClickListener? = null) {
        if (message.isEmpty()) {
            return
        }

        if (this.dialog != null) {
            if (this.dialog!!.isShowing) {
                return
            }
        }

        val alert = AlertDialog.Builder(this)
        alert.setMessage(message)
        alert.setPositiveButton("OK", listener)
        if (title != null)
            alert.setTitle(title!!)

        alert.show()

        this.dialog = alert.create()
    }

    fun showError(message: String, listener: DialogInterface.OnClickListener? = null) {
        showAlert(getString(R.string.text_title_error), message, listener)
    }

    fun showError(@StringRes resStr: Int, listener: DialogInterface.OnClickListener? = null) {
        showAlert(getString(R.string.text_title_error), getString(resStr), listener)
    }

    fun showDialogUserSessionInvalid() {
        showAlert("Sesi Telah Habis", "Sesi login Anda telah habis, silahkan login kembali", DialogInterface.OnClickListener { dialog, which ->
            //doLogout()
            Toast.makeText(this, "Anda telah logout", Toast.LENGTH_SHORT).show()
        })
    }

    fun doLogout(landingClass: Class<*>) {
        finish()
        val loginIntent = Intent(this, landingClass)
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(loginIntent)
    }

    fun showDialogOptions(title: String, options: Array<String>, defaultCheckedItem: Int, choiceListener: DialogInterface.OnClickListener, positiveListener: DialogInterface.OnClickListener) {
        val dialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setSingleChoiceItems(options, defaultCheckedItem, choiceListener)
                .setNegativeButton(getString(R.string.text_cancel), null)
                .setPositiveButton(getString(R.string.text_ok), positiveListener)
                .create()

        dialog.show()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        BaseApplication.activityResumed()
    }

    override fun onPause() {
        super.onPause()
        BaseApplication.activityPaused()
    }

    fun hideKeyboard() {
        KeyboardUtil.hideKeyboard(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            presenter?.disposeAllRequest()
        }
    }
}