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

        start()
    }

    private fun start() {
        flippedCards = 0
        flippedSet.clear()

        // Voltear todas las cartas (mostrar la imagen de "pregunta")
        for (i in 1..6) {
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)
            ) as ImageButton
            btnCard.setOnClickListener(this)
            btnCard.setBackgroundResource(R.drawable.icon_pregunta)
        }


        gameOverCard = (1..6).random()

        Log.d("Juego", "La carta perdedora es $gameOverCard")
    }

    private fun flip(card: Int) {
        val btnCard = findViewById<View>(
            resources.getIdentifier("card$card", "id", this.packageName)
        ) as ImageButton

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
        for (i in 1..6) {
            val btn = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)
            ) as ImageButton
            if (i == losingCard) {
                btn.setBackgroundResource(R.drawable.icon_chempe) // Carta perdedora
            } else {
                btn.setBackgroundResource(R.drawable.icon_cheems) // Cartas normales
            }
        }
    }

    private fun checkWin() {
        if (flippedCards == 5) { // Si volteó 5 cartas sin perder, ganó
            Toast.makeText(this, "¡Felicidades! Has ganado.", Toast.LENGTH_LONG).show()
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
        }
    }
}