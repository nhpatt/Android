package com.eduhern.dbquiz.activity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eduhern.dbquiz.models.Respuesta;

public class RespuestaAdapter extends ArrayAdapter<Respuesta> {

	public RespuestaAdapter(final QuestionActivity context, final int resource,
			final List<Respuesta> transactions) {
		super(context, resource, transactions);
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		if (convertView == null) {
			final LayoutInflater vi = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(com.eduhern.dbquiz.R.layout.fila, null);
		}

		final Respuesta transaction = getItem(position);
		final TextView fila = (TextView) convertView
				.findViewById(com.eduhern.dbquiz.R.id.fila1);

		fila.setText(transaction.getDescripcion());

		return convertView;
	}

}
