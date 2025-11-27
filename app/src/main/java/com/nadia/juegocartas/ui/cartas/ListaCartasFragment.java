package com.nadia.juegocartas.ui.cartas;

import static androidx.lifecycle.AndroidViewModel_androidKt.getApplication;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nadia.juegocartas.R;
import com.nadia.juegocartas.databinding.FragmentListaCartasBinding;
import com.nadia.juegocartas.modelos.Carta;
import com.nadia.juegocartas.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class ListaCartasFragment extends Fragment {
private FragmentListaCartasBinding binding;
    private ListaCartasViewModel mViewModel;

    public static ListaCartasFragment newInstance() {
        return new ListaCartasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentListaCartasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mViewModel = new ViewModelProvider(this).get(ListaCartasViewModel.class);
        mViewModel.getmListaCarta().observe(getViewLifecycleOwner(), new Observer<List<Carta>>() {
            @Override
            public void onChanged(List<Carta> cartas) {
                CartaAdapter adapter= new CartaAdapter(cartas, mViewModel);
                GridLayoutManager glm=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
                binding.listaCartas.setLayoutManager(glm);
                binding.listaCartas.setAdapter(adapter);
            }
        });
        int userId = ApiClient.obtenerUserId(requireContext());

        Log.d("Fragment", "User ID = " + userId);

        mViewModel.obtenerListaCartas(userId);
        mViewModel.getmBanderaJugar().observe(getViewLifecycleOwner(),flag -> {
            binding.btnConfirmarSeleccion.setEnabled(flag);
        });
        binding.btnConfirmarSeleccion.setOnClickListener(v -> {
            List<Carta> seleccionadas = mViewModel.getSeleccionadasLiveData().getValue();

            Bundle bundle = new Bundle();
            bundle.putSerializable("cartasSeleccionadas", new ArrayList<>(seleccionadas));
            Navigation.findNavController(v)
                    .navigate(R.id.action_listaCartasFragment_to_enfrentamientoFragment, bundle);
           // Toast.makeText(getContext()," Cartas Sellecionadas",Toast.LENGTH_LONG).show();
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListaCartasViewModel.class);
        // TODO: Use the ViewModel
    }

}