package com.example.kahoot

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ConfigActivity : AppCompatActivity() {

    private lateinit var databaseHelper: QuizDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        databaseHelper = QuizDatabaseHelper(this)

        val etQuestion = findViewById<EditText>(R.id.etQuestion)
        val etOptionA = findViewById<EditText>(R.id.etOptionA)
        val etOptionB = findViewById<EditText>(R.id.etOptionB)
        val etOptionC = findViewById<EditText>(R.id.etOptionC)
        val etOptionD = findViewById<EditText>(R.id.etOptionD)
        val rgCorrectAnswer = findViewById<RadioGroup>(R.id.rgCorrectAnswer)
        val btnSaveQuestion = findViewById<Button>(R.id.btnSaveQuestion)

        btnSaveQuestion.setOnClickListener {
            val questionText = etQuestion.text.toString()
            val optionA = etOptionA.text.toString()
            val optionB = etOptionB.text.toString()
            val optionC = etOptionC.text.toString()
            val optionD = etOptionD.text.toString()
            val correctAnswer = when (rgCorrectAnswer.checkedRadioButtonId) {
                R.id.rbOptionA -> "A"
                R.id.rbOptionB -> "B"
                R.id.rbOptionC -> "C"
                R.id.rbOptionD -> "D"
                else -> ""
            }

            if (questionText.isBlank() || optionA.isBlank() || optionB.isBlank() ||
                optionC.isBlank() || optionD.isBlank() || correctAnswer.isBlank()) {
                Toast.makeText(this, "Completa todos los campos y selecciona una respuesta correcta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val question = Question(
                question = questionText,
                optionA = optionA,
                optionB = optionB,
                optionC = optionC,
                optionD = optionD,
                correctAnswer = correctAnswer
            )

            databaseHelper.addQuestion(question)
            Toast.makeText(this, "Pregunta guardada correctamente", Toast.LENGTH_SHORT).show()

            etQuestion.text.clear()
            etOptionA.text.clear()
            etOptionB.text.clear()
            etOptionC.text.clear()
            etOptionD.text.clear()
            rgCorrectAnswer.clearCheck()
        }
    }
}
