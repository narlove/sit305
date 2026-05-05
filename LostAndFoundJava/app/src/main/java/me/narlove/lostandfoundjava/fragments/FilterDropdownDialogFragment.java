package me.narlove.lostandfoundjava.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import me.narlove.lostandfoundjava.R;
import me.narlove.lostandfoundjava.utilities.PostCategory;
import me.narlove.lostandfoundjava.utilities.PostType;
import me.narlove.lostandfoundjava.viewmodels.SelectionsViewModel;

public class FilterDropdownDialogFragment extends DialogFragment {

    private SelectionsViewModel selectionsVm;

    public FilterDropdownDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_filter_dropdown_dialog, container, false);

        selectionsVm = new ViewModelProvider(requireActivity()).get(SelectionsViewModel.class);

        // reference to each check box
        List<CheckBox> catCheckBoxes = new ArrayList<>() {{
            add(v.findViewById(R.id.checkElectronics));
            add(v.findViewById(R.id.checkPets));
            add(v.findViewById(R.id.checkWallets));
        }};

        List<CheckBox> typeCheckBoxes = new ArrayList<>() {{
            add(v.findViewById(R.id.checkLost));
            add(v.findViewById(R.id.checkFound));
        }};

        Button btnConfirm = v.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(clicked -> {
            List<PostCategory> selectedCategories = new ArrayList<>();
            List<PostType> selectedTypes = new ArrayList<>();

            // so fragile, depends on name of UI, please don't update
            for (CheckBox box : catCheckBoxes)
            {
                if (!box.isChecked()) continue;

                selectedCategories.add(PostCategory.fromSpinnerVal(box.getText().toString()));
            }

            for (CheckBox box : typeCheckBoxes)
            {
                if (!box.isChecked()) continue;

                selectedTypes.add(PostType.fromSpinnerVal(box.getText().toString()));
            }

            selectionsVm.updateCurrentSelection(selectedCategories, selectedTypes);

            dismiss();
        });

        return v;
    }
}