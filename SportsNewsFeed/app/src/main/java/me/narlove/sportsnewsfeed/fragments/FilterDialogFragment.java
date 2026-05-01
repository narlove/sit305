package me.narlove.sportsnewsfeed.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import me.narlove.sportsnewsfeed.R;
import me.narlove.sportsnewsfeed.persistence.FiltersViewModel;
import me.narlove.sportsnewsfeed.utilities.Category;

public class FilterDialogFragment extends DialogFragment {

    private FiltersViewModel mFiltersViewModel;

    public FilterDialogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_filter, container, false);

        mFiltersViewModel = new ViewModelProvider(requireActivity()).get(FiltersViewModel.class);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // reference to each check box
        List<CheckBox> checkBoxes = new ArrayList<>() {{
            add(view.findViewById(R.id.checkBoxing));
            add(view.findViewById(R.id.checkBasketball));
            add(view.findViewById(R.id.checkCricket));
            add(view.findViewById(R.id.checkSoccer));
            add(view.findViewById(R.id.checkTennis));
            add(view.findViewById(R.id.checkFootball));
        }};

        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(v -> {
            List<Category> selected = new ArrayList<>();

            // so fragile, depends on name of UI, please don't update
            for (CheckBox box : checkBoxes)
            {
                if (!box.isChecked()) continue;

                selected.add(Category.valueOf(box.getText().toString()));
            }

            mFiltersViewModel.updateCurrentCategories(selected);

            dismiss();
        });
    }
}
