package fdi.ucm.picday;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class StartPage extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        ListView list = (ListView) findViewById(R.id.challengeList);

        Challenge dailyChallenge = new Challenge("Daily", "Today is Nature Time!");
        Challenge hobbieChallenge = new Challenge("MyHobbie", "Take a picture of your favourite hobbie!");

        ChallengeDB challengeDB = new ChallengeDB(this);
        challengeDB.openForWrite();
        challengeDB.insertChallenge(dailyChallenge);
        challengeDB.insertChallenge(hobbieChallenge);

        ArrayList<Challenge> challenges = challengeDB.getAllChallenges();
        challengeDB.close();

        ArrayAdapter<Challenge> adapter = new ArrayAdapter<Challenge>(this,android.R.layout.simple_list_item_1, challenges);
        list.setAdapter(adapter);
    }
}
