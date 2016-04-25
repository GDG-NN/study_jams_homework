package marchenko.schedule;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.UnitViewHolder>{
    public static class UnitViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView subject1;
        TextView teacher1;
        TextView weekday1;
        TextView time1;

        UnitViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            subject1 = (TextView)itemView.findViewById(R.id.subject);
            teacher1 = (TextView)itemView.findViewById(R.id.teacher);
            weekday1 = (TextView)itemView.findViewById(R.id.weekday);
            time1 = (TextView)itemView.findViewById(R.id.time);
        }
    }

    List<unit> units;

    RVAdapter(List<unit> units){
        this.units = units;
    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public UnitViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resycler_item, viewGroup, false);
        UnitViewHolder pvh = new UnitViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UnitViewHolder unitViewHolder, int i) {
        unitViewHolder.subject1.setText(units.get(i).subject);
        unitViewHolder.teacher1.setText(units.get(i).teacher);
        unitViewHolder.weekday1.setText(units.get(i).weekday);
        unitViewHolder.time1.setText(units.get(i).time);
    }
}
