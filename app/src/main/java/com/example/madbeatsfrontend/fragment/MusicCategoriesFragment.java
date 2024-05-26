package com.example.madbeatsfrontend.fragment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.viewModel.EventsSpotsViewModel;

public class MusicCategoriesFragment extends DialogFragment {
    private SearchFragment searchFragment;
    private EventsSpotsViewModel eventsSpotsViewModel;
    Button buttonElectronic, buttonLatin, buttonUrban, buttonMainstream, buttonPopRock, buttonOthers;

    public MusicCategoriesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsSpotsViewModel = new ViewModelProvider(requireActivity()).get(EventsSpotsViewModel.class);
        searchFragment = (SearchFragment) getParentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_categories, container, false);

        buttonElectronic = view.findViewById(R.id.buttonElectronic);

        String electronicText = "ELECTRONIC\nTechno\nHouse\nTrance\nHardcore\nEtc.";
        SpannableString electronicSpannableString = new SpannableString(electronicText);

        electronicSpannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        electronicSpannableString.setSpan(new UnderlineSpan(), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        electronicSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 11, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        electronicSpannableString.setSpan(new RelativeSizeSpan(0.75f), 11, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        electronicSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 18, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        electronicSpannableString.setSpan(new RelativeSizeSpan(0.75f), 18, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        electronicSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 24, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        electronicSpannableString.setSpan(new RelativeSizeSpan(0.75f), 24, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        electronicSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 31, 39, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        electronicSpannableString.setSpan(new RelativeSizeSpan(0.75f), 31, 39, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        electronicSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 40, 44, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        electronicSpannableString.setSpan(new RelativeSizeSpan(0.75f), 40, 44, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        buttonElectronic.setText(electronicSpannableString);

        buttonLatin = view.findViewById(R.id.buttonLatin);

        String latinText = "LATIN\nSalsa\nBachata\nReggaeton\nCumbia\nEtc.";
        SpannableString latinSpannableString = new SpannableString(latinText);

        latinSpannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        latinSpannableString.setSpan(new UnderlineSpan(), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        latinSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 6, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        latinSpannableString.setSpan(new RelativeSizeSpan(0.75f), 6, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        latinSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 12, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        latinSpannableString.setSpan(new RelativeSizeSpan(0.75f), 12, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        latinSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 20, 29, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        latinSpannableString.setSpan(new RelativeSizeSpan(0.75f), 20, 29, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        latinSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 30, 40, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        latinSpannableString.setSpan(new RelativeSizeSpan(0.75f), 30, 40, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        buttonLatin.setText(latinSpannableString);

        buttonUrban = view.findViewById(R.id.buttonUrban);

        String urbanText = "URBAN\nRap\nRnB\nTrap\nAfro\nEtc.";
        SpannableString urbanSpannableString = new SpannableString(urbanText);

        urbanSpannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        urbanSpannableString.setSpan(new UnderlineSpan(), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        urbanSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 6, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        urbanSpannableString.setSpan(new RelativeSizeSpan(0.75f), 6, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        urbanSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 10, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        urbanSpannableString.setSpan(new RelativeSizeSpan(0.75f), 10, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        urbanSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 14, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        urbanSpannableString.setSpan(new RelativeSizeSpan(0.75f), 14, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        urbanSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 19, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        urbanSpannableString.setSpan(new RelativeSizeSpan(0.75f), 19, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        urbanSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 24, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        urbanSpannableString.setSpan(new RelativeSizeSpan(0.75f), 24, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        buttonUrban.setText(urbanSpannableString);

        buttonMainstream = view.findViewById(R.id.buttonMainstream);

        String mainstreamText = "MAINSTREAM\nHits\n(all genres)\n90's\n2000's\nEtc.";
        SpannableString mainstreamSpannableString = new SpannableString(mainstreamText);

        mainstreamSpannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mainstreamSpannableString.setSpan(new UnderlineSpan(), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mainstreamSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 11, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mainstreamSpannableString.setSpan(new RelativeSizeSpan(0.75f), 11, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mainstreamSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 16, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mainstreamSpannableString.setSpan(new RelativeSizeSpan(0.75f), 16, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mainstreamSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 29, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mainstreamSpannableString.setSpan(new RelativeSizeSpan(0.75f), 29, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mainstreamSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 34, 41, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mainstreamSpannableString.setSpan(new RelativeSizeSpan(0.75f), 34, 41, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mainstreamSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 41, 45, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mainstreamSpannableString.setSpan(new RelativeSizeSpan(0.75f), 41, 45, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        buttonMainstream.setText(mainstreamSpannableString);

        buttonPopRock = view.findViewById(R.id.buttonPopRock);

        String popRockText = "POP-ROCK\nPop\nRock\nPunk\nMetal\nEtc.";
        SpannableString popRockSpannableString = new SpannableString(popRockText);

        popRockSpannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        popRockSpannableString.setSpan(new UnderlineSpan(), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        popRockSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 9, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        popRockSpannableString.setSpan(new RelativeSizeSpan(0.75f), 9, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        popRockSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 13, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        popRockSpannableString.setSpan(new RelativeSizeSpan(0.75f), 13, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        popRockSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 18, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        popRockSpannableString.setSpan(new RelativeSizeSpan(0.75f), 18, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        popRockSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 23, 29, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        popRockSpannableString.setSpan(new RelativeSizeSpan(0.75f), 23, 29, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        popRockSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 29, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        popRockSpannableString.setSpan(new RelativeSizeSpan(0.75f), 29, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        buttonPopRock.setText(popRockSpannableString);

        buttonOthers = view.findViewById(R.id.buttonOthers);

        String othersText = "OTHERS\nReggae\nJazz\nSoul\nClassical\nEtc.";
        SpannableString othersSpannableString = new SpannableString(othersText);

        othersSpannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        othersSpannableString.setSpan(new UnderlineSpan(), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        othersSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        othersSpannableString.setSpan(new RelativeSizeSpan(0.75f), 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        othersSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 14, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        othersSpannableString.setSpan(new RelativeSizeSpan(0.75f), 14, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        othersSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 19, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        othersSpannableString.setSpan(new RelativeSizeSpan(0.75f), 19, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        othersSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 24, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        othersSpannableString.setSpan(new RelativeSizeSpan(0.75f), 24, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        othersSpannableString.setSpan(new StyleSpan(Typeface.NORMAL), 34, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        othersSpannableString.setSpan(new RelativeSizeSpan(0.75f), 34, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        buttonOthers.setText(othersSpannableString);

        buttonElectronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = "Electronic";
                // Llama al método en MusicCategoriesFragment para aplicar el filtro de categoría musical
                applyMusicCategoryFilter(selectedCategory);
            }
        });

        buttonLatin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = "Latin";
                applyMusicCategoryFilter(selectedCategory);
            }
        });

        buttonUrban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = "Urban";
                applyMusicCategoryFilter(selectedCategory);
            }
        });

        buttonMainstream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = "Mainstream";
                applyMusicCategoryFilter(selectedCategory);
            }
        });

        buttonPopRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = "Pop-Rock";
                applyMusicCategoryFilter(selectedCategory);
            }
        });

        buttonOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = "Others";
                applyMusicCategoryFilter(selectedCategory);
            }
        });

        return view;
    }

    public void applyMusicCategoryFilter(String selectedCategory) {
        if (getTargetFragment() instanceof SearchFragment) {
            SearchFragment searchFragment = (SearchFragment) getTargetFragment();
            searchFragment.updateMapWithFilteredSpotsByMusicCategory(selectedCategory);
        }
        dismiss();
        // Verifica si se seleccionó una categoría
        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            // Imprime la categoría seleccionada en el log
            Log.d("MusicCategoriesFragment", "Categoría seleccionada: " + selectedCategory);
        } else {
            // Si no se seleccionó ninguna categoría, imprime un mensaje de advertencia en el log
            Log.w("MusicCategoriesFragment", "No se seleccionó ninguna categoría");
        }
    }
}
