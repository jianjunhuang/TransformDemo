package xyz.juncat.transformdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testMethod()
    }

    fun testMethod() {
        Log.i(TAG, "testMethod: ")
        Test().testCost()
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}