package com.example.kahoot

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class PlayActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var rgOptions: RadioGroup
    private lateinit var rbOptionA: RadioButton
    private lateinit var rbOptionB: RadioButton
    private lateinit var rbOptionC: RadioButton
    private lateinit var rbOptionD: RadioButton
    private lateinit var btnContinue: Button

    private lateinit var databaseHelper: QuizDatabaseHelper
    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private var incorrectAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        tvQuestion = findViewById(R.id.tvQuestion)
        rgOptions = findViewById(R.id.rgOptions)
        rbOptionA = findViewById(R.id.rbOptionA)
        rbOptionB = findViewById(R.id.rbOptionB)
        rbOptionC = findViewById(R.id.rbOptionC)
        rbOptionD = findViewById(R.id.rbOptionD)
        btnContinue = findViewById(R.id.btnContinue)

        databaseHelper = QuizDatabaseHelper(this)

        // Obtener preguntas de la base de datos
        questions = databaseHelper.getAllQuestions().shuffled().take(5)

        if (questions.size < 8) {
            Toast.makeText(this, "Necesitas al menos 8 preguntas para jugar.", Toast.LENGTH_LONG).show()
            finish()
        } else {
            showQuestion()
        }

        btnContinue.setOnClickListener {
            val selectedOptionId = rgOptions.checkedRadioButtonId

            if (selectedOptionId == -1) {
                Toast.makeText(this, "Selecciona una respuesta antes de continuar.", Toast.LENGTH_SHORT).show()
            } else {
                val selectedAnswer = when (selectedOptionId) {
                    R.id.rbOptionA -> "A"
                    R.id.rbOptionB -> "B"
                    R.id.rbOptionC -> "C"
                    R.id.rbOptionD -> "D"
                    else -> ""
                }

                checkAnswer(selectedAnswer)
                rgOptions.clearCheck()

                currentQuestionIndex++
                if (currentQuestionIndex < questions.size) {
                    showQuestion()
                } else {
                    showSummary()
                }
            }
        }
    }

    private fun showQuestion() {
        val question = questions[currentQuestionIndex]
        tvQuestion.text = question.question
        rbOptionA.text = question.optionA
        rbOptionB.text = question.optionB
        rbOptionC.text = question.optionC
        rbOptionD.text = question.optionD
    }

    private fun checkAnswer(selectedAnswer: String) {
        val correctAnswer = questions[currentQuestionIndex].correctAnswer
        if (selectedAnswer == correctAnswer) {
            correctAnswers++
        } else {
            incorrectAnswers++
        }
    }

    private fun showSummary() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Resumen")
        builder.setMessage(
            "Total de preguntas: ${questions.size}\n" +
                    "Número de aciertos: $correctAnswers\n" +
                    "Número de fallos: $incorrectAnswers"
        )
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        builder.show()
    }
}
