package com.example.room

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.Date

class MainActivity : AppCompatActivity() {

    var db: NoteDatabase? = null

    private lateinit var nameET: EditText
    private lateinit var phoneET: EditText
    private lateinit var saveBTN: Button
    private lateinit var textTV: TextView
    private lateinit var toolbarMain: Toolbar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nameET = findViewById(R.id.nameET)
        phoneET = findViewById(R.id.phoneET)
        saveBTN = findViewById(R.id.saveBTN)
        textTV = findViewById(R.id.textTV)
        toolbarMain = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbarMain)
        title = "Контакты"

        db = NoteDatabase.getDatabase(this)
        readDatabase(db!!)

    }

    override fun onResume() {
        super.onResume()
        saveBTN.setOnClickListener {
            val note = Note(nameET.text.toString(), phoneET.text.toString(), Date().time.toString())
            addNote(db!!, note)
            readDatabase(db!!)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun addNote(db: NoteDatabase, note: Note) = GlobalScope.async {
        db.getNoteDao().insert(note)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun readDatabase(db: NoteDatabase) = GlobalScope.async {
        textTV.text = ""
        val list = db.getNoteDao().getAllNotes()
        list.forEach { i -> textTV.append(i.name + " " + i.phone + "\n") }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuMainExit -> {
                finishAffinity()
                Toast.makeText(
                    applicationContext,
                    "Программа завершена",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
