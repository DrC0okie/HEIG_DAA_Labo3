package ch.heigvd.daa.labo3

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import ch.heigvd.daa.labo3.databinding.ActivityMainBinding
import ch.heigvd.daa.labo3.model.Person
import ch.heigvd.daa.labo3.model.Student
import ch.heigvd.daa.labo3.model.Worker
import java.time.Instant
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var person: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setClickListeners()
        binding.radioGroupOccupation.clearCheck()
        initUI()
    }

    private fun setClickListeners() {

        with(binding) {
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
                Log.i(
                    "", if (radioButtonStudent.isChecked) {
                        createStudentFromUI()
                    } else {
                        createWorkerFromUI()
                    }.toString()
                )
            }

            radioGroupOccupation.setOnCheckedChangeListener { _, checkedId ->
                setGroupVisibility(checkedId)
            }
        }
    }

    private fun getSpinnerAdapter(resource: Int, context: Context): ArrayAdapter<CharSequence> {
        return ArrayAdapter.createFromResource(
            context, resource, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
                "",//TODO retrieve value from spinner
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
                "",//TODO retrieve value from spinner
                editTextWorkerCompany.text.toString(),
                "",//TODO retieve value from spinner
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

            when (person) {
                is Student -> {
                    editTextStudentSchool.setText(person.university)
                    editTextStudentGraduationyear.setText(person.graduationYear.toString())
                }

                is Worker -> {
                    // TODO spinnerWorkerSector
                    editTextWorkerCompany.setText(person.company)
                    editTextWorkerExperience.setText(person.experienceYear.toString())
                }
            }
        }
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

    private fun initUI() {
        setGroupVisibility(0)
        with(binding) {
            spinnerNationality.adapter = getSpinnerAdapter(R.array.nationalities, this@MainActivity)
            spinnerWorkerSector.adapter = getSpinnerAdapter(R.array.sectors, this@MainActivity)
            editTextBirthdate.setText(Person.dateFormatter.format(Date.from(Instant.now())))
        }
    }
}