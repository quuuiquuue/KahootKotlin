package com.example.kahoot

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Question(
    val id: Int = 0,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: String
)

class QuizDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quiz.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_QUESTIONS = "questions"
        private const val COLUMN_ID = "id"
        private const val COLUMN_QUESTION = "question"
        private const val COLUMN_OPTION_A = "option_a"
        private const val COLUMN_OPTION_B = "option_b"
        private const val COLUMN_OPTION_C = "option_c"
        private const val COLUMN_OPTION_D = "option_d"
        private const val COLUMN_CORRECT_ANSWER = "correct_answer"
    }

    override fun onCreate(db: SQLiteDatabase) {
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
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTIONS")
        onCreate(db)
    }

    fun addQuestion(question: Question) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUESTION, question.question)
            put(COLUMN_OPTION_A, question.optionA)
            put(COLUMN_OPTION_B, question.optionB)
            put(COLUMN_OPTION_C, question.optionC)
            put(COLUMN_OPTION_D, question.optionD)
            put(COLUMN_CORRECT_ANSWER, question.correctAnswer)
        }
        db.insert(TABLE_QUESTIONS, null, values)
        db.close()
    }

    fun getAllQuestions(): List<Question> {
        val questions = mutableListOf<Question>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_QUESTIONS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val question = Question(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    question = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION)),
                    optionA = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_A)),
                    optionB = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_B)),
                    optionC = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_C)),
                    optionD = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_D)),
                    correctAnswer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWER))
                )
                questions.add(question)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return questions
    }
}
