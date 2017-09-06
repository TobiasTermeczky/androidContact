package nl.yzaazy.contactslist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class PersonAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList mPersonArrayList;

    PersonAdapter(LayoutInflater layoutInflater, ArrayList<Person> personArrayList) {
        mInflater = layoutInflater;
        mPersonArrayList = personArrayList;
    }

    @Override
    public int getCount() {
        return mPersonArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        Log.i("getItem()", "");
        return mPersonArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.person_row, null);
            viewHolder = new ViewHolder();
            viewHolder.profilePicture = (ImageView) convertView.findViewById(R.id.profilePicture);
            viewHolder.firstName = (TextView) convertView.findViewById(R.id.firstNameList);
            viewHolder.lastName = (TextView) convertView.findViewById(R.id.lastNameList);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Person person = (Person) mPersonArrayList.get(position);
        viewHolder.firstName.setText(person.firstName);
        viewHolder.lastName.setText(person.lastName);
        new BitmapGetter().downLoadImage(person.profilePicture, viewHolder.profilePicture);
        return convertView;
    }

    private static class ViewHolder {
        ImageView profilePicture;
        TextView firstName;
        TextView lastName;
    }

}
