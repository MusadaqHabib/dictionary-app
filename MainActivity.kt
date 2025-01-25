package com.example.mydictionary

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mydictionary.models.DictionaryItem
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var searchView: SearchView
    private lateinit var suggestionListView: ListView
    private lateinit var dictionaryData: List<DictionaryItem>
    private lateinit var adapter: CustomAdapter
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var soundButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dictionaryData = intent.getSerializableExtra("dictionaryData") as? List<DictionaryItem> ?: emptyList()

        searchView = findViewById(R.id.searchbar)
        suggestionListView = findViewById(R.id.suggestionListView)
        soundButton = findViewById(R.id.soundButton)
        textToSpeech = TextToSpeech(this, this)

        // Use CustomAdapter
        adapter = CustomAdapter(this, dictionaryData)
        suggestionListView.adapter = adapter

        setupSearchView()
        setupSoundButton()
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText)
                return true
            }
        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showSuggestions()
            } else {
                hideSuggestions()
            }
        }

        suggestionListView.setOnItemClickListener { _, _, position, _ ->
            val selectedDictionaryItem = adapter.getItem(position)
            selectedDictionaryItem?.let { displaySelectedData(it as DictionaryItem) }
            hideSuggestions()
        }
    }

    private fun setupSoundButton() {
        soundButton.setOnClickListener {
            Log.e("TTS", "Sound button clicked")
            val selectedPosition = suggestionListView.selectedItemPosition
            if (selectedPosition >= 0 && selectedPosition < adapter.count) {
                val selectedDictionaryItem = adapter.getItem(selectedPosition)
                selectedDictionaryItem?.let { speakText(it as DictionaryItem) }
            }
        }
    }

    private fun displaySelectedData(dictionaryItem: DictionaryItem) {
        findViewById<TextView>(R.id.englishMeaningTextView).text = "Word: ${dictionaryItem.word}"
        findViewById<TextView>(R.id.urduMeaningTextView).text = "Meaning: ${dictionaryItem.meaning}"
        findViewById<TextView>(R.id.romanMeaningTextView).text = "Category: ${dictionaryItem.category}"
    }

    private fun showSuggestions() {
        suggestionListView.visibility = View.VISIBLE
    }

    private fun hideSuggestions() {
        suggestionListView.visibility = View.GONE
    }

    private fun speakText(dictionaryItem: DictionaryItem) {
        val textToSpeak = "Word: ${dictionaryItem.word}\nMeaning: ${dictionaryItem.meaning}\nCategory: ${dictionaryItem.category}"

        if (textToSpeech.isLanguageAvailable(Locale.getDefault()) == TextToSpeech.LANG_MISSING_DATA ||
            textToSpeech.isLanguageAvailable(Locale.getDefault()) == TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            Log.e("TTS", "Default language not supported")
            return
        }

        textToSpeech.language = Locale.getDefault()

        val result = textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)

        if (result == TextToSpeech.ERROR) {
            Log.e("TTS", "Error while speaking text")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Log.e("TTS", "Initialization successful")
        } else {
            Log.e("TTS", "Initialization failed: $status")
        }
    }

    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()

        super.onDestroy()
    }
}
