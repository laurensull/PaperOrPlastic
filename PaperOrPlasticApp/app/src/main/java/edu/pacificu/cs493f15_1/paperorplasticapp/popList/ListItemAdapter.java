package edu.pacificu.cs493f15_1.paperorplasticapp.popList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.TimerTask;

import edu.pacificu.cs493f15_1.paperorplasticapp.R;
import edu.pacificu.cs493f15_1.paperorplasticjava.ListItem;
import edu.pacificu.cs493f15_1.paperorplasticjava.PoPList;

/**
 * Created by sull0678 on 11/5/2015.
 */
public class ListItemAdapter extends ArrayAdapter<ListItem>
{
    private ArrayList<ListItem> mItemArray;
    int mLayoutResourceId;
    Context mContext;
    public int mPosition;
    final Handler mHandlerUI = new Handler(); //for waiting if needed


    private int nCounter;

    public ListItemAdapter (Context context, int layoutResourceId, ArrayList<ListItem> items)
    {
        super (context, layoutResourceId, items);
        this.mLayoutResourceId = layoutResourceId;
        this.mContext = context;
        this.mItemArray = items;
        this.setNotifyOnChange(true);
    }



    @Override
    public ListItem getItem (int position) {
        // TODO Auto-generated method stub
        return mItemArray.get (position);
    }


    public ArrayList<ListItem> getItems ()
    {
        return mItemArray;
    }

    @Override
    public void add (ListItem item) {
        // TODO Auto-generated method stub
        super.add (item);
        //mItemArray.add(item);
    }


    /*public void replaceItems (ArrayList<ListItem> sortedItems)
    {
        super.clear ();
        super.addAll();
        super.notifyDataSetChanged();

    }*/


    public Context getContext ()
    {
        return mContext;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        ItemHolder itemHolder = null;

        mPosition = position; //to see which item was clicked

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            //get items in row and set them to layout items
            itemHolder = new ItemHolder();

            itemHolder.itemButton = (Button)row.findViewById(R.id.bListItem);

            //set up check box functionality
            itemHolder.checkBox = (CheckBox)row.findViewById(R.id.itemCheckBox);
            itemHolder.checkBox.setTag(mPosition);
            itemHolder.checkBox.setOnClickListener(new OnCheckListener()
            {
                @Override
                public void onClick (View v)
                {
                    //to wait to remove for a certain amount of time
                    mPosition = (Integer) v.getTag();

                    if (!mbWaiting)
                    {
                        mbWaiting = Boolean.TRUE;

                        mTimerTask = new TimerTask()
                        {
                            public void run()
                            {
                                mHandlerUI.post(new Runnable() {
                                    public void run() {
                                        if (mbWaiting) {
                                            //remove item after certain amount of time?
                                            mItemArray.remove(mPosition);
                                            ListItemAdapter.this.notifyDataSetChanged();
                                            mbWaiting = false;
                                        }
                                    }
                                });
                            }

                        };
                        timer.schedule(mTimerTask, 3000);  //

                    }
                    else
                    {
                       mbWaiting = Boolean.FALSE;
                       mTimerTask.cancel();
                    }
                }
            });

            //set up item quantity editText
            itemHolder.itemQuantity = (EditText)row.findViewById(R.id.input_qty);
            itemHolder.itemQuantity.setText(String.valueOf(mItemArray.get(position).getQuantity()));
            itemHolder.itemQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if editText is not empty, assign new quantity to item
                    if (!s.toString().isEmpty())
                    {
                        mItemArray.get(position).setQuantity(Integer.parseInt(s.toString()));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }
            });

            //set upDeleteButton
            itemHolder.bDelete = (Button) row.findViewById(R.id.bDelete);
            itemHolder.bDelete.setVisibility(View.INVISIBLE);
            itemHolder.bDelete.setOnClickListener(new View.OnClickListener() {

                private View mView = null;

                @Override
                public void onClick(View v) {
                    mView = v;
                    PoPListItemsActivity parentActivity = (PoPListItemsActivity) getContext();
                    PoPList poPList = ((PoPListItemsActivity) getContext()).getPoPList();
                    Button delete = (Button) v;
                    int pos = (Integer) v.getTag();

                    //delete item
                    poPList.delete(pos);
                    //notify list adapter
                    parentActivity.getItemAdapter().notifyDataSetChanged();

                    if (!parentActivity.isOnEdit()) {
                        delete.setVisibility(View.INVISIBLE);
                        slideView();
                    }
                }

                //method of the delete button's onClickListener
                public void slideView() {
                    //wait a moment before sliding view so that list can be updated in time
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 100 milliseconds
                            ((PoPListItemsActivity) getContext()).slideItemView( ((View) mView.getParent()), PoPListItemsActivity.SLIDE_RIGHT_ITEM );

                        }
                    }, 60);

                }


            });



        }
        else
        {
            itemHolder = (ItemHolder) row.getTag();
        }

        //If previous item was deleted, then delete will show on newly added item, this fixes that
        if (itemHolder.bDelete.getVisibility() == View.VISIBLE && !((PoPListItemsActivity) getContext()).isOnEdit())
        {
            itemHolder.bDelete.setVisibility(View.INVISIBLE);
            ((PoPListItemsActivity) getContext()).slideItemView( ((View) itemHolder.bDelete.getParent()), PoPListItemsActivity.SLIDE_RIGHT_ITEM );
        }

        //used later when the row's button methods are already initialized but the information (like position and name are not)
        row.setTag(itemHolder);

        //set list row info
        ListItem item = mItemArray.get(position);
        itemHolder.itemButton.setText(item.getItemName());
        itemHolder.itemQuantity.setText(String.valueOf(mItemArray.get(position).getQuantity()));
        itemHolder.bDelete.setTag(position);

        return row;
    }

    static class ItemHolder
    {
        Button itemButton;
        CheckBox checkBox;
        EditText itemQuantity;
        Button bDelete;
    }


}
