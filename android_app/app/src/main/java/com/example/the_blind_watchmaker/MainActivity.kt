package com.example.the_blind_watchmaker

import com.example.the_blind_watchmaker.WatchBoard
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    private var canvasBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    private lateinit var watchBoard: WatchBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.watch_board)
//        watchBoard = findViewById(R.id.main_watch_board)
    }

}