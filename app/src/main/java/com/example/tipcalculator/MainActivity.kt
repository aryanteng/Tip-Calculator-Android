package com.example.tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.tipcalculator.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val PERCENTAGE = 15

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.skPercentage.progress = PERCENTAGE
        binding.tvPercentage.text = "$PERCENTAGE %"

        binding.skPercentage?.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "progress: $p1")
                binding.tvPercentage.text = "$p1 %"
                calculationCallback()
                updateTipDesc(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        binding.etBase.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "After text changed $p0")
                calculationCallback()
            }
        })
    }

    private fun calculationCallback(){
        if(binding.etBase.text.isEmpty()){
            binding.tvTotal.text = ""
            binding.tvTip.text = ""
            return
        }
        val base = binding.etBase.text.toString().toDouble()
        val percentage = binding.skPercentage.progress
        val tip = base * percentage / 1002
        val total = base + tip
        binding.tvTip.text = tip.toString()
        binding.tvTotal.text = total.toString()
    }

    private fun updateTipDesc(progress: Int){
        val tipDesc = when(progress){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        binding.tvTipDesc.text = tipDesc
        val color = ArgbEvaluator().evaluate(
            progress.toFloat()/binding.skPercentage.max,
            ContextCompat.getColor(this, R.color.red),
            ContextCompat.getColor(this, R.color.green)
        ) as Int
        binding.tvTipDesc.setTextColor(color)
    }
}