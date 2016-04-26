package com.oink.lessonschedule.activities;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oink.lessonschedule.additions.DividerItemDecoration;
import com.oink.lessonschedule.R;
import com.oink.lessonschedule.adapters.DayRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private final int DAYS_IN_WEEK = 7;

    String[] firstBuildingTime;
    String[] sixthBuildingTime;

    private JSONArray jsonDayArray;
    private RecyclerView dayRecyclerView;

    private TextView lessonLabel;
    private TextView lessonName;
    private TextView lessonAdditionalInfo;
    private TextView lessonClassroom;
    private TextView lessonTime;
    private LinearLayout currentLessonLayout;

    boolean isCurrentLesson;
    private CoordinatorLayout coordinatorLayout;
    private DayRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isCurrentLesson = savedInstanceState.getBoolean("isCurrentLesson");
        }
        else {
            isCurrentLesson = false;
        }

        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_layout);

        firstBuildingTime = getResources().getStringArray(R.array.first_building_time);
        sixthBuildingTime = getResources().getStringArray(R.array.sixth_building_time);

        if (isFirstExecute()) {
            writeJsonToFile(convertStreamToString(getResources().openRawResource(R.raw.schedule)));
        }

        try {
            jsonDayArray = loadJsonFromFile();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        setupRecyclerView();

        // Adding Toolbar to Main screen
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.schedule);

        getLessonViews();
        setupLesson(isCurrentLesson);
        addListenerToLessonLayout();
        setupAppbarFade();

        //For compatible with Android 4.x
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            int[] attrs = {android.R.attr.selectableItemBackground};
            TypedArray ta = obtainStyledAttributes(R.style.AppTheme, attrs);
            currentLessonLayout.setBackground(ta.getDrawable(0));
        }
    }

    private JSONArray loadJsonFromFile() throws JSONException, FileNotFoundException {
        FileInputStream jsonInputStream = openFileInput(getString(R.string.schedule_file_name));
        //Reading from .json file
        String rawData = convertStreamToString(jsonInputStream);
        JSONObject jsonObject = new JSONObject(rawData);
        JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.json_days));
        return jsonArray;
    }

    private void writeJsonToFile(String string) {
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(getString(R.string.schedule_file_name), Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isFirstExecute() {
        File file = new File(getFilesDir(), getString(R.string.schedule_file_name));
        return !file.exists();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isCurrentLesson", isCurrentLesson);
    }

    private void addListenerToLessonLayout() {
        currentLessonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCurrentLesson = !isCurrentLesson;
                setupLesson(isCurrentLesson);
            }
        });
    }

    private void setupRecyclerView() {
        dayRecyclerView = (RecyclerView) findViewById(R.id.day_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dayRecyclerView.setLayoutManager(layoutManager);

        adapter = new DayRecyclerAdapter(this, jsonDayArray);
        dayRecyclerView.setAdapter(adapter);

        dayRecyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    private void setupLesson(boolean current) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        day -= 2;
        if (day < 0) day = DAYS_IN_WEEK - 1;

        JSONArray lessons;
        try {
            if (c.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                lessons = jsonDayArray.getJSONObject(day).getJSONArray(getString(R.string.json_lesson_even));
            } else {
                lessons = jsonDayArray.getJSONObject(day).getJSONArray(getString(R.string.json_lesson_odd));
            }
            SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.time_format));
            String currentTime = sdf.format(new Date());
            if (current) {
                lessonLabel.setText(R.string.current_lesson_label);
                setupCurrentLesson(lessons, currentTime);
            } else {
                lessonLabel.setText(R.string.next_lesson_label);
                setLessonsViewVisibility(View.VISIBLE);
                setupNextLesson(lessons, day, c.get(Calendar.WEEK_OF_YEAR) % 2 == 0, currentTime);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_day_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_schedule:
                updateScheduleFromServer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateScheduleFromServer() {
        new updateTask().execute();
    }

    private void setupNextLesson(JSONArray lessons, int day, boolean even, String currentTime) throws JSONException {
        for (int i = 0; i < lessons.length(); ++i) {
            JSONObject lesson = lessons.getJSONObject(i);
            int lessonNumber = lesson.getInt(getString(R.string.json_lesson_number));
            boolean inSixthBuilding = lesson.getBoolean(getString(R.string.json_lesson_in_sixth_building));
            String lessonTime = getLessonTime(lessonNumber, inSixthBuilding);

            /*
            Lesson time in format HH:mm - HH:mm
            substring(0, 5) - getting lesson start time.
            */
            if (compareTime(currentTime, lessonTime.substring(0, 5)) > 0) {
                putStringsToLesson(lesson, lessonTime);
                return;
            }
        }

        setupNextLessonOnNextDay(day, even);
    }

    private void setupNextLessonOnNextDay(int day, boolean even) throws JSONException {
        boolean startChange = false;
        JSONObject lesson = null;
        do {
            day++;
            if (day == DAYS_IN_WEEK) {
                day = 0;
                even = !even;
            }

            if (even && jsonDayArray.getJSONObject(day).getJSONArray(getString(R.string.json_lesson_even)).length() != 0) {
                lesson = jsonDayArray.getJSONObject(day).getJSONArray(getString(R.string.json_lesson_even)).getJSONObject(0);
                startChange = true;
            }
            else if (!even && jsonDayArray.getJSONObject(day).getJSONArray(getString(R.string.json_lesson_odd)).length() != 0){
                lesson = jsonDayArray.getJSONObject(day).getJSONArray(getString(R.string.json_lesson_odd)).getJSONObject(0);
                startChange = true;
            }

            if (startChange) {
                String dayName = jsonDayArray.getJSONObject(day).getString(getString(R.string.json_day_name));
                String newLabelText = lessonLabel.getText() + " (" + dayName + ")";
                lessonLabel.setText(newLabelText);
                int lessonNumber = lesson.getInt(getString(R.string.json_lesson_number));
                boolean inSixthBuilding = lesson.getBoolean(getString(R.string.json_lesson_in_sixth_building));
                putStringsToLesson(lesson, getLessonTime(lessonNumber, inSixthBuilding));
                return;
            }
        } while (true);
    }

    private String getLessonTime(int lessonNumber, boolean inSixthBuilding) {
        if (inSixthBuilding) {
            return sixthBuildingTime[lessonNumber - 1];
        }
        else {
            return firstBuildingTime[lessonNumber - 1];
        }
    }

    private void setupCurrentLesson(JSONArray lessons, String currentTime) throws JSONException {
        for (int i = 0; i < lessons.length(); ++i) {
            JSONObject lesson = lessons.getJSONObject(i);
            int lessonNumber = lesson.getInt(getString(R.string.json_lesson_number));
            boolean inSixthBuilding = lesson.getBoolean(getString(R.string.json_lesson_in_sixth_building));
            String lessonTime;
            if (inSixthBuilding) {
                lessonTime = sixthBuildingTime[lessonNumber - 1];
            }
            else {
                lessonTime = firstBuildingTime[lessonNumber - 1];
            }
            if (betweenStartAndEndTime(currentTime, lessonTime)) {
                putStringsToLesson(lesson, lessonTime);
                return;
            }
        }
        putStringsToLesson();
    }

    /**
     * Compares two times in format HH:mm
     * @param firstTime first time in format HH:mm
     * @param secondTime second time in format HH:mm
     * @return < 0, if second time is earlier than first time,
     * > 0, is second time is later than first time,
     * == 0, if this times are equals.
     */
    private int compareTime(String firstTime, String secondTime) {
        //Getting "HH" parameters
        int firstHours = Integer.parseInt(firstTime.substring(0, 2));
        int secondHours = Integer.parseInt(secondTime.substring(0, 2));
        //Getting "mm" parameters
        int firstMinutes = Integer.parseInt(firstTime.substring(3, 5));
        int secondMinutes = Integer.parseInt(secondTime.substring(3, 5));
        if (firstHours == secondHours) {
            return secondMinutes - firstMinutes;
        }
        else {
            return secondHours - firstHours;
        }
    }

    /**
     * Checks whether the the currentTime is between lessonTime
     * @param currentTime time in format HH:mm
     * @param lessonTime time interval in format HH:mm - HH:mm
     * @return true, if currentTime between lessonTime, false otherwise
     */
    private boolean betweenStartAndEndTime(String currentTime, String lessonTime) {
        /*
        substring(0, 5) - getting lesson start time,
        substring(8, 13) - getting lesson end time.
        */
        return (compareTime(currentTime, lessonTime.substring(0, 5)) <= 0 &&
                compareTime(currentTime, lessonTime.substring(8, 13)) >= 0);
    }

    private void putStringsToLesson(JSONObject lessonObject, String lessonTimeString) throws JSONException {
        String additionalInfo = lessonObject.getString(getString(R.string.json_lesson_additional_info));
        String classroom = lessonObject.getString(getString(R.string.json_lesson_classroom));
        String name = lessonObject.getString(getString(R.string.json_lesson_name));


        lessonAdditionalInfo.setText(additionalInfo);
        if (additionalInfo.equals("")) lessonAdditionalInfo.setVisibility(View.GONE);
        lessonClassroom.setText(classroom);
        if (classroom.equals("")) lessonClassroom.setVisibility(View.GONE);
        lessonName.setText(name);
        if (name.equals("")) lessonName.setVisibility(View.GONE);
        this.lessonTime.setText(lessonTimeString);
    }

    private void putStringsToLesson() {
        lessonName.setText(R.string.main_no_lesson);
        setLessonsViewVisibility(View.GONE);
        lessonName.setVisibility(View.VISIBLE);
    }

    private void setupAppbarFade() {
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.appbar_container);
        assert appbarLayout != null;
        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            final float START_FADE_BORDER = 0.8f;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = 1 - ((float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange());
                if (percentage > START_FADE_BORDER) {
                    currentLessonLayout.setAlpha((percentage - START_FADE_BORDER) / (1 - percentage));
                }
                else currentLessonLayout.setAlpha(0);
            }
        });
    }

    private static String convertStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    public void setLessonsViewVisibility(int lessonsViewVisibility) {
        lessonName.setVisibility(lessonsViewVisibility);
        lessonAdditionalInfo.setVisibility(lessonsViewVisibility);
        lessonTime.setVisibility(lessonsViewVisibility);
        lessonClassroom.setVisibility(lessonsViewVisibility);
    }

    private void getLessonViews() {
        currentLessonLayout = (LinearLayout) findViewById(R.id.current_lesson_layout);
        lessonName = (TextView) findViewById(R.id.lesson_name);
        lessonAdditionalInfo = (TextView) findViewById(R.id.lesson_additional_info);
        lessonClassroom = (TextView) findViewById(R.id.lesson_classroom);
        lessonTime = (TextView) findViewById(R.id.lesson_time);
        lessonLabel = (TextView) findViewById(R.id.lesson_label);
    }

    class updateTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Snackbar.make(coordinatorLayout, R.string.loading_start, Snackbar.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                URL url = new URL(getString(R.string.update_server_url));
                URLConnection connection = url.openConnection();
                connection.connect();

                //Input stream to read file with 8k buffer
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()), 8192);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                //Closing stream
                reader.close();
                String json = sb.toString();
                jsonDayArray = new JSONObject(json).getJSONArray(getString(R.string.json_days));
                adapter.setJsonArray(jsonDayArray);
                writeJsonToFile(json);
            } catch (IOException | JSONException e) {
                Log.e("Download", "Broken connection");
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Snackbar.make(coordinatorLayout, R.string.loading_done, Snackbar.LENGTH_SHORT).show();
            }
            else {
                Snackbar.make(coordinatorLayout, R.string.loading_err, Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
