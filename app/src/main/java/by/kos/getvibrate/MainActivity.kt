package by.kos.getvibrate

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.*
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import by.kos.getvibrate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var vibrator: Vibrator
    private var patternVibro = PATTERN_ONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        setSpinner()
        binding.fabStart.isEnabled = true
        binding.spinnerPatterns.isEnabled = true
        binding.spinnerPatterns.onItemSelectedListener = this
        binding.fabStart.setOnClickListener {
            vibrate(patternVibro)
            binding.spinnerPatterns.isEnabled = false
            binding.fabStart.isEnabled = false
        }
        binding.fabStop.setOnClickListener {
            vibrator.cancel()
            binding.spinnerPatterns.isEnabled = true
            binding.fabStart.isEnabled = true
        }
    }

    override fun onStop() {
        super.onStop()
        vibrator.cancel()
    }

    private fun vibrate(pattern: LongArray) {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        val canVibrate: Boolean = vibrator.hasVibrator()

        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (vibrator.hasAmplitudeControl()) {
                    vibrator.vibrate(VibrationEffect.createWaveform(pattern, AMPLITUDE, 0))
                } else {
                    vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
                }
            } else {
                vibrator.vibrate(pattern, 0)
            }
        }
    }

    private fun setSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.patterns,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPatterns.adapter = adapter
        }
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.let {
            when (it.selectedItemPosition) {
                0 -> {
                    patternVibro = PATTERN_ONE
                    binding.lottie.setAnimation("patternone.json")
                    binding.lottie.playAnimation()
                    binding.lottie.repeatCount = ValueAnimator.INFINITE
                    binding.tvDescription.text = resources.getText(R.string.pattern_one_description)
                    (p0.getChildAt(0) as TextView).setTextColor(Color.GREEN)
                }
                1 -> {
                    patternVibro = PATTERN_TWO
                    binding.lottie.setAnimation("patterntwo.json")
                    binding.lottie.playAnimation()
                    binding.lottie.repeatCount = ValueAnimator.INFINITE
                    binding.tvDescription.text = resources.getText(R.string.pattern_two_description)
                    (p0.getChildAt(0) as TextView).setTextColor(Color.YELLOW)
                }
                2 -> {
                    patternVibro = PATTERN_THREE
                    binding.lottie.setAnimation("patternthree.json")
                    binding.lottie.playAnimation()
                    binding.lottie.repeatCount = ValueAnimator.INFINITE
                    binding.tvDescription.text = resources.getText(R.string.pattern_three_description)
                    (p0.getChildAt(0) as TextView).setTextColor(Color.RED)
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    companion object {
        private val PATTERN_ONE = longArrayOf(500, 100, 1000, 200)
        private val PATTERN_TWO = longArrayOf(100, 300, 50, 400)
        private val PATTERN_THREE = longArrayOf(0, 5000)
        private val AMPLITUDE = intArrayOf(0, 128, 0, 255)
    }
}