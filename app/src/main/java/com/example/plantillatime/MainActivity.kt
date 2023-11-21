package com.example.plantillatime

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var cronometro: Chronometer
    private lateinit var temporizador: TextView
    private lateinit var botonIniciar: Button
    private lateinit var botonPausar: Button
    private lateinit var botonReiniciar: Button
    private lateinit var botonDetener: Button

    private var tiempoCronometro: Long = 0

    private var tiempoTemporizador: Long = 0

    private lateinit var cuentaRegresiva: CountDownTimer

    private var cronometroPausado: Boolean = false

    private var temporizadorPausado: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cronometro = findViewById(R.id.cronometro)
        temporizador = findViewById(R.id.temporizador)
        botonIniciar = findViewById(R.id.botonIniciar)
        botonPausar = findViewById(R.id.botonPausar)
        botonReiniciar = findViewById(R.id.botonReiniciar)
        botonDetener = findViewById(R.id.botonDetener)

        botonIniciar.setOnClickListener {

            if (cronometroPausado) {
                cronometro.base = System.currentTimeMillis() - tiempoCronometro
                cronometro.start()
                cronometroPausado = false
            } else {
                cronometro.base = System.currentTimeMillis()
                cronometro.start()
            }

            if (temporizadorPausado) {
                cuentaRegresiva = object : CountDownTimer(tiempoTemporizador, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        tiempoTemporizador = millisUntilFinished
                        actualizarTemporizador()
                    }

                    override fun onFinish() {
                        temporizador.text = "00:00"
                    }
                }.start()
                temporizadorPausado = false
            } else {
                tiempoTemporizador = 600000
                cuentaRegresiva = object : CountDownTimer(tiempoTemporizador, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        tiempoTemporizador = millisUntilFinished
                        actualizarTemporizador()
                    }

                    override fun onFinish() {
                        temporizador.text = "00:00"
                    }
                }.start()
            }
        }

        botonPausar.setOnClickListener {
            if (!cronometroPausado) {
                cronometro.stop()
                tiempoCronometro = System.currentTimeMillis() - cronometro.base
                cronometroPausado = true
            }

            if (!temporizadorPausado) {
                cuentaRegresiva.cancel()
                temporizadorPausado = true
            }
        }

        botonReiniciar.setOnClickListener {
            if (!cronometroPausado) {
                cronometro.stop()
                cronometro.base = System.currentTimeMillis()
                cronometro.start()
            } else {
                cronometro.base = System.currentTimeMillis()
                tiempoCronometro = 0
            }

            if (!temporizadorPausado) {
                cuentaRegresiva.cancel()
                tiempoTemporizador = 600000
                cuentaRegresiva = object : CountDownTimer(tiempoTemporizador, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        tiempoTemporizador = millisUntilFinished
                        actualizarTemporizador()
                    }

                    override fun onFinish() {
                        temporizador.text = "00:00"
                    }
                }.start()
            } else {
                tiempoTemporizador = 600000
                actualizarTemporizador()
            }
        }

        botonDetener.setOnClickListener {
            if (!cronometroPausado) {
                cronometro.stop()
                cronometro.base = System.currentTimeMillis()
            } else {
                cronometro.base = System.currentTimeMillis()
                tiempoCronometro = 0
            }

            if (!temporizadorPausado) {
                cuentaRegresiva.cancel()
                tiempoTemporizador = 600000
                actualizarTemporizador()
            } else {
                tiempoTemporizador = 600000
                actualizarTemporizador()
            }
        }
    }

    private fun actualizarTemporizador() {
        val minutos = tiempoTemporizador / 60000
        val segundos = tiempoTemporizador % 60000 / 1000
        val texto = String.format("%02d:%02d", minutos, segundos)
        temporizador.text = texto
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Salir de la aplicación")
        builder.setMessage("¿Estás seguro de que quieres salir de la aplicación?")
        builder.setPositiveButton("Sí") { dialog, which ->
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }
}
