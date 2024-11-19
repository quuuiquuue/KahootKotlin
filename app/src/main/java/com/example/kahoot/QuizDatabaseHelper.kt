package com.example.kahoot

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class QuizDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quiz.db" // Nombre de la base de datos
        private const val DATABASE_VERSION = 1 // Versión de la base de datos

        private const val TABLE_QUESTIONS = "questions" // Nombre de la tabla
        private const val COLUMN_ID = "id"
        private const val COLUMN_QUESTION = "question"
        private const val COLUMN_OPTION_A = "option_a"
        private const val COLUMN_OPTION_B = "option_b"
        private const val COLUMN_OPTION_C = "option_c"
        private const val COLUMN_OPTION_D = "option_d"
        private const val COLUMN_CORRECT_ANSWER = "correct_answer"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Se crea la tabla de preguntas
        val createTableQuery = """
            CREATE TABLE $TABLE_QUESTIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_QUESTION TEXT NOT NULL,
                $COLUMN_OPTION_A TEXT NOT NULL,
                $COLUMN_OPTION_B TEXT NOT NULL,
                $COLUMN_OPTION_C TEXT NOT NULL,
                $COLUMN_OPTION_D TEXT NOT NULL,
                $COLUMN_CORRECT_ANSWER TEXT NOT NULL
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Si se actualiza la versión de la base de datos, se elimina la tabla y se crea de nuevo
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTIONS")
        onCreate(db)
    }

    // Método para agregar una nueva pregunta
    fun addQuestion(question: com.example.kahoot.Question) {
        val db = writableDatabase // Abrir la base de datos en modo escritura
        val values = ContentValues().apply {
            put(COLUMN_QUESTION, question.question)
            put(COLUMN_OPTION_A, question.optionA)
            put(COLUMN_OPTION_B, question.optionB)
            put(COLUMN_OPTION_C, question.optionC)
            put(COLUMN_OPTION_D, question.optionD)
            put(COLUMN_CORRECT_ANSWER, question.correctAnswer)
        }
        db.insert(TABLE_QUESTIONS, null, values) // Insertar la nueva pregunta
        db.close() // Cerrar la base de datos
    }

    // Método para obtener todas las preguntas de la base de datos
    fun getAllQuestions(): List<Question> {
        val questions = mutableListOf<Question>()
        val db = readableDatabase // Abrir la base de datos en modo lectura
        val cursor = db.query(
            TABLE_QUESTIONS, // Nombre de la tabla
            null, // Seleccionamos todas las columnas
            null, // No hay condiciones (where)
            null, // No hay parámetros adicionales
            null, // No agrupamos
            null, // No ordenamos
            null // No limitamos el número de resultados
        )

        // Recorrer el cursor y convertir cada fila en un objeto Question
        if (cursor.moveToFirst()) {
            do {
                val question = Question(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    question = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION)),
                    optionA = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_A)),
                    optionB = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_B)),
                    optionC = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_C)),
                    optionD = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_D)),
                    correctAnswer = cursor.getString(cursor.getColumnIndexOrThrow(
                        COLUMN_CORRECT_ANSWER
                    ))
                )
                questions.add(question)
            } while (cursor.moveToNext())
        }

        cursor.close() // Cerrar el cursor
        db.close() // Cerrar la base de datos
        return questions // Retornar la lista de preguntas
    }

    fun deleteAllQuestions() {
        val db = writableDatabase
        db.delete(TABLE_QUESTIONS, null, null)
        db.close()
    }
}

