package com.eduhern.dbquiz.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eduhern.dbquiz.R;
import com.eduhern.dbquiz.database.DatabaseHelper;
import com.eduhern.dbquiz.models.Pregunta;
import com.eduhern.dbquiz.models.Respuesta;
import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;

public class QuestionActivity extends OrmLiteBaseListActivity<DatabaseHelper> {

	private RespuestaAdapter adapter;
	private int respuestasCorrectas = 0;
	private Random numeroPreguntaAleatorio;
	private Pregunta preguntaActual;
	private List<Pregunta> preguntas = new ArrayList<Pregunta>();
	private final HashSet<Pregunta> preguntasYaJugadas = new HashSet<Pregunta>();
	private List<Respuesta> respuestas;
	public static final int MAXIMO_PREGUNTAS = 10;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		numeroPreguntaAleatorio = new Random();
		preguntas = getHelper().getPreguntaDAO().queryForAll();
		Log.d("DEPURACION", String.valueOf(preguntas.size()));

		recuperarSiguientePregunta();
		respuestas = preguntaActual.getRespuestas();
		adapter = new RespuestaAdapter(this, com.eduhern.dbquiz.R.layout.fila,
				respuestas);
		setListAdapter(adapter);

	}

	@Override
	protected void onListItemClick(final ListView l, final View v,
			final int posicion, final long id) {
		if (preguntaActual.getRespuestas().get(posicion).isCorrecta()) {
			Toast.makeText(this, "CORRECTO", Toast.LENGTH_SHORT).show();
			respuestasCorrectas++;
		} else {
			Toast.makeText(this, "FALLO", Toast.LENGTH_SHORT).show();
		}
		((TextView) findViewById(R.id.puntuacion1)).setText(String
				.valueOf(respuestasCorrectas));

		if (preguntasYaJugadas.size() == MAXIMO_PREGUNTAS
				|| preguntasYaJugadas.size() >= preguntas.size()) {
			final Intent intent = new Intent(this, ResultadoActivity.class);
			intent.putExtra("resultado", String.valueOf(respuestasCorrectas));
			finish();
			startActivity(intent);
		} else {
			recuperarSiguientePregunta();
			respuestas.clear();
			respuestas.addAll(preguntaActual.getRespuestas());
			adapter.notifyDataSetChanged();
		}
	}

	private void recuperarSiguientePregunta() {
		Integer posicionPreguntaActual = null;
		do {
			posicionPreguntaActual = numeroPreguntaAleatorio.nextInt(preguntas
					.size());
		} while (preguntasYaJugadas.contains(preguntas
				.get(posicionPreguntaActual)));
		preguntaActual = preguntas.get(posicionPreguntaActual);
		preguntasYaJugadas.add(preguntaActual);
		final int id = getResources().getIdentifier(preguntaActual.getImagen(),
				"drawable", getPackageName());

		final Drawable drawable = getResources().getDrawable(id);
		final ImageView imagen = (ImageView) findViewById(R.id.imageView2);
		imagen.setImageDrawable(drawable);
		final TextView textViewPregunta = (TextView) findViewById(R.id.texto_pregunta);
		textViewPregunta.setText(preguntaActual.getDescripcion());
	}
}
