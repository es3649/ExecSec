package com.es3649.execsec.activities.settings.groups;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.es3649.execsec.R;
import com.es3649.execsec.data.database.DB_Proxy;
import com.es3649.execsec.data.model.Group;

import java.util.List;

class GroupManagerAdapter extends RecyclerView.Adapter<GroupViewHolder> {

    private List<Group> groupList;
    private DB_Proxy db;

    GroupManagerAdapter(List<Group> groupList, DB_Proxy db) {
    this.groupList = groupList;
    this.db = db;
    }

    void newGroup() {
        Group g = new Group("","",-1);
        groupList.add(g);
        db.stashGroup(g);
        notifyDataSetChanged();
    }

    private void deleteGroup(int pos) {
        db.deleteGroup(groupList.get(pos));
        groupList.remove(pos);
        notifyDataSetChanged();
    }

    List<Group> getGroupList() {
        return this.groupList;
    }

    @Override
    public int getItemCount() {
        if (groupList == null) return 0;
        return groupList.size();
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lif = LayoutInflater.from(parent.getContext());
        return new GroupViewHolder(lif.inflate(R.layout.viewholder_group_manager, parent, false),
                new GroupViewHolder.DeleteListener() {
                    @Override
                    public void delete(int i) {
                        deleteGroup(i);
                    }
                });
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        holder.bind(groupList.get(position), position);
    }
}
