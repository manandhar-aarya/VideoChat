package com.eightsquare.videochatdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (true){//permissions granted
            openFragment(WebViewFragment.newInstance("title","https://www.html5rocks.com/en/tutorials/getusermedia/intro/"), true, false)
        }
    }


    fun clearBackStack() {
        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0)
            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    fun openFragment(fragment: Fragment, cleanStack: Boolean = false, addToBackStack: Boolean = true) {
        val ft = supportFragmentManager
                .beginTransaction()
        if (cleanStack)
            clearBackStack()
        ft.replace(R.id.container,
                fragment)
        if (addToBackStack)
            ft.addToBackStack(null)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commitAllowingStateLoss()
    }

}
