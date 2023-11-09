package ch.heigvd.daa.labo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import ch.heigvd.daa.labo3.databinding.ActivityMainBinding
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setClickListeners()
        setDefaultValues()
    }

    private fun setClickListeners() {

        with(binding) {

            ArrayAdapter.createFromResource(
                this@MainActivity,
                R.array.nationalities,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerNationality.adapter = adapter
            }

            imageButtonCake.setOnClickListener {
                //TODO : Material Datepicker (make a function)
            }

            editTextBirthdate.setOnClickListener {
                //TODO : call the same function as the imageButtonCake onClickListener
            }

            buttonCancel.setOnClickListener {
                //TODO
            }

            buttonSave.setOnClickListener {
                //TODO
            }
            radioGroupOccupation.setOnCheckedChangeListener { _, _ ->
                setGroupVisibility()
            }
        }
    }

    private fun setDefaultValues() {
        with(binding) {
            radioGroupOccupation.clearCheck()
            editTextBirthdate.setText(LocalDate.now().toString())
        }
    }

    private fun setGroupVisibility() {
        with(binding) {

            when (radioGroupOccupation.checkedRadioButtonId) {
                radioButtonStudent.id -> {
                    groupStudent.visibility = VISIBLE
                    groupWorker.visibility = GONE
                }

                radioButtonWorker.id -> {
                    groupStudent.visibility = GONE
                    groupWorker.visibility = VISIBLE
                }

                else -> {
                    groupStudent.visibility = GONE
                    groupWorker.visibility = GONE
                }
            }
        }
    }
}