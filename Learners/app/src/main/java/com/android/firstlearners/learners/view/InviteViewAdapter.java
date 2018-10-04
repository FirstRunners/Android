package com.android.firstlearners.learners.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.etc.Address;

import java.util.List;

public class InviteViewAdapter extends RecyclerView.Adapter<InviteViewHolder> {

    private List<Address> addressList;
    private List<Address> selectedItems;
    private View.OnClickListener inviteViewListener;

    public InviteViewAdapter(List<Address> addressList) {
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public InviteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_phonenumber,parent,false);
        return new InviteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteViewHolder holder, int position) {
        holder.view.setTag(position);
        holder.view.setOnClickListener(inviteViewListener);
        holder.name.setText(addressList.get(position).name);
        holder.phoneNumber.setText(addressList.get(position).phoneNumber);
        holder.isClicked.setImageResource(R.drawable.off);

        for(Address address : selectedItems) {
            if(address.position == position){
                holder.isClicked.setImageResource(R.drawable.check_on);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public void addItem(List<Address> selectedItems){
        this.selectedItems = selectedItems;
    }

    public void setInviteViewListener(View.OnClickListener inviteViewListener){
        this.inviteViewListener = inviteViewListener;
    }
}
