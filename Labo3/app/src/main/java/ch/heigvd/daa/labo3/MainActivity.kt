package ch.heigvd.daa.labo3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.daa.labo3.databinding.ActivityMainBinding
import ch.heigvd.daa.labo3.model.Person
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import ch.heigvd.daa.labo3.adapters.CustomSpinnerAdapter
import ch.heigvd.daa.labo3.model.Student
import ch.heigvd.daa.labo3.model.Worker
import java.util.Date
import java.time.Instant
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initDatePicker()
        initClickListeners()
        initUI()
    }

    private fun initDatePicker() {
        //Forbid dates in the future
        val dateConstraints =
            CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now())

        //Create the datePicker
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(resources.getString(R.string.main_base_birthdate_dialog_title))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(dateConstraints.build())
            .build()
    }

    private fun initClickListeners() {

        with(binding) {
            imageButtonCake.setOnClickListener {
                datePicker.show(supportFragmentManager, "DatePicker")
            }

            editTextBirthdate.setOnClickListener {
                datePicker.show(supportFragmentManager, "DatePicker")
            }

            buttonCancel.setOnClickListener {
                resetUI()
            }

            buttonSave.setOnClickListener {
                if (binding.radioGroupOccupation.checkedRadioButtonId == RadioButton.NO_ID) {
                    val message = resources.getString(R.string.main_base_occupation_error)
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                } else {
                    Log.i(
                        "", if (radioButtonStudent.isChecked) {
                            createStudentFromUI()
                        } else {
                            createWorkerFromUI()
                        }.toString()
                    )
                }
            }

            radioGroupOccupation.setOnCheckedChangeListener { _, checkedId ->
                setGroupVisibility(checkedId)
            }

            datePicker.addOnPositiveButtonClickListener {
                binding.editTextBirthdate.setText(Person.dateFormatter.format(Date(it)))
            }
        }
    }

    private fun getCalendarFromUI(): Calendar {
        return Calendar.getInstance().also {
            it.time =
                Person.dateFormatter.parse(binding.editTextBirthdate.text.toString()) ?: Date()
        }
    }

    private fun createStudentFromUI(): Student {
        with(binding) {
            return Student(
                editTextSurname.text.toString(),
                editTextName.text.toString(),
                getCalendarFromUI(),
                spinnerNationality.selectedItem.toString(),
                editTextStudentSchool.text.toString(),
                editTextStudentGraduationyear.text.toString().toInt(),
                editTextEmail.text.toString(),
                editTextComments.text.toString()
            )
        }
    }

    private fun createWorkerFromUI(): Worker {
        with(binding) {
            return Worker(
                editTextSurname.text.toString(),
                editTextName.text.toString(),
                getCalendarFromUI(),
                spinnerNationality.selectedItem.toString(),
                editTextWorkerCompany.text.toString(),
                spinnerWorkerSector.selectedItem.toString(),
                editTextWorkerExperience.text.toString().toInt(),
                editTextEmail.text.toString(),
                editTextComments.text.toString()
            )
        }
    }

    private fun setPersonDefaults(person: Person) {
        with(binding) {
            editTextName.setText(person.firstName)
            editTextSurname.setText(person.name)
            editTextBirthdate.setText(Person.dateFormatter.format(person.birthDay.time))
            editTextEmail.setText(person.email)
            editTextComments.setText(person.remark)
            setSpinnerValue(spinnerNationality, person.nationality)

            when (person) {
                is Student -> {
                    editTextStudentSchool.setText(person.university)
                    editTextStudentGraduationyear.setText(person.graduationYear.toString())
                }

                is Worker -> {
                    setSpinnerValue(spinnerWorkerSector, person.sector)
                    editTextWorkerCompany.setText(person.company)
                    editTextWorkerExperience.setText(person.experienceYear.toString())
                }
            }
        }
    }

    private fun setSpinnerValue(spinner: Spinner, value: String) {
        val adapter = spinner.adapter as ArrayAdapter<CharSequence>
        spinner.setSelection(adapter.getPosition(value))
    }

    private fun setGroupVisibility(buttonId: Int) {
        with(binding) {
            when (buttonId) {
                radioButtonStudent.id -> {
                    groupStudent.visibility = VISIBLE
                    groupWorker.visibility = GONE
                    setPersonDefaults(Person.exampleStudent)
                }

                radioButtonWorker.id -> {
                    groupStudent.visibility = GONE
                    groupWorker.visibility = VISIBLE
                    setPersonDefaults(Person.exampleWorker)
                }

                else -> {
                    groupStudent.visibility = GONE
                    groupWorker.visibility = GONE
                }
            }
        }
    }

    private fun initSpinners() {

        binding.spinnerNationality.adapter = CustomSpinnerAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf(getString(R.string.nationality_empty)) + resources.getStringArray(R.array.nationalities)
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spinnerWorkerSector.adapter = ArrayAdapter.createFromResource(
            this, R.array.sectors, android.R.layout.simple_spinner_item
        ).also { a -> a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    private fun initUI() {
        with(binding) {
            radioGroupOccupation.clearCheck()
            initSpinners()
            spinnerNationality.setSelection(0) // Set to "Sélectionner"
            editTextBirthdate.setText(Person.dateFormatter.format(Date.from(Instant.now())))
        }
    }

    private fun resetUI() {
        with(binding) {
            radioGroupOccupation.clearCheck()
            editTextName.setText("")
            editTextSurname.setText("")
            editTextBirthdate.setText("")
            editTextEmail.setText("")
            editTextComments.setText("")
            editTextStudentSchool.setText("")
            editTextStudentGraduationyear.setText("")
            editTextWorkerCompany.setText("")
            editTextWorkerExperience.setText("")
            spinnerWorkerSector.setSelection(0)
            spinnerNationality.setSelection(0)
        }
    }
}
