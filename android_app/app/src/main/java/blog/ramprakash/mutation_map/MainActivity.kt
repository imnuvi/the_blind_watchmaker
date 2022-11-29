package blog.ramprakash.mutation_map

import blog.ramprakash.mutation_map.WatchBoard
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider

class MainActivity : AppCompatActivity() {
    private var canvasBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    private lateinit var watchBoard: WatchBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.watch_board)
        supportActionBar?.hide()
        val watchBoard: WatchBoard = findViewById(R.id.main_watch_board);
//        val mutationSlider: Slider = findViewById(R.id.mutation_slider);
//        mutationSlider.addOnChangeListener{
//            mutationSlider, value, fromUser ->
//            watchBoard.updateAngle(value)
//            }
        }
}