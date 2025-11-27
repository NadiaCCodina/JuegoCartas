package com.nadia.juegocartas.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.nadia.juegocartas.R;

public class MiPopUpResultado extends DialogFragment {

    public interface OnCerrarPopup {
        void alCerrar();
    }

    private static final String ARG_MENSAJE = "mensaje";
    private OnCerrarPopup listener;

    public static MiPopUpResultado newInstance(String mensaje) {
        MiPopUpResultado popup = new MiPopUpResultado();
        Bundle args = new Bundle();
        args.putString(ARG_MENSAJE, mensaje);
        popup.setArguments(args);
        return popup;
    }

    public void setOnCerrarPopup(OnCerrarPopup listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.popup_resultado, container, false);

        TextView txt = view.findViewById(R.id.txtResultado);
        Button btn = view.findViewById(R.id.btnCerrar);

        txt.setText(getArguments().getString(ARG_MENSAJE));

        btn.setOnClickListener(v -> {
            if (listener != null) {
                listener.alCerrar();
            }
            dismiss();
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // HACER EL POPUP REDONDEADO Y AJUSTADO
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) listener.alCerrar();
    }
}
