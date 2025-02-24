package mx.itson.cheems

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button



class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var gameOverCard = 0
    private var flippedCards = 0  // contador de cartas volteadas
    private var flippedSet = mutableSetOf<Int>() // almacenar cartas volteadas
    private val totalCards = 12
    private var allFlipped = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRestart = findViewById<Button>(R.id.button_restart)
        btnRestart.setOnClickListener {
            start()
        }
        val btnSurrender = findViewById<Button>(R.id.button_surrender)
        btnSurrender.setOnClickListener {
            surrender()
        }
        val btnFlipAll = findViewById<Button>(R.id.button_flip_all)
        btnFlipAll.setOnClickListener {
            flipAllCards()
        }

    }

    private fun start() {
        flippedCards = 0
        flippedSet.clear()
        allFlipped = false

        // Voltear todas las cartas (mostrar la imagen de "pregunta")
        for (i in 1..totalCards) {
            val btnCard = findViewById<ImageButton>(
                resources.getIdentifier("card$i", "id", this.packageName)
            )
            btnCard.setOnClickListener(this)
            btnCard.setBackgroundResource(R.drawable.icon_pregunta)
            btnCard.isEnabled = true
        }


        gameOverCard = (1..totalCards).random()

        Log.d("Juego", "La carta perdedora es $gameOverCard")
    }

    private fun flip(card: Int) {
        val btnCard = findViewById<ImageButton>(
            resources.getIdentifier("card$card", "id", this.packageName)
        )

        if (card == gameOverCard) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //si la version de android del usuario es mayor o igual
                val vibratorManager = applicationContext.getSystemService((android.content.Context.VIBRATOR_MANAGER_SERVICE)) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                // si es menor a la 12, la va hacer de esta manera
                @Suppress("DEPRECATION")
                val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(1000)
            }

            Toast.makeText(this, getString(R.string.text_game_over), Toast.LENGTH_LONG).show()
            revealAllCards(card)
            disableAllCards()
        } else {
            if (!flippedSet.contains(card)) {
                // Voltear la carta y marcarla como volteada
                btnCard.setBackgroundResource(R.drawable.icon_cheems)
                flippedSet.add(card)
                flippedCards++
                checkWin() // Verificar si el usuario ha ganado
            }
        }
    }

    private fun revealAllCards(losingCard: Int) {
        // Revelar todas las cartas
        for (i in 1..totalCards) {
            val btn = findViewById<ImageButton>(
                resources.getIdentifier("card$i", "id", this.packageName)
            )
            if (i == losingCard) {
                btn.setBackgroundResource(R.drawable.icon_chempe) // Carta perdedora
            } else {
                btn.setBackgroundResource(R.drawable.icon_cheems) // Cartas normales
            }
        }
    }

    private fun checkWin() {
        if (flippedCards == totalCards -1) { // Si volteó todas las cartas sin perder, ganó
            Toast.makeText(this, getString(R.string.text_you_win), Toast.LENGTH_LONG).show()
            disableAllCards()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.card1 -> flip(1)
            R.id.card2 -> flip(2)
            R.id.card3 -> flip(3)
            R.id.card4 -> flip(4)
            R.id.card5 -> flip(5)
            R.id.card6 -> flip(6)
            R.id.card7 -> flip(7)
            R.id.card8 -> flip(8)
            R.id.card9 -> flip(9)
            R.id.card10 -> flip(10)
            R.id.card11 -> flip(11)
            R.id.card12 -> flip(12)
        }
    }
    private fun disableAllCards() {
        for (i in 1..totalCards) {
            val btnCard = findViewById<ImageButton>(
                resources.getIdentifier("card$i", "id", this.packageName)
            )
            btnCard.isEnabled = false
        }
    }
    private fun surrender(){
        revealAllCards(gameOverCard)
        disableAllCards()
        Toast.makeText(this, getString(R.string.text_game_over), Toast.LENGTH_LONG).show()
    }

    private fun flipAllCards() {
        allFlipped = !allFlipped
        for (i in 1..totalCards) {
            val btnCard = findViewById<ImageButton>(
                resources.getIdentifier("card$i", "id", this.packageName)
            )
            if (allFlipped) {
                if (i == gameOverCard) {
                    btnCard.setBackgroundResource(R.drawable.icon_chempe)
                } else {
                    btnCard.setBackgroundResource(R.drawable.icon_cheems)
                }
            } else {
                btnCard.setBackgroundResource(R.drawable.icon_pregunta)
            }
        }
    }
}