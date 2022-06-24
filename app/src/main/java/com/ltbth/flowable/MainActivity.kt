package com.ltbth.flowable

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private lateinit var disposable: Disposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flowableObservable = getFlowableObservable()
        val flowableObserver = getFlowableObserver()
        flowableObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .reduce(0
            ) { result, number -> result + number }
            .subscribe(flowableObserver)
    }

    private fun getFlowableObserver(): SingleObserver<Int> {
        return object: SingleObserver<Int> {
            override fun onSubscribe(d: Disposable) {
                Log.d("TAG","onSubscribe")
                disposable = d
            }

            override fun onSuccess(t: Int) {
                Log.d("TAG","onSuccess")
            }

            override fun onError(e: Throwable) {
                Log.d("TAG","onError: ${e.message}")
            }
        }
    }

    private fun getFlowableObservable(): Flowable<Int> {
        return Flowable.range(1,1000000000)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}