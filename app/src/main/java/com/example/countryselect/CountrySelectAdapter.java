package com.example.countryselect;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.countryselect.data.CountryData;
import com.example.countryselect.databinding.LayoutRvCountrySelectBinding;

import java.util.List;

public class CountrySelectAdapter extends RecyclerView.Adapter<CountrySelectAdapter.CountrySelectViewHolder> {

    private final List<CountryData> countryDataList;
    private final CountrySelectAdapterListener listener;

    public CountrySelectAdapter(List<CountryData> countryDataList, CountrySelectAdapterListener listener) {
        this.countryDataList = countryDataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CountrySelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutRvCountrySelectBinding binding = LayoutRvCountrySelectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CountrySelectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CountrySelectViewHolder holder, int position) {
        holder.bind(countryDataList.get(position));
        holder.binding.getRoot().setOnClickListener((v)-> listener.onClick(countryDataList.get(position)));
    }

    @Override
    public int getItemCount() {
        return countryDataList.size();
    }

    public static class CountrySelectViewHolder extends RecyclerView.ViewHolder {

        private final LayoutRvCountrySelectBinding binding;

        public CountrySelectViewHolder(@NonNull LayoutRvCountrySelectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final CountryData countryData) {
            binding.tvCountry.setText(countryData.getName());
            Glide
                    .with(binding.getRoot().getContext())
                    .load("https://www.countryflags.io/" + countryData.getCode() + "/flat/24.png")
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivCountry);
            binding.radioButton.setChecked(countryData.isSelected());
        }
    }

    interface CountrySelectAdapterListener{
        void onClick(CountryData countryData);
    }
}
