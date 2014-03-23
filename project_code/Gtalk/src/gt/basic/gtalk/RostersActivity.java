package gt.basic.gtalk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RostersActivity extends Activity {

	public static String KEY = "roster";

	private ListView rosterListView;
	private List<String> data=new ArrayList();
	private List<RosterEntry> entry=new ArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rosters);
		rosterListView = (ListView) findViewById(R.id.rostersListView);

		AsyncTask<Void, Integer, Roster> task = new AsyncTask<Void, Integer, Roster>() {

			@Override
			protected Roster doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return Connection.getRoster();
			}

			@Override
			protected void onPostExecute(Roster result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				Collection<RosterEntry> entries = result.getEntries();

				for (RosterEntry r : entries) {
					try
					{
						data.add(r.getUser()+r.getName());
						entry.add(r);
					}
					catch(Exception ee)
					{
						int kk=0;
						kk++;
					}
				}


				//String[] str={"123","456"};
				/*
				rosterListView.setAdapter(new ArrayAdapter<String>(
						RostersActivity.this, R.layout.item_roster, str));
				*/
				String[] str=new String[data.size()];
				for(int i=0;i<data.size();i++)
				{
					str[i]=data.get(i);
				}
				rosterListView.setAdapter(new ArrayAdapter<String>(RostersActivity.this,
						R.layout.item_roster, R.id.usernameTextView, str));
				//rosterListView.setAdapter(adapter)
			}

		};

		
		rosterListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> l, View v,
							int position, long id) {
						// TODO Auto-generated method stub
						int p = (int) id;
						Intent intent = new Intent(getApplicationContext(),
								TalkActivity2.class);
						intent.putExtra(KEY, entry.get(p).getUser());
						RostersActivity.this.startActivity(intent);
					}

				});
		
		task.execute();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

}
