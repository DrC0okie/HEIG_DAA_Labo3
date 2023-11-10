package ch.heigvd.daa.labo3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.daa.labo3.databinding.ActivityMainBinding
import ch.heigvd.daa.labo3.model.Person
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import java.sql.Date
import java.text.SimpleDateFormat

import java.util.Locale
import java.util.TimeZone


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageButtonCake.setOnClickListener() {
            datePicker();
        }
    }

    /**
     * Cette fonction est utilisée pour le choix de la date de naissance
     */
    fun datePicker()
    {
        // Bloquer les dates dans le futur pour les dates de naissance
        val constraintsBuilder = CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.show(supportFragmentManager, "DatePicker")

        // Evénement quand on clique sur le bouton OK
        datePicker.addOnPositiveButtonClickListener() {
            // Formattage de la date
            val date = Person.dateFormatter.format(Date(it))
            binding.editTextBirthdate.setText(date)

        }
    }

}
